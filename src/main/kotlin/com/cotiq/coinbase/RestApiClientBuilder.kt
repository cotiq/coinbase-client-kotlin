package com.cotiq.coinbase

import com.cotiq.coinbase.auth.ApiCredentials
import com.cotiq.coinbase.plugins.AdvancedTradeApiAuth
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy

/**
 * Builder for creating a [RestApiClient]
 */
class RestApiClientBuilder {

    private var credentials: ApiCredentials? = null
    private var engine: HttpClientEngine = CIO.create()

    /**
     * Configures the client to be authenticated with the given [credentials].
     * If not set, requests are made without authentication.
     */
    fun setCredentials(credentials: ApiCredentials): RestApiClientBuilder {
        this.credentials = credentials
        return this
    }

    /**
     * Sets the [HttpClientEngineFactory] to use for the client.
     * Default is [CIO] - an asynchronous coroutine-based engine.
     */
    fun setHttpClientEngine(httpClientEngine: HttpClientEngine): RestApiClientBuilder {
        this.engine = httpClientEngine
        return this
    }

    @OptIn(ExperimentalSerializationApi::class)
    fun build(): RestApiClient =
        RestApiClient(
            coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            httpClient = HttpClient(engine) {
                expectSuccess = true
                install(Logging) {
                    sanitizeHeader { header -> header == HttpHeaders.Authorization }
                }
                install(DefaultRequest) {
                    url("https://api.coinbase.com/api/v3/brokerage/")
                }
                install(ContentNegotiation) {
                    json(Json {
                        namingStrategy = JsonNamingStrategy.SnakeCase
                        ignoreUnknownKeys = true
                        isLenient = true
                        prettyPrint = true
                    })
                }
                credentials?.let {
                    install(AdvancedTradeApiAuth) {
                        keyName = it.apiKeyName
                        privateKey = it.apiPrivateKey.replace("\\n", "\n")
                    }
                }
            }
        )
}