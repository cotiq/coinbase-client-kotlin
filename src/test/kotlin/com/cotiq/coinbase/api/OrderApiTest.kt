package com.cotiq.coinbase.api

import com.cotiq.coinbase.api.order.CancelOrderResponse
import com.cotiq.coinbase.api.order.CreateOrderRequest
import com.cotiq.coinbase.api.order.LimitLimitGtc
import com.cotiq.coinbase.api.order.MarginType
import com.cotiq.coinbase.api.order.OrderConfiguration
import com.cotiq.coinbase.api.order.OrderPlacementSource
import com.cotiq.coinbase.api.order.OrderSide
import com.cotiq.coinbase.api.order.OrderStatus
import com.cotiq.coinbase.api.order.OrderType
import com.cotiq.coinbase.api.order.PreviewOrderRequest
import com.cotiq.coinbase.api.order.RejectReason
import com.cotiq.coinbase.api.order.TimeInForce
import com.cotiq.coinbase.api.order.TriggerStatus
import com.cotiq.coinbase.api.product.ProductType
import com.cotiq.coinbase.createTestApiClient
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class OrderApiTest {

    @Test
    fun `GET orders returns list of orders`()  {
        val ordersResponse = createTestApiClient("get_orders_filled")
            .orderApi.getOrders(orderStatus = listOf(OrderStatus.FILLED), limit = 1).callBlocking()

        assertEquals(1, ordersResponse.orders.size)
        with(ordersResponse.orders.first()) {
            assertEquals("a4ab4709-0678-47b8-9151-0c3540b9f6f5", orderId)
            assertEquals("BTC-EUR", productId)
            assertEquals("f8e2f9dd-73fe-4c73-af3a-40f446e22403", userId)
            assertNull(orderConfiguration.marketMarketIoc)
            assertNull(orderConfiguration.sorLimitIoc)
            assertNull(orderConfiguration.limitLimitGtd)
            assertNull(orderConfiguration.limitLimitFok)
            assertNull(orderConfiguration.stopLimitStopLimitGtc)
            assertNull(orderConfiguration.stopLimitStopLimitGtd)
            assertNull(orderConfiguration.triggerBracketGtc)
            assertNull(orderConfiguration.triggerBracketGtd)
            with(assertNotNull(orderConfiguration.limitLimitGtc)) {
                assertEquals("0.45646728", baseSize)
                assertEquals("24442.5", limitPrice)
                assertTrue(postOnly)
            }
            assertEquals(OrderSide.BUY, side)
            assertEquals("9389d8f1-5d49-4e5d-9526-d046bec0fba7", clientOrderId)
            assertEquals(OrderStatus.FILLED, status)
            assertEquals(TimeInForce.GOOD_UNTIL_CANCELLED, timeInForce)
            assertEquals("2023-08-23T17:42:11.993544Z", createdTime)
            assertEquals("100.00", completionPercentage)
            assertEquals("0.45646728", filledSize)
            assertEquals("24442.5", averageFilledPrice)
            assertEquals("3", numberOfFills)
            assertEquals("11157.2014914", filledValue)
            assertFalse(pendingCancel)
            assertFalse(sizeInQuote)
            assertEquals("11.1572014914", totalFees)
            assertFalse(sizeInclusiveOfFees)
            assertEquals("11168.3586928914", totalValueAfterFees)
            assertEquals(TriggerStatus.INVALID_ORDER_TYPE, triggerStatus)
            assertEquals(OrderType.LIMIT, orderType)
            assertEquals(RejectReason.REJECT_REASON_UNSPECIFIED, rejectReason)
            assertTrue(settled)
            assertEquals(ProductType.SPOT, productType)
            assertEquals("", rejectMessage)
            assertEquals("", cancelMessage)
            assertEquals(OrderPlacementSource.RETAIL_ADVANCED, orderPlacementSource)
            assertEquals("0", outstandingHoldAmount)
            assertFalse(isLiquidation)
            assertEquals("2023-08-23T17:42:30.336265Z", lastFillTime)
            assertTrue(editHistory.isEmpty())
            assertEquals("", leverage)
            assertEquals(MarginType.UNKNOWN_MARGIN_TYPE, marginType)
            assertEquals("2c63d2f0-b67e-4157-aece-a1bdd6d7bb9f", retailPortfolioId)
        }
        assertTrue(ordersResponse.hasNext)
        assertEquals("474305825", ordersResponse.cursor)
    }

    @Test
    fun `GET order returns existing order`() {
        val orderResponse = createTestApiClient("get_order_filled")
            .orderApi.getOrder("a4ab4709-0678-47b8-9151-0c3540b9f6f5").callBlocking()

        with(orderResponse.order) {
            assertEquals("a4ab4709-0678-47b8-9151-0c3540b9f6f5", orderId)
            assertEquals("BTC-EUR", productId)
            assertEquals("f8e2f9dd-73fe-4c73-af3a-40f446e22403", userId)
            assertNull(orderConfiguration.marketMarketIoc)
            assertNull(orderConfiguration.sorLimitIoc)
            assertNull(orderConfiguration.limitLimitGtd)
            assertNull(orderConfiguration.limitLimitFok)
            assertNull(orderConfiguration.stopLimitStopLimitGtc)
            assertNull(orderConfiguration.stopLimitStopLimitGtd)
            assertNull(orderConfiguration.triggerBracketGtc)
            assertNull(orderConfiguration.triggerBracketGtd)
            with(assertNotNull(orderConfiguration.limitLimitGtc)) {
                assertEquals("0.45646728", baseSize)
                assertEquals("24442.5", limitPrice)
                assertTrue(postOnly)
            }
            assertEquals(OrderSide.BUY, side)
            assertEquals("9389d8f1-5d49-4e5d-9526-d046bec0fba7", clientOrderId)
            assertEquals(OrderStatus.FILLED, status)
            assertEquals(TimeInForce.GOOD_UNTIL_CANCELLED, timeInForce)
            assertEquals("2023-08-23T17:42:11.993544Z", createdTime)
            assertEquals("100.00", completionPercentage)
            assertEquals("0.45646728", filledSize)
            assertEquals("24442.5", averageFilledPrice)
            assertEquals("3", numberOfFills)
            assertEquals("11157.2014914", filledValue)
            assertFalse(pendingCancel)
            assertFalse(sizeInQuote)
            assertEquals("11.1572014914", totalFees)
            assertFalse(sizeInclusiveOfFees)
            assertEquals("11168.3586928914", totalValueAfterFees)
            assertEquals(TriggerStatus.INVALID_ORDER_TYPE, triggerStatus)
            assertEquals(OrderType.LIMIT, orderType)
            assertEquals(RejectReason.REJECT_REASON_UNSPECIFIED, rejectReason)
            assertTrue(settled)
            assertEquals(ProductType.SPOT, productType)
            assertEquals("", rejectMessage)
            assertEquals("", cancelMessage)
            assertEquals(OrderPlacementSource.RETAIL_ADVANCED, orderPlacementSource)
            assertEquals("0", outstandingHoldAmount)
            assertFalse(isLiquidation)
            assertEquals("2023-08-23T17:42:30.336265Z", lastFillTime)
            assertTrue(editHistory.isEmpty())
            assertEquals("", leverage)
            assertEquals(MarginType.UNKNOWN_MARGIN_TYPE, marginType)
            assertEquals("2c63d2f0-b67e-4157-aece-a1bdd6d7bb9f", retailPortfolioId)
        }
    }

    @Test
    fun `POST cancel order cancels order`() {
        val cancelOrderResponse = createTestApiClient("post_cancel_order_success")
            .orderApi.cancelOrders(listOf("31d8810d-41dc-4321-b734-0a44495294a2")).callBlocking()

        assertEquals(1, cancelOrderResponse.results.size)
        with(cancelOrderResponse.results.first()) {
            assertEquals("31d8810d-41dc-4321-b734-0a44495294a2", orderId)
            assertTrue(success)
            assertEquals(CancelOrderResponse.FailureReason.UNKNOWN_CANCEL_FAILURE_REASON, failureReason)
        }
    }

    @Test
    fun `POST cancel order fails to cancel the order`() {
        val cancelOrderResponse = createTestApiClient("post_cancel_order_fail")
            .orderApi.cancelOrders(listOf("31d8810d-41dc-4321-b734-0a44495294a2")).callBlocking()

        assertEquals(1, cancelOrderResponse.results.size)
        with(cancelOrderResponse.results.first()) {
            assertEquals("31d8810d-41dc-4321-b734-0a44495294a2", orderId)
            assertFalse(success)
            assertEquals(CancelOrderResponse.FailureReason.UNKNOWN_CANCEL_ORDER, failureReason)
        }
    }

    @Test
    fun `POST preview order with invalid config (missing size) returns invalid order config error`() {
        val response = createTestApiClient("post_preview_order_invalid_config")
            .orderApi.previewOrder(
                PreviewOrderRequest(
                    productId = "SOL-USD",
                    side = OrderSide.BUY,
                    orderConfiguration = OrderConfiguration(
                        limitLimitGtc = LimitLimitGtc(
                            limitPrice = "100",
                            postOnly = true
                        )
                    )
                )
            ).callBlocking()
        with(response) {
            assertEquals(listOf("PREVIEW_INVALID_ORDER_CONFIG"), errs)
            assertEquals("3ad5e0aa-6be8-42c1-991c-113792307c7a", previewId)
        }
    }

    @Test
    fun `POST preview order returns insufficient fund error`() {
        val response = createTestApiClient("post_preview_order_insufficient_fund")
            .orderApi.previewOrder(
                PreviewOrderRequest(
                    productId = "SOL-USD",
                    side = OrderSide.BUY,
                    orderConfiguration = OrderConfiguration(
                        limitLimitGtc = LimitLimitGtc(
                            limitPrice = "100",
                            baseSize = "0.3",
                            postOnly = true
                        )
                    )
                )
            ).callBlocking()
        with(response) {
            assertEquals(listOf("PREVIEW_INSUFFICIENT_FUND"), errs)
            assertEquals("8991b857-7146-446f-9e84-47b1c6e7b304", previewId)
        }
    }

    @Test
    fun `POST preview order returns success`() {
        val response = createTestApiClient("post_preview_order_success")
            .orderApi.previewOrder(
                PreviewOrderRequest(
                    productId = "SOL-USD",
                    side = OrderSide.BUY,
                    orderConfiguration = OrderConfiguration(
                        limitLimitGtc = LimitLimitGtc(
                            limitPrice = "100",
                            baseSize = "0.1",
                            postOnly = true
                        )
                    )
                )
            ).callBlocking()
        with(response) {
            assertEquals("10.035", orderTotal)
            assertEquals("0.035", commissionTotal)
            assertTrue(errs.isEmpty())
            assertEquals(listOf("UNKNOWN"), warning)
            assertEquals("10", quoteSize)
            assertEquals("0.1", baseSize)
            assertEquals("255.73", bestBid)
            assertEquals("255.74", bestAsk)
            assertFalse(isMax)
            assertEquals("0", orderMarginTotal)
            assertEquals("0", leverage)
            assertEquals("0", longLeverage)
            assertEquals("0", shortLeverage)
            assertEquals("0", slippage)
            assertEquals("dcbfa9fc-1100-4ae7-9ad7-f8f7a2865de3", previewId)
            assertEquals("0", currentLiquidationBuffer)
            assertEquals("0", projectedLiquidationBuffer)
            assertEquals("", maxLeverage)
        }
    }

    @Test
    fun `POST create order returns insufficient fund error`() {
        val response = createTestApiClient("post_create_order_insufficient_fund")
            .orderApi.createOrder(
                CreateOrderRequest(
                    clientOrderId = "280b5780-8584-4a78-8a68-b0b471a6edfe",
                    productId = "SOL-USD",
                    side = OrderSide.BUY,
                    orderConfiguration = OrderConfiguration(
                        limitLimitGtc = LimitLimitGtc(
                            limitPrice = "100",
                            baseSize = "0.3",
                            postOnly = true
                        )
                    )
                )
            ).callBlocking()
        with(response) {
            assertFalse(success)
            with(assertNotNull(errorResponse)) {
                assertEquals("INSUFFICIENT_FUND", error)
                assertEquals("Insufficient balance in source account", message)
                assertEquals("", errorDetails)
                assertEquals("PREVIEW_INSUFFICIENT_FUND", previewFailureReason)
            }
            with(assertNotNull(orderConfiguration?.limitLimitGtc)) {
                assertEquals("0.3", baseSize)
                assertEquals("100", limitPrice)
                assertTrue(postOnly)
            }
        }
    }

    @Test
    fun `POST create order returns success`() {
        val response = createTestApiClient("post_create_order_success")
            .orderApi.createOrder(
                CreateOrderRequest(
                    clientOrderId = "3a7b42c6-cc12-4932-98ba-33fd8ddb60a0",
                    productId = "SOL-USD",
                    side = OrderSide.BUY,
                    orderConfiguration = OrderConfiguration(
                        limitLimitGtc = LimitLimitGtc(
                            limitPrice = "100",
                            baseSize = "0.1",
                            postOnly = true
                        )
                    )
                )
            ).callBlocking()
        with(response) {
            assertTrue(success)
            with(assertNotNull(successResponse)) {
                assertEquals("e5ed0e4f-3c86-4bec-818a-707fee427180", orderId)
                assertEquals("SOL-USDC", productId)
                assertEquals(OrderSide.BUY, side)
                assertEquals("3a7b42c6-cc12-4932-98ba-33fd8ddb60a0", clientOrderId)
            }
            with(assertNotNull(orderConfiguration?.limitLimitGtc)) {
                assertEquals("0.1", baseSize)
                assertEquals("100", limitPrice)
                assertTrue(postOnly)
            }
        }
    }
}