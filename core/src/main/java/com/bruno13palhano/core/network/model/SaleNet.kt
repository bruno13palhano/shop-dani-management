package com.bruno13palhano.core.network.model

import com.squareup.moshi.Json

data class SaleNet (
    @Json val id: Long,
    @Json val productId: Long,
    @Json val stockOrderId: Long,
    @Json val customerId: Long,
    @Json val quantity: Int,
    @Json val purchasePrice: Float,
    @Json val salePrice: Float,
    @Json val dateOfSale: Long,
    @Json val dateOfPayment: Long,
    @Json val isOrderedByCustomer: Boolean,
    @Json val isPaid: Boolean,
    @Json val canceled: Boolean
)