package com.bruno13palhano.shopdanimanagement.repository

import com.bruno13palhano.core.data.repository.delivery.DeliveryRepository
import com.bruno13palhano.core.model.Delivery
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class TestDeliveryRepository : DeliveryRepository<Delivery> {
    private val deliveries = mutableListOf<Delivery>()

    override suspend fun insert(model: Delivery): Long {
        deliveries.add(model)
        return deliveries.lastIndex.toLong()
    }

    override suspend fun update(model: Delivery) {
        val index = getIndex(id = model.id, list = deliveries)
        if (isIndexValid(index = index)) deliveries[index] = model
    }

    override suspend fun updateDeliveryPrice(id: Long, deliveryPrice: Float) {
        val index = getIndex(id = id, list = deliveries)

        if (isIndexValid(index = index)) {
            val delivery = Delivery(
                id = id,
                saleId = deliveries[index].saleId,
                customerName = deliveries[index].customerName,
                address = deliveries[index].address,
                phoneNumber = deliveries[index].phoneNumber,
                productName = deliveries[index].productName,
                price = deliveries[index].price,
                deliveryPrice = deliveryPrice,
                shippingDate = deliveries[index].shippingDate,
                deliveryDate = deliveries[index].deliveryDate,
                delivered = deliveries[index].delivered
            )

            deliveries[index] = delivery
        }
    }

    override suspend fun updateShippingDate(id: Long, shippingDate: Long) {
        val index = getIndex(id = id, list = deliveries)

        if (isIndexValid(index = index)) {
            val delivery = Delivery(
                id = id,
                saleId = deliveries[index].saleId,
                customerName = deliveries[index].customerName,
                address = deliveries[index].address,
                phoneNumber = deliveries[index].phoneNumber,
                productName = deliveries[index].productName,
                price = deliveries[index].price,
                deliveryPrice = deliveries[index].deliveryPrice,
                shippingDate = shippingDate,
                deliveryDate = deliveries[index].deliveryDate,
                delivered = deliveries[index].delivered
            )

            deliveries[index] = delivery
        }
    }

    override suspend fun updateDeliveryDate(id: Long, deliveryDate: Long) {
        val index = getIndex(id = id, list = deliveries)

        if (isIndexValid(index = index)) {
            val delivery = Delivery(
                id = id,
                saleId = deliveries[index].saleId,
                customerName = deliveries[index].customerName,
                address = deliveries[index].address,
                phoneNumber = deliveries[index].phoneNumber,
                productName = deliveries[index].productName,
                price = deliveries[index].price,
                deliveryPrice = deliveries[index].deliveryPrice,
                shippingDate = deliveries[index].shippingDate,
                deliveryDate = deliveryDate,
                delivered = deliveries[index].delivered
            )

            deliveries[index] = delivery
        }
    }

    override suspend fun updateDelivered(id: Long, delivered: Boolean) {
        val index = getIndex(id = id, list = deliveries)

        if (isIndexValid(index = index)) {
            val delivery = Delivery(
                id = id,
                saleId = deliveries[index].saleId,
                customerName = deliveries[index].customerName,
                address = deliveries[index].address,
                phoneNumber = deliveries[index].phoneNumber,
                productName = deliveries[index].productName,
                price = deliveries[index].price,
                deliveryPrice = deliveries[index].deliveryPrice,
                shippingDate = deliveries[index].shippingDate,
                deliveryDate = deliveries[index].deliveryDate,
                delivered = delivered
            )

            deliveries[index] = delivery
        }
    }

    override fun getDeliveries(delivered: Boolean): Flow<List<Delivery>> {
        return if (delivered) {
            flowOf(deliveries.filter { it.delivered })
        } else {
            flowOf(deliveries.filter { !it.delivered })
        }
    }

    override fun getCanceledDeliveries(): Flow<List<Delivery>> = flowOf(deliveries)

    override suspend fun deleteById(id: Long) {
        val index = getIndex(id = id, list = deliveries)
        if (isIndexValid(index = index)) deliveries.removeAt(index)
    }

    override fun getAll(): Flow<List<Delivery>> = flowOf(deliveries)

    override fun getById(id: Long): Flow<Delivery> {
        val index = getIndex(id = id, list = deliveries)
        return if (isIndexValid(index = index)) flowOf(deliveries[index]) else flowOf()
    }

    override fun getLast(): Flow<Delivery> = flowOf(deliveries.last())
}