package com.bruno13palhano.core.network.model

import com.bruno13palhano.core.model.Customer
import com.squareup.moshi.Json

data class CustomerNet(
    @Json(name = "id") val id: Long,
    @Json(name = "name") val name: String,
    @Json(name = "photo") val photo: ByteArray,
    @Json(name = "email") val email: String,
    @Json(name = "address") val address: String,
    @Json(name = "phoneNumber") val phoneNumber: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CustomerNet

        if (id != other.id) return false
        if (name != other.name) return false
        if (!photo.contentEquals(other.photo)) return false
        if (email != other.email) return false
        if (address != other.address) return false
        if (phoneNumber != other.phoneNumber) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + photo.contentHashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + address.hashCode()
        result = 31 * result + phoneNumber.hashCode()
        return result
    }
}

internal fun CustomerNet.asExternal() = Customer(
    id = id,
    name = name,
    photo = photo,
    email = email,
    address = address,
    phoneNumber = phoneNumber
)

internal fun Customer.asNetwork() = CustomerNet(
    id = id,
    name = name,
    photo = photo,
    email = email,
    address = address,
    phoneNumber = phoneNumber
)