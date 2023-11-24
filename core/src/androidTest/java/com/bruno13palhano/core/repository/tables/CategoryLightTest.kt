package com.bruno13palhano.core.repository.tables

import com.bruno13palhano.cache.ShopDatabase
import com.bruno13palhano.core.data.CategoryData
import com.bruno13palhano.core.data.repository.category.CategoryLight
import com.bruno13palhano.core.mocks.makeRandomCategory
import com.bruno13palhano.core.model.Category
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class CategoryLightTest {
    @Inject lateinit var database: ShopDatabase
    private lateinit var categoryTable: CategoryData<Category>
    private lateinit var firstCategory: Category
    private lateinit var secondCategory: Category
    private lateinit var thirdCategory: Category

    @get:Rule
    val hiltTestRule = HiltAndroidRule(this)

    @Before
    fun before() {
        hiltTestRule.inject()

        categoryTable = CategoryLight(database.categoryTableQueries, Dispatchers.IO)

        firstCategory = makeRandomCategory(id = 1L)
        secondCategory = makeRandomCategory(id = 2L)
        thirdCategory = makeRandomCategory(id = 3L)
    }

    @Test
    fun shouldInsertCategoryInTheDatabase() = runTest {
        categoryTable.insert(firstCategory)

        launch(Dispatchers.IO) {
            categoryTable.getAll().collect { categories ->
                assertThat(categories).contains(firstCategory)
                cancel()
            }
        }
    }

    @Test
    fun shouldUpdateCategoryInTheDatabase_IfCategoryExists() = runTest {
        val updateCategory = makeRandomCategory(id = 1L)
        categoryTable.insert(firstCategory)
        categoryTable.update(updateCategory)

        launch(Dispatchers.IO) {
            categoryTable.getAll().collect { categories ->
                assertThat(categories).contains(updateCategory)
                cancel()
            }
        }
    }

    @Test
    fun shouldDoNotUpdateCategoryInTheDatabase_IfCategoryNotExists() = runTest {
        categoryTable.insert(firstCategory)
        categoryTable.update(secondCategory)

        launch(Dispatchers.IO) {
            categoryTable.getAll().collect { categories ->
                assertThat(categories).doesNotContain(secondCategory)
                cancel()
            }
        }
    }

    @Test
    fun shouldDeleteCategoryWithThisIdInTheDatabase_ifCategoryExists() = runTest {
        insertTwoCategories()
        categoryTable.deleteById(firstCategory.id)

        launch(Dispatchers.IO) {
            categoryTable.getAll().collect { categories ->
                assertThat(categories).doesNotContain(firstCategory)
                cancel()
            }
        }
    }

    @Test
    fun shouldNotDeleteCategoryInTheDatabase_ifCategoryWithThisIdNotExists() = runTest {
        insertTwoCategories()
        categoryTable.deleteById(thirdCategory.id)

        launch(Dispatchers.IO) {
            categoryTable.getAll().collect { categories ->
                assertThat(categories).containsExactly(firstCategory, secondCategory)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnAllCategoriesInTheDatabase_ifDatabaseIsNotEmpty() = runTest {
        insertAllCategories()
        launch(Dispatchers.IO) {
            categoryTable.getAll().collect { categories ->
                assertThat(categories).containsExactly(firstCategory, secondCategory, thirdCategory)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnEmptyList_ifDatabaseIsEmpty() = runTest {
        launch(Dispatchers.IO) {
            categoryTable.getAll().collect { categories ->
                assertThat(categories).isEmpty()
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnCategoryWithThisId_ifExists() = runTest {
        insertTwoCategories()
        launch(Dispatchers.IO) {
            categoryTable.getById(secondCategory.id).collect { category ->
                Assert.assertEquals(category, secondCategory)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnLastCategory_ifExists() = runTest {
        insertTwoCategories()
        launch(Dispatchers.IO) {
            categoryTable.getLast().collect { category ->
                Assert.assertEquals(category, secondCategory)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnCategoriesThatMatchesWithThisSearch() = runTest {
        val category1 = makeRandomCategory(id = 1L, name = "Others")
        val category2 = makeRandomCategory(id = 2L, name = "Perfumes")
        val category3 = makeRandomCategory(id = 3L, "Soaps")

        categoryTable.insert(category1)
        categoryTable.insert(category2)
        categoryTable.insert(category3)

        launch(Dispatchers.IO) {
            categoryTable.search(value = category1.category).collect { categories ->
                assertThat(categories).contains(category1)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnEmptyList_ifThereIsNothingThatMatchesThisSearch() = runTest {
        insertAllCategories()
        launch(Dispatchers.IO) {
            categoryTable.search(value = " ").collect { categories ->
                assertThat(categories).containsNoneOf(firstCategory, secondCategory, thirdCategory)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnAllCategories_ifThisSearchIsEmpty() = runTest {
        insertAllCategories()
        launch(Dispatchers.IO) {
            categoryTable.search(value = "").collect { categories ->
                assertThat(categories).containsExactly(firstCategory, secondCategory, thirdCategory)
                cancel()
            }
        }
    }

    private suspend fun insertTwoCategories() {
        categoryTable.insert(firstCategory)
        categoryTable.insert(secondCategory)
    }

    private suspend fun insertAllCategories() {
        categoryTable.insert(firstCategory)
        categoryTable.insert(secondCategory)
        categoryTable.insert(thirdCategory)
    }
}