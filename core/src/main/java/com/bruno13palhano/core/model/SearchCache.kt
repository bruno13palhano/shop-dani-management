package com.bruno13palhano.core.model

/**
 * A cache for search.
 *
 * A class to wrapper the searches.
 * @property id the id of this SearchCache.
 * @property search the value of the search.
 */
data class SearchCache(
    val id: Long,
    val search: String,
)
