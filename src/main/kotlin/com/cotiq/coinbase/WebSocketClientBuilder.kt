package com.cotiq.coinbase

import com.cotiq.coinbase.auth.ApiCredentials
import com.cotiq.coinbase.auth.JwtCreator
import com.cotiq.coinbase.websocket.DefaultWebSocketClient
import com.cotiq.coinbase.websocket.WebSocketConfig
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.websocket.*
import io.ktor.serialization.kotlinx.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy


enum class DataFeed(val url: String) {
    /**
     * Public WebSocket feed that delivers real-time updates on market orders and trades
     * for various cryptocurrency products.
     */
    MarketData("wss://advanced-trade-ws.coinbase.com"),
    /**
     * Authenticated WebSocket feed that provides real-time updates on the user’s orders,
     * including order status and updates on active trades.
     */
    UserOrderData("wss://advanced-trade-ws-user.coinbase.com"),
}

/**
 * Builder for creating a [WebSocketClient]
 */
class WebSocketClientBuilder {

    private var dataFeed: DataFeed? = null
    private var credentials: ApiCredentials? = null
    private var httpClientEngine: HttpClientEngine = CIO.create()

    @OptIn(ExperimentalSerializationApi::class)
    private val json = Json {
        namingStrategy = JsonNamingStrategy.SnakeCase
        explicitNulls = false
        ignoreUnknownKeys = true
    }

    /**
     * Sets the WebSocket data feed to use.
     */
    fun setDataFeed(dataFeed: DataFeed): WebSocketClientBuilder {
        this.dataFeed = dataFeed
        return this
    }

    /**
     * Configures the client to be authenticated with the given [credentials].
     * If not set, requests are made without authentication.
     */
    fun setCredentials(credentials: ApiCredentials): WebSocketClientBuilder {
        this.credentials = credentials
        return this
    }

    /**
     * Sets the [HttpClientEngine] to use.
     * Default is [CIO] - an asynchronous coroutine-based engine.
     */
    fun setHttpClientEngine(httpClientEngine: HttpClientEngine): WebSocketClientBuilder {
        this.httpClientEngine = httpClientEngine
        return this
    }

    fun build(): WebSocketClient {

        val config = WebSocketConfig(
            webSocketUrl = requireNotNull(dataFeed?.url) { "DataFeed is not set" }
        )
        return DefaultWebSocketClient(
            coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            httpClient = HttpClient(httpClientEngine) {
                install(Logging)
                install(WebSockets) {
                    contentConverter = KotlinxWebsocketSerializationConverter(
                        format = json
                    )
                }
            },
            jsonParser = json,
            jwtCreator = credentials?.let {
                JwtCreator(
                    keyName = it.apiKeyName,
                    privateKey = it.apiPrivateKey.replace("\\n", "\n")
                )
            },
            config = config,
        )
    }
}