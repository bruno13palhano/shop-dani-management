package com.bruno13palhano.shopdanimanagement.products

import com.bruno13palhano.core.data.repository.product.ProductRepository
import com.bruno13palhano.core.data.repository.searchcache.SearchCacheRepository
import com.bruno13palhano.core.model.Product
import com.bruno13palhano.shopdanimanagement.StandardDispatcherRule
import com.bruno13palhano.shopdanimanagement.makeRandomProduct
import com.bruno13palhano.shopdanimanagement.makeRandomSearchCache
import com.bruno13palhano.shopdanimanagement.repository.TestProductRepository
import com.bruno13palhano.shopdanimanagement.repository.TestSearchCacheRepository
import com.bruno13palhano.shopdanimanagement.ui.screens.common.CommonItem
import com.bruno13palhano.shopdanimanagement.ui.screens.products.viewmodel.SearchProductsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
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
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class SearchProductsViewModelTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val standardDispatcherRule = StandardDispatcherRule()

    private lateinit var productRepository: ProductRepository
    private lateinit var searchCacheRepository: SearchCacheRepository
    private lateinit var sut: SearchProductsViewModel
    private val products: List<Product>  = listOf(
        makeRandomProduct(id = 1L, name = "Homem"),
        makeRandomProduct(id = 2L, name = "Essencial"),
        makeRandomProduct(id = 3L, name = "Homem")
    )
    private var searchList = listOf(
        makeRandomSearchCache(search = "homem"),
        makeRandomSearchCache(search = "perfumes"),
        makeRandomSearchCache(search = "sabonetes")
    )

    @Before
    fun setup() {
        productRepository = TestProductRepository()
        searchCacheRepository = TestSearchCacheRepository()
        sut = SearchProductsViewModel(productRepository, searchCacheRepository)
    }

    @Test
    fun whenSearch_shouldDelegateToProductRepository() = runTest {
        val search = "A"
        val productRep = mock<ProductRepository>()
        val searchCacheRep = mock<SearchCacheRepository>()
        val sut = SearchProductsViewModel(productRep, searchCacheRep)

        whenever(productRep.search(any())).thenAnswer { flowOf(products) }
        sut.search(search)

        advanceUntilIdle()

        verify(productRep).search(search)
    }

    @Test
    fun whenSearch_ifThereIsAnyMatch_shouldSetTheReturnValueToProducts() = runTest {
        insertProducts()
        val search = "Homem"
        val collectJob = launch { sut.products.collect() }

        sut.search(search = search)

        advanceUntilIdle()

        assertEquals(mapToItems(listOf(products[0], products[2])), sut.products.value)

        collectJob.cancel()
    }

    @Test
    fun whenSearch_ifThereIsNoMatch_shouldSetProductsToEmptyList() = runTest {
        insertProducts()
        val search = "Luna"
        val collectJob = launch { sut.products.collect() }

        sut.search(search = search)

        advanceUntilIdle()

        assertEquals(emptyList<CommonItem>(), sut.products.value)

        collectJob.cancel()
    }

    @Test
    fun whenSearchPerCategory_shouldDelegateToProductRepository() = runTest {
        val search = "B"
        val productRep = mock<ProductRepository>()
        val searchCacheRep = mock<SearchCacheRepository>()
        val sut = SearchProductsViewModel(productRep, searchCacheRep)

        whenever(productRep.searchPerCategory(any(), any())).thenAnswer { flowOf(products) }
        sut.searchPerCategory(search, 1L)

        advanceUntilIdle()

        verify(productRep).searchPerCategory(any(), any())
    }

    @Test
    fun whenSearchPerCategory_ifThereIsAnyMatch_shouldSetTheReturnValueToProducts() = runTest {
        insertProducts()
        val search = "Homem"
        val collectJob = launch { sut.products.collect() }

        sut.searchPerCategory(search = search, categoryId = 1L)

        advanceUntilIdle()

        assertEquals(mapToItems(listOf(products[0])), sut.products.value)

        collectJob.cancel()
    }

    @Test
    fun whenSearchPerCategory_ifThereIsNoAnyMatch_shouldSetProductsToEmptyList() = runTest {
        insertProducts()
        val search = "Homem"
        val collectJob = launch { sut.products.collect() }

        sut.searchPerCategory(search = search, categoryId = 2L)

        advanceUntilIdle()

        assertEquals(mapToItems(emptyList()), sut.products.value)

        collectJob.cancel()
    }

    @Test
    fun whenGetSearchCache_shouldDelegateToSearchCacheRepository() = runTest {
        val productRep = mock<ProductRepository>()
        val searchCacheRep = mock<SearchCacheRepository>()
        val sut = SearchProductsViewModel(productRep, searchCacheRep)

        whenever(searchCacheRep.getAll()).thenAnswer { flowOf(products) }
        sut.getSearchCache()

        advanceUntilIdle()

        verify(searchCacheRep).getAll()
    }

    @Test
    fun whenGetSearchCache_shouldSetPropertySearchCache() = runTest {
        insertSearchCache()

        val collectJob = launch { sut.searchCache.collect() }
        sut.getSearchCache()

        advanceUntilIdle()

        assertEquals(searchList, sut.searchCache.value)

        collectJob.cancel()
    }

    @Test
    fun whenInsertSearch_shouldDelegateToProductRepository() = runTest {
        val search = "C"
        val productRep = mock<ProductRepository>()
        val searchCacheRep = mock<SearchCacheRepository>()
        val sut = SearchProductsViewModel(productRep, searchCacheRep)

        sut.insertSearch(search = search)

        advanceUntilIdle()

        verify(searchCacheRep).insert(any())
    }

    private fun mapToItems(products: List<Product>) =
        products.map {
            CommonItem(
                id = it.id,
                photo = it.photo,
                title = it.name,
                subtitle = it.company,
                description = it.description
            )
        }

    private suspend fun insertProducts() =
        products.forEach { productRepository.insert(it, {}, {}) }
    private suspend fun insertSearchCache() =
        searchList.forEach { searchCacheRepository.insert(it) }
}