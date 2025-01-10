package com.cotiq.coinbase.api.order

import com.cotiq.coinbase.api.product.ProductType
import kotlinx.serialization.Serializable

/**
 * Represents a request to create an order.
 *
 * @property clientOrderId A unique ID provided for the order (used for identification purposes). If the ID provided is not unique, the order will not be created and the order corresponding with that ID will be returned instead.
 * @property productId The trading pair (e.g. 'BTC-USD').
 * @property side The side of the market that the order is on.
 * @property orderConfiguration The configuration of the order (e.g. the order type, size, etc.).
 * @property leverage The amount of leverage for the order (default is 1.0).
 * @property marginType Margin Type for this order (default is CROSS).
 * @property previewId Preview ID for this order, to associate this order with a preview request.
 */
@Serializable
data class CreateOrderRequest(
    val clientOrderId: String,
    val productId: String,
    val side: OrderSide,
    val orderConfiguration: OrderConfiguration,
    val leverage: String? = null,
    val marginType: MarginType? = null,
    val previewId: String? = null,
)

@Serializable
data class CreateOrderResponse(
    val success: Boolean,
    val successResponse: SuccessResponse? = null,
    val errorResponse: ErrorResponse? = null,
    val orderConfiguration: OrderConfiguration? = null,
) {
    @Serializable
    data class SuccessResponse(
        val orderId: String,
        val productId: String,
        val side: OrderSide,
        val clientOrderId: String,
    )

    @Serializable
    data class ErrorResponse(
        val error: String? = null,
        val message: String? = null,
        val errorDetails: String? = null,
        val previewFailureReason: String? = null,
        val newOrderFailureReason: String? = null,
    )
}

/**
 * Represents a request to preview an order.
 *
 * @property productId The trading pair (e.g. 'BTC-USD').
 * @property side The side of the market that the order is on.
 * @property orderConfiguration The configuration of the order (e.g. the order type, size, etc.).
 * @property leverage The amount of leverage for the order (default is 1.0).
 * @property marginType Margin Type for this order (default is CROSS).
 */
@Serializable
data class PreviewOrderRequest(
    val productId: String,
    val side: OrderSide,
    val orderConfiguration: OrderConfiguration,
    val leverage: String? = null,
    val marginType: MarginType? = null,
)

/**
 * Represents a response to preview an order.
 *
 * @property orderTotal The total amount of the order.
 * @property commissionTotal Currency amount of the applied commission (so not the rate that was used on input).
 * @property errs List of potential failure reasons were this order to be submitted.
 * @property warning List of warnings related to the order.
 * @property quoteSize The amount of the second Asset in the Trading Pair. For example, on the BTC/USD Order Book, USD is the Quote Asset.
 * @property baseSize The amount of the first Asset in the Trading Pair. For example, on the BTC-USD Order Book, BTC is the Base Asset.
 * @property bestBid The best bid price.
 * @property bestAsk The best ask price.
 * @property isMax Indicates whether tradable_balance should be set to the maximum amount.
 * @property orderMarginTotal The total margin for the order.
 * @property leverage The amount of leverage for the order (default is 1.0).
 * @property longLeverage The long leverage for the order.
 * @property shortLeverage The short leverage for the order.
 * @property slippage The slippage for the order.
 * @property previewId The ID of the preview.
 * @property currentLiquidationBuffer The current liquidation buffer.
 * @property projectedLiquidationBuffer The projected liquidation buffer.
 * @property maxLeverage The maximum leverage for the order.
 */
@Serializable
data class PreviewOrderResponse(
    val orderTotal: String,
    val commissionTotal: String,
    val errs: List<String>,
    val warning: List<String>,
    val quoteSize: String,
    val baseSize: String,
    val bestBid: String,
    val bestAsk: String,
    val isMax: Boolean,
    val orderMarginTotal: String? = null,
    val leverage: String? = null,
    val longLeverage: String? = null,
    val shortLeverage: String? = null,
    val slippage: String? = null,
    val previewId: String? = null,
    val currentLiquidationBuffer: String? = null,
    val projectedLiquidationBuffer: String? = null,
    val maxLeverage: String? = null
)

@Serializable
data class CancelOrderRequest(
    val orderIds: List<String>,
)

@Serializable
data class CancelOrderResponse(
    val results: List<Result>
) {
    @Serializable
    data class Result(
        val success: Boolean,
        val failureReason: FailureReason,
        val orderId: String
    )

    enum class FailureReason {
        UNKNOWN_CANCEL_FAILURE_REASON,
        INVALID_CANCEL_REQUEST,
        UNKNOWN_CANCEL_ORDER,
        COMMANDER_REJECTED_CANCEL_ORDER,
        DUPLICATE_CANCEL_REQUEST,
        INVALID_CANCEL_PRODUCT_ID,
        INVALID_CANCEL_FCM_TRADING_SESSION,
        NOT_ALLOWED_TO_CANCEL,
        ORDER_IS_FULLY_FILLED,
        ORDER_IS_BEING_REPLACED
    }
}

/**
 * Represents a response to get an order.
 *
 * @property order The retrieved order.
 */
@Serializable
data class GetOrderResponse(
    val order: Order,
)

/**
 * Represents a response to list orders.
 *
 * @property orders A list of orders matching the query.
 * @property hasNext Whether there are additional pages for this query.
 * @property cursor For paginated responses, returns all responses that come after this value.
 */
@Serializable
data class ListOrdersResponse(
    val orders: List<Order>,
    val hasNext: Boolean,
    val cursor: String,
)

/**
 * Represents an order.
 *
 * @property orderId The ID of the order.
 * @property productId The trading pair (e.g. 'BTC-USD').
 * @property userId The id of the User owning this Order.
 * @property orderConfiguration The configuration of the order (e.g. the order type, size, etc.).
 * @property side The side of the market that the order is on.
 * @property clientOrderId The unique ID provided for the order (used for identification purposes).
 * @property status The current state of the order.
 * @property timeInForce The client specified window for which the order can remain active.
 * @property createdTime Timestamp in RFC3339 format for when the order was created.
 * @property completionPercentage The percent of total order amount that has been filled.
 * @property filledSize The portion (in base currency) of total order amount that has been filled.
 * @property averageFilledPrice The average of all prices of fills for this order.
 * @property numberOfFills Number of fills that have been posted for this order.
 * @property filledValue The portion (in quote current) of total order amount that has been filled.
 * @property pendingCancel Whether a cancel request has been initiated for the order, and not yet completed.
 * @property sizeInQuote Whether the order was placed with quote currency.
 * @property totalFees The total fees for the order.
 * @property sizeInclusiveOfFees Whether the order size includes fees.
 * @property totalValueAfterFees Derived field: filled_value + total_fees for 'BUY' orders and filled_value - total_fees for 'SELL' orders.
 * @property triggerStatus The trigger status of the order, with respect to stop price.
 * @property orderType Type of the order.
 * @property rejectReason The reason for rejecting the order.
 * @property settled True if the order is fully filled, false otherwise.
 * @property productType The type of order, i.e. Spot or Future.
 * @property rejectMessage Message stating why the order was rejected.
 * @property cancelMessage Message stating why the order was canceled.
 * @property orderPlacementSource Message stating which source an order was placed from.
 * @property outstandingHoldAmount The remaining hold amount (holdAmount - holdAmountReleased). [value is 0 if holdReleased is true]
 * @property isLiquidation True if order is of liquidation type.
 * @property lastFillTime Time in RFC3339 of the most recent fill for this order.
 * @property editHistory An array of the latest 5 edits per order.
 * @property leverage The amount of leverage for the order (default is 1.0).
 * @property marginType Margin Type for this order (default is CROSS).
 * @property retailPortfolioId The ID of the portfolio this order is associated with.
 */
@Serializable
data class Order(
    val orderId: String,
    val productId: String,
    val userId: String,
    val orderConfiguration: OrderConfiguration,
    val side: OrderSide,
    val clientOrderId: String,
    val status: OrderStatus,
    val timeInForce: TimeInForce,
    val createdTime: String,
    val completionPercentage: String,
    val filledSize: String,
    val averageFilledPrice: String,
    val numberOfFills: String,
    val filledValue: String,
    val pendingCancel: Boolean,
    val sizeInQuote: Boolean,
    val totalFees: String,
    val sizeInclusiveOfFees: Boolean,
    val totalValueAfterFees: String,
    val triggerStatus: TriggerStatus,
    val orderType: OrderType,
    val rejectReason: RejectReason,
    val settled: Boolean,
    val productType: ProductType,
    val rejectMessage: String,
    val cancelMessage: String,
    val orderPlacementSource: OrderPlacementSource,
    val outstandingHoldAmount: String,
    val isLiquidation: Boolean,
    val lastFillTime: String? = null,
    val editHistory: List<OrderEdit>,
    val leverage: String,
    val marginType: MarginType,
    val retailPortfolioId: String,
)

/**
 * Represents the configuration of an order.
 *
 * @property marketMarketIoc Buy or sell a specified quantity of an Asset at the current best available market price.
 * @property sorLimitIoc Buy or sell a specified quantity of an Asset at a specified price. The Order will only post to the Order Book if it will immediately Fill; any remaining quantity is canceled.
 * @property limitLimitGtc Buy or sell a specified quantity of an Asset at a specified price. If posted, the Order will remain on the Order Book until canceled.
 * @property limitLimitGtd Buy or sell a specified quantity of an Asset at a specified price. If posted, the Order will remain on the Order Book until a certain time is reached or the Order is canceled.
 * @property limitLimitFok Buy or sell a specified quantity of an Asset at a specified price. The Order will only post to the Order Book if it is to immediately and completely Fill.
 * @property stopLimitStopLimitGtc Posts an Order to buy or sell a specified quantity of an Asset, but only if and when the last trade price on the Order Book equals or surpasses the Stop Price. If posted, the Order will remain on the Order Book until a certain time is reached or the Order.
 * @property stopLimitStopLimitGtd Posts an Order to buy or sell a specified quantity of an Asset, but only if and when the last trade price on the Order Book equals or surpasses the Stop Price. If posted, the Order will remain on the Order Book until a certain time is reached or the Order.
 * @property triggerBracketGtc A Limit Order to buy or sell a specified quantity of an Asset at a specified price, with stop limit order parameters embedded in the order. If posted, the Order will remain on the Order Book until canceled.
 * @property triggerBracketGtd A Limit Order to buy or sell a specified quantity of an Asset at a specified price, with stop limit order parameters embedded in the order. If posted, the Order will remain on the Order Book until a certain time is reached or the Order is canceled.
 */
@Serializable
data class OrderConfiguration(
    val marketMarketIoc: MarketMarketIoc? = null,
    val sorLimitIoc: SorLimitIoc? = null,
    val limitLimitGtc: LimitLimitGtc? = null,
    val limitLimitGtd: LimitLimitGtd? = null,
    val limitLimitFok: LimitLimitFok? = null,
    val stopLimitStopLimitGtc: StopLimitStopLimitGtc? = null,
    val stopLimitStopLimitGtd: StopLimitStopLimitGtd? = null,
    val triggerBracketGtc: TriggerBracketGtc? = null,
    val triggerBracketGtd: TriggerBracketGtd? = null,
)

/**
 * Represents an edit to an existing order.
 *
 * @property price The updated price of the order.
 * @property size The updated size of the order.
 * @property replaceAcceptTimestamp The timestamp when the edit was accepted.
 */
@Serializable
data class OrderEdit(
    val price: String? = null,
    val size: String? = null,
    val replaceAcceptTimestamp: String,
)

/**
 * Represents a market order with immediate-or-cancel (IOC) time in force.
 *
 * @property quoteSize The amount of the second Asset in the Trading Pair. For example, on the BTC/USD Order Book, USD is the Quote Asset.
 * @property baseSize The amount of the first Asset in the Trading Pair. For example, on the BTC-USD Order Book, BTC is the Base Asset.
 */
@Serializable
data class MarketMarketIoc(
    val quoteSize: String? = null,
    val baseSize: String? = null,
)

/**
 * Represents a limit order with immediate-or-cancel (IOC) time in force.
 *
 * @property quoteSize The amount of the second Asset in the Trading Pair. For example, on the BTC/USD Order Book, USD is the Quote Asset.
 * @property baseSize The amount of the first Asset in the Trading Pair. For example, on the BTC-USD Order Book, BTC is the Base Asset.
 * @property limitPrice The specified price, or better, that the Order should be executed at. A Buy Order will execute at or lower than the limit price. A Sell Order will execute at or higher than the limit price.
 */
@Serializable
data class SorLimitIoc(
    val quoteSize: String? = null,
    val baseSize: String? = null,
    val limitPrice: String,
)

/**
 * Represents a limit order with good-till-cancelled (GTC) time in force.
 *
 * @property quoteSize The amount of the second Asset in the Trading Pair. For example, on the BTC/USD Order Book, USD is the Quote Asset.
 * @property baseSize The amount of the first Asset in the Trading Pair. For example, on the BTC-USD Order Book, BTC is the Base Asset.
 * @property limitPrice The specified price, or better, that the Order should be executed at. A Buy Order will execute at or lower than the limit price. A Sell Order will execute at or higher than the limit price.
 * @property postOnly Enable or disable Post-only Mode. When enabled, only Maker Orders will be posted to the Order Book. Orders that will be posted as a Taker Order will be rejected.
 */
@Serializable
data class LimitLimitGtc(
    val quoteSize: String? = null,
    val baseSize: String? = null,
    val limitPrice: String,
    val postOnly: Boolean,
)

/**
 * Represents a limit order with good-till-date (GTD) time in force.
 *
 * @property quoteSize The amount of the second Asset in the Trading Pair. For example, on the BTC/USD Order Book, USD is the Quote Asset.
 * @property baseSize The amount of the first Asset in the Trading Pair. For example, on the BTC-USD Order Book, BTC is the Base Asset.
 * @property limitPrice The specified price, or better, that the Order should be executed at. A Buy Order will execute at or lower than the limit price. A Sell Order will execute at or higher than the limit price.
 * @property postOnly Enable or disable Post-only Mode. When enabled, only Maker Orders will be posted to the Order Book. Orders that will be posted as a Taker Order will be rejected.
 * @property endTime The time in RFC3339 format at which the order will be cancelled if it is not Filled.
 */
@Serializable
data class LimitLimitGtd(
    val quoteSize: String? = null,
    val baseSize: String? = null,
    val limitPrice: String,
    val postOnly: Boolean,
    val endTime: String,
)

/**
 * Represents a limit order with fill-or-kill (FOK) time in force.
 *
 * @property quoteSize The amount of the second Asset in the Trading Pair. For example, on the BTC/USD Order Book, USD is the Quote Asset.
 * @property baseSize The amount of the first Asset in the Trading Pair. For example, on the BTC-USD Order Book, BTC is the Base Asset.
 * @property limitPrice The specified price, or better, that the Order should be executed at. A Buy Order will execute at or lower than the limit price. A Sell Order will execute at or higher than the limit price.
 */
@Serializable
data class LimitLimitFok(
    val quoteSize: String? = null,
    val baseSize: String? = null,
    val limitPrice: String,
)

/**
 * Represents a stop limit order with a good-till-cancelled (GTC) time in force.
 *
 * @property quoteSize The amount of the second Asset in the Trading Pair. For example, on the BTC/USD Order Book, USD is the Quote Asset.
 * @property baseSize The amount of the first Asset in the Trading Pair. For example, on the BTC-USD Order Book, BTC is the Base Asset.
 * @property limitPrice The specified price, or better, that the Order should be executed at. A Buy Order will execute at or lower than the limit price. A Sell Order will execute at or higher than the limit price.
 * @property stopPrice The specified price that will trigger the placement of the Order.
 * @property stopDirection The direction of the stop limit Order. If Up, then the Order will trigger when the last trade price goes above the stop_price. If Down, then the Order will trigger when the last trade price goes below the stop_price.
 */
@Serializable
data class StopLimitStopLimitGtc(
    val quoteSize: String? = null,
    val baseSize: String? = null,
    val limitPrice: String,
    val stopPrice: String,
    val stopDirection: StopDirection,
)

/**
 * Represents a stop limit order with a good-till-date (GTD) time in force.
 *
 * @property quoteSize The amount of the second Asset in the Trading Pair. For example, on the BTC/USD Order Book, USD is the Quote Asset.
 * @property baseSize The amount of the first Asset in the Trading Pair. For example, on the BTC-USD Order Book, BTC is the Base Asset.
 * @property limitPrice The specified price, or better, that the Order should be executed at. A Buy Order will execute at or lower than the limit price. A Sell Order will execute at or higher than the limit price.
 * @property stopPrice The specified price that will trigger the placement of the Order.
 * @property stopDirection The direction of the stop limit Order. If Up, then the Order will trigger when the last trade price goes above the stop_price. If Down, then the Order will trigger when the last trade price goes below the stop_price.
 * @property endTime The time at which the order will be cancelled if it is not Filled.
 */
@Serializable
data class StopLimitStopLimitGtd(
    val quoteSize: String? = null,
    val baseSize: String? = null,
    val limitPrice: String,
    val stopPrice: String,
    val stopDirection: StopDirection,
    val endTime: String,
)

/**
 * Represents a trigger bracket order with a good-till-cancelled (GTC) time in force.
 *
 * @property baseSize The amount of the first Asset in the Trading Pair. For example, on the BTC-USD Order Book, BTC is the Base Asset.
 * @property limitPrice The specified price, or better, that the Order should be executed at. A Buy Order will execute at or lower than the limit price. A Sell Order will execute at or higher than the limit price.
 * @property stopTriggerPrice The price level (in quote currency) where the position will be exited. When triggered, a stop limit order is automatically placed with a limit price 5% higher for BUYS and 5% lower for SELLS.
 */
@Serializable
data class TriggerBracketGtc(
    val baseSize: String,
    val limitPrice: String,
    val stopTriggerPrice: String,
)

/**
 * Represents a trigger bracket order with a good-till-date (GTD) time in force.
 *
 * @property baseSize The amount of the first Asset in the Trading Pair. For example, on the BTC-USD Order Book, BTC is the Base Asset.
 * @property limitPrice The specified price, or better, that the Order should be executed at. A Buy Order will execute at or lower than the limit price. A Sell Order will execute at or higher than the limit price.
 * @property stopTriggerPrice The price level (in quote currency) where the position will be exited. When triggered, a stop limit order is automatically placed with a limit price 5% higher for BUYS and 5% lower for SELLS.
 * @property endTime The time in RFC3339 format at which the order will be cancelled if it is not Filled.
 */
@Serializable
data class TriggerBracketGtd(
    val baseSize: String,
    val limitPrice: String,
    val stopTriggerPrice: String,
    val endTime: String,
)

enum class StopDirection {
    STOP_DIRECTION_STOP_UP,
    STOP_DIRECTION_STOP_DOWN,
}

enum class MarginType {
    UNKNOWN_MARGIN_TYPE,
    CROSS,
    ISOLATED,
}

enum class OrderSide {
    BUY,
    SELL,
}

enum class OrderStatus {
    PENDING, OPEN, FILLED, CANCELLED, EXPIRED, FAILED, UNKNOWN_ORDER_STATUS, QUEUED, CANCEL_QUEUED,
}

enum class TimeInForce {
    UNKNOWN_TIME_IN_FORCE, GOOD_UNTIL_DATE_TIME, GOOD_UNTIL_CANCELLED, IMMEDIATE_OR_CANCEL, FILL_OR_KILL,
}

enum class TriggerStatus {
    UNKNOWN_TRIGGER_STATUS, INVALID_ORDER_TYPE, STOP_PENDING, STOP_TRIGGERED,
}

enum class OrderType {
    UNKNOWN_ORDER_TYPE, MARKET, LIMIT, STOP, STOP_LIMIT, BRACKET,
}

enum class RejectReason {
    REJECT_REASON_UNSPECIFIED, HOLD_FAILURE, TOO_MANY_OPEN_ORDERS, REJECT_REASON_INSUFFICIENT_FUNDS, RATE_LIMIT_EXCEEDED,
}

enum class OrderPlacementSource {
    UNKNOWN_PLACEMENT_SOURCE, RETAIL_SIMPLE, RETAIL_ADVANCED
}

enum class ContractExpiryType {
    UNKNOWN_CONTRACT_EXPIRY_TYPE, EXPIRING, PERPETUAL
}

enum class SortBy {
    UNKNOWN_SORT_BY, LIMIT_PRICE, LAST_FILL_TIME
}
