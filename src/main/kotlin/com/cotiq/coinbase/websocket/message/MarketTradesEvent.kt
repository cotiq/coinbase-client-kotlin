package com.cotiq.coinbase.websocket.message

import kotlinx.serialization.Serializable

@Serializable
data class MarketTradesEvent(
    val type: String,
    val trades: List<Trade>,
): Event {
    @Serializable
    data class Trade(
        val tradeId: String,
        val productId: String,
        val price: String,
        val size: String,
        val side: String,
        val time: String,
    )
}
