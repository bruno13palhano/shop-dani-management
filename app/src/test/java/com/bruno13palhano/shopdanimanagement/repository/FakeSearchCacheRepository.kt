package com.bruno13palhano.shopdanimanagement.repository

import com.bruno13palhano.core.data.repository.searchcache.SearchCacheRepository
import com.bruno13palhano.core.model.SearchCache
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeSearchCacheRepository : SearchCacheRepository {
    private val searchCacheList = mutableListOf<SearchCache>()

    override suspend fun insert(model: SearchCache): Long {
        searchCacheList.add(model)
        return searchCacheList.lastIndex.toLong()
    }

    override suspend fun deleteById(search: String) {
        var index = -1
        for (i in 0 until searchCacheList.size) {
            if (searchCacheList[i].search == search) {
                index = i
            }
        }

        if (index != -1) {
            searchCacheList.removeAt(index)
        }
    }

    override fun getAll(): Flow<List<SearchCache>> {
        return flowOf(searchCacheList)
    }
}