package com.bruno13palhano.core.repository.tables

import com.bruno13palhano.cache.ShopDatabase
import com.bruno13palhano.core.data.CategoryData
import com.bruno13palhano.core.data.repository.category.CategoryLight
import com.bruno13palhano.core.data.repository.category.CategoryRepository
import com.bruno13palhano.core.mocks.makeRandomCategory
import com.bruno13palhano.core.model.Category
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
class CategoryLightTest {
    @Inject lateinit var database: ShopDatabase
    private lateinit var categoryRepository: CategoryData<Category>
    private lateinit var zeroIdCategory: Category
    private lateinit var firstCategory: Category
    private lateinit var secondCategory: Category
    private lateinit var thirdCategory: Category

    @get:Rule
    val hiltTestRule = HiltAndroidRule(this)

    @Before
    fun before() {
        hiltTestRule.inject()

        val categoryData = CategoryLight(database.categoryTableQueries, Dispatchers.IO)
        categoryRepository = CategoryRepository(categoryData)

        zeroIdCategory = makeRandomCategory(id = 0L)
        firstCategory = makeRandomCategory(id = 1L)
        secondCategory = makeRandomCategory(id = 2L)
        thirdCategory = makeRandomCategory(id = 3L)
    }

    @Test
    fun shouldInsertCategoryInTheDatabase() = runBlocking {
        val latch = CountDownLatch(1)
        categoryRepository.insert(firstCategory)

        val job = async(Dispatchers.IO) {
            categoryRepository.getAll().take(3).collect { categories ->
                latch.countDown()
                assertThat(categories).contains(firstCategory)
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldUpdateCategoryInTheDatabase_IfCategoryExists() = runBlocking {
        val latch = CountDownLatch(1)
        val updateCategory = makeRandomCategory(id = 1L)

        categoryRepository.insert(firstCategory)
        categoryRepository.update(updateCategory)

        val job = async(Dispatchers.IO) {
            categoryRepository.getAll().take(3).collect { categories ->
                latch.countDown()
                assertThat(categories).contains(updateCategory)
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldDoNotUpdateCategoryInTheDatabase_IfCategoryNotExists() = runBlocking {
        val latch = CountDownLatch(1)

        categoryRepository.insert(firstCategory)
        categoryRepository.update(zeroIdCategory)

        val job = async(Dispatchers.IO) {
            categoryRepository.getAll().take(3).collect { categories ->
                latch.countDown()
                assertThat(categories).doesNotContain(zeroIdCategory)
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldDeleteCategoryWithThisIdInTheDatabase_ifCategoryExists() = runBlocking {
        val latch = CountDownLatch(1)

        categoryRepository.insert(firstCategory)
        categoryRepository.insert(secondCategory)

        categoryRepository.deleteById(firstCategory.id)

        val job = async(Dispatchers.IO) {
            categoryRepository.getAll().take(3).collect { categories ->
                latch.countDown()
                assertThat(categories).doesNotContain(firstCategory)
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldNotDeleteCategoryInTheDatabase_ifCategoryWithThisIdNotExists() = runBlocking {
        val latch = CountDownLatch(1)

        categoryRepository.insert(firstCategory)
        categoryRepository.insert(secondCategory)

        categoryRepository.deleteById(thirdCategory.id)

        val job = async(Dispatchers.IO) {
            categoryRepository.getAll().take(3).collect { categories ->
                latch.countDown()
                assertThat(categories).containsExactly(firstCategory, secondCategory)
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldReturnAllCategoriesInTheDatabase_ifDatabaseIsNotEmpty() = runBlocking {
        val latch = CountDownLatch(1)
        categoryRepository.insert(firstCategory)
        categoryRepository.insert(secondCategory)
        categoryRepository.insert(thirdCategory)

        val job = async(Dispatchers.IO) {
            categoryRepository.getAll().take(3).collect { categories ->
                latch.countDown()
                assertThat(categories).containsExactly(firstCategory, secondCategory, thirdCategory)
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
            categoryRepository.getAll().take(3).collect { categories ->
                latch.countDown()
                assertThat(categories).isEmpty()
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldReturnCategoryWithThisId_ifExists() = runBlocking {
        val latch = CountDownLatch(1)

        categoryRepository.insert(firstCategory)
        categoryRepository.insert(secondCategory)

        val job = async(Dispatchers.IO) {
            categoryRepository.getById(secondCategory.id).collect { category ->
                latch.countDown()
                Assert.assertEquals(category, secondCategory)
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldReturnLastCategory_ifExists() = runBlocking {
        val latch = CountDownLatch(1)

        categoryRepository.insert(firstCategory)
        categoryRepository.insert(secondCategory)

        val job = async(Dispatchers.IO) {
            categoryRepository.getLast().collect { category ->
                latch.countDown()
                Assert.assertEquals(category, secondCategory)
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldReturnCategoriesThatMatchesWithThisSearch() = runBlocking {
        val latch = CountDownLatch(1)
        val category1 = makeRandomCategory(id = 1L, name = "Others")
        val category2 = makeRandomCategory(id = 2L, name = "Perfumes")
        val category3 = makeRandomCategory(id = 3L, "Soaps")

        categoryRepository.insert(category1)
        categoryRepository.insert(category2)
        categoryRepository.insert(category3)

        val job = async(Dispatchers.IO) {
            categoryRepository.search(value = category1.name).take(3).collect { categories ->
                latch.countDown()
                assertThat(categories).contains(category1)
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldReturnEmptyList_ifThereIsNothingThatMatchesThisSearch() = runBlocking {
        val latch = CountDownLatch(1)

        categoryRepository.insert(firstCategory)
        categoryRepository.insert(secondCategory)
        categoryRepository.insert(thirdCategory)

        val job = async(Dispatchers.IO) {
            categoryRepository.search(value = " ").take(3).collect { categories ->
                latch.countDown()
                assertThat(categories).containsNoneOf(firstCategory, secondCategory, thirdCategory)
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldReturnAllCategories_ifThisSearchIsEmpty() = runBlocking {
        val latch = CountDownLatch(1)

        categoryRepository.insert(firstCategory)
        categoryRepository.insert(secondCategory)
        categoryRepository.insert(thirdCategory)

        val job = async(Dispatchers.IO) {
            categoryRepository.search(value = "").take(3).collect { categories ->
                latch.countDown()
                assertThat(categories).containsExactly(firstCategory, secondCategory, thirdCategory)
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }
}