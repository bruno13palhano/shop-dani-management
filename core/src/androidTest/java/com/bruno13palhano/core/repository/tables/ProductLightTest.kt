package com.bruno13palhano.core.repository.tables

import com.bruno13palhano.cache.ShopDatabase
import com.bruno13palhano.core.data.ProductData
import com.bruno13palhano.core.data.repository.product.ProductLight
import com.bruno13palhano.core.data.repository.product.ProductRepository
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
class ProductLightTest {
    @Inject lateinit var database: ShopDatabase
    private lateinit var productRepository: ProductData<Product>
    private lateinit var zeroIdProduct: Product
    private lateinit var firstProduct: Product
    private lateinit var secondProduct: Product
    private lateinit var thirdProduct: Product

    @get:Rule
    val hiltTestRule = HiltAndroidRule(this)

    @Before
    fun before() {
        hiltTestRule.inject()

        val productData = ProductLight(
            productQueries = database.shopDatabaseQueries,
            productCategoriesQueries = database.productCategoriesTableQueries,
            ioDispatcher = Dispatchers.IO
        )
        productRepository = ProductRepository(productData)

        zeroIdProduct = makeRandomProduct(id = 0L)
        firstProduct = makeRandomProduct(id = 1L)
        secondProduct = makeRandomProduct(id = 2L)
        thirdProduct = makeRandomProduct(id = 3L)
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
        productRepository.update(zeroIdProduct)
    }

    @Test
    fun shouldDeleteProductInTheDatabase_ifProductExists() = runTest {
        productRepository.insert(firstProduct)
        productRepository.insert(secondProduct)
        productRepository.insert(thirdProduct)

        productRepository.delete(firstProduct)

        launch(Dispatchers.IO) {
            productRepository.getAll().collect { products ->
                assertThat(products).doesNotContain(firstProduct)
                cancel()
            }
        }
    }

    @Test
    fun shouldNotDeleteProductInTheDatabase_ifProductNotExists() = runTest {
        productRepository.insert(firstProduct)

        productRepository.delete(zeroIdProduct)

        launch(Dispatchers.IO) {
            productRepository.getAll().collect { products ->
                assertThat(products).contains(firstProduct)
                cancel()
            }
        }
    }

    @Test
    fun shouldDeleteProductWithThisIdInTheDatabase_ifProductExists() = runTest {
        productRepository.insert(firstProduct)
        productRepository.insert(secondProduct)
        productRepository.insert(thirdProduct)

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
        productRepository.insert(firstProduct)
        productRepository.insert(secondProduct)
        productRepository.insert(thirdProduct)

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
        productRepository.insert(firstProduct)
        productRepository.insert(secondProduct)
        productRepository.insert(thirdProduct)

        launch(Dispatchers.IO) {
            productRepository.getLast().collect { product ->
                Assert.assertEquals(product, thirdProduct)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnProductsThatMatchesWithCategorySearch() = runTest {
        val product1 = makeRandomProduct(
            id = 1L,
            categories = listOf(Category(id = 1L, name = "Perfumes"))
        )
        val product2 = makeRandomProduct(id = 2L,
            categories = listOf(
                Category(id = 2L, name = "Soaps"),
                Category(id = 1L, name = "Perfumes")
            )
        )
        val product3 = makeRandomProduct(
            id = 3L,
            categories = listOf(
                Category(id = 3L, name = "Others")
            )
        )

        productRepository.insert(product1)
        productRepository.insert(product2)
        productRepository.insert(product3)

        val search = product3.categories[0].name

        launch(Dispatchers.IO) {
            productRepository.search(value = search).collect { products ->
                assertThat(products).containsExactly(product3)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnProductsThatMatchesWithNameSearch() = runTest {
        val product1 = makeRandomProduct(id = 1L, name = "Homem")
        val product2 = makeRandomProduct(id = 2L, name = "Kaiak")
        val product3 = makeRandomProduct(id = 3L, name = "Luna")

        productRepository.insert(product1)
        productRepository.insert(product2)
        productRepository.insert(product3)

        val search = product3.name

        launch(Dispatchers.IO) {
            productRepository.search(value = search).collect { products ->
                assertThat(products).containsExactly(product3)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnProductsThatMatchesWithDescriptionSearch() = runTest {
        val product1 = makeRandomProduct(id = 1L, description = "Top 10")
        val product2 = makeRandomProduct(id = 2L, description = "Most sale")
        val product3 = makeRandomProduct(id = 3L, description = "Light fragrance")

        productRepository.insert(product1)
        productRepository.insert(product2)
        productRepository.insert(product3)

        val search = product3.description

        launch(Dispatchers.IO) {
            productRepository.search(value = search).collect { products ->
                assertThat(products).containsExactly(product3)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnProductsThatMatchesWithCompanySearch() = runTest {
        val product1 = makeRandomProduct(id = 1L, company = "Natura")
        val product2 = makeRandomProduct(id = 2L, company = "Natura")
        val product3 = makeRandomProduct(id = 3L, company = "Avon")

        productRepository.insert(product1)
        productRepository.insert(product2)
        productRepository.insert(product3)

        val search = product1.company

        launch(Dispatchers.IO) {
            productRepository.search(value = search).collect { products ->
                assertThat(products).containsExactly(product1, product2)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnEmptyList_ifThereIsNothingThatMatchesWithThisSearch() = runTest {
        productRepository.insert(firstProduct)
        productRepository.insert(secondProduct)
        productRepository.insert(thirdProduct)

        launch(Dispatchers.IO) {
            productRepository.search(value = " ").collect { products ->
                assertThat(products).isEmpty()
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnAllProducts_ifThisSearchIsEmpty() = runTest {
        productRepository.insert(firstProduct)
        productRepository.insert(secondProduct)
        productRepository.insert(thirdProduct)

        launch(Dispatchers.IO) {
            productRepository.search(value = "").collect { products ->
                assertThat(products).containsExactly(firstProduct, secondProduct, thirdProduct)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnProductsInThisCategoryThatMatchesWithNameSearch() = runTest {
        val product1 = makeRandomProduct(id = 1L, name = "Homem")
        val product2 = makeRandomProduct(id = 2L, name = "Kaiak")
        val product3 = makeRandomProduct(id = 3L, name = "Luna")

        productRepository.insert(product1)
        productRepository.insert(product2)
        productRepository.insert(product3)

        launch(Dispatchers.IO) {
            productRepository.searchPerCategory(
                value = product1.name,
                categoryId = product1.id
            ).collect { products ->
                assertThat(products).containsExactly(product1)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnProductsInThisCategoryThatMatchesWithDescriptionSearch() = runTest {
        val product1 = makeRandomProduct(id = 1L, description = "Homem")
        val product2 = makeRandomProduct(id = 2L, description = "Kaiak")
        val product3 = makeRandomProduct(id = 3L, description = "Luna")

        productRepository.insert(product1)
        productRepository.insert(product2)
        productRepository.insert(product3)

        launch(Dispatchers.IO) {
            productRepository.searchPerCategory(
                value = product1.description,
                categoryId = product1.id
            ).collect { products ->
                assertThat(products).containsExactly(product1)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnProductsInThisCategoryThatMatchesWithCompanySearch() = runTest {
        val product1 = makeRandomProduct(id = 1L, company = "Natura")
        val product2 = makeRandomProduct(id = 2L, company = "Natura")
        val product3 = makeRandomProduct(id = 3L, company = "Avon")

        productRepository.insert(product1)
        productRepository.insert(product2)
        productRepository.insert(product3)

        launch(Dispatchers.IO) {
            productRepository.searchPerCategory(
                value = product1.company,
                categoryId = product1.id
            ).collect { products ->
                assertThat(products).containsExactly(product1)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnEmptyList_ifThereIsNothingThatMatchesWithThisSearchInThisCategory() = runTest {
        productRepository.insert(firstProduct)
        productRepository.insert(secondProduct)
        productRepository.insert(thirdProduct)

        launch(Dispatchers.IO) {
            productRepository.searchPerCategory(
                value = " ",
                categoryId = firstProduct.id
            ).collect { products ->
                assertThat(products).isEmpty()
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnAllProductsInThisCategory_ifThisSearchIsEmpty() = runTest {
        productRepository.insert(firstProduct)
        productRepository.insert(secondProduct)
        productRepository.insert(thirdProduct)

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
        val product1 = makeRandomProduct(id = 1L, categories = listOf(Category(id = 1L, "Perfumes")))
        val product2 = makeRandomProduct(id = 2L, categories = listOf(Category(id = 2L, name = "Soaps")))
        val product3 = makeRandomProduct(id = 3L, categories = listOf(Category(id = 1L, "Perfumes")))

        productRepository.insert(product1)
        productRepository.insert(product2)
        productRepository.insert(product3)

        launch(Dispatchers.IO) {
            productRepository.getByCategory(category = "Perfumes").collect { products ->
                assertThat(products).containsExactly(product1, product3)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnEmptyList_ifThisCategoryIsEmpty() = runTest {
        val product1 = makeRandomProduct(id = 1L, categories = listOf(Category(id = 1L, "Perfumes")))
        val product2 = makeRandomProduct(id = 2L, categories = listOf(Category(id = 2L, name = "Soaps")))
        val product3 = makeRandomProduct(id = 3L, categories = listOf(Category(id = 1L, "Perfumes")))

        productRepository.insert(product1)
        productRepository.insert(product2)
        productRepository.insert(product3)

        launch(Dispatchers.IO) {
            productRepository.getByCategory(category = "Others").collect { products ->
                assertThat(products).isEmpty()
                cancel()
            }
        }
    }
}