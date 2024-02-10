package com.bruno13palhano.core.data.repository

import com.bruno13palhano.core.model.Sale

internal val salesSheetHeaders = listOf(
    "Customer",
    "Product",
    "Company",
    "Address",
    "Purchase Price",
    "Sale Price",
    "Delivery Price"
)

internal fun mapSalesToSheet(sale: Sale): List<String> {
    return listOf(
        sale.customerName,
        sale.name,
        sale.company,
        sale.address,
        sale.purchasePrice.toString(),
        sale.salePrice.toString(),
        sale.deliveryPrice.toString()
    )
}