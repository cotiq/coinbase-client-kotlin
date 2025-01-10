package com.cotiq.coinbase.websocket.message

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * The Coinbase Advanced Trade WebSocket channels that can be subscribed to.
 * For more info see [Advanced Trade WebSocket Channels](https://docs.cdp.coinbase.com/advanced-trade/docs/ws-channels)
 *
 * @property channelName The name of the channel
 */
@Serializable(with = FeedChannel.Serializer::class)
enum class FeedChannel(val channelName: String) {
    Unknown("unknown"),
    Heartbeats("heartbeats"),
    Candles("candles"),
    Status("status"),
    Ticker("ticker"),
    TickerBatch("ticker_batch"),
    Level2("level2"),
    User("user"),
    MarketTrades("market_trades"),
    FuturesBalanceSummary("futures_balance_summary");

    internal object Serializer : KSerializer<FeedChannel> {
        private val entriesByChannelName = entries.associateBy { it.channelName }

        override val descriptor: SerialDescriptor =
            PrimitiveSerialDescriptor("com.cotiq.coinbase.websocket.message.FeedChannel", PrimitiveKind.STRING)

        override fun serialize(encoder: Encoder, value: FeedChannel) =
            encoder.encodeString(value.channelName)

        override fun deserialize(decoder: Decoder): FeedChannel =
            entriesByChannelName[decoder.decodeString()] ?: Unknown
    }

}
