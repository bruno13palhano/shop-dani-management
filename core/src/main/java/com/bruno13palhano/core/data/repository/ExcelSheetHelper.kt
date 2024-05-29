package com.bruno13palhano.core.data.repository

import com.bruno13palhano.core.model.Sale

internal val salesSheetHeaders =
    listOf(
        "Customer",
        "Product",
        "Company",
        "Address",
        "Purchase Price",
        "Sale Price",
        "Delivery Price",
    )

internal val amazonSalesSheetHeaders =
    listOf(
        "Code",
        "Request Number",
        "Product",
        "Customer",
        "Company",
        "Purchase Price",
        "Amazon Price",
        "Amazon Tax",
        "Amazon Profit",
        "Amazon SKU",
        "Resale Profit",
        "Total Profit",
    )

internal fun mapSalesToSheet(sale: Sale): List<String> {
    return listOf(
        sale.customerName,
        sale.name,
        sale.company,
        sale.address,
        sale.purchasePrice.toString(),
        sale.salePrice.toString(),
        sale.deliveryPrice.toString(),
    )
}

internal fun mapAmazonSalesToSheet(sale: Sale): List<String> {
    return listOf(
        sale.amazonCode,
        sale.amazonRequestNumber.toString(),
        sale.name,
        sale.customerName,
        sale.company,
        sale.purchasePrice.toString(),
        sale.salePrice.toString(),
        sale.amazonTax.toString(),
        sale.amazonProfit.toString(),
        sale.amazonSKU,
        sale.resaleProfit.toString(),
        sale.totalProfit.toString(),
    )
}
