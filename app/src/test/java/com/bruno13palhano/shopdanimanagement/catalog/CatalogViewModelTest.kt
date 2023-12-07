package com.bruno13palhano.shopdanimanagement.catalog

import com.bruno13palhano.core.data.repository.catalog.CatalogRepository
import com.bruno13palhano.core.model.Catalog
import com.bruno13palhano.shopdanimanagement.StandardDispatcherRule
import com.bruno13palhano.shopdanimanagement.makeRandomCatalog
import com.bruno13palhano.shopdanimanagement.repository.TestCatalogRepository
import com.bruno13palhano.shopdanimanagement.ui.screens.catalog.viewmodel.CatalogViewModel
import com.bruno13palhano.shopdanimanagement.ui.screens.common.ExtendedItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
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
class CatalogViewModelTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val standardDispatcherRule = StandardDispatcherRule()

    private lateinit var catalogRepository: CatalogRepository<Catalog>
    private lateinit var sut: CatalogViewModel

    private var catalogItems = listOf(
        makeRandomCatalog(id = 1L),
        makeRandomCatalog(id = 2L),
        makeRandomCatalog(id = 3L)
    )

    @Before
    fun setup() = runBlocking {
        catalogRepository = TestCatalogRepository()
        sut = CatalogViewModel(catalogRepository)
        insertCatalogItems()
    }

    @Test
    fun getAll_shouldSetCatalogItemsProperty() = runTest {
        val collectJob = launch { sut.catalogItems.collect() }

        sut.getAll()
        advanceUntilIdle()

        assertEquals(mapToItems(catalogItems), sut.catalogItems.value)

        collectJob.cancel()
    }

    @Test
    fun getAll_shouldCallGetAllFromCatalogRepository() = runTest {
        val catalogRepository = mock<CatalogRepository<Catalog>>()
        val sut = CatalogViewModel(catalogRepository)

        whenever(catalogRepository.getAll()).doAnswer { flowOf() }

        sut.getAll()
        advanceUntilIdle()

        verify(catalogRepository).getAll()
    }

    @Test
    fun getOrderedByName_shouldSetCatalogItemsProperty() = runTest {
        val collectJob = launch { sut.catalogItems.collect() }

        sut.getOrderedByName(isOrderedAsc = true)
        advanceUntilIdle()

        assertEquals(mapToItems(catalogItems.sortedBy { it.name }), sut.catalogItems.value)

        collectJob.cancel()
    }

    @Test
    fun getOrderedByName_shouldCallGetOrderByNameFormRepositories() = runTest {
        val catalogRepository = mock<CatalogRepository<Catalog>>()
        val sut = CatalogViewModel(catalogRepository)

        whenever(catalogRepository.getOrderedByName(any())).doAnswer { flowOf() }

        sut.getOrderedByName(isOrderedAsc = false)
        advanceUntilIdle()

        verify(catalogRepository).getOrderedByName(any())
    }

    @Test
    fun getOrderedByPrice_shouldSetCatalogItemsProperty() = runTest {
        val collectJob = launch { sut.catalogItems.collect() }

        sut.getOrderedByPrice(isOrderedAsc = true)

        advanceUntilIdle()

        assertEquals(mapToItems(catalogItems.sortedBy { it.price }), sut.catalogItems.value)

        collectJob.cancel()
    }

    @Test
    fun getOrderedByPrice_shouldCallGetOrderedByPriceFromRepository() = runTest {
        val catalogRepository = mock<CatalogRepository<Catalog>>()
        val sut = CatalogViewModel(catalogRepository)

        whenever(catalogRepository.getOrderedByPrice(any())).doAnswer { flowOf() }
        sut.getOrderedByPrice(isOrderedAsc = true)

        advanceUntilIdle()

        verify(catalogRepository).getOrderedByPrice(any())
    }

    private suspend fun insertCatalogItems() =
        catalogItems.forEach { catalogRepository.insert(model = it) }

    private fun mapToItems(catalogItems: List<Catalog>) = catalogItems.map { item ->
        ExtendedItem(
            id = item.id,
            photo = item.photo,
            title = item.title,
            firstSubtitle = item.name,
            secondSubtitle = item.price.toString(),
            description = item.description,
            footer = item.discount.toString()
        )
    }
}