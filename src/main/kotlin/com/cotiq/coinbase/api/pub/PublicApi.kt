package com.cotiq.coinbase.api.pub

import com.cotiq.coinbase.api.ApiCall
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.CoroutineScope

class PublicApi(
    private val httpClient: HttpClient,
    private val scope: CoroutineScope,
) {

    /**
     * Get the current time
     */
    fun getServerTime(): ApiCall<ServerTime> = ApiCall(scope) {
        httpClient.get("time").body()
    }

}

