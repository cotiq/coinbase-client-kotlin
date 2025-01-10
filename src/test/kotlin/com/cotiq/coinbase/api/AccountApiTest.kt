package com.cotiq.coinbase.api

import com.cotiq.coinbase.api.account.AccountPlatform
import com.cotiq.coinbase.api.account.AccountType
import com.cotiq.coinbase.createTestApiClient
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AccountApiTest {

    @Test
    fun `GET accounts returns list of accounts`() {
        val accountsResponse = createTestApiClient("get_accounts")
            .accountApi.getAccounts().callBlocking()

        assertEquals(1, accountsResponse.accounts.size)
        with(accountsResponse.accounts.first()) {
            assertEquals("3cf1430f-086d-4aae-9762-2dfae422fa7b", uuid)
            assertEquals("ETH2 Wallet", name)
            assertEquals("ETH2", currency)
            assertEquals("0", availableBalance.value)
            assertEquals("ETH2", availableBalance.currency)
            assertTrue(default)
            assertTrue(active)
            assertEquals("2021-12-17T18:27:01.243Z", createdAt)
            assertEquals("2021-12-17T18:27:01.243Z", updatedAt)
            assertNull(deletedAt)
            assertEquals(AccountType.ACCOUNT_TYPE_CRYPTO, type)
            assertFalse(ready)
            assertEquals("0", hold.value)
            assertEquals("ETH2", hold.currency)
            assertEquals("f568a05a-e345-4e74-871a-5c99300fb15d", retailPortfolioId)
            assertEquals(AccountPlatform.ACCOUNT_PLATFORM_CONSUMER, platform)
        }
    }
}