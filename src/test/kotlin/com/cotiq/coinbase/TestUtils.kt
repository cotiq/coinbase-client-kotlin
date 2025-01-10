package com.cotiq.coinbase

import io.ktor.client.engine.mock.*
import io.ktor.http.*
import java.nio.file.Files
import java.nio.file.Paths

fun createTestApiClient(responseFilename: String): RestApiClient {
    return RestApiClientBuilder()
        .setHttpClientEngine(createMockEngine(responseFilename))
        .build()
}

private fun createMockEngine(responseFilename: String): MockEngine =
    MockEngine { _ ->
        respond(
            content = Files.readString(Paths.get("src/test/resources/$responseFilename.json")),
            status = HttpStatusCode.OK,
            headers = headersOf(
                HttpHeaders.ContentType,
                ContentType.Application.Json.toString()
            )
        )
    }