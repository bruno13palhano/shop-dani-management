package com.bruno13palhano.shopdanimanagement.products

import com.bruno13palhano.core.data.CategoryData
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.shopdanimanagement.StandardDispatcherRule
import com.bruno13palhano.shopdanimanagement.repository.TestCategoryRepository
import com.bruno13palhano.shopdanimanagement.ui.screens.products.viewmodel.ProductCategoriesViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
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

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class ProductCategoriesViewModelTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val standardDispatcherRule = StandardDispatcherRule()

    private lateinit var categoryRepository: CategoryData<Category>
    private lateinit var sut: ProductCategoriesViewModel

    private var categories = listOf(
        Category(id = 1L, category = "Perfumes"),
        Category(id = 2L, category = "Soaps"),
        Category(id = 3L, category = "Others")
    )

    @Before
    fun setup() {
        categoryRepository = TestCategoryRepository()
        sut = ProductCategoriesViewModel(categoryRepository)
    }

    @Test
    fun updateName_shouldChangeNewNameProperty() {
        val newName = "test"
        sut.updateName(newName = newName)

        assertEquals(newName, sut.newName)
    }

    @Test
    fun insertCategory_shouldCallInsertFromCategoryRepository() = runTest {
        val categoryRepository = mock<CategoryData<Category>>()
        val sut = ProductCategoriesViewModel(categoryRepository)

        sut.insertCategory()
        advanceUntilIdle()

        verify(categoryRepository).insert(any())
    }

    @Test
    fun insertCategory_shouldChangeNewNameToEmpty() = runTest {
        val name = "Test"

        sut.updateName(newName = name)
        sut.insertCategory()

        assertEquals("", sut.newName)
    }

    @Test
    fun collectCategories_shouldReturnAllCategories() = runTest {
        insertCategories()
        val collectJob = launch { sut.categories.collect() }
        advanceUntilIdle()

        assertEquals(categories, sut.categories.value)

        collectJob.cancel()
    }

    private suspend fun insertCategories() = categories.forEach { categoryRepository.insert(it) }
}