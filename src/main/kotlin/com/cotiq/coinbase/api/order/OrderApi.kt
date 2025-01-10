package com.cotiq.coinbase.api.order

import com.cotiq.coinbase.api.ApiCall
import com.cotiq.coinbase.api.product.ProductType
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope

class OrderApi(
    private val httpClient: HttpClient,
    private val scope: CoroutineScope,
) {

    /**
     * Get a list of orders
     * @param orderIds ID(s) of order(s).
     * @param productIds Optional string of the product ID(s). Defaults to null, or fetch for all products.
     * @param productType Returns orders matching this product type. By default, returns all product types.
     * @param orderStatus Only returns orders matching the specified order statuses.
     * @param timeInForces Only orders matching this time in force(s) are returned. Default is to return all time in forces.
     * @param orderTypes Only returns orders matching the specified order types (e.g. MARKET). By default, returns all order types.
     * @param orderSide Only returns the orders matching the specified side (e.g. 'BUY', 'SELL'). By default, returns all sides.
     * @param startDate The start date in RFC3339 format to fetch orders from (inclusive). If provided, only orders created after this date will be returned.
     * @param endDate The end date in RFC3339 format to fetch orders from (exclusive). If provided, only orders with creation time before this date will be returned.
     * @param orderPlacementSource Only returns the orders matching this placement source. By default, returns RETAIL_ADVANCED placement source.
     * @param contractExpiryType Only returns the orders matching the contract expiry type. Only applicable if product_type is set to FUTURE.
     * @param assetFilters Only returns the orders where the quote, base or underlying asset matches the provided asset filter(s) (e.g. 'BTC').
     * @param limit The number of orders to display per page (no default amount). If 'has_next' is true, additional pages of orders are available to be fetched. Use the cursor parameter to start on a specified page.
     * @param cursor For paginated responses, returns all responses that come after this value.
     * @param sortBy Sort results by a field, results use unstable pagination. Default is to sort by creation time.
     */
    fun getOrders(
        orderIds: List<String> = emptyList(),
        productIds: List<String> = emptyList(),
        productType: ProductType? = null,
        orderStatus: List<OrderStatus> = emptyList(),
        timeInForces: List<TimeInForce> = emptyList(),
        orderTypes: List<OrderType> = emptyList(),
        orderSide: OrderSide? = null,
        startDate: String? = null,
        endDate: String? = null,
        orderPlacementSource: OrderPlacementSource? = null,
        contractExpiryType: ContractExpiryType? = null,
        assetFilters: List<String> = emptyList(),
        limit: Int? = null,
        cursor: String? = null,
        sortBy: SortBy? = null,
    ): ApiCall<ListOrdersResponse> = ApiCall(scope) {
        httpClient.get("orders/historical/batch") {
            url {
                orderIds.forEach { parameters.append("order_ids", it) }
                productIds.forEach { parameters.append("product_ids", it) }
                productType?.let { parameters.append("product_type", it.name) }
                orderStatus.forEach { parameters.append("order_status", it.name) }
                timeInForces.forEach { parameters.append("time_in_forces", it.name) }
                orderTypes.forEach { parameters.append("order_types", it.name) }
                orderSide?.let { parameters.append("order_side", it.name) }
                startDate?.let { parameters.append("start_date", it) }
                endDate?.let { parameters.append("end_date", it) }
                orderPlacementSource?.let { parameters.append("order_placement_source", it.name) }
                contractExpiryType?.let { parameters.append("contract_expiry_type", it.name) }
                assetFilters.forEach { parameters.append("asset_filters", it) }
                limit?.let { parameters.append("limit", it.toString()) }
                cursor?.let { parameters.append("cursor", it) }
                sortBy?.let { parameters.append("sort_by", it.name) }
            }
        }.body()
    }

    /**
     * Get a single order by order ID.
     * @param orderId The ID of the order.
     */
    fun getOrder(orderId: String): ApiCall<GetOrderResponse> = ApiCall(scope) {
        httpClient.get("orders/historical/$orderId").body()
    }

    /**
     * Initiate cancel requests for one or more orders.
     * @param orderIds The order ID.
     */
    fun cancelOrders(orderIds: List<String>): ApiCall<CancelOrderResponse> = ApiCall(scope) {
        httpClient.post("orders/batch_cancel") {
            contentType(ContentType.Application.Json)
            setBody(CancelOrderRequest(orderIds))
        }.body()
    }

    /**
     * Preview an order.
     * @param request The order request.
     */
    fun previewOrder(request: PreviewOrderRequest): ApiCall<PreviewOrderResponse> = ApiCall(scope) {
        httpClient.post("orders/preview") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    /**
     * Create an order.
     */
    fun createOrder(request: CreateOrderRequest): ApiCall<CreateOrderResponse> = ApiCall(scope) {
        httpClient.post("orders") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }
}

