package com.bruno13palhano.shopdanimanagement.catalog

import com.bruno13palhano.core.data.repository.catalog.CatalogRepository
import com.bruno13palhano.core.data.repository.product.ProductRepository
import com.bruno13palhano.shopdanimanagement.StandardDispatcherRule
import com.bruno13palhano.shopdanimanagement.makeRandomCatalog
import com.bruno13palhano.shopdanimanagement.makeRandomProduct
import com.bruno13palhano.shopdanimanagement.repository.TestCatalogRepository
import com.bruno13palhano.shopdanimanagement.repository.TestProductRepository
import com.bruno13palhano.shopdanimanagement.ui.screens.catalog.viewmodel.CatalogItemViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
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
class CatalogStockItemViewModelTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val standardDispatcherRule = StandardDispatcherRule()

    private lateinit var sut: CatalogItemViewModel
    private lateinit var catalogRepository: CatalogRepository
    private lateinit var productRepository: ProductRepository
    private val catalogItems = listOf(
        makeRandomCatalog(id = 1L),
        makeRandomCatalog(id = 2L),
        makeRandomCatalog(id = 3L)
    )

    @Before
    fun setup() {
        catalogRepository = TestCatalogRepository()
        productRepository = TestProductRepository()
        sut = CatalogItemViewModel(catalogRepository, productRepository)
    }

    @Test
    fun updateTitle_shouldChangeTitle() {
        val title = "title"
        sut.updateTitle(title = title)

        assertEquals(title, sut.title)
    }

    @Test
    fun updateDescription_ShouldChangeDescription() {
        val description = "description"
        sut.updateDescription(description = description)

        assertEquals(description, sut.description)
    }

    @Test
    fun updatePrice_shouldChangePrice() {
        val price = "12.4"
        sut.updatePrice(price = price)

        assertEquals(price, sut.price)
    }

    @Test
    fun updateDiscount_shouldChangeDiscount() {
        val discount = "10"
        sut.updateDiscount(discount = discount)

        assertEquals(discount, sut.discount)
    }

    @Test
    fun getProduct_shouldCallGetByIdFromProductRepository() = runTest {
        val productRepository = mock<ProductRepository>()
        val catalogRepository = mock<CatalogRepository>()
        val sut = CatalogItemViewModel(catalogRepository, productRepository)

        whenever(productRepository.getById(any())).doAnswer { flowOf() }

        sut.getProduct(id = 1L)
        advanceUntilIdle()

        verify(productRepository).getById(any())
    }

    @Test
    fun getProduct_shouldSetNameAndPhotoProperty() = runTest {
        val product = makeRandomProduct(id = 1L)
        productRepository.insert(model = product, {}, {})

        sut.getProduct(id = 1L)
        advanceUntilIdle()

        assertEquals(product.name, sut.name)
        assertEquals(product.photo, sut.photo)
    }

    @Test
    fun getCatalogItem_shouldCallGetByIdFromCatalogRepository() = runTest {
        val productRepository = mock<ProductRepository>()
        val catalogRepository = mock<CatalogRepository>()
        val sut = CatalogItemViewModel(catalogRepository, productRepository)

        whenever(catalogRepository.getById(any())).doAnswer { flowOf() }

        sut.getCatalogItem(id = 1L)
        advanceUntilIdle()

        verify(catalogRepository).getById(any())
    }

    @Test
    fun getCatalogItem_shouldSetCatalogItemProperties() = runTest {
        val item = catalogItems[1]
        insertCatalogItems()

        sut.getCatalogItem(id = item.id)
        advanceUntilIdle()

        assertEquals(item.name, sut.name)
        assertEquals(item.photo, sut.photo)
        assertEquals(item.title, sut.title)
        assertEquals(item.description, sut.description)
        assertEquals(item.discount.toString(), sut.discount)
        assertEquals(item.price.toString(), sut.price)
    }

    @Test
    fun insert_shouldCallInsertFromCatalogRepository() = runTest {
        val productRepository = mock<ProductRepository>()
        val catalogRepository = mock<CatalogRepository>()
        val sut = CatalogItemViewModel(catalogRepository, productRepository)

        sut.insert({}, {})
        advanceUntilIdle()

        verify(catalogRepository).insert(any(), any(), any())
    }

    @Test
    fun update_shouldCallUpdateFromCatalogRepository() = runTest {
        val productRepository = mock<ProductRepository>()
        val catalogRepository = mock<CatalogRepository>()
        val sut = CatalogItemViewModel(catalogRepository, productRepository)

        sut.update({}, {})
        advanceUntilIdle()

        verify(catalogRepository).update(any(), any(), any())
    }

    @Test
    fun delete_shouldCallDeleteByIdFromCatalogRepository() = runTest {
        val productRepository = mock<ProductRepository>()
        val catalogRepository = mock<CatalogRepository>()
        val sut = CatalogItemViewModel(catalogRepository, productRepository)

        sut.delete({}, {})
        advanceUntilIdle()

        verify(catalogRepository).deleteById(any(), any(), any(), any())
    }

    private suspend fun insertCatalogItems() =
        catalogItems.forEach { catalogRepository.insert(it, {}, {}) }
}