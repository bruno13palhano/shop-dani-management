package com.bruno13palhano.core.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.bruno13palhano.core.data.SearchCacheData
import com.bruno13palhano.core.data.database.model.SearchCacheEntity
import kotlinx.coroutines.flow.Flow

/**
 * [SearchCacheEntity] Dao interface.
 *
 * A Data Access Object for the [SearchCacheEntity].
 * This interface is responsible for handling [SearchCacheEntity] access to the Room database.
 */
@Dao
internal interface SearchCacheDao : SearchCacheData<SearchCacheEntity> {
    /**
     * Inserts a [SearchCacheEntity] into the database.
     * @param model the new [SearchCacheEntity].
     * @return the id of the new [SearchCacheEntity].
     */
    @Insert
    override suspend fun insert(model: SearchCacheEntity): Long

    /**
     * Deletes the [SearchCacheEntity] specified by this [id].
     * @param id the [id] for this [SearchCacheEntity].
     */
    @Query("DELETE FROM search_cache_table WHERE id = :id")
    override suspend fun deleteById(id: Long)

    /**
     * Gets all [SearchCacheEntity].
     * @return a [Flow] containing a [List] of all [SearchCacheEntity].
     */
    @Query("SELECT * FROM search_cache_table")
    override fun getAll(): Flow<List<SearchCacheEntity>>
}