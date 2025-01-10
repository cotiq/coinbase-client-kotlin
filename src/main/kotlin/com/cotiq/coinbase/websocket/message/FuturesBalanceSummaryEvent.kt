package com.cotiq.coinbase.websocket.message

import kotlinx.serialization.Serializable


@Serializable
data class FuturesBalanceSummaryEvent(
    val type: String,
    val fcmBalanceSummary: FcmBalanceSummary,
): Event {

    /**
     * Futures Balance Summary
     *
     * @property futuresBuyingPower The amount of your cash balance that is available to trade CFM futures
     * @property totalUsdBalance Aggregate USD maintained across your CFTC-regulated futures account and your Coinbase Inc. spot account
     * @property cbiUsdBalance USD maintained in your Coinbase Inc. spot account
     * @property cfmUsdBalance USD maintained in your CFTC-regulated futures account. Funds held in your futures account are not available to trade spot
     * @property totalOpenOrdersHoldAmount Your total balance on hold for spot and futures open orders
     * @property unrealizedPnl Your current unrealized PnL across all open positions
     * @property dailyRealizedPnl Your realized PnL from the current trade date. May include profit or loss from positions you’ve closed on the current trade date
     * @property initialMargin Margin required to initiate futures positions. Once futures orders are placed, these funds cannot be used to trade spot. The actual amount of funds necessary to support executed futures orders will be moved to your futures account
     * @property availableMargin Funds available to meet your anticipated margin requirement. This includes your CBI spot USD, CFM futures USD, and Futures PnL, less any holds for open spot or futures orders
     * @property liquidationThreshold When your available funds for collateral drop to the liquidation threshold, some or all of your futures positions will
     * @property liquidationBufferAmount Funds available in excess of the liquidation threshold, calculated as available margin minus liquidation threshold. If your liquidation buffer amount reaches 0, your futures positions and/or open orders will be liquid
     * @property liquidationBufferPercentage Funds available in excess of the liquidation threshold expressed as a percentage. If your liquidation buffer percentage reaches 0%, your futures positions and/or open orders will be liquidated as necessary
     * @property intradayMarginWindowMeasure The period of time used to calculate margin requirements for positions held intraday before settling overnight
     * @property overnightMarginWindowMeasure The period of time used to calculate increased margin requirements for positions held and left unsettled overnight
     * @property marginWindowType FCM_MARGIN_WINDOW_TYPE_UNSPECIFIED, FCM_MARGIN_WINDOW_TYPE_OVERNIGHT, FCM_MARGIN_WINDOW_TYPE_WEEKEND, FCM_MARGIN_WINDOW_TYPE_INTRADAY, FCM_MARGIN_WINDOW_TYPE_TRANSITION
     * @property marginLevel MARGIN_LEVEL_TYPE_UNSPECIFIED, MARGIN_LEVEL_TYPE_BASE, MARGIN_LEVEL_TYPE_WARNING, MARGIN_LEVEL_TYPE_DANGER, MARGIN_LEVEL_TYPE_LIQUIDATION
     * @property initialMargin Margin required to initiate futures positions. Once futures orders are placed, these funds cannot be used to trade spot. The actual amount of funds necessary to support executed futures orders will be moved to your futures account
     * @property maintenanceMargin The amount of funds required to maintain your futures positions
     * @property liquidationBufferPercentage Funds available in excess of the liquidation threshold expressed as a percentage. If your liquidation buffer percentage reaches 0%, your futures positions and/or open orders will be liquidated as necessary
     * @property intradayMarginWindowMeasure The period of time used to calculate margin requirements for positions held intraday before settling overnight
     * @property overnightMarginWindowMeasure The period of time used to calculate increased margin requirements for positions held and left unsettled overnight
     */
    @Serializable
    data class FcmBalanceSummary(
        val futuresBuyingPower: String,
        val totalUsdBalance: String,
        val cbiUsdBalance: String,
        val cfmUsdBalance: String,
        val totalOpenOrdersHoldAmount: String,
        val unrealizedPnl: String,
        val dailyRealizedPnl: String,
        val initialMargin: String,
        val availableMargin: String,
        val liquidationThreshold: String,
        val liquidationBufferAmount: String,
        val liquidationBufferPercentage: String,
        val intradayMarginWindowMeasure: MarginWindowMeasure,
        val overnightMarginWindowMeasure: MarginWindowMeasure,
    )

    @Serializable
    data class MarginWindowMeasure(
        val marginWindowType: MarginWindowType,
        val marginLevel: MarginLevel,
        val initialMargin: String,
        val maintenanceMargin: String,
        val liquidationBufferPercentage: String,
        val totalHold: String,
        val futuresBuyingPower: String,
    ) {
        enum class MarginWindowType {
            FCM_MARGIN_WINDOW_TYPE_UNSPECIFIED,
            FCM_MARGIN_WINDOW_TYPE_OVERNIGHT,
            FCM_MARGIN_WINDOW_TYPE_WEEKEND,
            FCM_MARGIN_WINDOW_TYPE_INTRADAY,
            FCM_MARGIN_WINDOW_TYPE_TRANSITION,
        }
        enum class MarginLevel {
            MARGIN_LEVEL_TYPE_UNSPECIFIED,
            MARGIN_LEVEL_TYPE_BASE,
            MARGIN_LEVEL_TYPE_WARNING,
            MARGIN_LEVEL_TYPE_DANGER,
            MARGIN_LEVEL_TYPE_LIQUIDATION,
        }
    }

}