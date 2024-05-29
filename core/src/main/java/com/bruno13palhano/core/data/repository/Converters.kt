package com.bruno13palhano.core.data.repository

import com.bruno13palhano.core.model.Catalog
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.model.Customer
import com.bruno13palhano.core.model.DataVersion
import com.bruno13palhano.core.model.Product
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.core.model.StockItem
import com.bruno13palhano.core.model.User
import com.bruno13palhano.core.network.model.CatalogNet
import com.bruno13palhano.core.network.model.CategoryNet
import com.bruno13palhano.core.network.model.CustomerNet
import com.bruno13palhano.core.network.model.DataVersionNet
import com.bruno13palhano.core.network.model.ProductNet
import com.bruno13palhano.core.network.model.SaleNet
import com.bruno13palhano.core.network.model.StockItemNet
import com.bruno13palhano.core.network.model.UserNet
import java.util.Base64

fun catalogToCatalogNet(model: Catalog) =
    CatalogNet(
        id = model.id,
        productId = model.productId,
        title = model.title,
        description = model.description,
        discount = model.discount,
        price = model.price,
        timestamp = model.timestamp,
    )

fun catalogNetToCatalog(model: CatalogNet) =
    Catalog(
        id = model.id,
        productId = model.productId,
        name = "",
        photo = byteArrayOf(),
        title = model.title,
        description = model.description,
        discount = model.discount,
        price = model.price,
        timestamp = model.timestamp,
    )

fun categoryNetToCategory(categoryNet: CategoryNet) =
    Category(
        id = categoryNet.id,
        category = categoryNet.category,
        timestamp = categoryNet.timestamp,
    )

fun categoryToCategoryNet(category: Category) =
    CategoryNet(
        id = category.id,
        category = category.category,
        timestamp = category.timestamp,
    )

fun customerNetToCustomer(customerNet: CustomerNet) =
    Customer(
        id = customerNet.id,
        name = customerNet.name,
        photo = Base64.getDecoder().decode(customerNet.photo),
        email = customerNet.email,
        address = customerNet.address,
        city = customerNet.city,
        phoneNumber = customerNet.phoneNumber,
        gender = customerNet.gender,
        age = customerNet.age,
        timestamp = customerNet.timestamp,
    )

fun customerToCustomerNet(customer: Customer) =
    CustomerNet(
        id = customer.id,
        name = customer.name,
        photo = Base64.getEncoder().encodeToString(customer.photo),
        email = customer.email,
        address = customer.address,
        city = customer.city,
        phoneNumber = customer.phoneNumber,
        gender = customer.gender,
        age = customer.age,
        timestamp = customer.timestamp,
    )

fun productToProductNet(product: Product) =
    ProductNet(
        id = product.id,
        name = product.name,
        code = product.code,
        description = product.description,
        photo = Base64.getEncoder().encodeToString(product.photo),
        date = product.date,
        categories = product.categories.map { categoryToCategoryNet(it) },
        company = product.company,
        timestamp = product.timestamp,
    )

fun productNetToProduct(productNet: ProductNet) =
    Product(
        id = productNet.id,
        name = productNet.name,
        code = productNet.code,
        description = productNet.description,
        photo = Base64.getDecoder().decode(productNet.photo),
        date = productNet.date,
        categories = productNet.categories.map { categoryNetToCategory(it) },
        company = productNet.company,
        timestamp = productNet.timestamp,
    )

fun versionToVersionNet(version: DataVersion) =
    DataVersionNet(
        id = version.id,
        name = version.name,
        timestamp = version.timestamp,
    )

fun versionNetToVersion(versionNet: DataVersionNet) =
    DataVersion(
        id = versionNet.id,
        name = versionNet.name,
        timestamp = versionNet.timestamp,
    )

fun saleToSaleNet(sale: Sale) =
    SaleNet(
        id = sale.id,
        productId = sale.productId,
        stockId = sale.stockId,
        customerId = sale.customerId,
        quantity = sale.quantity,
        purchasePrice = sale.purchasePrice,
        salePrice = sale.salePrice,
        deliveryPrice = sale.deliveryPrice,
        amazonCode = sale.amazonCode,
        amazonRequestNumber = sale.amazonRequestNumber,
        amazonTax = sale.amazonTax,
        amazonProfit = sale.amazonProfit,
        amazonSKU = sale.amazonSKU,
        resaleProfit = sale.resaleProfit,
        totalProfit = sale.totalProfit,
        dateOfSale = sale.dateOfSale,
        dateOfPayment = sale.dateOfPayment,
        shippingDate = sale.shippingDate,
        deliveryDate = sale.deliveryDate,
        isOrderedByCustomer = sale.isOrderedByCustomer,
        isPaidByCustomer = sale.isPaidByCustomer,
        delivered = sale.delivered,
        canceled = sale.canceled,
        isAmazon = sale.isAmazon,
        timestamp = sale.timestamp,
    )

fun saleNetToSale(saleNet: SaleNet) =
    Sale(
        id = saleNet.id,
        productId = saleNet.productId,
        stockId = saleNet.stockId,
        customerId = saleNet.customerId,
        name = "",
        customerName = "",
        photo = byteArrayOf(),
        address = "",
        phoneNumber = "",
        quantity = saleNet.quantity,
        purchasePrice = saleNet.purchasePrice,
        salePrice = saleNet.salePrice,
        deliveryPrice = saleNet.deliveryPrice,
        categories = listOf(),
        company = "",
        amazonCode = saleNet.amazonCode,
        amazonRequestNumber = saleNet.amazonRequestNumber,
        amazonTax = saleNet.amazonTax,
        amazonProfit = saleNet.amazonProfit,
        amazonSKU = saleNet.amazonSKU,
        resaleProfit = saleNet.resaleProfit,
        totalProfit = saleNet.totalProfit,
        dateOfSale = saleNet.dateOfSale,
        dateOfPayment = saleNet.dateOfPayment,
        shippingDate = saleNet.shippingDate,
        deliveryDate = saleNet.deliveryDate,
        isOrderedByCustomer = saleNet.isOrderedByCustomer,
        isPaidByCustomer = saleNet.isPaidByCustomer,
        delivered = saleNet.delivered,
        canceled = saleNet.canceled,
        isAmazon = saleNet.isAmazon,
        timestamp = saleNet.timestamp,
    )

fun stockItemToStockItemNet(stockItem: StockItem) =
    StockItemNet(
        id = stockItem.id,
        productId = stockItem.productId,
        date = stockItem.date,
        dateOfPayment = stockItem.dateOfPayment,
        validity = stockItem.validity,
        quantity = stockItem.quantity,
        purchasePrice = stockItem.purchasePrice,
        salePrice = stockItem.salePrice,
        isPaid = stockItem.isPaid,
        timestamp = stockItem.timestamp,
    )

fun stockItemNetToStockItem(stockItemNet: StockItemNet) =
    StockItem(
        id = stockItemNet.id,
        productId = stockItemNet.productId,
        name = "",
        photo = byteArrayOf(),
        date = stockItemNet.date,
        dateOfPayment = stockItemNet.dateOfPayment,
        validity = stockItemNet.validity,
        quantity = stockItemNet.quantity,
        categories = listOf(),
        company = "",
        purchasePrice = stockItemNet.purchasePrice,
        salePrice = stockItemNet.salePrice,
        isPaid = stockItemNet.isPaid,
        timestamp = stockItemNet.timestamp,
    )

fun userNetToUser(userNet: UserNet) =
    User(
        id = userNet.id,
        username = userNet.username,
        email = userNet.email,
        password = userNet.password,
        photo = Base64.getDecoder().decode(userNet.photo),
        role = userNet.role,
        enabled = userNet.enabled,
        timestamp = userNet.timestamp,
    )

fun userToUserNet(user: User) =
    UserNet(
        id = user.id,
        username = user.username,
        email = user.email,
        password = user.password,
        photo = Base64.getEncoder().encodeToString(user.photo),
        role = user.role,
        enabled = user.enabled,
        timestamp = user.timestamp,
    )
