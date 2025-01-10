package com.cotiq.coinbase.websocket.message

import kotlinx.serialization.Serializable

interface Event

@Serializable
data class Message<T: Event>(
    val channel: FeedChannel,
    val clientId: String,
    val timestamp: String,
    val sequenceNum: Long,
    val events: List<T>,
)