package com.bruno13palhano.core.repository.tables

import com.bruno13palhano.cache.ShopDatabase
import com.bruno13palhano.core.data.repository.product.ProductRepository
import com.bruno13palhano.core.data.repository.product.DefaultProductData
import com.bruno13palhano.core.data.repository.product.DefaultProductRepository
import com.bruno13palhano.core.mocks.makeRandomProduct
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.model.Product
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class DefaultProductDataTest {
    @Inject lateinit var database: ShopDatabase
    private lateinit var productRepository: ProductRepository<Product>
    private lateinit var firstProduct: Product
    private lateinit var secondProduct: Product
    private lateinit var thirdProduct: Product

    @get:Rule
    val hiltTestRule = HiltAndroidRule(this)

    @Before
    fun before() {
        hiltTestRule.inject()

        val productData = DefaultProductData(
            productQueries = database.shopDatabaseQueries,
            productCategoriesQueries = database.productCategoriesTableQueries,
            ioDispatcher = Dispatchers.IO
        )
        productRepository = DefaultProductRepository(productData)

        firstProduct = makeRandomProduct(
            id = 1L,
            categories = listOf(Category(id = 1L, "Perfumes")),
            company = "Natura",
            description = "Top 10"
        )
        secondProduct = makeRandomProduct(
            id = 2L,
            categories = listOf(
                Category(id = 1L, category = "Perfumes"),
                Category(id = 2L, category = "Soaps")
            ),
            company = "Natura",
            description = "Most sale"
        )
        thirdProduct = makeRandomProduct(
            id = 3L,
            categories = listOf(Category(id = 1L, "Others")),
            company = "Avon",
            description = "Light fragrance"
        )
    }

    @Test
    fun shouldInsertProductInTheDatabase() = runTest {
        productRepository.insert(firstProduct)
        launch {
            productRepository.getAll().collect { products ->
                assertThat(products).contains(firstProduct)
                cancel()
            }
        }
    }

    @Test
    fun shouldUpdateProductInTheDatabase_ifProductExists() = runTest {
        val updatedProduct = makeRandomProduct(id =1L)
        productRepository.insert(firstProduct)
        productRepository.update(updatedProduct)

        launch(Dispatchers.IO) {
            productRepository.getAll().collect { products ->
                assertThat(products).contains(updatedProduct)
                cancel()
            }
        }
    }

    @Test(expected = NullPointerException::class)
    fun shouldThrowNullPointerException_ifProductNotExists() = runTest {
        productRepository.insert(firstProduct)
        productRepository.update(secondProduct)
    }

    @Test
    fun shouldDeleteProductWithThisIdInTheDatabase_ifProductExists() = runTest {
        insertAllProducts()
        productRepository.deleteById(firstProduct.id)

        launch(Dispatchers.IO) {
            productRepository.getAll().collect { products ->
                assertThat(products).doesNotContain(firstProduct)
                cancel()
            }
        }
    }

    @Test
    fun shouldNotDeleteProductWithThisIdInTheDatabase_ifProductNotExists() = runTest {
        productRepository.insert(firstProduct)
        productRepository.insert(secondProduct)

        productRepository.deleteById(thirdProduct.id)

        launch(Dispatchers.IO) {
            productRepository.getAll().collect { products ->
                assertThat(products).containsExactly(firstProduct, secondProduct)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnAllProductsInTheDatabase_ifDatabaseIsNotEmpty() = runTest {
        insertAllProducts()
        launch(Dispatchers.IO) {
            productRepository.getAll().collect { products ->
                assertThat(products).containsExactly(firstProduct, secondProduct, thirdProduct)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnEmptyList_ifDatabaseIsEmpty() = runTest {
        launch(Dispatchers.IO) {
            productRepository.getAll().take(3).collect { products ->
                assertThat(products).isEmpty()
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnProductWithThisId_ifExists() = runTest {
        productRepository.insert(firstProduct)
        launch(Dispatchers.IO) {
            productRepository.getById(id = firstProduct.id).collect { product ->
                Assert.assertEquals(product, firstProduct)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnLastProduct_ifNotEmpty() = runTest {
        insertAllProducts()
        launch(Dispatchers.IO) {
            productRepository.getLast().collect { product ->
                Assert.assertEquals(product, thirdProduct)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnProductsThatMatchesWithCategorySearch() = runTest {
        val search = thirdProduct.categories[0].category
        insertAllProducts()

        launch(Dispatchers.IO) {
            productRepository.search(value = search).collect { products ->
                assertThat(products).containsExactly(thirdProduct)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnProductsThatMatchesWithNameSearch() = runTest {
        val search = thirdProduct.name
        insertAllProducts()

        launch(Dispatchers.IO) {
            productRepository.search(value = search).collect { products ->
                assertThat(products).containsExactly(thirdProduct)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnProductsThatMatchesWithDescriptionSearch() = runTest {
        val search = thirdProduct.description
        insertAllProducts()

        launch(Dispatchers.IO) {
            productRepository.search(value = search).collect { products ->
                assertThat(products).containsExactly(thirdProduct)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnProductsThatMatchesWithCompanySearch() = runTest {
        val search = firstProduct.company
        insertAllProducts()

        launch(Dispatchers.IO) {
            productRepository.search(value = search).collect { products ->
                assertThat(products).containsExactly(firstProduct, secondProduct)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnEmptyList_ifThereIsNothingThatMatchesWithThisSearch() = runTest {
        insertAllProducts()
        launch(Dispatchers.IO) {
            productRepository.search(value = "*").collect { products ->
                assertThat(products).isEmpty()
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnAllProducts_ifThisSearchIsEmpty() = runTest {
        insertAllProducts()
        launch(Dispatchers.IO) {
            productRepository.search(value = "").collect { products ->
                assertThat(products).containsExactly(firstProduct, secondProduct, thirdProduct)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnProductsInThisCategoryThatMatchesWithNameSearch() = runTest {
        insertAllProducts()
        launch(Dispatchers.IO) {
            productRepository.searchPerCategory(
                value = firstProduct.name,
                categoryId = firstProduct.id
            ).collect { products ->
                assertThat(products).containsExactly(firstProduct)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnProductsInThisCategoryThatMatchesWithDescriptionSearch() = runTest {
        insertAllProducts()
        launch(Dispatchers.IO) {
            productRepository.searchPerCategory(
                value = firstProduct.description,
                categoryId = firstProduct.id
            ).collect { products ->
                assertThat(products).containsExactly(firstProduct)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnProductsInThisCategoryThatMatchesWithCompanySearch() = runTest {
        insertAllProducts()
        launch(Dispatchers.IO) {
            productRepository.searchPerCategory(
                value = firstProduct.company,
                categoryId = firstProduct.id
            ).collect { products ->
                assertThat(products).containsExactly(firstProduct)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnEmptyList_ifThereIsNothingThatMatchesWithThisSearchInThisCategory() = runTest {
        insertAllProducts()
        launch(Dispatchers.IO) {
            productRepository.searchPerCategory(
                value = "*",
                categoryId = firstProduct.id
            ).collect { products ->
                assertThat(products).isEmpty()
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnAllProductsInThisCategory_ifThisSearchIsEmpty() = runTest {
        insertAllProducts()
        launch(Dispatchers.IO) {
            productRepository.searchPerCategory(
                value = "",
                categoryId = thirdProduct.id
            ).collect { products ->
                assertThat(products).containsExactly(thirdProduct)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnAllProductsInThisCategory_ifNotEmpty() = runTest {
        insertAllProducts()
        launch(Dispatchers.IO) {
            productRepository.getByCategory(category = "Perfumes").collect { products ->
                assertThat(products).containsExactly(firstProduct, secondProduct)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnEmptyList_ifThisCategoryIsEmpty() = runTest {
        insertAllProducts()
        launch(Dispatchers.IO) {
            productRepository.getByCategory(category = "Make").collect { products ->
                assertThat(products).isEmpty()
                cancel()
            }
        }
    }

    private suspend fun insertAllProducts() {
        productRepository.insert(firstProduct)
        productRepository.insert(secondProduct)
        productRepository.insert(thirdProduct)
    }
}