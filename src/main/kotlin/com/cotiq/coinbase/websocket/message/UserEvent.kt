package com.cotiq.coinbase.websocket.message

import kotlinx.serialization.Serializable


@Serializable
data class UserEvent(
    val type: String,
    val orders: List<Order>,
    val positions: Positions,
): Event {
    /**
     * Order details
     * @property avgPrice Average filled price of the order so far
     * @property cancelReason Reason for order cancellation
     * @property clientOrderId Unique identifier of order specified by client
     * @property completionPercentage Percentage of order completion
     * @property contractExpiryType Can be one of: UNKNOWN_CONTRACT_EXPIRY, EXPIRING, PERPETUAL
     * @property cumulativeQuantity Amount the order is filled, in base currency
     * @property filledValue Value of the filled order
     * @property leavesQuantity Amount remaining, in same currency as order was placed in (quote or base)
     * @property limitPrice Can be one of: 'Limit Price': Order is Limit or Stop Limit type, '0': Order is not Limit or Stop Limit type
     * @property numberOfFills Number of fills for the order
     * @property orderId Unique identifier of order
     * @property orderSide Can be one of: BUY, SELL
     * @property orderType Can be one of: LIMIT, MARKET, STOP_LIMIT
     * @property outstandingHoldAmount Outstanding hold amount for the order
     * @property postOnly Can be one of: true, false
     * @property productId The product ID for which this order was placed
     * @property productType Can be one of: UNKNOWN_PRODUCT_TYPE, SPOT, FUTURE
     * @property rejectReason Reason for order rejection
     * @property retailPortfolioId The ID of the portfolio this order is associated with.
     * @property riskManagedBy Can be one of: UNKNOWN_RISK_MANAGEMENT_TYPE, MANAGED_BY_FCM, MANAGED_BY_VENUE
     * @property status Can be one of: PENDING: Order is not yet open, OPEN: Order is waiting to be fully filled, FILLED: Order is 100% filled, CANCEL_QUEUED: Order queued to be cancelled by user or system, CANCELLED: Order was cancelled by user or system, EXPIRED: TWAP order was not filled by the expiry time, FAILED: Order cannot be placed at all
     * @property stopPrice Can be one of: 'Stop Price': Order is Stop Limit type, '0': Order is not Stop Limit type
     * @property timeInForce Can be one of: UNKNOWN_TIME_IN_FORCE, GOOD_UNTIL_DATE_TIME, GOOD_UNTIL_CANCELLED, IMMEDIATE_OR_CANCEL, FILL_OR_KILL
     * @property totalFees Commission paid for the order
     * @property totalValueAfterFees Total value of the order after fees
     * @property triggerStatus Can be one of: UNKNOWN_TRIGGER_STATUS, INVALID_ORDER_TYPE, STOP_PENDING, STOP_TRIGGERED
     * @property creationTime When the order was placed
     * @property endTime End Time: Order has end time, 0001-01-01T00:00:00Z: End Time not applicable
     * @property startTime Start Time: Order has start time, 0001-01-01T00:00:00Z: Start Time not applicable
     */
    @Serializable
    data class Order(
        val avgPrice: String,
        val cancelReason: String,
        val clientOrderId: String,
        val completionPercentage: String,
        val contractExpiryType: String,
        val cumulativeQuantity: String,
        val filledValue: String,
        val leavesQuantity: String,
        val limitPrice: String,
        val numberOfFills: String,
        val orderId: String,
        val orderSide: String,
        val orderType: String,
        val outstandingHoldAmount: String,
        val postOnly: String,
        val productId: String,
        val productType: String,
        val rejectReason: String = "",
        val retailPortfolioId: String,
        val riskManagedBy: String,
        val status: String,
        val stopPrice: String,
        val timeInForce: String,
        val totalFees: String,
        val totalValueAfterFees: String,
        val triggerStatus: String,
        val creationTime: String,
        val endTime: String,
        val startTime: String,
    )
    /**
     * The positions fields are in beta and is currently returned as an empty array by default.
     * To enable access to the positions fields in the User WebSocket channel, please reach out to us through Discord.
     */
    @Serializable
    data class Positions(
        val perpetualFuturesPositions: List<PerpetualFuturesPosition>,
        val expiringFuturesPositions: List<ExpiringFuturesPosition>,
    ) {

        /**
         * Perpetual futures position details
         *
         * @property productId Name of the instrument the position is in, e.g. BTC-PERP-INTX
         * @property portfolioUuid The uuid of the portfolio this order is associated with
         * @property vwap The price of the position based on the last settlement period
         * @property entryVwap Volume weighted entry price of the position (not reset to the last funding price)
         * @property positionSide The side of the position. Can be one of: Long, Short
         * @property marginType The margin type of the position. Can be one of: Cross, Isolated
         * @property netSize The size of the position with positive values reflecting a long position and negative values reflecting a short position
         * @property buyOrderSize Cumulative size of all the open buy orders
         * @property sellOrderSize Cumulative size of all the open sell orders
         * @property leverage The leverage of the position
         * @property markPrice The current mark price value for the instrument of this position used in risk and margin calculations
         * @property liquidationPrice Price at which the position will be liquidated at
         * @property imNotional The amount this position contributes to the initial margin
         * @property mmNotional The amount this position contributes to the maintenance margin
         * @property positionNotional The notional value of the position
         * @property unrealizedPnl The profit or loss of this position (resets to 0 after settlement)
         * @property aggregatedPnl The total profit or loss of this position since the initial opening of the position
         */
        @Serializable
        data class PerpetualFuturesPosition(
            val productId: String,
            val portfolioUuid: String,
            val vwap: String,
            val entryVwap: String,
            val positionSide: String,
            val marginType: String,
            val netSize: String,
            val buyOrderSize: String,
            val sellOrderSize: String,
            val leverage: String,
            val markPrice: String,
            val liquidationPrice: String,
            val imNotional: String,
            val mmNotional: String,
            val positionNotional: String,
            val unrealizedPnl: String,
            val aggregatedPnl: String,
        )

        /**
         * Expiring futures position details
         *
         * @property productId Name of the instrument the position is in, e.g. BTC-12Jun24-CDE
         * @property side The side of the position. Can be one of: Long, Short
         * @property numberOfContracts The size of your position in contracts
         * @property realizedPnl Your realized PnL for your position
         * @property unrealizedPnl Your current unrealized PnL for your position
         * @property entryPrice The average entry price at which you entered your current position
         */
        @Serializable
        data class ExpiringFuturesPosition(
            val productId: String,
            val side: String,
            val numberOfContracts: String,
            val realizedPnl: String,
            val unrealizedPnl: String,
            val entryPrice: String,
        )
    }
}