package com.bruno13palhano.core.data.repository.delivery

import com.bruno13palhano.core.data.DeliveryData
import com.bruno13palhano.core.data.database.dao.DeliveryDao
import com.bruno13palhano.core.data.database.model.asExternalModel
import com.bruno13palhano.core.data.database.model.asInternalModel
import com.bruno13palhano.core.model.Delivery
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class DeliveryRoom @Inject constructor(
    private val deliveryDao: DeliveryDao
) : DeliveryData<Delivery> {
    override suspend fun insert(model: Delivery): Long =
        deliveryDao.insert(model.asInternalModel())

    override suspend fun update(model: Delivery) =
        deliveryDao.update(model.asInternalModel())

    override suspend fun updateShippingDate(id: Long, shippingDate: Long) {
        deliveryDao.updateShippingDate(id, shippingDate)
    }

    override suspend fun updateDeliveryDate(id: Long, deliveryDate: Long) {
        deliveryDao.updateDeliveryDate(id, deliveryDate)
    }

    override suspend fun updateDelivered(id: Long, delivered: Boolean) {
        deliveryDao.updateDelivered(id, delivered)
    }

    override suspend fun delete(model: Delivery) =
        deliveryDao.delete(model.asInternalModel())

    override suspend fun deleteById(id: Long) =
        deliveryDao.deleteById(id)

    override fun getAll(): Flow<List<Delivery>> {
        return deliveryDao.getAll()
            .map { it.map { entity -> entity.asExternalModel() } }
    }

    override fun getById(id: Long): Flow<Delivery> {
        return deliveryDao.getById(id)
            .map { it.asExternalModel() }
            .catch { it.printStackTrace() }
    }

    override fun getLast(): Flow<Delivery> {
        return deliveryDao.getLast()
            .map { it.asExternalModel() }
            .catch { it.printStackTrace() }
    }

    override fun getDeliveries(delivered: Boolean): Flow<List<Delivery>> {
        return deliveryDao.getDeliveries(delivered)
            .map { it.map { entity -> entity.asExternalModel() } }
    }
}