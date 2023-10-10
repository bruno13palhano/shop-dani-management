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
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
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
    fun shouldInsertSearchCacheInDatabase() = runTest {
        searchCacheRepository.insert(firstCache)

        launch(Dispatchers.IO) {
            searchCacheRepository.getAll().collect { searchCaches ->
                assertThat(searchCaches).contains(firstCache)
                cancel()
            }
        }
    }

    @Test
    fun shouldDeleteSearchCacheWithThisIdInDatabase_ifSearchCacheExists() = runTest {
        searchCacheRepository.insert(firstCache)
        searchCacheRepository.insert(secondCache)

        searchCacheRepository.deleteById(firstCache.search)

        launch(Dispatchers.IO) {
            searchCacheRepository.getAll().collect { searchCaches ->
                assertThat(searchCaches).doesNotContain(firstCache)
                cancel()
            }
        }
    }

    @Test
    fun shouldNotDeleteSearchCacheInDatabase_ifSearchCacheWithThisIdNotExists() = runTest {
        searchCacheRepository.insert(firstCache)
        searchCacheRepository.insert(secondCache)

        searchCacheRepository.deleteById(thirdCache.search)

        launch(Dispatchers.IO) {
            searchCacheRepository.getAll().take(3).collect { searchCaches ->
                assertThat(searchCaches).containsExactly(firstCache, secondCache)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnAllSearchCachesInTheDatabase_ifDatabaseIsNotEmpty() = runTest {
        searchCacheRepository.insert(firstCache)
        searchCacheRepository.insert(secondCache)
        searchCacheRepository.insert(thirdCache)

        launch(Dispatchers.IO) {
            searchCacheRepository.getAll().collect { searchCaches ->
                assertThat(searchCaches).containsExactly(firstCache, secondCache, thirdCache)
                cancel()
            }
        }
    }

    @Test
    fun shouldReturnEmptyList_ifDatabaseIsEmpty() = runTest {
        launch(Dispatchers.IO) {
            searchCacheRepository.getAll().collect { searchCaches ->
                assertThat(searchCaches).isEmpty()
                cancel()
            }
        }
    }
}