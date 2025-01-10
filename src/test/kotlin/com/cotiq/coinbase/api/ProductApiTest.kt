package com.cotiq.coinbase.api

import com.cotiq.coinbase.api.product.Candle
import com.cotiq.coinbase.api.product.ProductMarketTradeSide
import com.cotiq.coinbase.api.product.ProductType
import com.cotiq.coinbase.api.product.ProductVenue
import com.cotiq.coinbase.createTestApiClient
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ProductApiTest {

    @Test
    fun `GET products returns list of products`() {
        val productsResponse = createTestApiClient("get_products")
            .productApi.getProducts().callBlocking()

        assertEquals(1, productsResponse.products.size)
        assertEquals(1, productsResponse.numProducts)
        with(productsResponse.products.first()) {
            assertEquals("SOL-USDC", productId)
            assertEquals("269.12", price)
            assertEquals("6.79788880511131", pricePercentageChange24h)
            assertEquals("4992749.46269898", volume24h)
            assertEquals("5.37125033365961", volumePercentageChange24h)
            assertEquals("0.00000001", baseIncrement)
            assertEquals("0.01", quoteIncrement)
            assertEquals("1", quoteMinSize)
            assertEquals("25000000", quoteMaxSize)
            assertEquals("0.00000001", baseMinSize)
            assertEquals("1274000", baseMaxSize)
            assertEquals("Solana", baseName)
            assertEquals("USDC", quoteName)
            assertFalse(watched)
            assertFalse(isDisabled)
            assertFalse(new)
            assertEquals("online", status)
            assertFalse(cancelOnly)
            assertFalse(limitOnly)
            assertFalse(postOnly)
            assertFalse(tradingDisabled)
            assertFalse(auctionMode)
            assertEquals(ProductType.SPOT, productType)
            assertEquals("USDC", quoteCurrencyId)
            assertEquals("SOL", baseCurrencyId)
            assertEquals("", midMarketPrice)
            assertEquals("SOL-USD", alias)
            assertTrue(aliasTo.isEmpty())
            assertEquals("SOL", baseDisplaySymbol)
            assertEquals("USD", quoteDisplaySymbol)
            assertFalse(viewOnly)
            assertEquals("0.01", priceIncrement)
            assertEquals("SOL-USDC", displayName)
            assertEquals(ProductVenue.CBE, productVenue)
            assertEquals("1343648735.4", approximateQuote24hVolume)
        }
    }

    @Test
    fun `GET product returns product details`() {
        val product = createTestApiClient("get_product")
            .productApi.getProduct("BTC-USD").callBlocking()

        with(product) {
            assertEquals("BTC-USD", productId)
            assertEquals("104780.43", price)
            assertEquals("0.6844554620132", pricePercentageChange24h)
            assertEquals("7638.00857182", volume24h)
            assertEquals("-41.41881120025845", volumePercentageChange24h)
            assertEquals("0.00000001", baseIncrement)
            assertEquals("0.01", quoteIncrement)
            assertEquals("1", quoteMinSize)
            assertEquals("150000000", quoteMaxSize)
            assertEquals("0.00000001", baseMinSize)
            assertEquals("3400", baseMaxSize)
            assertEquals("Bitcoin", baseName)
            assertEquals("US Dollar", quoteName)
            assertTrue(watched)
            assertFalse(isDisabled)
            assertFalse(new)
            assertEquals("online", status)
            assertFalse(cancelOnly)
            assertFalse(limitOnly)
            assertFalse(postOnly)
            assertFalse(tradingDisabled)
            assertFalse(auctionMode)
            assertEquals(ProductType.SPOT, productType)
            assertEquals("USD", quoteCurrencyId)
            assertEquals("BTC", baseCurrencyId)
            assertEquals("", midMarketPrice)
            assertEquals("", alias)
            assertEquals(1, aliasTo.size)
            assertEquals("BTC-USDC", aliasTo.first())
            assertEquals("BTC", baseDisplaySymbol)
            assertEquals("USD", quoteDisplaySymbol)
            assertFalse(viewOnly)
            assertEquals("0.01", priceIncrement)
            assertEquals("BTC-USD", displayName)
            assertEquals(ProductVenue.CBE, productVenue)
            assertEquals("800313822.5", approximateQuote24hVolume)
        }
    }

    @Test
    fun `GET best_bid_ask returns list of pricebooks`() {
        val bestBidAskResponse = createTestApiClient("get_best_bid_ask")
            .productApi.getBestBidAsk(listOf("BTC-USD")).callBlocking()

        assertEquals(1, bestBidAskResponse.pricebooks.size)
        with(bestBidAskResponse.pricebooks.first()) {
            assertEquals("BTC-USD", productId)
            assertEquals("101722.02", asks.first().price)
            assertEquals("0.00403655", asks.first().size)
            assertEquals("101716.47", bids.first().price)
            assertEquals("0.00004292", bids.first().size)
            assertEquals("2025-01-21T06:18:27.904951Z", time)
        }
    }

    @Test
    fun `GET product_book returns product book`() {
        val productBookResponse = createTestApiClient("get_product_book")
            .productApi.getProductBook("BTC-USD", 3)
            .callBlocking()

        with(productBookResponse) {
            assertEquals("102240.845", last)
            assertEquals("102240.845", midMarket)
            assertEquals("0.000978082635", spreadBps)
            assertEquals("0.01", spreadAbsolute)
        }
        with(productBookResponse.pricebook) {
            assertEquals("BTC-USD", productId)
            assertEquals(3, bids.size)
            assertEquals("102240.84", bids[0].price)
            assertEquals("0.0811067", bids[0].size)
            assertEquals("102235.58", bids[1].price)
            assertEquals("0.08262611", bids[1].size)
            assertEquals("102234.4", bids[2].price)
            assertEquals("0.03249858", bids[2].size)
            assertEquals(3, asks.size)
            assertEquals("102240.85", asks[0].price)
            assertEquals("0.04385283", asks[0].size)
            assertEquals("102247.39", asks[1].price)
            assertEquals("0.0349621", asks[1].size)
            assertEquals("102250.33", asks[2].price)
            assertEquals("0.001", asks[2].size)
            assertEquals("2025-01-21T07:15:55.518582Z", time)
        }
    }

    @Test
    fun `GET product candles returns product candles`() {
        val productCandlesResponse = createTestApiClient("get_product_candles")
            .productApi.getProductCandles(
                id = "BTC-USD",
                start = 1640991600,
                end = 1643670000,
                granularity = Candle.Granularity.ONE_DAY,
                limit = 2
            )
            .callBlocking()

        assertEquals(2, productCandlesResponse.candles.size)
        with(productCandlesResponse.candles[0]) {
            assertEquals("1643587200", start)
            assertEquals("36640.94", low)
            assertEquals("38785.04", high)
            assertEquals("37904.99", open)
            assertEquals("38491.93", close)
            assertEquals("19654.98487168", volume)
        }
        with(productCandlesResponse.candles[1]) {
            assertEquals("1643500800", start)
            assertEquals("37365.31", low)
            assertEquals("38384.07", high)
            assertEquals("38189.82", open)
            assertEquals("37901.35", close)
            assertEquals("10632.53591257", volume)
        }
    }

    @Test
    fun `GET product market trades returns product market trades`() {
        val productMarketTradesResponse = createTestApiClient("get_product_market_trades")
            .productApi.getProductMarketTrades(id = "BTC-USD", start = 1640991600, end = 1643670000, limit = 2)
            .callBlocking()

        assertEquals(2, productMarketTradesResponse.trades.size)
        assertEquals("102605.48", productMarketTradesResponse.bestBid)
        assertEquals("102605.49", productMarketTradesResponse.bestAsk)

        with(productMarketTradesResponse.trades[0]) {
            assertEquals("765658830", tradeId)
            assertEquals("BTC-USD", productId)
            assertEquals("102605.49", price)
            assertEquals("0.0000193", size)
            assertEquals("2025-01-23T08:02:10.985501Z", time)
            assertEquals(ProductMarketTradeSide.SELL, side)
            assertEquals("coinbase", exchange)
        }
        with(productMarketTradesResponse.trades[1]) {
            assertEquals("765658829", tradeId)
            assertEquals("BTC-USD", productId)
            assertEquals("102605.49", price)
            assertEquals("0.00649983", size)
            assertEquals("2025-01-23T08:02:10.660749Z", time)
            assertEquals(ProductMarketTradeSide.SELL, side)
            assertEquals("coinbase", exchange)
        }
    }

}