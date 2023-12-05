package com.bruno13palhano.core.model

import java.time.OffsetDateTime

/**
 * Products categories.
 *
 * All categories for products.
 */
data class Category(
    override val id: Long,
    val category: String,
    override val timestamp: OffsetDateTime
) : Model(id = id, timestamp = timestamp)