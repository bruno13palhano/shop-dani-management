package com.bruno13palhano.shopdanimanagement.products

import com.bruno13palhano.core.data.CategoryData
import com.bruno13palhano.core.data.ProductData
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.model.Product
import com.bruno13palhano.shopdanimanagement.StandardDispatcherRule
import com.bruno13palhano.shopdanimanagement.ui.components.CategoryCheck
import com.bruno13palhano.shopdanimanagement.ui.screens.products.viewmodel.ProductViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.junit.MockitoRule
import org.mockito.kotlin.any

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class ProductViewModelTest {

    @Mock
    lateinit var productRepository: ProductData<Product>

    @Mock
    lateinit var categoryRepository: CategoryData<Category>

    private lateinit var sut: ProductViewModel

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get: Rule
    val standardDispatcherRule = StandardDispatcherRule()

    private var name = "Essencial"
    private var code = "2"
    private var description = "Perfume masculino"
    private var photo = byteArrayOf()
    private var date: Long = 0L
    private var categories = listOf(
        Category(id = 1L, name = "Perfumes"),
        Category(id = 2L, name = "Soaps"),
        Category(id = 3L, name = "Others")
    )
    private var company = "Natura"

    @Before
    fun setup() {
        sut = ProductViewModel(productRepository, categoryRepository)
    }

    @Test
    fun getAllCategories_shouldSetAllCategoriesProperty() = runTest {
        val categoriesCheck = listOf(
            CategoryCheck(id = 1L, category = "Perfumes", isChecked = false),
            CategoryCheck(id = 2L, category = "Soaps", isChecked = false),
            CategoryCheck(id = 3L, category = "Others", isChecked = false)
        )

        `when`(categoryRepository.getAll()).thenReturn(flow { emit(categories) })
        sut.getAllCategories()

        advanceUntilIdle()

        assertEquals(categoriesCheck, sut.allCategories)
    }

    @Test
    fun getById_shouldSetProductProperties_ifProductExists() = runTest {
        `when`(productRepository.getById(id = 1L))
            .thenReturn(
                flow {
                    emit(
                        Product(
                            id = 1L,
                            name = name,
                            code = code,
                            description = description,
                            photo = photo,
                            date = date,
                            categories = categories,
                            company = company
                        )
                    )
                }
            )
        sut.getProduct(id = 1L)

        advanceUntilIdle()
        assertProperties()
    }

    @Test
    fun updateName_shouldChangeNameProperty() {
        sut.updateName(name)
        assertEquals(name, sut.name)
    }

    @Test
    fun updateCode_shouldChangeCodeProperty() {
        sut.updateCode(code)
        assertEquals(code, sut.code)
    }

    @Test
    fun updateDescription_shouldChangeDescriptionProperty() {
        sut.updateDescription(description)
        assertEquals(description, sut.description)
    }

    @Test
    fun updatePhoto_shouldChangePhotoProperty() {
        sut.updatePhoto(photo)
        assertEquals(photo, sut.photo)
    }

    @Test
    fun updateDate_shouldChangeDateProperty() {
        sut.updateDate(date)
        assertEquals(date, sut.date)
    }

    @Test
    fun updateCategory_shouldChangeCategoryProperty() {
        val category = listOf(
            CategoryCheck(id = 1L, category = "Perfumes", isChecked = false),
            CategoryCheck(id = 2L, category = "Soaps", isChecked = false),
            CategoryCheck(id = 3L, category = "Others", isChecked = true)
        )
        sut.updateCategories(category)
        assertEquals(category[2].category, sut.category)
    }

    @Test
    fun setCategoryChecked_shouldChangeCategoryProperty() = runTest {
        `when`(categoryRepository.getAll()).thenReturn(flow { emit(categories) })
        sut.getAllCategories()

        advanceUntilIdle()

        sut.setCategoryChecked(3L)
        assertEquals(categories[2].name, sut.category)
    }

    @Test
    fun updateCompany_shouldChangeCompanyProperty() {
        sut.updateCompany(company)
        assertEquals(company, sut.company)
    }

    @Test
    fun insert_shouldSetProductProperties() = runTest {
        `when`(productRepository.insert(any())).then { setProperties() }
        sut.insertProduct()

        advanceUntilIdle()
        assertProperties()
    }

    @Test
    fun update_shouldChangeProductProperties() = runTest {
        `when`(productRepository.update(any())).then { setProperties() }
        sut.updateProduct(id = 1L)

        advanceUntilIdle()
        assertProperties()
    }

    @Test
    fun delete_shouldSetProductProperties() = runTest {
        `when`(productRepository.deleteById(id = 1L)).then { setProperties() }
        sut.deleteProduct(id = 1L)

        advanceUntilIdle()
        assertProperties()
    }

    private fun setProperties() {
        sut.updateName(name)
        sut.updateCode(code)
        sut.updateDescription(description)
        sut.updatePhoto(photo)
        sut.updateDate(date)
        sut.updateCompany(company)
    }

    private fun assertProperties() {
        assertEquals(name, sut.name)
        assertEquals(code, sut.code)
        assertEquals(description, sut.description)
        assertEquals(photo, sut.photo)
        assertEquals(date, sut.date)
        assertEquals(company, sut.company)
    }
}