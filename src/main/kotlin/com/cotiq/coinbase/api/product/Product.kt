package com.cotiq.coinbase.api.product

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

enum class ProductMarketTradeSide {
    BUY, SELL
}

/**
 * Represents a market trade for a product.
 *
 * @property tradeId The ID of the trade that was placed.
 * @property productId The trading pair (e.g. 'BTC-USD').
 * @property price The price of the trade, in quote currency.
 * @property size The size of the trade, in base currency.
 * @property time The time of the trade in RFC3339 format.
 * @property side The side of the trade.
 * @property exchange The exchange where the trade was placed.
 */
@Serializable
data class ProductMarketTrade(
    val tradeId: String,
    val productId: String,
    val price: String,
    val size: String,
    val time: String,
    val side: ProductMarketTradeSide,
    val exchange: String,
)

@Serializable
data class ProductMarketTradesResponse(
    val trades: List<ProductMarketTrade>,
    val bestBid: String,
    val bestAsk: String,
)

@Serializable
data class Candle(
    val start: String,
    val low: String,
    val high: String,
    val open: String,
    val close: String,
    val volume: String,
) {
    enum class Granularity {
        UNKNOWN_GRANULARITY, ONE_MINUTE, FIVE_MINUTE, FIFTEEN_MINUTE, ONE_HOUR, TWO_HOUR, SIX_HOUR, ONE_DAY
    }
}

@Serializable
data class ProductCandlesResponse(
    val candles: List<Candle>,
)

@Serializable
data class ProductBookResponse(
    val pricebook: PriceBook,
    val last: String,
    val midMarket: String,
    val spreadBps: String,
    val spreadAbsolute: String,
)

@Serializable
data class PriceBook(
    val productId: String,
    val bids: List<BookEntry>,
    val asks: List<BookEntry>,
    val time: String,
)

@Serializable
data class BookEntry(
    val price: String,
    val size: String,
)

@Serializable
data class BestBidAskResponse(
    val pricebooks: List<PriceBook>,
)

/**
 * Represents a response containing a list of products.
 *
 * @property products Array of objects, each representing one product.
 * @property numProducts Number of products that were returned.
 */
@Serializable
data class ProductsResponse(
    val products: List<Product>,
    val numProducts: Int,
)

/**
 * Represents a product in the trading system.
 *
 * @property productId The trading pair (e.g. 'BTC-USD').
 * @property price The current price for the product, in quote currency.
 * @property pricePercentageChange24h The amount the price of the product has changed, in percent, in the last 24 hours.
 * @property volume24h The trading volume for the product in the last 24 hours.
 * @property volumePercentageChange24h The amount the volume of the product has changed, in percent, in the last 24 hours.
 * @property baseIncrement Minimum amount base value can be increased or decreased at once.
 * @property quoteIncrement Minimum amount quote value can be increased or decreased at once.
 * @property quoteMinSize Minimum size that can be represented of quote currency.
 * @property quoteMaxSize Maximum size that can be represented of quote currency.
 * @property baseMinSize Minimum size that can be represented of base currency.
 * @property baseMaxSize Maximum size that can be represented of base currency.
 * @property baseName Name of the base currency.
 * @property quoteName Name of the quote currency.
 * @property watched Whether the product is on the user's watchlist.
 * @property isDisabled Whether the product is disabled for trading.
 * @property new Whether the product is 'new'.
 * @property status The status of the product.
 * @property cancelOnly Whether or not orders of the product can only be cancelled, not placed or edited.
 * @property limitOnly Whether or not orders of the product can only be limit orders, not market orders.
 * @property postOnly Whether or not orders of the product can only be posted, not cancelled.
 * @property tradingDisabled Whether the product is disabled for trading for all market participants.
 * @property auctionMode Whether the product is in auction mode.
 * @property productType The type of product.
 * @property quoteCurrencyId Symbol of the quote currency.
 * @property baseCurrencyId Symbol of the base currency.
 * @property midMarketPrice The current midpoint of the bid-ask spread, in quote currency.
 * @property alias Product id for the corresponding unified book.
 * @property aliasTo Product ids that this product serves as an alias for.
 * @property baseDisplaySymbol Symbol of the base display currency.
 * @property quoteDisplaySymbol Symbol of the quote display currency.
 * @property viewOnly Reflects whether an FCM product has expired. For SPOT, set get_tradability_status to get a return value here. Defaulted to false for all other product types.
 * @property priceIncrement Minimum amount price can be increased or decreased at once.
 * @property displayName Display name for the product e.g. BTC-PERP-INTX => BTC PERP.
 * @property productVenue The sole venue id for the product. Defaults to CBE if the product is not specific to a single venue.
 * @property approximateQuote24hVolume The approximate trading volume for the product in the last 24 hours based on the current quote.
 */
@Serializable
@OptIn(ExperimentalSerializationApi::class)
data class Product(
    val productId: String,
    val price: String,
    @JsonNames("price_percentage_change_24h")
    val pricePercentageChange24h: String,
    @JsonNames("volume_24h")
    val volume24h: String,
    @JsonNames("volume_percentage_change_24h")
    val volumePercentageChange24h: String,
    val baseIncrement: String,
    val quoteIncrement: String,
    val quoteMinSize: String,
    val quoteMaxSize: String,
    val baseMinSize: String,
    val baseMaxSize: String,
    val baseName: String,
    val quoteName: String,
    val watched: Boolean,
    val isDisabled: Boolean,
    val new: Boolean,
    val status: String,
    val cancelOnly: Boolean,
    val limitOnly: Boolean,
    val postOnly: Boolean,
    val tradingDisabled: Boolean,
    val auctionMode: Boolean,
    val productType: ProductType,
    val quoteCurrencyId: String,
    val baseCurrencyId: String,
    val midMarketPrice: String,
    val alias: String,
    val aliasTo: List<String>,
    val baseDisplaySymbol: String,
    val quoteDisplaySymbol: String,
    val viewOnly: Boolean,
    val priceIncrement: String,
    val displayName: String,
    val productVenue: ProductVenue,
    @JsonNames("approximate_quote_24h_volume")
    val approximateQuote24hVolume: String,
)

enum class ProductType {
    UNKNOWN_PRODUCT_TYPE, SPOT, FUTURE
}

enum class ProductVenue {
    UNKNOWN_VENUE_TYPE, CBE, FCM, INTX
}