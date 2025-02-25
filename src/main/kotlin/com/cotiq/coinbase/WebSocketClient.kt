package com.cotiq.coinbase

import com.cotiq.coinbase.api.ApiCall
import com.cotiq.coinbase.websocket.message.Event
import com.cotiq.coinbase.websocket.message.FeedChannel
import com.cotiq.coinbase.websocket.message.Message
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow


sealed class ConnectionState {
    data object Connecting : ConnectionState()
    data object Connected : ConnectionState()
    data object Disconnecting : ConnectionState()
    data object Disconnected : ConnectionState()
}

interface WebSocketClient {

    val connectionState: StateFlow<ConnectionState>
    val messages: SharedFlow<Message<out Event>>

    /**
     * Subscribe to a channel.
     * If the client is authenticated, the client will automatically authenticate this subscription request.
     */
    fun subscribe(channel: FeedChannel, productIds: List<String>? = null): ApiCall<Unit>

    /**
     * Unsubscribe from a channel.
     * If the client is authenticated, the client will automatically authenticate this subscription request.
     */
    fun unsubscribe(channel: FeedChannel, productIds: List<String>? = null): ApiCall<Unit>

    /**
     * Starts the WebSocket client.
     * Observe [connectionState] to know when the client is connected.
     */
    fun start(): ApiCall<Unit>

    /**
     * Stops the WebSocket client gracefully.
     * This allows the client to be restarted later.
     * Observe [connectionState] to know when the client is disconnected.
     */
    fun stop(): ApiCall<Unit>

    /**
     * Closes the WebSocket client permanently.
     * Once closed, the client cannot be restarted.
     */
    fun close(): ApiCall<Unit>

    /**
     * Registers a callback to be invoked for every received message.
     */
    fun <E: Event> onMessage(block: (message: Message<E>) -> Unit)

    /**
     * Registers a callback to be invoked when the connection state changes.
     */
    fun onState(block: (ConnectionState) -> Unit)
}