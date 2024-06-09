package com.bruno13palhano.core.model

data class StockItem(
    override val id: Long,
    val productId: Long,
    val name: String,
    val photo: ByteArray,
    val date: Long,
    val dateOfPayment: Long,
    val validity: Long,
    val quantity: Int,
    val categories: List<Category>,
    val company: String,
    val purchasePrice: Float,
    val salePrice: Float,
    val isPaid: Boolean,
    override val timestamp: String
) : Model(id = id, timestamp = timestamp) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StockItem

        if (id != other.id) return false
        if (productId != other.productId) return false
        if (name != other.name) return false
        if (!photo.contentEquals(other.photo)) return false
        if (date != other.date) return false
        if (dateOfPayment != other.dateOfPayment) return false
        if (validity != other.validity) return false
        if (quantity != other.quantity) return false
        if (categories != other.categories) return false
        if (company != other.company) return false
        if (purchasePrice != other.purchasePrice) return false
        if (salePrice != other.salePrice) return false
        if (isPaid != other.isPaid) return false
        return timestamp == other.timestamp
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + productId.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + photo.contentHashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + dateOfPayment.hashCode()
        result = 31 * result + validity.hashCode()
        result = 31 * result + quantity
        result = 31 * result + categories.hashCode()
        result = 31 * result + company.hashCode()
        result = 31 * result + purchasePrice.hashCode()
        result = 31 * result + salePrice.hashCode()
        result = 31 * result + isPaid.hashCode()
        result = 31 * result + timestamp.hashCode()
        return result
    }
}