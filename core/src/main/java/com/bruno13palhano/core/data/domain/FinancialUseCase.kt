package com.bruno13palhano.core.data.domain

import com.bruno13palhano.core.data.di.SaleRep
import com.bruno13palhano.core.data.repository.sale.SaleRepository
import com.bruno13palhano.core.model.FinancialInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FinancialUseCase @Inject constructor(
    @SaleRep private val saleRepository: SaleRepository
){
    operator fun invoke(): Flow<FinancialInfo> {
        return saleRepository.getAll().map {
            var allSalesPurchasePrice = 0F
            var allSales = 0F
            var allDeliveriesPrice = 0F
            var stockSales = 0F
            var ordersSales = 0F

            it.map { sale ->
                allSales += sale.salePrice
                allSalesPurchasePrice += sale.purchasePrice

                if (sale.isOrderedByCustomer) {
                    ordersSales += sale.salePrice
                } else {
                    stockSales += sale.salePrice
                }
            }

            it.map { sale ->
                allDeliveriesPrice += sale.deliveryPrice
            }

            FinancialInfo(
                allSales = allSales,
                stockSales = stockSales,
                ordersSales = ordersSales,
                profit = allSales - (allSalesPurchasePrice + allDeliveriesPrice),
            )
        }
    }
}