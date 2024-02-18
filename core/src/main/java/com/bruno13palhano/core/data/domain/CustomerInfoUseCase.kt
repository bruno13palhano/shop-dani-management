package com.bruno13palhano.core.data.domain

import com.bruno13palhano.core.data.di.CustomerRep
import com.bruno13palhano.core.data.di.SaleRep
import com.bruno13palhano.core.data.repository.customer.CustomerRepository
import com.bruno13palhano.core.data.repository.sale.SaleRepository
import javax.inject.Inject

class CustomerInfoUseCase  @Inject constructor(
    @CustomerRep private val customerRepository: CustomerRepository,
    @SaleRep private val saleRepository: SaleRepository
) {

}