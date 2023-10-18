package com.bruno13palhano.core.model

/**
 * Products categories.
 *
 * All categories for products.
 */
data class Category(
    override val id: Long,
    val name: String
) : Model(id = id)