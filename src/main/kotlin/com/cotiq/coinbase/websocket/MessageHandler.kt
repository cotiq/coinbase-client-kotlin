package com.cotiq.coinbase.websocket

import com.cotiq.coinbase.auth.JwtCreator
import com.cotiq.coinbase.websocket.message.Event
import com.cotiq.coinbase.websocket.message.FeedChannel
import com.cotiq.coinbase.websocket.message.Message
import com.cotiq.coinbase.websocket.message.MessageDeserializationStrategy
import io.ktor.websocket.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory

internal class MessageHandler(
    private val sessionProvider: suspend () -> WebSocketSession?,
    private val jsonParser: Json,
    private val jwtCreator: JwtCreator? = null,
    private val bufferSize: Int = Channel.UNLIMITED,
) {

    private val _messages = MutableSharedFlow<Message<out Event>>()
    val messages = _messages.asSharedFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    fun cleanup() {
        _messages.resetReplayCache()
    }

    suspend fun listenSocket() = coroutineScope {
        val session = sessionProvider() ?: run {
            logger.error("WebSocket session is null. Cannot listen for messages")
            return@coroutineScope
        }
        try {
            session.incoming.receiveAsFlow()
                .buffer(bufferSize)
                .mapNotNull { processFrame(it) }
                .collect { handleTextPayload(it) }
            logger.info("WebSocket session closed. Stopping message processing")
        } catch (e: CancellationException) {
            logger.info("WebSocket reading cancelled")
        }
    }

    private fun processFrame(frame: Frame): String? = when (frame) {
        is Frame.Text -> frame.readText()
        is Frame.Close -> {
            logger.info("Received Frame.Close: ${frame.readReason()}")
            null
        }
        else -> {
            logger.warn("Received unexpected WebSocket frame: ${frame.frameType}")
            null
        }
    }

    private suspend fun handleTextPayload(jsonString: String) {
        try {
            val value = jsonParser.decodeFromString(MessageDeserializationStrategy(), jsonString)
            _messages.emit(value)
        } catch (e: SerializationException) {
            logger.warn("Failed to deserialize payload: $jsonString", e)
        } catch (e: Exception) {
            logger.error("Unexpected error while handling payload: $jsonString", e)
        }
    }

    suspend fun subscribe(channel: FeedChannel, productIds: List<String>? = null) =
        send(channel.channelName, "subscribe", productIds)

    suspend fun unsubscribe(channel: FeedChannel, productIds: List<String>? = null) =
        send(channel.channelName, "unsubscribe", productIds)

    private suspend fun send(channelName: String, type: String, productIds: List<String>? = null) {
        val session = sessionProvider() ?: throw IllegalStateException("Cannot send when not connected")
        val payload = RequestMessage(
            type = type,
            channel = channelName,
            productIds = productIds,
            jwt = jwtCreator?.createJwt(),
        )
        val text = jsonParser.encodeToString(payload)
        session.send(Frame.Text(text))
        logger.info("Sent message: $payload")
    }

    companion object {
        private val logger = LoggerFactory.getLogger(MessageHandler::class.java)
    }

    @Serializable
    private data class RequestMessage(
        val type: String,
        val channel: String,
        val productIds: List<String>? = null,
        val jwt: String? = null,
    )
}