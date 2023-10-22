package com.bruno13palhano.shopdanimanagement.insights

import com.bruno13palhano.core.data.SaleData
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.shopdanimanagement.StandardDispatcherRule
import com.bruno13palhano.shopdanimanagement.repository.TestSaleRepository
import com.bruno13palhano.shopdanimanagement.ui.screens.insights.viewmodel.SalesByCompanyViewModel
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
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class SalesByCompanyViewModelTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get: Rule
    val standardDispatcherRule = StandardDispatcherRule()

    private lateinit var saleRepository: SaleData<Sale>
    private lateinit var sut: SalesByCompanyViewModel

    @Before
    fun setup() {
        saleRepository = TestSaleRepository()
        sut = SalesByCompanyViewModel(saleRepository)
    }

    @Test
    fun getChartByRange_shouldCallGetAllFromRepository() = runTest {
        val saleRepository = mock<SaleData<Sale>>()
        val sut = SalesByCompanyViewModel(saleRepository)

        whenever(saleRepository.getAll()).doAnswer { flowOf() }

        sut.getChartByRange(rangeOfDays = 7)
        advanceUntilIdle()

        verify(saleRepository).getAll()
    }
}