package com.bruno13palhano.core.model

import java.time.OffsetDateTime

/**
* Product model.
*
* A class to model a product.
* @property id product's id.
* @property name product's name.
* @property code product's code.
* @property description product's description.
* @property date the day that the product was registered.
* @property photo product's photo Uri.
* @property categories a list of categories related with this product.
* @property company the company that produces the product.
*/
data class Product(
    override val id: Long,
    val name: String,
    val code: String,
    val description: String,
    val photo: ByteArray,
    val date: Long,
    val categories: List<Category>,
    val company: String,
    override val timestamp: OffsetDateTime
) : Model(id = id, timestamp = timestamp) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Product

        if (id != other.id) return false
        if (name != other.name) return false
        if (code != other.code) return false
        if (description != other.description) return false
        if (!photo.contentEquals(other.photo)) return false
        if (date != other.date) return false
        if (categories != other.categories) return false
        if (company != other.company) return false
        if (timestamp != other.timestamp) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + code.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + photo.contentHashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + categories.hashCode()
        result = 31 * result + company.hashCode()
        result = 31 * result + timestamp.hashCode()
        return result
    }
}
