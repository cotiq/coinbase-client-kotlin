package com.cotiq.coinbase.websocket.message

import kotlinx.serialization.Serializable

@Serializable
data class Level2Event(
    val type: String,
    val productId: String,
    val updates: List<UpdateEntry>,
): Event {
    @Serializable
    data class UpdateEntry(
        val side: String,
        val eventTime: String,
        val priceLevel: String,
        val newQuantity: String,
    )
}
