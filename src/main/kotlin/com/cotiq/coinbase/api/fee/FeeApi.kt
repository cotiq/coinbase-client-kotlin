package com.cotiq.coinbase.api.fee

import com.cotiq.coinbase.api.ApiCall
import com.cotiq.coinbase.api.order.ContractExpiryType
import com.cotiq.coinbase.api.product.ProductType
import com.cotiq.coinbase.api.product.ProductVenue
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.CoroutineScope

class FeeApi(
    private val httpClient: HttpClient,
    private val scope: CoroutineScope,
) {

    /**
     * Get a summary of transactions with fee tiers, total volume, and fees.
     * @param productType Only returns the orders matching this product type. By default, returns all product types.
     * @param contractExpiryType Only returns the orders matching this contract expiry type. Only applicable if product_type is set to FUTURE.
     * @param productVenue Venue for product
     */
    fun getTransactionSummary(
        productType: ProductType? = null,
        contractExpiryType: ContractExpiryType? = null,
        productVenue: ProductVenue? = null
    ): ApiCall<TransactionSummary> = ApiCall(scope) {
        httpClient.get("transaction_summary") {
            url {
                productType?.let { parameters.append("product_type", it.name) }
                contractExpiryType?.let { parameters.append("contract_expiry_type", it.name) }
                productVenue?.let { parameters.append("product_venue", it.name) }
            }
        }.body()
    }

}