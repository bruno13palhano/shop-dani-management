package com.bruno13palhano.core.data.repository.delivery

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import cache.DeliveryTableQueries
import com.bruno13palhano.core.data.DeliveryData
import com.bruno13palhano.core.data.di.Dispatcher
import com.bruno13palhano.core.data.di.ShopDaniManagementDispatchers.IO
import com.bruno13palhano.core.model.Delivery
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeliveryLight @Inject constructor(
    private val deliveryQueries: DeliveryTableQueries,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : DeliveryData<Delivery> {
    override suspend fun insert(model: Delivery): Long {
        deliveryQueries.insert(
            saleId = model.saleId,
            deliveryPrice = model.deliveryPrice.toDouble(),
            shippingDate = model.shippingDate,
            deliveryDate = model.deliveryDate,
            delivered = model.delivered
        )
        return deliveryQueries.getLastId().executeAsOne()
    }

    override suspend fun update(model: Delivery) {
        deliveryQueries.update(
            id = model.id,
            saleId = model.saleId,
            deliveryPrice = model.deliveryPrice.toDouble(),
            shippingDate = model.shippingDate,
            deliveryDate = model.deliveryDate,
            delivered = model.delivered
        )
    }

    override suspend fun delete(model: Delivery) {
        deliveryQueries.delete(id = model.id)
    }

    override suspend fun updateShippingDate(id: Long, shippingDate: Long) {
        deliveryQueries.updateShippingDate(shippingDate = shippingDate, id = id)
    }

    override suspend fun updateDeliveryDate(id: Long, deliveryDate: Long) {
        deliveryQueries.updateDeliveryDate(deliveryDate = deliveryDate, id = id)
    }

    override suspend fun updateDelivered(id: Long, delivered: Boolean) {
        deliveryQueries.updateDelivered(delivered = delivered, id = id)
    }

    override fun getDeliveries(delivered: Boolean): Flow<List<Delivery>> {
        return deliveryQueries.getDeliveries(delivered = delivered, mapper = ::mapDelivery)
            .asFlow().mapToList(ioDispatcher)
    }

    override suspend fun deleteById(id: Long) {
        deliveryQueries.delete(id = id)
    }

    override fun getAll(): Flow<List<Delivery>> {
        return deliveryQueries.getAll(mapper = ::mapDelivery)
            .asFlow().mapToList(ioDispatcher)
    }

    override fun getById(id: Long): Flow<Delivery> {
        return deliveryQueries.getById(id = id, mapper = ::mapDelivery)
            .asFlow().mapToOne(ioDispatcher)
    }

    override fun getLast(): Flow<Delivery> {
        return deliveryQueries.getLast(mapper = ::mapDelivery)
            .asFlow().mapToOne(ioDispatcher)
    }

    private fun mapDelivery(
        id: Long,
        saleId: Long,
        customerName: String,
        address: String,
        phoneNumber: String,
        productName: String,
        price: Double,
        deliveryPrice: Double,
        shippingDate: Long,
        deliveryDate: Long,
        delivered: Boolean
    ) = Delivery(
        id = id,
        saleId = saleId,
        customerName = customerName,
        address = address,
        phoneNumber = phoneNumber,
        productName = productName,
        price = price.toFloat(),
        deliveryPrice = deliveryPrice.toFloat(),
        shippingDate = shippingDate,
        deliveryDate = deliveryDate,
        delivered = delivered
    )
}