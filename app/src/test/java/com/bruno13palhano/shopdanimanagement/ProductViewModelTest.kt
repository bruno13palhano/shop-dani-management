package com.bruno13palhano.shopdanimanagement

import app.cash.turbine.turbineScope
import com.bruno13palhano.core.data.CategoryData
import com.bruno13palhano.core.data.ProductData
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.model.Product
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

    private lateinit var product: Product

    @Before
    fun setup() {
        sut = ProductViewModel(productRepository, categoryRepository)

        product = Product(
            id = 1L,
            name = "Homem",
            code = "1",
            description = "tests",
            photo = byteArrayOf(),
            date = 100000L,
            categories = listOf(Category(id = 1L, name = "Perfumes")),
            company = "Avon"
        )
    }

    @Test
    fun updateName_shouldChangeTheProductName() = runTest {
        val nameResult = "Bruno"

        turbineScope {
            sut.updateName("Bruno")
            assertEquals(nameResult, sut.name)
        }
    }


    @Test
    fun getAllCategories_shouldSetAllCategoriesProperty() = runTest {
        val categories = listOf(
            Category(id = 1L, name = "Perfumes"),
            Category(id = 2L, name = "Soaps"),
            Category(id = 3L, name = "Others")
        )

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
        `when`(productRepository.getById(id = 1L)).thenReturn(flow { emit(product) } )
        sut.getProduct(id = 1L)

        advanceUntilIdle()
        productPropertiesTest()
    }

    @Test
    fun updateName_shouldChangeNameProperty() {
        val name = "Essencial"
        sut.updateName(name)
        assertEquals(name, sut.name)
    }

    @Test
    fun updateCode_shouldChangeCodeProperty() {
        val code = "3"
        sut.updateCode(code)
        assertEquals(code, sut.code)
    }

    @Test
    fun updateDescription_shouldChangeDescriptionProperty() {
        val description = "Perfume masculino"
        sut.updateDescription(description)
        assertEquals(description, sut.description)
    }

    @Test
    fun updatePhoto_shouldChangePhotoProperty() {
        val photo = byteArrayOf()
        sut.updatePhoto(photo)
        assertEquals(photo, sut.photo)
    }

    @Test
    fun updateDate_shouldChangeDateProperty() {
        val date = 0L
        sut.updateDate(date)
        assertEquals(date, sut.date)
    }

    @Test
    fun updateCategory_shouldChangeCategoryProperty() {
        val category = listOf(
            CategoryCheck(id = 1L, category = "Perfumes", isChecked = false),
            CategoryCheck(id = 2L, category = "Soaps", isChecked = false),
            CategoryCheck(id = 3L, category = "Others", isChecked = false)
        )
        sut.updateCategories(category)
        assertEquals(category.joinToString(", ") { it.category }, sut.category)
    }

    @Test
    fun updateCompany_shouldChangeCompanyProperty() {
        val company = "Natura"
        sut.updateCompany(company)
        assertEquals(company, sut.company)
    }

    @Test
    fun insert_shouldSetProductProperties() = runTest {
        val name = "Essencial"
        val code = "2"
        val description = "Perfume masculino"
        val photo = byteArrayOf()
        val date = 200000L
        val company = "Natura"

        `when`(productRepository.insert(any()))
            .then {
                sut.updateName(name)
                sut.updateCode(code)
                sut.updateDescription(description)
                sut.updatePhoto(photo)
                sut.updateDate(date)
                sut.updateCompany(company)
            }

        sut.insertProduct()

        advanceUntilIdle()

        assertEquals(name, sut.name)
        assertEquals(code, sut.code)
        assertEquals(description, sut.description)
        assertEquals(photo, sut.photo)
        assertEquals(date, sut.date)
        assertEquals(company, sut.company)
    }

    @Test
    fun update_shouldChangeProductProperties() = runTest {
        val name = "Essencial"
        val code = "2"
        val description = "Perfume masculino"
        val photo = byteArrayOf()
        val date = 200000L
        val company = "Natura"

        `when`(productRepository.update(any())).then {
            sut.updateName(name)
            sut.updateCode(code)
            sut.updateDescription(description)
            sut.updatePhoto(photo)
            sut.updateDate(date)
            sut.updateCompany(company)
        }

        sut.updateProduct(id = 1L)

        advanceUntilIdle()
        assertEquals(name, sut.name)
        assertEquals(code, sut.code)
        assertEquals(description, sut.description)
        assertEquals(photo, sut.photo)
        assertEquals(date, sut.date)
        assertEquals(company, sut.company)
    }

    @Test
    fun delete_shouldSetProductProperties() = runTest {
        val name = "Essencial"
        val code = "2"
        val description = "Perfume masculino"
        val photo = byteArrayOf()
        val date = 200000L
        val company = "Natura"

        `when`(productRepository.deleteById(id = 1L)).then {
            sut.updateName(name)
            sut.updateCode(code)
            sut.updateDescription(description)
            sut.updatePhoto(photo)
            sut.updateDate(date)
            sut.updateCompany(company)
        }

        sut.deleteProduct(id = 1L)

        advanceUntilIdle()
        assertEquals(name, sut.name)
        assertEquals(code, sut.code)
        assertEquals(description, sut.description)
        assertEquals(photo, sut.photo)
        assertEquals(date, sut.date)
        assertEquals(company, sut.company)
    }

    private fun productPropertiesTest() {
        assertEquals(product.name, sut.name)
        assertEquals(product.code, sut.code)
        assertEquals(product.description, sut.description)
        assertEquals(product.photo, sut.photo)
        assertEquals(product.date, sut.date)
        assertEquals(product.categories.joinToString(", ") { it.name }, sut.category)
        assertEquals(product.company, sut.company)
    }
}