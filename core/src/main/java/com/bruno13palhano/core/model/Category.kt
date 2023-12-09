package com.bruno13palhano.core.model

/**
 * Products categories.
 *
 * All categories for products.
 */
data class Category(
    override val id: Long,
    val category: String,
    override val timestamp: String
) : Model(id = id, timestamp = timestamp)