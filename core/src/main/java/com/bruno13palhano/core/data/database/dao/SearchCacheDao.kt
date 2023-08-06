package com.bruno13palhano.core.data.database.dao

import androidx.room.Dao
import com.bruno13palhano.core.data.database.model.SearchCacheEntity
import kotlinx.coroutines.flow.Flow

/**
 * [SearchCacheEntity] Dao interface.
 *
 * A Data Access Object for the [SearchCacheEntity].
 * This interface is responsible for handling [SearchCacheEntity] access to the Room database.
 */
@Dao
internal interface SearchCacheDao {
    /**
     * Inserts a [SearchCacheEntity] into the database.
     * @param searchCache the new [SearchCacheEntity].
     * @return the id of the new [SearchCacheEntity].
     */
    suspend fun insert(searchCache: SearchCacheEntity): Long

    /**
     * Deletes the [SearchCacheEntity] specified by this [id].
     * @param id the [id] for this [SearchCacheEntity].
     */
    suspend fun deleteById(id: Long)

    /**
     * Gets all [SearchCacheEntity].
     * @return a [Flow] containing a [List] of all [SearchCacheEntity].
     */
    fun getAll(): Flow<List<SearchCacheEntity>>
}