package com.bruno13palhano.core.data.repository

import com.bruno13palhano.core.data.CustomerData
import com.bruno13palhano.core.data.database.dao.CustomerDao
import com.bruno13palhano.core.data.database.model.asExternalModel
import com.bruno13palhano.core.data.database.model.asInternalModel
import com.bruno13palhano.core.model.Customer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class CustomerRepository @Inject constructor(
    private val customerDao: CustomerDao
) : CustomerData<Customer> {
    override suspend fun insert(model: Customer): Long {
        return customerDao.insert(model.asInternalModel())
    }

    override suspend fun update(model: Customer) {
        customerDao.update(model.asInternalModel())
    }

    override suspend fun delete(model: Customer) {
        customerDao.delete(model.asInternalModel())
    }

    override suspend fun deleteById(id: Long) {
        customerDao.deleteById(id)
    }

    override fun getAll(): Flow<List<Customer>> {
        return customerDao.getAll()
            .map {
                it.map { entity -> entity.asExternalModel() }
            }
    }

    override fun getById(id: Long): Flow<Customer> {
        return customerDao.getById(id)
            .map { entity -> entity.asExternalModel() }
            .catch { it.printStackTrace() }
    }

    override fun getLast(): Flow<Customer> {
        return customerDao.getLast()
            .map { entity -> entity.asExternalModel() }
            .catch { it.printStackTrace() }
    }
}