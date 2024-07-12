package com.bruno13palhano.core.mocks

import com.bruno13palhano.core.model.Catalog
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.model.Customer
import com.bruno13palhano.core.model.DataVersion
import com.bruno13palhano.core.model.Delivery
import com.bruno13palhano.core.model.Product
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.core.model.SearchCache
import com.bruno13palhano.core.model.StockItem
import kotlin.random.Random

private const val LENGTH = 10
private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

fun makeRandomCatalog(
    id: Long,
    productId: Long = getRandomLong(),
    name: String = getRandomString(),
    photo: ByteArray = byteArrayOf(),
    title: String = getRandomString(),
    description: String = getRandomString(),
    discount: Long = getRandomLong(),
    price: Float = getRandomFloat(),
    timestamp: String = getRandomString(),
) = Catalog(
    id = id,
    productId = productId,
    name = name,
    photo = photo,
    title = title,
    description = description,
    discount = discount,
    price = price,
    timestamp = timestamp,
)

fun makeRandomCategory(
    id: Long,
    name: String = getRandomString(),
    timestamp: String = getRandomString(),
) = Category(id = id, category = name, timestamp = timestamp)

fun makeRandomCustomer(
    id: Long,
    name: String = getRandomString(),
    photo: ByteArray = byteArrayOf(),
    email: String = getRandomString(),
    address: String = getRandomString(),
    city: String = getRandomString(),
    phoneNumber: String = getRandomString(),
    gender: String = getRandomString(),
    age: Int = getRandomInt(),
    timestamp: String = getRandomString(),
) = Customer(
    id = id,
    name = name,
    photo = photo,
    email = email,
    address = address,
    city = city,
    phoneNumber = phoneNumber,
    gender = gender,
    age = age,
    timestamp = timestamp,
)

fun makeRandomDelivery(
    id: Long,
    saleId: Long = getRandomLong(),
    customer: Customer = makeRandomCustomer(id = id),
    productName: String = getRandomString(),
    price: Float = getRandomFloat(),
    deliveryPrice: Float = getRandomFloat(),
    shippingDate: Long = getRandomLong(),
    deliveryDate: Long = getRandomLong(),
    delivered: Boolean = getRandomBoolean(),
    timestamp: String = getRandomString(),
) = Delivery(
    id = id,
    saleId = saleId,
    customerName = customer.name,
    address = customer.address,
    phoneNumber = customer.phoneNumber,
    productName = productName,
    price = price,
    deliveryPrice = deliveryPrice,
    shippingDate = shippingDate,
    deliveryDate = deliveryDate,
    delivered = delivered,
    timestamp = timestamp,
)

fun makeRandomProduct(
    id: Long,
    name: String = getRandomString(),
    code: String = getRandomString(),
    description: String = getRandomString(),
    photo: ByteArray = byteArrayOf(),
    date: Long = getRandomLong(),
    categories: List<Category> =
        listOf(
            makeRandomCategory(1L),
            makeRandomCategory(2L),
            makeRandomCategory(3L),
        ),
    company: String = getRandomString(),
    timestamp: String = getRandomString(),
) = Product(
    id = id,
    name = name,
    code = code,
    description = description,
    photo = photo,
    date = date,
    categories = categories,
    company = company,
    timestamp = timestamp,
)

fun makeRandomSearchCache(search: String = getRandomString()) = SearchCache(search = search)

fun makeRandomStockOrder(
    id: Long,
    product: Product = makeRandomProduct(id = id),
    date: Long = getRandomLong(),
    dateOfPayment: Long = getRandomLong(),
    validity: Long = getRandomLong(),
    quantity: Int = getRandomInt(),
    purchasePrice: Float = getRandomFloat(),
    salePrice: Float = getRandomFloat(),
    isPaid: Boolean = getRandomBoolean(),
    timestamp: String = getRandomString(),
) = StockItem(
    id = id,
    productId = product.id,
    name = product.name,
    photo = product.photo,
    date = date,
    dateOfPayment = dateOfPayment,
    validity = validity,
    quantity = quantity,
    categories = product.categories,
    company = product.company,
    purchasePrice = purchasePrice,
    salePrice = salePrice,
    isPaid = isPaid,
    timestamp = timestamp,
)

fun makeRandomSale(
    id: Long,
    stockItem: StockItem = makeRandomStockOrder(id = id),
    delivery: Delivery = makeRandomDelivery(id = id, saleId = id),
    customer: Customer = makeRandomCustomer(id = id),
    isOrderedByCustomer: Boolean = getRandomBoolean(),
    quantity: Int = getRandomInt(),
    amazonCode: String = getRandomString(),
    amazonRequestNumber: Long = getRandomLong(),
    amazonTax: Int = getRandomInt(),
    amazonProfit: Float = getRandomFloat(),
    amazonSKU: String = getRandomString(),
    resaleProfit: Float = getRandomFloat(),
    totalProfit: Float = getRandomFloat(),
    dateOfSale: Long = getRandomLong(),
    dateOfPayment: Long = getRandomLong(),
    shippingDate: Long = getRandomLong(),
    deliveryDate: Long = getRandomLong(),
    isPaidByCustomer: Boolean = getRandomBoolean(),
    delivered: Boolean = getRandomBoolean(),
    canceled: Boolean = getRandomBoolean(),
    isAmazon: Boolean = getRandomBoolean(),
    timestamp: String = getRandomString(),
) = Sale(
    id = id,
    productId = stockItem.productId,
    stockId = stockItem.id,
    customerId = customer.id,
    name = stockItem.name,
    customerName = customer.name,
    photo = stockItem.photo,
    address = customer.address,
    phoneNumber = customer.phoneNumber,
    quantity = quantity,
    purchasePrice = stockItem.purchasePrice,
    salePrice = stockItem.salePrice,
    deliveryPrice = delivery.deliveryPrice,
    shippingDate = shippingDate,
    deliveryDate = deliveryDate,
    categories = stockItem.categories,
    company = stockItem.company,
    amazonCode = amazonCode,
    amazonRequestNumber = amazonRequestNumber,
    amazonTax = amazonTax,
    amazonProfit = amazonProfit,
    amazonSKU = amazonSKU,
    resaleProfit = resaleProfit,
    totalProfit = totalProfit,
    dateOfSale = dateOfSale,
    dateOfPayment = dateOfPayment,
    isOrderedByCustomer = isOrderedByCustomer,
    isPaidByCustomer = isPaidByCustomer,
    delivered = delivered,
    canceled = canceled,
    isAmazon = isAmazon,
    timestamp = timestamp,
)

fun makeRandomDataVersion(
    id: Long = getRandomLong(),
    name: String = getRandomString(),
    timestamp: String = getRandomString(),
) = DataVersion(
    id = id,
    name = name,
    timestamp = timestamp,
)

private fun getRandomString() =
    (1..LENGTH)
        .map { Random.nextInt(0, charPool.size).let { charPool[it] } }
        .joinToString("")

private fun getRandomLong() = (1..LENGTH).sumOf { Random.nextLong(0, 1000L) }

private fun getRandomInt() = (1..LENGTH).sumOf { Random.nextInt(0, 1000) }

private fun getRandomFloat() = (1..LENGTH).map { Random.nextFloat() }.sum()

private fun getRandomBoolean() = Random.nextBoolean()
