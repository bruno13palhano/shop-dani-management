package com.bruno13palhano.core.data.repository.searchcache

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import cache.SearchCacheTableQueries
import com.bruno13palhano.core.data.di.Dispatcher
import com.bruno13palhano.core.data.di.ShopDaniManagementDispatchers.IO
import com.bruno13palhano.core.model.SearchCache
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class DefaultSearchCacheData @Inject constructor(
    private val searchQueries: SearchCacheTableQueries,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : SearchCacheData {
    override suspend fun insert(model: SearchCache): Long {
        searchQueries.insert(model.search, model.search)
        return searchQueries.getLastId().executeAsOne()
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