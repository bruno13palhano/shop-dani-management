package com.bruno13palhano.core.data.repository.searchcache

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import cache.SearchCacheTableQueries
import com.bruno13palhano.core.data.SearchCacheData
import com.bruno13palhano.core.data.di.Dispatcher
import com.bruno13palhano.core.data.di.ShopDaniManagementDispatchers.IO
import com.bruno13palhano.core.model.SearchCache
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchCacheLight @Inject constructor(
    private val searchQueries: SearchCacheTableQueries,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : SearchCacheData<SearchCache> {
    override suspend fun insert(model: SearchCache): Long {
        searchQueries.insert(model.search, model.search)
        return 0L
    }

    override suspend fun deleteById(search: String) {
        searchQueries.delete(search)
    }

    override fun getAll(): Flow<List<SearchCache>> {
        return searchQueries.getAll(::mapSearchCache)
            .asFlow().mapToList(ioDispatcher)
    }

    private fun mapSearchCache(
        id: String,
        search: String
    ) = SearchCache(
        search = search
    )
}