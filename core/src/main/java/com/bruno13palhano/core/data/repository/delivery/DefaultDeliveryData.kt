package com.bruno13palhano.core.data.repository.delivery

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import cache.DeliveryTableQueries
import com.bruno13palhano.core.data.di.Dispatcher
import com.bruno13palhano.core.data.di.ShopDaniManagementDispatchers.IO
import com.bruno13palhano.core.model.Delivery
import com.bruno13palhano.core.model.isNew
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

class DefaultDeliveryData @Inject constructor(
    private val deliveryQueries: DeliveryTableQueries,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : DeliveryData {
    override suspend fun insert(
        model: Delivery,
        onError: (error: Int) -> Unit,
        onSuccess: (id: Long) -> Unit
    ): Long {
        try {
            if (model.isNew()) {
                deliveryQueries.insert(
                    saleId = model.saleId,
                    deliveryPrice = model.deliveryPrice.toDouble(),
                    shippingDate = model.shippingDate,
                    deliveryDate = model.deliveryDate,
                    delivered = model.delivered,
                    timestamp = model.timestamp
                )
                val id = deliveryQueries.getLastId().executeAsOne()
                onSuccess(id)

                return id
            } else {
                deliveryQueries.insertWithId(
                    id = model.id,
                    saleId = model.saleId,
                    deliveryPrice = model.deliveryPrice.toDouble(),
                    shippingDate = model.shippingDate,
                    deliveryDate = model.deliveryDate,
                    delivered = model.delivered,
                    timestamp = model.timestamp
                )
                onSuccess(model.id)

                return model.id
            }
        } catch (e: Exception) {
            e.printStackTrace()
            onError(1)

            return 0L
        }
    }

    override suspend fun update(
        model: Delivery,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    ) {
        try {
            deliveryQueries.update(
                id = model.id,
                saleId = model.saleId,
                deliveryPrice = model.deliveryPrice.toDouble(),
                shippingDate = model.shippingDate,
                deliveryDate = model.deliveryDate,
                delivered = model.delivered,
                timestamp = model.timestamp
            )
            onSuccess()
        } catch (e: Exception) {
            e.printStackTrace()
            onError(2)
        }
    }

    override suspend fun updateDeliveryPrice(id: Long, deliveryPrice: Float) {
        deliveryQueries.updateDeliveryPrice(id = id, deliveryPrice = deliveryPrice.toDouble())
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

    override suspend fun deleteById(
        id: Long,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    ) {
        try {
            deliveryQueries.delete(id = id)
            onSuccess()
        } catch (e: Exception) {
            e.printStackTrace()
            onError(3)
        }
    }

    override fun getAll(): Flow<List<Delivery>> {
        return deliveryQueries.getAll(mapper = ::mapDelivery)
            .asFlow().mapToList(ioDispatcher)
    }

    override fun getById(id: Long): Flow<Delivery> {
        return deliveryQueries.getById(id = id, mapper = ::mapDelivery)
            .asFlow().mapToOne(ioDispatcher)
            .catch { it.printStackTrace() }
    }

    override fun getLast(): Flow<Delivery> {
        return deliveryQueries.getLast(mapper = ::mapDelivery)
            .asFlow().mapToOne(ioDispatcher)
            .catch { it.printStackTrace() }
    }

    override fun getCanceledDeliveries(): Flow<List<Delivery>> {
        return deliveryQueries.getCanceledDeliveries(mapper = ::mapDelivery)
            .asFlow().mapToList(ioDispatcher)
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
        delivered: Boolean,
        timestamp: String
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
        delivered = delivered,
        timestamp = timestamp
    )
}