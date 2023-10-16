package com.bruno13palhano.shopdanimanagement.products

import com.bruno13palhano.core.data.CategoryData
import com.bruno13palhano.core.data.ProductData
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.model.Product
import com.bruno13palhano.shopdanimanagement.StandardDispatcherRule
import com.bruno13palhano.shopdanimanagement.makeRandomProduct
import com.bruno13palhano.shopdanimanagement.repository.TestCategoryRepository
import com.bruno13palhano.shopdanimanagement.repository.TestProductRepository
import com.bruno13palhano.shopdanimanagement.ui.screens.common.CommonItem
import com.bruno13palhano.shopdanimanagement.ui.screens.products.viewmodel.ProductListViewModel
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
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class ProductListViewModelTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val standardDispatcherRule = StandardDispatcherRule()

    private lateinit var productRepository: ProductData<Product>
    private lateinit var categoryRepository: CategoryData<Category>
    private lateinit var sut: ProductListViewModel
    private var products = listOf(
        makeRandomProduct(id = 1L, name = "Homem"),
        makeRandomProduct(id = 2L, name = "Essencial"),
        makeRandomProduct(id = 3L, name = "Homem")
    )
    private var categories = listOf(
        Category(id = 1L, name = "Perfumes"),
        Category(id = 2L, name = "Soaps"),
        Category(id = 3L, name = "Others")
    )

    @Before
    fun setUp() {
        productRepository = TestProductRepository()
        categoryRepository = TestCategoryRepository()
        sut = ProductListViewModel(categoryRepository, productRepository)
    }

    @Test
    fun updateName_shouldChangeNameProperty() {
        val name = "Test"
        sut.updateName(name = name)

        assertEquals(name, sut.name)
    }

    @Test
    fun updateCategory_shouldCallUpdateFromCategoryRepository() = runTest {
        val productRep = mock<ProductData<Product>>()
        val categoryRep = mock<CategoryData<Category>>()
        val sut = ProductListViewModel(categoryRep, productRep)

        sut.updateCategory(id = 1L)
        advanceUntilIdle()

        verify(categoryRep).update(any())
    }

    @Test
    fun getCategory_shouldCallGetByIdFromCategoryRepository() = runTest {
        val productRep = mock<ProductData<Product>>()
        val categoryRep = mock<CategoryData<Category>>()
        val sut = ProductListViewModel(categoryRep, productRep)

        whenever(categoryRep.getById(any())).doAnswer { flowOf() }
        sut.getCategory(id = 1L)
        advanceUntilIdle()

        verify(categoryRep).getById(any())
    }

    @Test
    fun getCategory_shouldSetName_ifCategoryExists() = runTest {
        val name = categories[0].name
        insertCategories()

        sut.getCategory(id = 1L)
        advanceUntilIdle()

        assertEquals(name, sut.name)
    }

    @Test
    fun whenCollectCategories_shouldReturnAllCategories() = runTest {
        insertCategories()
        val collectJob = launch { sut.categories.collect() }
        advanceUntilIdle()

        assertEquals(categories.map { it.name }, sut.categories.value)
        collectJob.cancel()
    }

    @Test
    fun getAllProducts_shouldCallGetAllFromProductRepository() = runTest {
        val productRep = mock<ProductData<Product>>()
        val categoryRep = mock<CategoryData<Category>>()
        val sut = ProductListViewModel(categoryRep, productRep)

        whenever(productRep.getAll()).doAnswer { flowOf() }
        sut.getAllProducts()
        advanceUntilIdle()

        verify(productRep).getAll()
    }

    @Test
    fun getAllProducts_shouldSetOrderProperty() = runTest {
        insertProducts()
        val collectJob = launch { sut.orders.collect() }

        sut.getAllProducts()
        advanceUntilIdle()

        assertEquals(mapToItems(products), sut.orders.value)

        collectJob.cancel()
    }

    @Test
    fun getProductsByCategory_shouldCallGetProductsByCategoryFromProductRepository() = runTest {
        val category = "Perfumes"
        val productRep = mock<ProductData<Product>>()
        val categoryRep = mock<CategoryData<Category>>()
        val sut = ProductListViewModel(categoryRep, productRep)

        whenever(productRep.getByCategory(any())).doAnswer { flowOf() }
        sut.getProductsByCategory(category = category)
        advanceUntilIdle()

        verify(productRep).getByCategory(any())
    }

    @Test
    fun getProductsByCategory_shouldSetOrderProperty() = runTest {
        insertProducts()
        val category = products[2].categories[1].name
        val collectJob = launch { sut.orders.collect() }

        sut.getProductsByCategory(category = category)
        advanceUntilIdle()

        assertEquals(mapToItems(listOf(products[2])), sut.orders.value)

        collectJob.cancel()
    }

    private fun mapToItems(products: List<Product>) =
        products.map {
            CommonItem(
                id = it.id,
                photo = it.photo,
                title = it.name,
                subtitle = it.company,
                description = it.date.toString()
            )
        }

    private suspend fun insertCategories() = categories.forEach { categoryRepository.insert(it) }
    private suspend fun insertProducts() = products.forEach { productRepository.insert(it) }
}