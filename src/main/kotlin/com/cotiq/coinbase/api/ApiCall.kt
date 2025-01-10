package com.cotiq.coinbase.api

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.future.future
import kotlinx.coroutines.runBlocking
import java.util.concurrent.CompletableFuture

class ApiCall<T>
internal constructor(
    private val coroutineScope: CoroutineScope,
    private val request: suspend () -> T
) {
    suspend fun call(): T = request()
    fun future(): CompletableFuture<T> = coroutineScope.future { request() }
    fun callBlocking(): T = runBlocking { request() }
}
