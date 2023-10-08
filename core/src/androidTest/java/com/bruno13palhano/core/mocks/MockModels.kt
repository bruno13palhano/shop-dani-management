package com.bruno13palhano.core.mocks

import com.bruno13palhano.core.model.Catalog
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.model.Customer
import com.bruno13palhano.core.model.Delivery
import com.bruno13palhano.core.model.Product
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.core.model.SearchCache
import com.bruno13palhano.core.model.StockOrder
import kotlin.random.Random

private const val LENGTH = 10
private val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

fun makeRandomCatalog(id: Long) = Catalog(
    id = id,
    productId = getRandomLong(),
    name = getRandomString(),
    photo = byteArrayOf(),
    title = getRandomString(),
    description = getRandomString(),
    discount = getRandomLong(),
    price = getRandomFloat()
)

fun makeRandomCategory(id: Long) = Category(id = id, name = getRandomString())

fun makeRandomCustomer(id: Long) = Customer(
    id = id,
    name = getRandomString(),
    photo = byteArrayOf(),
    email = getRandomString(),
    address = getRandomString(),
    phoneNumber = getRandomString()
)

fun makeRandomDelivery(id: Long) = Delivery(
    id = id,
    saleId = getRandomLong(),
    customerName = getRandomString(),
    address = getRandomString(),
    phoneNumber = getRandomString(),
    productName = getRandomString(),
    price = getRandomFloat(),
    deliveryPrice = getRandomFloat(),
    shippingDate = getRandomLong(),
    deliveryDate = getRandomLong(),
    delivered = getRandomBoolean()
)

fun makeRandomProduct(id: Long) = Product(
    id = id,
    name = getRandomString(),
    code = getRandomString(),
    description = getRandomString(),
    photo = byteArrayOf(),
    date = getRandomLong(),
    categories = listOf(
        makeRandomCategory(1L),
        makeRandomCategory(2L),
        makeRandomCategory(3L)
    ),
    company = getRandomString()
)

fun makeRandomSale(id: Long) = Sale(
    id = id,
    productId = getRandomLong(),
    stockOrderId = getRandomLong(),
    customerId = getRandomLong(),
    name = getRandomString(),
    customerName = getRandomString(),
    photo = byteArrayOf(),
    quantity = getRandomInt(),
    purchasePrice = getRandomFloat(),
    salePrice = getRandomFloat(),
    deliveryPrice = getRandomFloat(),
    categories = listOf(
        makeRandomCategory(1L),
        makeRandomCategory(2L),
        makeRandomCategory(3L)
    ),
    company = getRandomString(),
    dateOfSale = getRandomLong(),
    dateOfPayment = getRandomLong(),
    isOrderedByCustomer = getRandomBoolean(),
    isPaidByCustomer = getRandomBoolean(),
    canceled = getRandomBoolean()
)

fun makeRandomSearchCache() = SearchCache(search = getRandomString())

fun makeRandomStockOrder(id: Long) = StockOrder(
    id = id,
    productId = getRandomLong(),
    name = getRandomString(),
    photo = byteArrayOf(),
    date = getRandomLong(),
    validity = getRandomLong(),
    quantity = getRandomInt(),
    categories = listOf(
        makeRandomCategory(1L),
        makeRandomCategory(2L),
        makeRandomCategory(3L)
    ),
    company = getRandomString(),
    purchasePrice = getRandomFloat(),
    salePrice = getRandomFloat(),
    isOrderedByCustomer = getRandomBoolean(),
    isPaid = getRandomBoolean()
)

private fun getRandomString() = (1..LENGTH)
    .map { Random.nextInt(0, charPool.size).let { charPool[it] } }
    .joinToString("")

private fun getRandomLong() = (1..LENGTH).sumOf { Random.nextLong(0, 1000L) }

private fun getRandomInt() = (1..LENGTH).sumOf { Random.nextInt(0, 1000) }

private fun getRandomFloat() = (1 .. LENGTH).map { Random.nextFloat() }.sum()

private fun getRandomBoolean() = Random.nextBoolean()