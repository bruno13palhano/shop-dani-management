package com.bruno13palhano.shopdanimanagement.insights

import com.bruno13palhano.core.data.SaleData
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.shopdanimanagement.StandardDispatcherRule
import com.bruno13palhano.shopdanimanagement.repository.TestSaleRepository
import com.bruno13palhano.shopdanimanagement.ui.screens.insights.viewmodel.LastSalesViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.junit.MockitoRule
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class LastSalesViewModelTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get: Rule
    val standardDispatcherRule = StandardDispatcherRule()

    private lateinit var saleRepository: SaleData<Sale>
    private lateinit var sut: LastSalesViewModel

    @Before
    fun setup() {
        saleRepository = TestSaleRepository()
        sut = LastSalesViewModel(saleRepository)
    }

    @Test
    fun setLastSalesEntryByRange_shouldCallGetLastSalesFromRepository() = runTest {
        val saleRepository = mock<SaleData<Sale>>()
        val sut = LastSalesViewModel(saleRepository)

        whenever(saleRepository.getLastSales(any(), any())).doAnswer { flowOf() }

        sut.setLastSalesEntryByRange(rangeOfDays = 7)
        advanceUntilIdle()

        verify(saleRepository).getLastSales(any(), any())
    }
}