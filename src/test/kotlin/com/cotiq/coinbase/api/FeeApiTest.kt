package com.cotiq.coinbase.api

import com.cotiq.coinbase.createTestApiClient
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class FeeApiTest {

    @Test
    fun `GET transaction summary returns transaction summary`() {
        val transactionSummary = createTestApiClient("get_transaction_summary")
            .feeApi.getTransactionSummary().callBlocking()

        with(transactionSummary) {
            assertEquals("5204.026052744", totalVolume)
            assertEquals("31.224156296464002", totalFees)
            with(assertNotNull(feeTier)) {
                assertEquals("Intro 2", pricingTier)
                assertEquals("1000", usdFrom)
                assertEquals("10000", usdTo)
                assertEquals("0.0075", takerFeeRate)
                assertEquals("0.0035", makerFeeRate)
                assertEquals("500000", aopFrom)
                assertEquals("1000000", aopTo)
            }
            assertNull(marginRate)
            assertNull(goodsAndServicesTax)
            assertEquals("5204.026052744", advancedTradeOnlyVolume)
            assertEquals("31.224156296464002", advancedTradeOnlyFees)
            assertEquals("0", coinbaseProVolume)
            assertEquals("0", coinbaseProFees)
            assertEquals("19.29", totalBalance)
        }
    }

}