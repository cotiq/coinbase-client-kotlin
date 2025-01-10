package com.cotiq.coinbase.api.fee

import kotlinx.serialization.Serializable

/**
 * Represents the response of a successful operation.
 *
 * @property totalVolume Total volume across assets, denoted in USD.
 * @property totalFees Total fees across assets, denoted in USD.
 * @property feeTier Description of maker and taker rates across all applicable fee tiers.
 * @property marginRate Margin rate, only applicable to product_type `FUTURE`.
 * @property goodsAndServicesTax Goods and Services Tax details.
 * @property advancedTradeOnlyVolume Advanced Trade volume (non-inclusive of Pro) across assets, denoted in USD.
 * @property advancedTradeOnlyFees Advanced Trade fees (non-inclusive of Pro) across assets, denoted in USD.
 * @property coinbaseProVolume Coinbase Pro volume across assets, denoted in USD.
 * @property coinbaseProFees Coinbase Pro fees across assets, denoted in USD.
 * @property totalBalance Total balance across assets and products, denoted in USD.
 */
@Serializable
data class TransactionSummary(
    val totalVolume: String,
    val totalFees: String,
    val feeTier: FeeTier,
    val marginRate: MarginRate? = null,
    val goodsAndServicesTax: GoodsAndServicesTax? = null,
    val advancedTradeOnlyVolume: String? = null,
    val advancedTradeOnlyFees: String? = null,
    val coinbaseProVolume: String? = null,
    val coinbaseProFees: String? = null,
    val totalBalance: String? = null
)

/**
 * Represents the fee tier details.
 *
 * @property pricingTier Pricing tier for user, determined by notional (USD) volume.
 * @property usdFrom Lower bound (inclusive) of pricing tier in notional volume.
 * @property usdTo Upper bound (exclusive) of pricing tier in notional volume.
 * @property takerFeeRate Taker fee rate, applied if the order takes liquidity.
 * @property makerFeeRate Maker fee rate, applied if the order creates liquidity.
 * @property aopFrom Lower bound (inclusive) of pricing tier in USD of total assets on platform.
 * @property aopTo Upper bound (exclusive) of pricing tier in USD of total assets on platform.
 */
@Serializable
data class FeeTier(
    val pricingTier: String,
    val usdFrom: String,
    val usdTo: String,
    val takerFeeRate: String,
    val makerFeeRate: String,
    val aopFrom: String,
    val aopTo: String
)

/**
 * Represents the margin rate.
 *
 * @property value String representation allows for unlimited precision.
 */
@Serializable
data class MarginRate(
    val value: String
)

/**
 * Represents the Goods and Services Tax details.
 *
 * @property rate GST rate.
 * @property type GST type
 */
@Serializable
data class GoodsAndServicesTax(
    val rate: String,
    val type: Type,
) {
    enum class Type {
        INCLUSIVE,
        EXCLUSIVE
    }
}