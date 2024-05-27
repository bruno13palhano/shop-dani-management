package com.bruno13palhano.core.network.model

import com.bruno13palhano.core.model.Model
import com.squareup.moshi.Json

data class CustomerNet(
    @Json(name = "id") override val id: Long,
    @Json(name = "name") val name: String,
    @Json(name = "photo") val photo: String,
    @Json(name = "email") val email: String,
    @Json(name = "address") val address: String,
    @Json(name = "city") val city: String,
    @Json(name = "phoneNumber") val phoneNumber: String,
    @Json(name = "gender") val gender: String,
    @Json(name = "age") val age: Int,
    @Json(name = "timestamp") override val timestamp: String
): Model(id=  id, timestamp = timestamp) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CustomerNet

        if (id != other.id) return false
        if (name != other.name) return false
        if (photo != other.photo) return false
        if (email != other.email) return false
        if (address != other.address) return false
        if (phoneNumber != other.phoneNumber) return false
        if (timestamp != other.timestamp) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + photo.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + address.hashCode()
        result = 31 * result + phoneNumber.hashCode()
        result = 31 * result + timestamp.hashCode()
        return result
    }
}