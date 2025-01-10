package com.cotiq.coinbase.websocket

import com.cotiq.coinbase.ConnectionState
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.websocket.*
import kotlinx.atomicfu.AtomicBoolean
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withTimeout
import org.slf4j.Logger
import org.slf4j.LoggerFactory

internal class ConnectionManager(
    private val httpClient: HttpClient,
    private val scope: CoroutineScope,
    private val config: WebSocketConfig,
) {

    private val isRunning: AtomicBoolean = atomic(false)

    private val _connectionState = MutableStateFlow<ConnectionState>(ConnectionState.Disconnected)
    val connectionState: StateFlow<ConnectionState> get() = _connectionState.asStateFlow()

    private var webSocketSession: DefaultClientWebSocketSession? = null
    private val sessionMutex = Mutex()

    private val backoff = ExponentialBackoff(
        baseDelay = config.initialRetryDelay,
        maxDelay = config.maxRetryDelay
    )

    private var connectionJob: Job? = null

    /**
     * Attempts to establish a WebSocket connection.
     */
    private suspend fun attemptConnection(): DefaultClientWebSocketSession? {
        return try {
            withTimeout(config.connectionTimeout) {
                httpClient.webSocketSession { url(config.webSocketUrl) }
            }
        } catch (e: Exception) {
            when (e) {
                is CancellationException -> throw e
                else -> {
                    logger.error("Connection attempt failed: ${e.message}", e)
                    null
                }
            }
        }
    }

    fun start() {

        check(isRunning.compareAndSet(false, true)) { "Already running" }

        connectionJob = scope.launch {
            while (isActive) {
                logger.debug("Attempting to connect to ${config.webSocketUrl}")
                _connectionState.emit(ConnectionState.Connecting)

                // Try to establish a connection.
                val session = attemptConnection()
                if (session == null) {
                    // Connection failed, delay and try again.
                    backoff.delayNext()
                    continue
                }

                sessionMutex.withLock { webSocketSession = session }

                // Reset backoff when a connection is successfully made.
                backoff.reset()

                _connectionState.emit(ConnectionState.Connected)
                logger.info("Connected to ${config.webSocketUrl}")

                val closeReason: CloseReason? = try {
                    session.closeReason.await()
                } catch (e: Exception) {
                    when (e) {
                        is CancellationException -> throw e
                        else -> {
                            logger.error("Error while reading the socket", e)
                            null
                        }
                    }
                }

                logger.info("WebSocket closed: ${closeReason?.code  ?: "No Code"} - ${closeReason?.message ?: "No message"}")

                sessionMutex.withLock { webSocketSession = null }
                _connectionState.emit(ConnectionState.Disconnected)

                if (isActive) {
                    // Delay before attempting a reconnection.
                    backoff.delayNext()
                }
            }
        }
    }

    suspend fun stop() {
        check(isRunning.compareAndSet(true, false)) { "Already stopped" }

        if (isHealthy()) {
            sessionMutex.withLock {
                try {
                    webSocketSession?.close(CloseReason(1000, "Client stopped"))
                } catch (e: Exception) {
                    logger.error("Error while disconnecting", e)
                }
                webSocketSession = null
            }
        }
        connectionJob?.cancelAndJoin()
        connectionJob = null
        logger.info("stopped")
    }

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun isHealthy(): Boolean = sessionMutex.withLock {
        webSocketSession?.let { session ->
            session.isActive &&
                    !session.outgoing.isClosedForSend &&
                    !session.incoming.isClosedForReceive
        } ?: false
    }

    /**
     * Provides safe access to the current WebSocket session.
     */
    suspend fun getSession(): DefaultClientWebSocketSession? = sessionMutex.withLock { webSocketSession }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(ConnectionManager::class.java)
    }
}