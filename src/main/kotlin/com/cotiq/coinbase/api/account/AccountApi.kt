package com.cotiq.coinbase.api.account

import com.cotiq.coinbase.api.ApiCall
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.CoroutineScope

class AccountApi(
    private val httpClient: HttpClient,
    private val scope: CoroutineScope
) {

    /**
     * Get information obout an account by account ID.
     * @param uuid The account's UUID.
     */
    fun getAccount(uuid: String): ApiCall<AccountResponse> = ApiCall(scope) {
        httpClient
            .get("accounts/$uuid")
            .body()
    }

    /**
     * Get a list of authenticated accounts for the current user.
     * @param limit The number of accounts to display per page. By default, displays 49 (max 250). If 'has_next' is true, additional pages of accounts are available to be fetched. Use the cursor parameter to start on a specified page.
     * @param cursor For paginated responses, returns all responses that come after this value.
     */
    fun getAccounts(limit: Int? = null, cursor: String? = null): ApiCall<AccountsResponse> = ApiCall(scope) {
        httpClient
            .get("accounts") {
                url {
                    limit?.let { parameters.append("limit", it.toString()) }
                    cursor?.let { parameters.append("cursor", it) }
                }
            }
            .body()
    }

    /**
     * Get information about API key permissions
     */
    fun getApiKeyPermissions(): ApiCall<KeyPermissions> = ApiCall(scope) {
        httpClient.get("key_permissions").body()
    }

}
