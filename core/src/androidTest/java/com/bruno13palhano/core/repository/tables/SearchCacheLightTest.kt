package com.bruno13palhano.core.repository.tables

import com.bruno13palhano.cache.ShopDatabase
import com.bruno13palhano.core.data.SearchCacheData
import com.bruno13palhano.core.data.repository.searchcache.SearchCacheLight
import com.bruno13palhano.core.data.repository.searchcache.SearchCacheRepository
import com.bruno13palhano.core.mocks.makeRandomSearchCache
import com.bruno13palhano.core.model.SearchCache
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.CountDownLatch
import javax.inject.Inject

@HiltAndroidTest
class SearchCacheLightTest {
    @Inject lateinit var database: ShopDatabase
    private lateinit var searchCacheRepository: SearchCacheData<SearchCache>
    private lateinit var firstCache: SearchCache
    private lateinit var secondCache: SearchCache
    private lateinit var thirdCache: SearchCache

    @get:Rule
    val hiltTestRule = HiltAndroidRule(this)

    @Before
    fun before() {
        hiltTestRule.inject()

        val searchCacheData = SearchCacheLight(database.searchCacheTableQueries, Dispatchers.IO)
        searchCacheRepository = SearchCacheRepository(searchCacheData)

        firstCache = makeRandomSearchCache(search = "Bruno")
        secondCache = makeRandomSearchCache(search = "Perfumes")
        thirdCache = makeRandomSearchCache(search = "Soaps")
    }

    @Test
    fun shouldInsertSearchCacheInDatabase() = runBlocking {
        val latch = CountDownLatch(1)

        searchCacheRepository.insert(firstCache)

        val job = async(Dispatchers.IO) {
            searchCacheRepository.getAll().take(3).collect { searchCaches ->
                latch.countDown()
                assertThat(searchCaches).contains(firstCache)
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldDeleteSearchCacheWithThisIdInDatabase_ifSearchCacheExists() = runBlocking {
        val latch = CountDownLatch(1)

        searchCacheRepository.insert(firstCache)
        searchCacheRepository.insert(secondCache)

        searchCacheRepository.deleteById(firstCache.search)

        val job = async(Dispatchers.IO) {
            searchCacheRepository.getAll().take(3).collect { searchCaches ->
                latch.countDown()
                assertThat(searchCaches).doesNotContain(firstCache)
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldNotDeleteSearchCacheInDatabase_ifSearchCacheWithThisIdNotExists() = runBlocking {
        val latch = CountDownLatch(1)

        searchCacheRepository.insert(firstCache)
        searchCacheRepository.insert(secondCache)

        searchCacheRepository.deleteById(thirdCache.search)

        val job = async(Dispatchers.IO) {
            searchCacheRepository.getAll().take(3).collect { searchCaches ->
                latch.countDown()
                assertThat(searchCaches).containsExactly(firstCache, secondCache)
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

    @Test
    fun shouldReturnAllSearchCachesInTheDatabase_ifDatabaseIsNotEmpty() = runBlocking {
        val latch = CountDownLatch(1)

        searchCacheRepository.insert(firstCache)
        searchCacheRepository.insert(secondCache)
        searchCacheRepository.insert(thirdCache)

        val job = async(Dispatchers.IO) {
            searchCacheRepository.getAll().take(3).collect { searchCaches ->
                latch.countDown()
                assertThat(searchCaches).containsExactly(firstCache, secondCache, thirdCache)
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
            searchCacheRepository.getAll().take(3).collect { searchCaches ->
                latch.countDown()
                assertThat(searchCaches).isEmpty()
            }
        }

        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }
}