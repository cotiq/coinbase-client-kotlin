package com.cotiq.coinbase.auth

/**
 * Coinbase API credentials.
 * See more at https://docs.cdp.coinbase.com/advanced-trade/docs/ws-auth
 */
data class ApiCredentials(
    val apiKeyName: String,
    val apiPrivateKey: String,
)