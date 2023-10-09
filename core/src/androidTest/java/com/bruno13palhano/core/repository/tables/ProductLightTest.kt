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
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.CountDownLatch
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
    fun shouldInsertProductInTheDatabase() = runBlocking {
        val latch = CountDownLatch(1)
        productRepository.insert(firstProduct)

        val job = async(Dispatchers.IO) {
            productRepository.getAll().take(3).collect { products ->
                latch.countDown()
                assertThat(products).contains(firstProduct)
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldUpdateProductInTheDatabase_ifProductExists() = runBlocking {
        val latch = CountDownLatch(1)
        val updatedProduct = makeRandomProduct(id =1L)
        productRepository.insert(firstProduct)

        productRepository.update(updatedProduct)

        val job = async(Dispatchers.IO) {
            productRepository.getAll().take(3).collect { products ->
                latch.countDown()
                assertThat(products).contains(updatedProduct)
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test(expected = NullPointerException::class)
    fun shouldThrowNullPointerException_ifProductNotExists() = runBlocking {
        productRepository.insert(firstProduct)
        productRepository.update(zeroIdProduct)
    }

    @Test
    fun shouldDeleteProductInTheDatabase_ifProductExists() = runBlocking {
        val latch = CountDownLatch(1)
        productRepository.insert(firstProduct)
        productRepository.insert(secondProduct)
        productRepository.insert(thirdProduct)

        productRepository.delete(firstProduct)

        val job = async(Dispatchers.IO) {
            productRepository.getAll().take(3).collect { products ->
                latch.countDown()
                assertThat(products).doesNotContain(firstProduct)
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldNotDeleteProductInTheDatabase_ifProductNotExists() = runBlocking {
        val latch = CountDownLatch(1)
        productRepository.insert(firstProduct)

        productRepository.delete(zeroIdProduct)

        val job = async(Dispatchers.IO) {
            productRepository.getAll().take(3).collect { products ->
                latch.countDown()
                assertThat(products).contains(firstProduct)
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldDeleteProductWithThisIdInTheDatabase_ifProductExists() = runBlocking {
        val latch = CountDownLatch(1)
        productRepository.insert(firstProduct)
        productRepository.insert(secondProduct)
        productRepository.insert(thirdProduct)

        productRepository.deleteById(firstProduct.id)

        val job = async(Dispatchers.IO) {
            productRepository.getAll().take(3).collect { products ->
                latch.countDown()
                assertThat(products).doesNotContain(firstProduct)
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldNotDeleteProductWithThisIdInTheDatabase_ifProductNotExists() = runBlocking {
        val latch = CountDownLatch(1)
        productRepository.insert(firstProduct)
        productRepository.insert(secondProduct)

        productRepository.deleteById(thirdProduct.id)

        val job = async(Dispatchers.IO) {
            productRepository.getAll().take(3).collect { products ->
                latch.countDown()
                assertThat(products).containsExactly(firstProduct, secondProduct)
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldReturnAllProductsInTheDatabase_ifDatabaseIsNotEmpty() = runBlocking {
        val latch = CountDownLatch(1)
        productRepository.insert(firstProduct)
        productRepository.insert(secondProduct)
        productRepository.insert(thirdProduct)

        val job = async(Dispatchers.IO) {
            productRepository.getAll().take(3).collect { products ->
                latch.countDown()
                assertThat(products).containsExactly(firstProduct, secondProduct, thirdProduct)
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldReturnEmptyList_ifDatabaseIsEmpty() = runBlocking {
        val latch = CountDownLatch(1)

        val job = async(Dispatchers.IO) {
            productRepository.getAll().take(3).collect { products ->
                latch.countDown()
                assertThat(products).isEmpty()
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldReturnProductWithThisId_ifExists() = runBlocking {
        val latch = CountDownLatch(1)

        productRepository.insert(firstProduct)

        val job = async(Dispatchers.IO) {
            productRepository.getById(id = firstProduct.id).collect { product ->
                latch.countDown()
                Assert.assertEquals(product, firstProduct)
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldReturnLastProduct_ifNotEmpty() = runBlocking {
        val latch = CountDownLatch(1)

        productRepository.insert(firstProduct)
        productRepository.insert(secondProduct)
        productRepository.insert(thirdProduct)

        val job = async(Dispatchers.IO) {
            productRepository.getLast().collect { product ->
                latch.countDown()
                Assert.assertEquals(product, thirdProduct)
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldReturnProductsThatMatchesWithCategorySearch() = runBlocking {
        val latch = CountDownLatch(1)

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
            categories = listOf(Category(id = 3L, name = "Others")
            )
        )

        productRepository.insert(product1)
        productRepository.insert(product2)
        productRepository.insert(product3)

        val search = product3.categories[0].name

        val job = async(Dispatchers.IO) {
            productRepository.search(value = search).collect { products ->
                latch.countDown()
                assertThat(products).containsExactly(product3)
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldReturnProductsThatMatchesWithNameSearch() = runBlocking {
        val latch = CountDownLatch(1)

        val product1 = makeRandomProduct(id = 1L, name = "Homem")
        val product2 = makeRandomProduct(id = 2L, name = "Kaiak")
        val product3 = makeRandomProduct(id = 3L, name = "Luna")

        productRepository.insert(product1)
        productRepository.insert(product2)
        productRepository.insert(product3)

        val search = product3.name

        val job = async(Dispatchers.IO) {
            productRepository.search(value = search).collect { products ->
                latch.countDown()
                assertThat(products).containsExactly(product3)
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldReturnProductsThatMatchesWithDescriptionSearch() = runBlocking {
        val latch = CountDownLatch(1)

        val product1 = makeRandomProduct(id = 1L, description = "Top 10")
        val product2 = makeRandomProduct(id = 2L, description = "Most sale")
        val product3 = makeRandomProduct(id = 3L, description = "Light fragrance")

        productRepository.insert(product1)
        productRepository.insert(product2)
        productRepository.insert(product3)

        val search = product3.description

        val job = async(Dispatchers.IO) {
            productRepository.search(value = search).collect { products ->
                latch.countDown()
                assertThat(products).containsExactly(product3)
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldReturnProductsThatMatchesWithCompanySearch() = runBlocking {
        val latch = CountDownLatch(1)

        val product1 = makeRandomProduct(id = 1L, company = "Natura")
        val product2 = makeRandomProduct(id = 2L, company = "Natura")
        val product3 = makeRandomProduct(id = 3L, company = "Avon")

        productRepository.insert(product1)
        productRepository.insert(product2)
        productRepository.insert(product3)

        val search = product1.company

        val job = async(Dispatchers.IO) {
            productRepository.search(value = search).collect { products ->
                latch.countDown()
                assertThat(products).containsExactly(product1, product2)
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldReturnEmptyList_ifThereIsNothingThatMatchesWithThisSearch() = runBlocking {
        val latch = CountDownLatch(1)

        productRepository.insert(firstProduct)
        productRepository.insert(secondProduct)
        productRepository.insert(thirdProduct)

        val job = async(Dispatchers.IO) {
            productRepository.search(value = " ").collect { products ->
                latch.countDown()
                assertThat(products).isEmpty()
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldReturnAllProducts_ifThisSearchIsEmpty() = runBlocking {
        val latch = CountDownLatch(1)

        productRepository.insert(firstProduct)
        productRepository.insert(secondProduct)
        productRepository.insert(thirdProduct)

        val job = async(Dispatchers.IO) {
            productRepository.search(value = "").collect { products ->
                latch.countDown()
                assertThat(products).containsExactly(firstProduct, secondProduct, thirdProduct)
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldReturnProductsInThisCategoryThatMatchesWithNameSearch() = runBlocking {
        val latch = CountDownLatch(1)

        val product1 = makeRandomProduct(id = 1L, name = "Homem")
        val product2 = makeRandomProduct(id = 2L, name = "Kaiak")
        val product3 = makeRandomProduct(id = 3L, name = "Luna")

        productRepository.insert(product1)
        productRepository.insert(product2)
        productRepository.insert(product3)

        val job = async(Dispatchers.IO) {
            productRepository.searchPerCategory(
                value = product1.name,
                categoryId = product1.id
            ).collect { products ->
                latch.countDown()
                assertThat(products).containsExactly(product1)
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldReturnProductsInThisCategoryThatMatchesWithDescriptionSearch() = runBlocking {
        val latch = CountDownLatch(1)

        val product1 = makeRandomProduct(id = 1L, description = "Homem")
        val product2 = makeRandomProduct(id = 2L, description = "Kaiak")
        val product3 = makeRandomProduct(id = 3L, description = "Luna")

        productRepository.insert(product1)
        productRepository.insert(product2)
        productRepository.insert(product3)

        val job = async(Dispatchers.IO) {
            productRepository.searchPerCategory(
                value = product1.description,
                categoryId = product1.id
            ).collect { products ->
                latch.countDown()
                assertThat(products).containsExactly(product1)
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldReturnProductsInThisCategoryThatMatchesWithCompanySearch() = runBlocking {
        val latch = CountDownLatch(1)

        val product1 = makeRandomProduct(id = 1L, company = "Natura")
        val product2 = makeRandomProduct(id = 2L, company = "Natura")
        val product3 = makeRandomProduct(id = 3L, company = "Avon")

        productRepository.insert(product1)
        productRepository.insert(product2)
        productRepository.insert(product3)

        val job = async(Dispatchers.IO) {
            productRepository.searchPerCategory(
                value = product1.company,
                categoryId = product1.id
            ).collect { products ->
                latch.countDown()
                assertThat(products).containsExactly(product1)
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldReturnEmptyList_ifThereIsNothingThatMatchesWithThisSearchInThisCategory() = runBlocking {
        val latch = CountDownLatch(1)

        productRepository.insert(firstProduct)
        productRepository.insert(secondProduct)
        productRepository.insert(thirdProduct)

        val job = async(Dispatchers.IO) {
            productRepository.searchPerCategory(
                value = " ",
                categoryId = firstProduct.id
            ).collect { products ->
                latch.countDown()
                assertThat(products).isEmpty()
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldReturnAllProductsInThisCategory_ifThisSearchIsEmpty() = runBlocking {
        val latch = CountDownLatch(1)

        productRepository.insert(firstProduct)
        productRepository.insert(secondProduct)
        productRepository.insert(thirdProduct)

        val job = async(Dispatchers.IO) {
            productRepository.searchPerCategory(
                value = "",
                categoryId = thirdProduct.id
            ).collect { products ->
                latch.countDown()
                assertThat(products).containsExactly(thirdProduct)
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldReturnAllProductsInThisCategory_ifNotEmpty() = runBlocking {
        val latch = CountDownLatch(1)

        val product1 = makeRandomProduct(id = 1L, categories = listOf(Category(id = 1L, "Perfumes")))
        val product2 = makeRandomProduct(id = 2L, categories = listOf(Category(id = 2L, name = "Soaps")))
        val product3 = makeRandomProduct(id = 3L, categories = listOf(Category(id = 1L, "Perfumes")))

        productRepository.insert(product1)
        productRepository.insert(product2)
        productRepository.insert(product3)

        val job = async(Dispatchers.IO) {
            productRepository.getByCategory(category = "Perfumes").collect { products ->
                latch.countDown()
                assertThat(products).containsExactly(product1, product3)
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldReturnEmptyList_ifThisCategoryIsEmpty() = runBlocking {
        val latch = CountDownLatch(1)

        val product1 = makeRandomProduct(id = 1L, categories = listOf(Category(id = 1L, "Perfumes")))
        val product2 = makeRandomProduct(id = 2L, categories = listOf(Category(id = 2L, name = "Soaps")))
        val product3 = makeRandomProduct(id = 3L, categories = listOf(Category(id = 1L, "Perfumes")))

        productRepository.insert(product1)
        productRepository.insert(product2)
        productRepository.insert(product3)

        val job = async(Dispatchers.IO) {
            productRepository.getByCategory(category = "Others").collect { products ->
                latch.countDown()
                assertThat(products).isEmpty()
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }
}