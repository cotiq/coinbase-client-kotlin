package com.cotiq.coinbase.websocket

import com.cotiq.coinbase.ConnectionState
import com.cotiq.coinbase.WebSocketClient
import com.cotiq.coinbase.api.ApiCall
import com.cotiq.coinbase.auth.JwtCreator
import com.cotiq.coinbase.websocket.message.Event
import com.cotiq.coinbase.websocket.message.FeedChannel
import com.cotiq.coinbase.websocket.message.Message
import io.ktor.client.*
import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.update
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

data class WebSocketConfig(
    val webSocketUrl: String,
    val initialRetryDelay: Duration = 2.seconds,
    val maxRetryDelay: Duration = 60.seconds,
    val maxRetryAttempts: Int = Int.MAX_VALUE,
    val connectionTimeout: Duration = 5.seconds,
    val messageBufferSize: Int = 1000,
)

private enum class ClientState {
    Idle,
    Running,
    Closed,
}

class DefaultWebSocketClient(
    private val config: WebSocketConfig,
    private val coroutineScope: CoroutineScope,
    httpClient: HttpClient,
    jsonParser: Json,
    jwtCreator: JwtCreator? = null,
): WebSocketClient {

    private val stateMutex = Mutex()
    private val state: AtomicRef<ClientState> = atomic(ClientState.Idle)

    private val connectionManager = ConnectionManager(httpClient, coroutineScope, config)
    private val messageHandler = MessageHandler(
        sessionProvider = { connectionManager.getSession() },
        jsonParser = jsonParser,
        jwtCreator = jwtCreator,
        bufferSize = config.messageBufferSize
    )

    override val connectionState get() = connectionManager.connectionState
    override val messages get() = messageHandler.messages

    init {
        connectionManager.connectionState.onEach {
            if (it is ConnectionState.Connected) {
                messageHandler.listenSocket()
            }
        }.launchIn(coroutineScope)
    }

    override fun subscribe(channel: FeedChannel, productIds: List<String>?) = apiCall {
        check(connectionManager.isHealthy()) { "WebSocket is not healthy" }
        messageHandler.subscribe(channel, productIds)
    }

    override fun unsubscribe(channel: FeedChannel, productIds: List<String>?) = apiCall {
        check(connectionManager.isHealthy()) { "WebSocket is not healthy" }
        messageHandler.unsubscribe(channel)
    }


    override fun start() = apiCall {
        resetState()
        connectionManager.start()
    }

    override fun stop() = apiCall {
        when (state.value) {
            ClientState.Closed -> throw IllegalStateException("Client is already closed")
            ClientState.Idle -> {
                logger.debug("Client is already stopped")
                return@apiCall
            }

            else -> { /* ok */
            }
        }
        setState(ClientState.Idle)
        connectionManager.stop()
    }

    override fun close() = apiCall {
        if (state.value == ClientState.Closed) {
            logger.warn("Client is already closed")
            return@apiCall
        }
        setState(ClientState.Closed)
        connectionManager.stop()
        messageHandler.cleanup()
        coroutineScope.cancel()
    }

    private suspend fun resetState() = stateMutex.withLock {
        when (state.value) {
            ClientState.Running -> throw IllegalStateException("Client is already running")
            ClientState.Closed -> throw IllegalStateException("Client is already closed")
            else -> { /* ok */
            }
        }
        setState(ClientState.Running)
    }

    private fun setState(newState: ClientState) {
        logger.debug("Client state changed: ${state.value} -> $newState")
        state.update { newState }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <E : Event> onMessage(eventType: KClass<E>, block: (message: Message<E>) -> Unit) {
        messages.buffer(config.messageBufferSize)
            .filter { message -> message.events.any { eventType.isInstance(it) } }
            .map { it as Message<E> }
            .onEach { block(it) }
            .launchIn(coroutineScope)
    }

    override fun onState(block: (ConnectionState) -> Unit) {
        connectionState
            .onEach { block(it) }
            .launchIn(coroutineScope)
    }

    private fun <T> apiCall(request: suspend () -> T) = ApiCall(coroutineScope, request)

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(DefaultWebSocketClient::class.java)
    }
}
