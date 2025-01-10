package com.cotiq.coinbase.api.account

import kotlinx.serialization.Serializable

@Serializable
data class KeyPermissions(
    val canView: Boolean,
    val canTrade: Boolean,
    val canTransfer: Boolean,
    val portfolioUuid: String,
    val portfolioType: String,
)

