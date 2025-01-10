package com.cotiq.coinbase

import com.cotiq.coinbase.api.account.AccountApi
import com.cotiq.coinbase.api.fee.FeeApi
import com.cotiq.coinbase.api.pub.PublicApi
import com.cotiq.coinbase.api.order.OrderApi
import com.cotiq.coinbase.api.product.ProductApi
import io.ktor.client.*
import kotlinx.coroutines.CoroutineScope

class RestApiClient(
    httpClient: HttpClient,
    coroutineScope: CoroutineScope,
) {

    /**
     * Account API
     */
    val accountApi by lazy {
        AccountApi(httpClient, coroutineScope)
    }

    /**
     * Fee API
     */
    val feeApi: FeeApi by lazy {
        FeeApi(httpClient, coroutineScope)
    }

    /**
     * Order API
     */
    val orderApi: OrderApi by lazy {
        OrderApi(httpClient, coroutineScope)
    }

    /**
     * Product API
     */
    val productApi: ProductApi by lazy {
        ProductApi(httpClient, coroutineScope)
    }

    val publicApi: PublicApi by lazy {
        PublicApi(httpClient, coroutineScope)
    }

}