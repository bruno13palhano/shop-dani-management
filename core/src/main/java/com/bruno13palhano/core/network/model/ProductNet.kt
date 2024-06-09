package com.bruno13palhano.core.network.model

import com.bruno13palhano.core.model.Model
import com.squareup.moshi.Json

data class ProductNet(
    @Json(name = "id") override val id: Long,
    @Json(name = "name") val name: String,
    @Json(name = "code") val code: String,
    @Json(name = "description") val description: String,
    @Json(name = "photo") val photo: String,
    @Json(name = "date") val date: Long,
    @Json(name = "categories") val categories: List<CategoryNet>,
    @Json(name = "company") val company: String,
    @Json(name = "timestamp") override val timestamp: String
) : Model(id = id, timestamp = timestamp) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProductNet

        if (id != other.id) return false
        if (name != other.name) return false
        if (code != other.code) return false
        if (description != other.description) return false
        if (photo != other.photo) return false
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
        result = 31 * result + photo.hashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + categories.hashCode()
        result = 31 * result + company.hashCode()
        result = 31 * result + timestamp.hashCode()
        return result
    }
}