package com.bruno13palhano.core.repository.tables

import com.bruno13palhano.cache.ShopDatabase
import com.bruno13palhano.core.data.repository.category.CategoryData
import com.bruno13palhano.core.data.repository.category.DefaultCategoryData
import com.bruno13palhano.core.data.repository.product.DefaultProductData
import com.bruno13palhano.core.data.repository.product.ProductData
import com.bruno13palhano.core.mocks.makeRandomDataVersion
import com.bruno13palhano.core.mocks.makeRandomProduct
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.model.DataVersion
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
    private lateinit var productTable: ProductData
    private lateinit var categoryTable: CategoryData
    private lateinit var firstProduct: Product
    private lateinit var secondProduct: Product
    private lateinit var thirdProduct: Product
    private lateinit var firstCategory: Category
    private lateinit var secondCategory: Category
    private lateinit var thirdCategory: Category
    private lateinit var dataVersion: DataVersion

    @get:Rule
    val hiltTestRule = HiltAndroidRule(this)

    @Before
    fun before() {
        hiltTestRule.inject()

         productTable = DefaultProductData(
            productQueries = database.shopDatabaseQueries,
            productCategoriesQueries = database.productCategoriesTableQueries,
            versionQueries = database.versionTableQueries,
            ioDispatcher = Dispatchers.IO
        )

        categoryTable = DefaultCategoryData(
            database.categoryTableQueries,
            database.versionTableQueries,
            Dispatchers.IO
        )

        firstCategory = Category(id = 1L, category = "Perfumes", timestamp = "")
        secondCategory = Category(id = 2L, category = "Soaps", timestamp = "")
        thirdCategory = Category(id = 1L, "Others", timestamp = "")

        dataVersion = makeRandomDataVersion(id = 1L)
        firstProduct = makeRandomProduct(
            id = 1L,
            categories = listOf(firstCategory),
            company = "Natura",
            description = "Top 10"
        )
        secondProduct = makeRandomProduct(
            id = 2L,
            categories = listOf(
                firstCategory,
                secondCategory
            ),
            company = "Natura",
            description = "Most sale"
        )
        thirdProduct = makeRandomProduct(
            id = 3L,
            categories = listOf(thirdCategory),
            company = "Avon",
            description = "Light fragrance"
        )
    }

    @Test
    fun shouldInsertProductInTheDatabase() = runTest {
        productTable.insert(firstProduct, dataVersion, {}, {})
        launch {
            productTable.getAll().collect { products ->
                assertThat(products).contains(firstProduct)
                cancel()
            }
        }
    }

    @Test
    fun shouldUpdateProductInTheDatabase_ifProductExists() = runTest {
        val updatedProduct = makeRandomProduct(id =1L)
        productTable.insert(firstProduct, dataVersion, {}, {})
        productTable.update(updatedProduct, dataVersion, {}, {})

        launch(Dispatchers.IO) {
            productTable.getAll().collect { products ->
                assertThat(products).contains(updatedProduct)
                cancel()
            }
        }
    }

    @Test
    fun shouldDeleteProductWithThisIdInTheDatabase_ifProductExists() = runTest {
        insertAllProducts()
        productTable.deleteById(firstProduct.id, dataVersion, {}, {})

        launch(Dispatchers.IO) {
            productTable.getAll().collect { products ->
                assertThat(products).doesNotContain(firstProduct)
                cancel()
            }
        }
    }

    @Test
    fun shouldNotDeleteProductWithThisIdInTheDatabase_ifProductNotExists() = runTest {
        productTable.insert(firstProduct, dataVersion, {}, {})
        productTable.insert(secondProduct, dataVersion, {}, {})

        productTable.deleteById(thirdProduct.id, dataVersion, {}, {})

        launch(Dispatchers.IO) {
            productTable.getAll().collect { products ->
                assertThat(products).containsExactly(firstProduct, secondProduct)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnAllProductsInTheDatabase_ifDatabaseIsNotEmpty() = runTest {
        insertAllProducts()
        launch(Dispatchers.IO) {
            productTable.getAll().collect { products ->
                assertThat(products).containsExactly(firstProduct, secondProduct, thirdProduct)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnEmptyList_ifDatabaseIsEmpty() = runTest {
        launch(Dispatchers.IO) {
            productTable.getAll().take(3).collect { products ->
                assertThat(products).isEmpty()
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnProductWithThisId_ifExists() = runTest {
        productTable.insert(firstProduct, dataVersion, {}, {})
        launch(Dispatchers.IO) {
            productTable.getById(id = firstProduct.id).collect { product ->
                Assert.assertEquals(product, firstProduct)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnLastProduct_ifNotEmpty() = runTest {
        insertAllProducts()
        launch(Dispatchers.IO) {
            productTable.getLast().collect { product ->
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
            productTable.search(value = search).collect { products ->
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
            productTable.search(value = search).collect { products ->
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
            productTable.search(value = search).collect { products ->
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
            productTable.search(value = search).collect { products ->
                assertThat(products).containsExactly(firstProduct, secondProduct)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnEmptyList_ifThereIsNothingThatMatchesWithThisSearch() = runTest {
        insertAllProducts()
        launch(Dispatchers.IO) {
            productTable.search(value = "*").collect { products ->
                assertThat(products).isEmpty()
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnAllProducts_ifThisSearchIsEmpty() = runTest {
        insertAllProducts()
        launch(Dispatchers.IO) {
            productTable.search(value = "").collect { products ->
                assertThat(products).containsExactly(firstProduct, secondProduct, thirdProduct)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnProductsInThisCategoryThatMatchesWithNameSearch() = runTest {
        insertAllProducts()
        launch(Dispatchers.IO) {
            productTable.searchPerCategory(
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
            productTable.searchPerCategory(
                value = firstProduct.description,
                categoryId = firstProduct.categories[0].id
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
            productTable.searchPerCategory(
                value = firstProduct.company,
                categoryId = firstProduct.categories[0].id
            ).collect { products ->
                assertThat(products).containsExactly(firstProduct, secondProduct)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnEmptyList_ifThereIsNothingThatMatchesWithThisSearchInThisCategory() = runTest {
        insertAllProducts()
        launch(Dispatchers.IO) {
            productTable.searchPerCategory(
                value = "*",
                categoryId = firstProduct.id
            ).collect { products ->
                assertThat(products).isEmpty()
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnAllProducts_ifThisSearchPerCategoryIsEmpty() = runTest {
        insertAllProducts()
        launch(Dispatchers.IO) {
            productTable.searchPerCategory(
                value = "",
                categoryId = thirdProduct.categories[0].id
            ).collect { products ->
                assertThat(products).containsExactly(firstProduct, secondProduct, thirdProduct)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnAllProductsInThisCategory_ifNotEmpty() = runTest {
        insertAllProducts()
        launch(Dispatchers.IO) {
            productTable.getByCategory(category = "Perfumes").collect { products ->
                assertThat(products).containsExactly(firstProduct, secondProduct)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnEmptyList_ifThisCategoryIsEmpty() = runTest {
        insertAllProducts()
        launch(Dispatchers.IO) {
            productTable.getByCategory(category = "Make").collect { products ->
                assertThat(products).isEmpty()
                cancel()
            }
        }
    }

    private suspend fun insertAllProducts() {
        categoryTable.insert(firstCategory, dataVersion, {}, {})
        categoryTable.insert(secondCategory, dataVersion, {}, {})
        categoryTable.insert(thirdCategory, dataVersion, {}, {})

        productTable.insert(firstProduct, dataVersion, {}, {})
        productTable.insert(secondProduct, dataVersion, {}, {})
        productTable.insert(thirdProduct, dataVersion, {}, {})
    }
}