package com.bruno13palhano.core.data.domain

import com.bruno13palhano.core.data.di.SaleRep
import com.bruno13palhano.core.data.repository.sale.SaleRepository
import com.bruno13palhano.core.model.FinancialInfo
import com.bruno13palhano.core.model.Sale
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FinancialUseCase @Inject constructor(
    @SaleRep private val saleRepository: SaleRepository
) {
    operator fun invoke(): Flow<FinancialInfo> {
        return saleRepository.getAll().map { mapToFinancialInfo(it) }
    }

    private fun mapToFinancialInfo(sales: List<Sale>): FinancialInfo {
        var allSalesPurchasePrice = 0F
        var allSales = 0F
        var allDeliveriesPrice = 0F
        var stockSales = 0F
        var ordersSales = 0F

        sales.map { sale ->
            allSales += (sale.salePrice * sale.quantity) //fixed wrong amount
            allSalesPurchasePrice += sale.purchasePrice

            if (sale.isOrderedByCustomer) {
                ordersSales += sale.salePrice
            } else {
                stockSales += sale.salePrice
            }
        }

        sales.map { sale -> allDeliveriesPrice += sale.deliveryPrice }

        return FinancialInfo(
            allSales = allSales,
            stockSales = stockSales,
            ordersSales = ordersSales,
            profit = calculateProfit(
                sales = allSales,
                salesPurchasePrice = allSalesPurchasePrice,
                deliveriesPrice = allDeliveriesPrice
            ),
        )
    }

    private fun calculateProfit(
        sales: Float,
        salesPurchasePrice: Float,
        deliveriesPrice: Float
    ) = sales - (salesPurchasePrice + deliveriesPrice)
}