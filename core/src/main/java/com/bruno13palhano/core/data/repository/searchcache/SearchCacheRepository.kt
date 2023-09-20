package com.bruno13palhano.core.data.repository.searchcache

import com.bruno13palhano.core.data.SearchCacheData
import com.bruno13palhano.core.data.di.InternalSearchCacheLight
import com.bruno13palhano.core.model.SearchCache
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class SearchCacheRepository @Inject constructor(
    @InternalSearchCacheLight private val searchCacheData: SearchCacheData<SearchCache>
) : SearchCacheData<SearchCache> {
    override suspend fun insert(model: SearchCache): Long {
        return searchCacheData.insert(model = model)
    }

    override suspend fun deleteById(search: String) {
        return searchCacheData.deleteById(search)
    }

    override fun getAll(): Flow<List<SearchCache>> {
        return searchCacheData.getAll()
    }
}