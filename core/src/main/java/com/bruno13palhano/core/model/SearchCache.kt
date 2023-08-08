package com.bruno13palhano.core.model

/**
 * A cache for search.
 *
 * A class to wrapper the searches.
 * @property search the value of the search.
 * @property isOrderedByCustomer defines where to perform the search, stocked or ordered.
 */
data class SearchCache(
    val search: String,
    val isOrderedByCustomer: Boolean
)
