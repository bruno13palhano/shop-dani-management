package com.bruno13palhano.shopdanimanagement.ui.screens.common

data class Stock(
    val id: Long,
    val name: String,
    val photo: ByteArray,
    val purchasePrice: Float,
    val quantity: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Stock

        if (id != other.id) return false
        if (name != other.name) return false
        if (!photo.contentEquals(other.photo)) return false
        if (purchasePrice != other.purchasePrice) return false
        return quantity == other.quantity
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + photo.contentHashCode()
        result = 31 * result + purchasePrice.hashCode()
        result = 31 * result + quantity
        return result
    }
}