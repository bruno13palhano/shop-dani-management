package com.bruno13palhano.shopdanimanagement.products

import com.bruno13palhano.core.data.ProductData
import com.bruno13palhano.core.data.SearchCacheData
import com.bruno13palhano.core.model.Product
import com.bruno13palhano.core.model.SearchCache
import com.bruno13palhano.shopdanimanagement.StandardDispatcherRule
import com.bruno13palhano.shopdanimanagement.makeRandomProduct
import com.bruno13palhano.shopdanimanagement.makeRandomSearchCache
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
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.junit.MockitoRule
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.stub
import org.mockito.kotlin.verify

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class SearchProductsViewModelTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val standardDispatcherRule = StandardDispatcherRule()

    @Mock
    lateinit var productRepository: ProductData<Product>

    @Mock
    lateinit var searchCacheRepository: SearchCacheData<SearchCache>

    private lateinit var sut: SearchProductsViewModel
    private lateinit var products: List<Product>
    private lateinit var searchList: List<SearchCache>
    private val search = "test"
    private val categoryId = 2L

    @Before
    fun setup() {
        sut = SearchProductsViewModel(productRepository, searchCacheRepository)
        products = listOf(
            makeRandomProduct(id = 1L),
            makeRandomProduct(id = 2L),
            makeRandomProduct(id = 3L)
        )
        searchList = listOf(
            makeRandomSearchCache(search = "homem"),
            makeRandomSearchCache(search = "perfumes"),
            makeRandomSearchCache(search = "sabonetes")
        )
    }

    @Test
    fun whenSearch_shouldDelegateToProductRepository() = runTest {
        productRepository.stub {
            onBlocking { search(value = search) } doAnswer { flowOf(products) }
        }
        sut.search(search)

        advanceUntilIdle()

        verify(productRepository).search(search)
    }

    @Test
    fun whenSearch_shouldSetPropertyProduct() = runTest {
        productRepository.stub {
            onBlocking { search(value = search) } doAnswer { flowOf(products) }
        }

        val job = launch { sut.products.collect() }
        sut.search(search)

        advanceUntilIdle()

        assertEquals(convertToItems(products), sut.products.value)

        job.cancel()
    }

    @Test
    fun whenSearchPerCategory_shouldDelegateToProductRepository() = runTest {
        productRepository.stub {
            onBlocking { searchPerCategory(value = search, categoryId = categoryId) }
                .doAnswer { flowOf(products) }
        }
        sut.searchPerCategory(search = search, categoryId = categoryId)

        advanceUntilIdle()

        verify(productRepository).searchPerCategory(value = search, categoryId = categoryId)
    }

    @Test
    fun whenSearchPerCategory_shouldSetPropertyProduct() = runTest {
        productRepository.stub {
            onBlocking { searchPerCategory(value = search, categoryId = categoryId) }
                .doAnswer { flowOf(products) }
        }

        val collectJob = launch { sut.products.collect() }
        sut.searchPerCategory(search = search, categoryId = categoryId)

        advanceUntilIdle()

        assertEquals(convertToItems(products), sut.products.value)

        collectJob.cancel()
    }

    @Test
    fun whenGetSearchCache_shouldDelegateToSearchCacheRepository() = runTest {
        searchCacheRepository.stub { onBlocking { getAll() } doAnswer { flowOf(searchList) } }
        sut.getSearchCache()

        advanceUntilIdle()

        verify(searchCacheRepository).getAll()
    }

    @Test
    fun whenGetSearchCache_shouldSetPropertySearchCache() = runTest {
        searchCacheRepository.stub { onBlocking { getAll() } doAnswer { flowOf(searchList) } }

        val collectJob = launch { sut.searchCache.collect() }
        sut.getSearchCache()

        advanceUntilIdle()

        assertEquals(searchList, sut.searchCache.value)

        collectJob.cancel()
    }

    @Test
    fun whenInsertSearch_shouldDelegateToProductRepository() = runTest {
        searchCacheRepository.stub { onBlocking { insert(any()) }.then {  } }
        sut.insertSearch(search = search)

        advanceUntilIdle()

        verify(searchCacheRepository).insert(any())
    }

    @Test
    fun whenInsertSearch_shouldSetPropertyProduct() = runTest {
        searchCacheRepository.stub {
            onBlocking { insert(any()) }.then {
                onBlocking { searchCacheRepository.getAll() }
                    .doAnswer { flowOf(searchList) }
            }
        }

        val collectJob = launch { sut.searchCache.collect() }

        sut.insertSearch(search = search)
        sut.getSearchCache()

        advanceUntilIdle()

        assertEquals(searchList, sut.searchCache.value)

        collectJob.cancel()
    }

    private fun convertToItems(products: List<Product>) =
        products.map {
            CommonItem(
                id = it.id,
                photo = it.photo,
                title = it.name,
                subtitle = it.company,
                description = it.description
            )
        }
}