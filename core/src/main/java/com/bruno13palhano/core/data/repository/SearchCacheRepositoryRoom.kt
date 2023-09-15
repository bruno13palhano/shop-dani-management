package com.bruno13palhano.core.data.repository

import com.bruno13palhano.core.data.SearchCacheData
import com.bruno13palhano.core.data.database.dao.SearchCacheDao
import com.bruno13palhano.core.data.database.model.asExternalModel
import com.bruno13palhano.core.data.database.model.asInternalModel
import com.bruno13palhano.core.model.SearchCache
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class SearchCacheRepositoryRoom @Inject constructor(
    private val searchCacheDao: SearchCacheDao
) : SearchCacheData<SearchCache> {
    override suspend fun insert(model: SearchCache): Long {
        return searchCacheDao.insert(model.asInternalModel())
    }

    override suspend fun deleteById(search: String) {
        searchCacheDao.deleteById(search)
    }

    override fun getAll(): Flow<List<SearchCache>> {
        return searchCacheDao.getAll()
            .map {
                it.map { entity -> entity.asExternalModel() }
            }
    }
}