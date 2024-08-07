package com.bruno13palhano.core.repository.tables

import com.bruno13palhano.cache.ShopDatabase
import com.bruno13palhano.core.data.repository.searchcache.LocalSearchCacheData
import com.bruno13palhano.core.data.repository.searchcache.SearchCacheData
import com.bruno13palhano.core.mocks.makeRandomSearchCache
import com.bruno13palhano.core.model.SearchCache
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class LocalSearchCacheDataTest {
    @Inject lateinit var database: ShopDatabase
    private lateinit var searchCacheTable: SearchCacheData
    private lateinit var firstCache: SearchCache
    private lateinit var secondCache: SearchCache
    private lateinit var thirdCache: SearchCache

    @get:Rule
    val hiltTestRule = HiltAndroidRule(this)

    @Before
    fun before() {
        hiltTestRule.inject()

        searchCacheTable = LocalSearchCacheData(database.searchCacheTableQueries, Dispatchers.IO)

        firstCache = makeRandomSearchCache(search = "Bruno")
        secondCache = makeRandomSearchCache(search = "Perfumes")
        thirdCache = makeRandomSearchCache(search = "Soaps")
    }

    @Test
    fun shouldInsertSearchCacheInDatabase() =
        runTest {
            searchCacheTable.insert(firstCache)

            launch(Dispatchers.IO) {
                searchCacheTable.getAll().collect { searchCaches ->
                    assertThat(searchCaches).contains(firstCache)
                    cancel()
                }
            }
        }

    @Test
    fun shouldDeleteSearchCacheWithThisIdInDatabase_ifSearchCacheExists() =
        runTest {
            searchCacheTable.insert(firstCache)
            searchCacheTable.insert(secondCache)

            searchCacheTable.deleteById(firstCache.search)

            launch(Dispatchers.IO) {
                searchCacheTable.getAll().collect { searchCaches ->
                    assertThat(searchCaches).doesNotContain(firstCache)
                    cancel()
                }
            }
        }

    @Test
    fun shouldNotDeleteSearchCacheInDatabase_ifSearchCacheWithThisIdNotExists() =
        runTest {
            searchCacheTable.insert(firstCache)
            searchCacheTable.insert(secondCache)

            searchCacheTable.deleteById(thirdCache.search)

            launch(Dispatchers.IO) {
                searchCacheTable.getAll().collect { searchCaches ->
                    assertThat(searchCaches).containsExactly(firstCache, secondCache)
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnAllSearchCachesInTheDatabase_ifDatabaseIsNotEmpty() =
        runTest {
            searchCacheTable.insert(firstCache)
            searchCacheTable.insert(secondCache)
            searchCacheTable.insert(thirdCache)

            launch(Dispatchers.IO) {
                searchCacheTable.getAll().collect { searchCaches ->
                    assertThat(searchCaches).containsExactly(firstCache, secondCache, thirdCache)
                    cancel()
                }
            }
        }

    @Test
    fun shouldReturnEmptyList_ifDatabaseIsEmpty() =
        runTest {
            launch(Dispatchers.IO) {
                searchCacheTable.getAll().collect { searchCaches ->
                    assertThat(searchCaches).isEmpty()
                    cancel()
                }
            }
        }
}
