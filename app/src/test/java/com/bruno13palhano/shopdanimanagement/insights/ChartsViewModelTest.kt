package com.bruno13palhano.shopdanimanagement.insights

import com.bruno13palhano.core.data.SaleData
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.shopdanimanagement.StandardDispatcherRule
import com.bruno13palhano.shopdanimanagement.repository.TestSaleRepository
import com.bruno13palhano.shopdanimanagement.ui.screens.insights.viewmodel.ChartsViewModel
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
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class ChartsViewModelTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get: Rule
    val standardDispatcherRule = StandardDispatcherRule()

    private lateinit var saleRepository: SaleData<Sale>
    private lateinit var sut: ChartsViewModel

    @Before
    fun setup() {
        saleRepository = TestSaleRepository()
        sut = ChartsViewModel(saleRepository)
    }

    @Test
    fun setItemsDayRange_shouldCallGetLastSalesFromRepository() = runTest {
        val saleRepository = mock<SaleData<Sale>>()
        val sut = ChartsViewModel(saleRepository)

        whenever(saleRepository.getLastSales(any(), any())).doAnswer { flowOf() }

        sut.setItemsDaysRange(rangeOfDays = 7)
        advanceUntilIdle()

        verify(saleRepository, times(2)).getLastSales(any(), any())
    }
}