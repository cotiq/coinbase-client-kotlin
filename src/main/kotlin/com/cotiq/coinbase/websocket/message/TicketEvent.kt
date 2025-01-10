package com.cotiq.coinbase.websocket.message

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class TickerEvent(
    val type: String,
    val tickers: List<Ticker>,
): Event

@Serializable
data class TickerBatchEvent(
    val type: String,
    val tickers: List<Ticker>,
): Event

@Serializable
@OptIn(ExperimentalSerializationApi::class)
data class Ticker(
    val type: String,
    val productId: String,
    val price: String,
    @JsonNames("volume_24_h")
    val volume24h: String,
    @JsonNames("low_24_h")
    val low24h: String,
    @JsonNames("high_24_h")
    val high24h: String,
    @JsonNames("low_52_w")
    val low52w: String,
    @JsonNames("high_52_w")
    val high52w: String,
    @JsonNames("price_percent_chg_24_h")
    val pricePercentChange24h: String,
    val bestBid: String,
    val bestBidQuantity: String,
    val bestAsk: String,
    val bestAskQuantity: String,
): Event