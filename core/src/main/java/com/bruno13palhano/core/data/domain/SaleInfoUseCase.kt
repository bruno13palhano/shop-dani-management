package com.bruno13palhano.core.data.domain

import com.bruno13palhano.core.data.di.CustomerRep
import com.bruno13palhano.core.data.di.SaleRep
import com.bruno13palhano.core.data.repository.customer.CustomerRepository
import com.bruno13palhano.core.data.repository.sale.SaleRepository
import com.bruno13palhano.core.model.SaleInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class SaleInfoUseCase @Inject constructor(
    @SaleRep private val saleRepository: SaleRepository,
    @CustomerRep private val customerRepository: CustomerRepository
) {
    operator fun invoke(saleId: Long, customerId: Long): Flow<SaleInfo> {
        return combine(
            saleRepository.getById(id = saleId),
            customerRepository.getById(id = customerId)
        ) { sale, customer ->
            if (sale.customerId == customer.id) {
                SaleInfo(
                    saleId = sale.id,
                    productId = sale.productId,
                    customerId = customer.id,
                    productName = sale.name,
                    customerName = customer.name,
                    productPhoto = sale.photo,
                    customerPhoto = customer.photo,
                    address = customer.address,
                    phoneNumber = customer.phoneNumber,
                    email = customer.email,
                    salePrice = sale.salePrice,
                    deliveryPrice = sale.deliveryPrice,
                    quantity = sale.quantity,
                    dateOfSale = sale.dateOfSale
                )
            } else { SaleInfo.emptySaleInfo() }
        }
    }
}