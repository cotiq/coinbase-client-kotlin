package com.cotiq.coinbase.api.account

import kotlinx.serialization.Serializable

/**
 * Represents a response containing a list of accounts.
 *
 * @property accounts List of accounts.
 */
@Serializable
data class AccountsResponse(
    val accounts: List<Account>,
)

/**
 * Represents a response containing a single account.
 *
 * @property account The account.
 */
@Serializable
data class AccountResponse(
    val account: Account,
)

/**
 * Represents an account.
 *
 * @property uuid Unique identifier for the account.
 * @property name Name of the account.
 * @property currency Currency symbol of the account.
 * @property availableBalance Available balance in the account.
 * @property default Indicates whether this account is the user's primary account.
 * @property active Indicates whether this account is active.
 * @property createdAt Time (RFC3339) when this account was created.
 * @property updatedAt Time (RFC3339) when this account was last updated.
 * @property deletedAt Time (RFC3339) when this account was deleted.
 * @property type Type of the account.
 * @property ready Indicates whether this account is ready for trading.
 * @property hold Amount held for pending transfers against the available balance.
 * @property retailPortfolioId ID of the portfolio associated with this account.
 * @property platform Platform indicates if the account is for spot (CONSUMER), US Derivatives (CFM_CONSUMER), or International Exchange (INTX).
 */
@Serializable
data class Account(
    val uuid: String,
    val name: String,
    val currency: String,
    val availableBalance: AccountBalance,
    val default: Boolean,
    val active: Boolean,
    val createdAt: String,
    val updatedAt: String? = null,
    val deletedAt: String? = null,
    val type: AccountType,
    val ready: Boolean,
    val hold: AccountBalance,
    val retailPortfolioId: String,
    val platform: AccountPlatform
)

/**
 * Represents the balance of an account.
 *
 * @property value Amount of currency represented by this object.
 * @property currency Denomination of the currency.
 */
@Serializable
data class AccountBalance(
    val value: String,
    val currency: String
)

/**
 * Enum representing the platform of the account.
 */
enum class AccountPlatform {
    ACCOUNT_PLATFORM_UNSPECIFIED, ACCOUNT_PLATFORM_CONSUMER, ACCOUNT_PLATFORM_CFM_CONSUMER, ACCOUNT_PLATFORM_INTX
}

/**
 * Enum representing the type of the account.
 */
enum class AccountType {
    ACCOUNT_TYPE_UNSPECIFIED, ACCOUNT_TYPE_CRYPTO, ACCOUNT_TYPE_FIAT, ACCOUNT_TYPE_VAULT, ACCOUNT_TYPE_PERP_FUTURES
}
