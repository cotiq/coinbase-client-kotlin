package com.cotiq.coinbase.websocket.message

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

internal class MessageDeserializationStrategy : DeserializationStrategy<Message<out Event>> {

    private val channelDecoders = mapOf(
        FeedChannel.Heartbeats.channelName to decoder<HeartbeatEvent>(),
        FeedChannel.Candles.channelName to decoder<CandleEvent>(),
        FeedChannel.Status.channelName to decoder<StatusEvent>(),
        FeedChannel.Ticker.channelName to decoder<Ticker>(),
        FeedChannel.TickerBatch.channelName to decoder<TickerBatchEvent>(),
        FeedChannel.Level2.channelName to decoder<Level2Event>(),
        FeedChannel.User.channelName to decoder<UserEvent>(),
        FeedChannel.MarketTrades.channelName to decoder<MarketTradesEvent>(),
        FeedChannel.FuturesBalanceSummary.channelName to decoder<FuturesBalanceSummaryEvent>()
    )

    override val descriptor: SerialDescriptor
        get() = JsonElement.serializer().descriptor

    override fun deserialize(decoder: Decoder): Message<out Event> {
        val jsonDecoder = decoder as? JsonDecoder
            ?: throw SerializationException("This deserialization strategy only supports JSON")
        val jsonObject = jsonDecoder.decodeJsonElement().jsonObject
        val channelName = jsonObject["channel"]?.jsonPrimitive?.content
            ?: throw SerializationException("Field 'channel' is missing in the message")

        val decoderFunction = channelDecoders[channelName] ?: throw SerializationException("Unsupported channel: $channelName")
        return decoderFunction(jsonObject, jsonDecoder.json)
    }

    private inline fun <reified T : Event> decoder(): (JsonObject, Json) -> Message<T> {
        return { jsonObject, json -> json.decodeFromJsonElement<Message<T>>(jsonObject) }
    }
}

