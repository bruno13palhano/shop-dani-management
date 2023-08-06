package com.bruno13palhano.core.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bruno13palhano.core.model.SearchCache

/**
 * [SearchCache] as an Entity.
 *
 * An entity class to persist search cache in database.
 * @property id the id of this SearchCacheEntity.
 * @property search the value of the search.
 */
@Entity(tableName = "search_cache_table")
internal data class SearchCacheEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "name")
    val search: String
)

/**
 * Transforms [SearchCacheEntity] into [SearchCache].
 * @return [SearchCache]
 */
internal fun SearchCacheEntity.asExternalModel() = SearchCache(
    id = id,
    search = search
)

/**
 * Transforms [SearchCache] into [SearchCacheEntity].
 * @return [SearchCacheEntity].
 */
internal fun SearchCache.asInternalModel() = SearchCacheEntity(
    id = id,
    search = search
)