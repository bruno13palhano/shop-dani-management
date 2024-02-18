package com.bruno13palhano.core.model

data class CustomerInfo(
    val name: String,
    val address: String,
    val photo: ByteArray,
    val owingValue: String,
    val purchasesValue: String,
    val lastPurchaseValue: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CustomerInfo

        if (name != other.name) return false
        if (address != other.address) return false
        if (!photo.contentEquals(other.photo)) return false
        if (owingValue != other.owingValue) return false
        if (purchasesValue != other.purchasesValue) return false
        return lastPurchaseValue == other.lastPurchaseValue
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + address.hashCode()
        result = 31 * result + photo.contentHashCode()
        result = 31 * result + owingValue.hashCode()
        result = 31 * result + purchasesValue.hashCode()
        result = 31 * result + lastPurchaseValue.hashCode()
        return result
    }

    companion object {
        fun emptyCustomerInfo() = CustomerInfo(
            name = "",
            address = "",
            photo = byteArrayOf(),
            owingValue = "",
            purchasesValue = "",
            lastPurchaseValue = ""
        )
    }
}