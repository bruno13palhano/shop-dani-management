package com.bruno13palhano.core.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bruno13palhano.core.model.SearchCache

/**
 * [SearchCache] as an Entity.
 *
 * An entity class to persist search cache in database.
 * @property search the value of the search.
 * @property isOrderedByCustomer defines where to perform the search, stocked or ordered.
 */
@Entity(tableName = "search_cache_table")
internal data class SearchCacheEntity(
    @PrimaryKey
    @ColumnInfo(name = "search")
    val search: String,

    @ColumnInfo(name = "is_ordered_by_customer")
    val isOrderedByCustomer: Boolean
)

/**
 * Transforms [SearchCacheEntity] into [SearchCache].
 * @return [SearchCache]
 */
internal fun SearchCacheEntity.asExternalModel() = SearchCache(
    search = search,
    isOrderedByCustomer = isOrderedByCustomer
)

/**
 * Transforms [SearchCache] into [SearchCacheEntity].
 * @return [SearchCacheEntity].
 */
internal fun SearchCache.asInternalModel() = SearchCacheEntity(
    search = search,
    isOrderedByCustomer = isOrderedByCustomer
)