package com.cotiq.coinbase.api

import com.cotiq.coinbase.createTestApiClient
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class PublicApiTest {

    @Test
    fun `GET time returns server time`() {
        val time = createTestApiClient("get_time")
            .publicApi.getServerTime().callBlocking()
        with(time) {
            assertEquals("2025-01-31T07:24:45Z", iso)
            assertEquals(1738308285, epochSeconds)
            assertEquals(1738308285753, epochMillis)
        }
    }
}