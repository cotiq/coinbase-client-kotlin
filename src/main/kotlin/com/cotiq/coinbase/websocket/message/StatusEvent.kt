package com.cotiq.coinbase.websocket.message

import kotlinx.serialization.Serializable

@Serializable
data class StatusEvent(
    val type: String,
    val products: List<Product>,
): Event {
    @Serializable
    data class Product(
        val productType: String,
        val id: String,
        val baseCurrency: String,
        val quoteCurrency: String,
        val baseIncrement: String,
        val quoteIncrement: String,
        val displayName: String,
        val status: String,
        val statusMessage: String,
        val minMarketFunds: String,
    )
}