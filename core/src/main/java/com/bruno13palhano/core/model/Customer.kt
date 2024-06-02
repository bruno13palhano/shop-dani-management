package com.bruno13palhano.core.model

data class Customer(
    override val id: Long,
    val name: String,
    val photo: ByteArray,
    val email: String,
    val address: String,
    val city: String,
    val phoneNumber: String,
    val gender: String,
    val age: Int,
    override val timestamp: String,
) : Model(id = id, timestamp = timestamp), Buyable {
    override fun buy(
        salable: Salable,
        purchase: Purchase,
    ): Receipt {
        return salable.sell(purchase = purchase)
    }

    override fun cancel(
        reversible: Reversible,
        purchase: Purchase,
    ): Receipt {
        return reversible.reverse(purchase = purchase)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Customer

        if (id != other.id) return false
        if (name != other.name) return false
        if (!photo.contentEquals(other.photo)) return false
        if (email != other.email) return false
        if (address != other.address) return false
        if (city != other.city) return false
        if (phoneNumber != other.phoneNumber) return false
        if (gender != other.gender) return false
        if (age != other.age) return false
        return timestamp == other.timestamp
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + photo.contentHashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + address.hashCode()
        result = 31 * result + city.hashCode()
        result = 31 * result + phoneNumber.hashCode()
        result = 31 * result + gender.hashCode()
        result = 31 * result + age
        result = 31 * result + timestamp.hashCode()
        return result
    }
}
