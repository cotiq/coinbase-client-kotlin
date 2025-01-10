package com.cotiq.coinbase.api.pub

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

/**
 * Represents the server time.
 *
 * @property iso An ISO-8601 representation of the timestamp
 * @property epochSeconds A second-precision representation of the timestamp
 * @property epochMillis A millisecond-precision representation of the timestamp
 */
@Serializable
@OptIn(ExperimentalSerializationApi::class)
data class ServerTime(
    val iso: String,
    @JsonNames("epochSeconds")
    val epochSeconds: Long,
    @JsonNames("epochMillis")
    val epochMillis: Long,
)