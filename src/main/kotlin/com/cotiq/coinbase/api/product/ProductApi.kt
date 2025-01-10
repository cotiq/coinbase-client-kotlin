package com.cotiq.coinbase.api.product

import com.cotiq.coinbase.api.ApiCall
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.CoroutineScope

class ProductApi(
    private val httpClient: HttpClient,
    private val scope: CoroutineScope,
) {

    /**
     * Get the best bid/ask for all products. A subset of all products can be returned instead by using the product_ids input.
     */
    fun getBestBidAsk(productIds: List<String> = emptyList()): ApiCall<BestBidAskResponse> = ApiCall(scope) {
        httpClient.get("best_bid_ask") {
            productIds.forEach { productId ->
                url {
                    parameters.append("product_ids", productId)
                }
            }
        }.body()
    }

    /**
     * Get information on a single product by product ID.
     * @param id The trading pair (e.g. 'BTC-USD').
     */
    fun getProduct(id: String): ApiCall<Product> = ApiCall(scope) {
        httpClient.get("products/$id").body()
    }

    /**
     * Get a list of bids/asks for a single product. The amount of detail shown can be customized with the limit parameter.
     * @param productId The trading pair (e.g. 'BTC-USD').
     * @param limit The number of bid/asks to be returned.
     * @param aggregationPriceIncrement The minimum price intervals at which buy and sell orders are grouped or combined in the order book.
     */
    fun getProductBook(
        productId: String,
        limit: Int? = null,
        aggregationPriceIncrement: String? = null
    ): ApiCall<ProductBookResponse> = ApiCall(scope) {
        httpClient.get("product_book") {
            url {
                parameters.append("product_id", productId)
                limit?.let { parameters.append("limit", it.toString()) }
                aggregationPriceIncrement?.let { parameters.append("aggregation_price_increment", it) }
            }
        }.body()
    }

    /**
     * Gets a list of available currency pairs for trading.
     * @param limit The number of products to be returned.
     * @param offset The number of products to skip before returning.
     * @param productType Only returns the orders matching this product type. By default, returns all product types.
     * @param productIds The list of trading pairs (e.g. 'BTC-USD').
     */
    fun getProducts(
        limit: Int? = null,
        offset: Int? = null,
        productType: ProductType? = null,
        productIds: List<String> = emptyList()
    ): ApiCall<ProductsResponse> = ApiCall(scope) {
        httpClient.get("products") {
            url {
                limit?.let { parameters.append("limit", it.toString()) }
                offset?.let { parameters.append("offset", it.toString()) }
                productType?.let { parameters.append("product_type", it.name) }
                productIds.forEach { productId ->
                    parameters.append("product_ids", productId)
                }
            }
        }.body()
    }

    /**
     * Get snapshot information by product ID about the last trades (ticks) and best bid/ask.
     * @param id The trading pair (e.g. 'BTC-USD').
     * @param limit The number of trades to be returned.
     * @param start The UNIX timestamp indicating the start of the time interval.
     * @param end The UNIX timestamp indicating the end of the time interval.
     */
    fun getProductMarketTrades(
        id: String,
        limit: Int,
        start: Long? = null,
        end: Long? = null
    ): ApiCall<ProductMarketTradesResponse> = ApiCall(scope) {
        httpClient.get("products/$id/ticker") {
            url {
                parameters.append("limit", limit.toString())
                start?.let { parameters.append("start", it.toString()) }
                end?.let { parameters.append("end", it.toString()) }
            }
        }.body()
    }

    /**
     * Get rates for a single product by product ID, grouped in buckets.
     * @param id The trading pair (e.g. 'BTC-USD').
     * @param start The UNIX timestamp indicating the start of the time interval.
     * @param end The UNIX timestamp indicating the end of the time interval.
     * @param granularity The timeframe each candle represents.
     * @param limit The number of candle buckets to be returned. By default, returns 350 (max 350).
     */
    fun getProductCandles(
        id: String,
        start: Long,
        end: Long,
        granularity: Candle.Granularity,
        limit: Int? = null
    ): ApiCall<ProductCandlesResponse> = ApiCall(scope) {
        httpClient.get("products/$id/candles") {
            url {
                parameters.append("start", start.toString())
                parameters.append("end", end.toString())
                parameters.append("granularity", granularity.name)
                limit?.let { parameters.append("limit", it.toString()) }
            }
        }.body()
    }
}