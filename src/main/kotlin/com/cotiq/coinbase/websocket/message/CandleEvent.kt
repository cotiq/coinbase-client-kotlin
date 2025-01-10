package com.cotiq.coinbase.websocket.message

import kotlinx.serialization.Serializable

@Serializable
data class CandleEvent(
    val type: String,
    val candles: List<Candle>,
): Event {
    @Serializable
    data class Candle(
        val start: String,
        val high: String,
        val low: String,
        val open: String,
        val close: String,
        val volume: String,
        val productId: String,
    )
}
