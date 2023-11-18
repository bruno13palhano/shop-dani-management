package com.bruno13palhano.core.network.model

import com.squareup.moshi.Json

data class ProductNet(
    @Json val id: Long,
    @Json val name: String,
    @Json val code: String,
    @Json val description: String,
    @Json val photo: ByteArray,
    @Json val date: Long,
    @Json val company: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProductNet

        if (id != other.id) return false
        if (name != other.name) return false
        if (code != other.code) return false
        if (description != other.description) return false
        if (!photo.contentEquals(other.photo)) return false
        if (date != other.date) return false
        if (company != other.company) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + code.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + photo.contentHashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + company.hashCode()
        return result
    }
}
