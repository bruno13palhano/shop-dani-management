package com.bruno13palhano.core.mocks

import com.bruno13palhano.core.model.Catalog
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.model.Company
import com.bruno13palhano.core.model.Customer
import com.bruno13palhano.core.model.Delivery
import com.bruno13palhano.core.model.Product
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.core.model.SearchCache
import com.bruno13palhano.core.model.StockOrder
import kotlin.random.Random

private const val LENGTH = 10
private val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

fun makeRandomCatalog(
    id: Long,
    productId: Long = getRandomLong(),
    name: String = getRandomString(),
    photo: ByteArray = byteArrayOf(),
    title: String = getRandomString(),
    description: String = getRandomString(),
    discount: Long = getRandomLong(),
    price: Float = getRandomFloat()
) = Catalog(
    id = id,
    productId = productId,
    name = name,
    photo = photo,
    title = title,
    description = description,
    discount = discount,
    price = price
)

fun makeRandomCategory(id: Long) = Category(id = id, name = getRandomString())

fun makeRandomCustomer(
    id: Long,
    name: String = getRandomString(),
    photo: ByteArray = byteArrayOf(),
    email: String = getRandomString(),
    address: String = getRandomString(),
    phoneNumber: String = getRandomString()
) = Customer(
    id = id,
    name = name,
    photo = photo,
    email = email,
    address = address,
    phoneNumber = phoneNumber
)

fun makeRandomDelivery(
    id: Long,
    saleId: Long = getRandomLong(),
    customerName: String = getRandomString(),
    address: String = getRandomString(),
    phoneNumber: String = getRandomString(),
    productName: String = getRandomString(),
    price: Float = getRandomFloat(),
    deliveryPrice: Float = getRandomFloat(),
    shippingDate: Long = getRandomLong(),
    deliveryDate: Long = getRandomLong(),
    delivered: Boolean = getRandomBoolean()
) = Delivery(
    id = id,
    saleId = saleId,
    customerName = customerName,
    address = address,
    phoneNumber = phoneNumber,
    productName = productName,
    price = price,
    deliveryPrice = deliveryPrice,
    shippingDate = shippingDate,
    deliveryDate = deliveryDate,
    delivered = delivered
)

fun makeRandomProduct(
    id: Long,
    name: String = getRandomString(),
    code: String = getRandomString(),
    description: String = getRandomString(),
    photo: ByteArray = byteArrayOf(),
    date: Long = getRandomLong(),
    categories: List<Category> = listOf(
        makeRandomCategory(1L),
        makeRandomCategory(2L),
        makeRandomCategory(3L)
    ),
    company: String = getRandomString()
) = Product(
    id = id,
    name = name,
    code = code,
    description = description,
    photo = photo,
    date = date,
    categories = categories,
    company = company
)

fun makeRandomSale(
    id: Long,
    productId: Long = getRandomLong(),
    stockOrderId: Long = getRandomLong(),
    customerId: Long = getRandomLong(),
    name: String = getRandomString(),
    customerName: String = getRandomString(),
    photo: ByteArray = byteArrayOf(),
    quantity: Int = getRandomInt(),
    purchasePrice: Float = getRandomFloat(),
    salePrice: Float = getRandomFloat(),
    deliveryPrice: Float = getRandomFloat(),
    categories: List<Category> = listOf(
        makeRandomCategory(1L),
        makeRandomCategory(2L),
        makeRandomCategory(3L)
    ),
    company: String = getRandomString(),
    dateOfSale: Long = getRandomLong(),
    dateOfPayment: Long = getRandomLong(),
    isOrderedByCustomer: Boolean = getRandomBoolean(),
    isPaidByCustomer: Boolean = getRandomBoolean(),
    canceled: Boolean = getRandomBoolean()
) = Sale(
    id = id,
    productId = productId,
    stockOrderId = stockOrderId,
    customerId = customerId,
    name = name,
    customerName = customerName,
    photo = photo,
    quantity = quantity,
    purchasePrice = purchasePrice,
    salePrice = salePrice,
    deliveryPrice = deliveryPrice,
    categories = categories,
    company = company,
    dateOfSale = dateOfSale,
    dateOfPayment = dateOfPayment,
    isOrderedByCustomer = isOrderedByCustomer,
    isPaidByCustomer = isPaidByCustomer,
    canceled = canceled
)

fun makeRandomSearchCache(search: String = getRandomString()) = SearchCache(search = search)

fun makeRandomStockOrder(
    id: Long,
    productId: Long = getRandomLong(),
    name: String = getRandomString(),
    photo: ByteArray = byteArrayOf(),
    date: Long = getRandomLong(),
    validity: Long = getRandomLong(),
    quantity: Int = getRandomInt(),
    categories: List<Category> = listOf(
        makeRandomCategory(1L),
        makeRandomCategory(2L),
        makeRandomCategory(3L)
    ),
    company: String = getRandomString(),
    purchasePrice: Float = getRandomFloat(),
    salePrice: Float = getRandomFloat(),
    isOrderedByCustomer: Boolean = getRandomBoolean(),
    isPaid: Boolean = getRandomBoolean()
) = StockOrder(
    id = id,
    productId = productId,
    name = name,
    photo = photo,
    date = date,
    validity = validity,
    quantity = quantity,
    categories = categories,
    company = company,
    purchasePrice = purchasePrice,
    salePrice = salePrice,
    isOrderedByCustomer = isOrderedByCustomer,
    isPaid = isPaid
)

private fun getRandomString() = (1..LENGTH)
    .map { Random.nextInt(0, charPool.size).let { charPool[it] } }
    .joinToString("")

private fun getRandomLong() = (1..LENGTH).sumOf { Random.nextLong(0, 1000L) }

private fun getRandomInt() = (1..LENGTH).sumOf { Random.nextInt(0, 1000) }

private fun getRandomFloat() = (1 .. LENGTH).map { Random.nextFloat() }.sum()

private fun getRandomBoolean() = Random.nextBoolean()