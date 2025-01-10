package com.cotiq.coinbase.plugins

import com.cotiq.coinbase.auth.JwtCreator
import io.ktor.client.plugins.api.*


internal val AdvancedTradeApiAuth = createClientPlugin("AdvancedTradeApiAuth", ::AdvancedTradeApiAuthConfig) {
    val jwtCreator = JwtCreator(
        keyName = pluginConfig.keyName,
        privateKey = pluginConfig.privateKey
    )
    onRequest { request, _ ->
        val uri: String = request.method.value + " " + request.url.host + request.url.pathSegments.joinToString("/")
        val jwt: String = jwtCreator.createJwt(uri)
        request.headers.append("Authorization", "Bearer $jwt")
    }
}

internal data class AdvancedTradeApiAuthConfig(
    var keyName: String = "",
    var privateKey: String = "",
)
