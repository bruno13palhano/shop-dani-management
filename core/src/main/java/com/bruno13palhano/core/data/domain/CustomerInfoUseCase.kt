package com.bruno13palhano.core.data.domain

import com.bruno13palhano.core.data.di.CustomerRep
import com.bruno13palhano.core.data.di.SaleRep
import com.bruno13palhano.core.data.repository.customer.CustomerRepository
import com.bruno13palhano.core.data.repository.sale.SaleRepository
import com.bruno13palhano.core.model.CustomerInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class CustomerInfoUseCase  @Inject constructor(
    @CustomerRep private val customerRepository: CustomerRepository,
    @SaleRep private val saleRepository: SaleRepository
) {
    fun getCustomerInfo(customerId: Long): Flow<CustomerInfo> {
        return combine(
            saleRepository.getByCustomerId(customerId = customerId),
            customerRepository.getById(id = customerId)
        ) { purchases, customer ->
            var owingValue = 0F
            var purchasesValue = 0F
            var lastPurchaseValue = 0F
            purchases.filter { !it.isPaidByCustomer }.map {
                owingValue += saleAmount(price = it.salePrice, quantity = it.quantity)
            }
            purchases.map {
                purchasesValue += saleAmount(price = it.salePrice, quantity = it.quantity)
            }
            purchases.map {
                if (it.salePrice != 0F) lastPurchaseValue =
                    saleAmount(price = it.salePrice, quantity = it.quantity)
            }

            CustomerInfo(
                name = customer.name,
                address = customer.address,
                photo = customer.photo,
                owingValue = owingValue.toString(),
                purchasesValue = purchasesValue.toString(),
                lastPurchaseValue = lastPurchaseValue.toString()
            )
        }
    }

    fun getCustomerSales(customerId: Long) = saleRepository.getByCustomerId(customerId = customerId)

    private fun saleAmount(price: Float, quantity: Int) = (price * quantity)
}