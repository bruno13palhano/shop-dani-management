package com.bruno13palhano.shopdanimanagement.products.repository

import com.bruno13palhano.core.data.SearchCacheData
import com.bruno13palhano.core.model.SearchCache
import com.bruno13palhano.shopdanimanagement.makeRandomSearchCache
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class TestSearchCacheRepository : SearchCacheData<SearchCache> {
    val searchCacheList = mutableListOf(
        makeRandomSearchCache(search = "test"),
        makeRandomSearchCache(search = "homem"),
        makeRandomSearchCache(search = "soaps")
    )

    override suspend fun insert(model: SearchCache): Long {
        searchCacheList.add(model)
        return searchCacheList.lastIndex.toLong()
    }

    override suspend fun deleteById(search: String) {
        var index = 1000
        for (i in 0 .. searchCacheList.size) {
            if (searchCacheList[i].search == search)
                index = i
        }

        if (index < 1000)
            searchCacheList.removeAt(index)
    }

    override fun getAll(): Flow<List<SearchCache>> {
        return flowOf(searchCacheList)
    }
}