package com.cotiq.coinbase.websocket.message

import kotlinx.serialization.Serializable

@Serializable
data class HeartbeatEvent(
    val currentTime: String,
    val heartbeatCounter: Long,
): Event
