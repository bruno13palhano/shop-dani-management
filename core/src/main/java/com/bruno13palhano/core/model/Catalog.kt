package com.bruno13palhano.core.model

import java.time.OffsetDateTime

data class Catalog(
    override val id: Long,
    val productId: Long,
    val name: String,
    val photo: ByteArray,
    val title: String,
    val description: String,
    val discount: Long,
    val price: Float,
    val timestamp: OffsetDateTime
) : Model(id = id) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Catalog

        if (id != other.id) return false
        if (productId != other.productId) return false
        if (name != other.name) return false
        if (!photo.contentEquals(other.photo)) return false
        if (title != other.title) return false
        if (description != other.description) return false
        if (discount != other.discount) return false
        if (price != other.price) return false
        if (timestamp != other.timestamp) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + productId.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + photo.contentHashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + discount.hashCode()
        result = 31 * result + price.hashCode()
        result = 31 * result + timestamp.hashCode()
        return result
    }
}
