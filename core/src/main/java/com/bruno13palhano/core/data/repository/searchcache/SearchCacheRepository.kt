package com.bruno13palhano.core.data.repository.searchcache

import com.bruno13palhano.core.model.SearchCache
import kotlinx.coroutines.flow.Flow

interface SearchCacheRepository {
    suspend fun insert(model: SearchCache): Long
    suspend fun deleteById(search: String)
    fun getAll(): Flow<List<SearchCache>>
}