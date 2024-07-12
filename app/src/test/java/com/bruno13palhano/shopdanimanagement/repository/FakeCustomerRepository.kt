package com.bruno13palhano.shopdanimanagement.repository

import com.bruno13palhano.core.data.repository.customer.CustomerRepository
import com.bruno13palhano.core.model.Customer
import com.bruno13palhano.core.sync.Synchronizer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class FakeCustomerRepository : CustomerRepository {
    private val customers = mutableListOf<Customer>()

    override fun search(search: String): Flow<List<Customer>> {
        return flowOf(customers).map {
            it.filter { customer ->
                customer.name == search || customer.email == search || customer.address == search ||
                    customer.phoneNumber == search
            }
        }
    }

    override fun getOrderedByName(isOrderedAsc: Boolean): Flow<List<Customer>> {
        return if (isOrderedAsc) {
            flowOf(customers.sortedBy { it.name })
        } else {
            flowOf(customers.sortedByDescending { it.name })
        }
    }

    override fun getOrderedByAddress(isOrderedAsc: Boolean): Flow<List<Customer>> {
        return if (isOrderedAsc) {
            flowOf(customers.sortedBy { it.address })
        } else {
            flowOf(customers.sortedByDescending { it.address })
        }
    }

    override suspend fun insert(
        model: Customer,
        onError: (error: Int) -> Unit,
        onSuccess: (id: Long) -> Unit
    ): Long {
        customers.add(model)
        onSuccess(model.id)
        return model.id
    }

    override suspend fun update(
        model: Customer,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    ) {
        val index = getIndex(id = model.id, list = customers)
        if (isIndexValid(index = index)) {
            customers[index] = model
            onSuccess()
        }
    }

    override suspend fun deleteById(
        id: Long,
        timestamp: String,
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    ) {
        val index = getIndex(id = id, list = customers)
        if (isIndexValid(index = index)) {
            customers.removeAt(index = index)
            onSuccess()
        }
    }

    override fun getAll(): Flow<List<Customer>> = flowOf(customers)

    override fun getById(id: Long): Flow<Customer> {
        val index = getIndex(id = id, list = customers)
        return if (isIndexValid(index = index)) flowOf(customers[index]) else flowOf()
    }

    override fun getLast(): Flow<Customer> = flowOf(customers.last())

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean {
        TODO("Not yet implemented")
    }
}