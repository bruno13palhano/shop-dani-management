package com.bruno13palhano.core.model

data class StockOrder(
    override val id: Long,
    val productId: Long,
    val name: String,
    val photo: ByteArray,
    val date: Long,
    val validity: Long,
    val quantity: Int,
    val categories: List<Category>,
    val company: String,
    val purchasePrice: Float,
    val salePrice: Float,
    val isOrderedByCustomer: Boolean,
    val isPaid: Boolean,
    override val timestamp: String
) : Model(id = id, timestamp = timestamp) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StockOrder

        if (id != other.id) return false
        if (productId != other.productId) return false
        if (name != other.name) return false
        if (!photo.contentEquals(other.photo)) return false
        if (date != other.date) return false
        if (validity != other.validity) return false
        if (quantity != other.quantity) return false
        if (categories != other.categories) return false
        if (company != other.company) return false
        if (purchasePrice != other.purchasePrice) return false
        if (salePrice != other.salePrice) return false
        if (isOrderedByCustomer != other.isOrderedByCustomer) return false
        if (isPaid != other.isPaid) return false
        if (timestamp != other.timestamp) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + productId.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + photo.contentHashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + validity.hashCode()
        result = 31 * result + quantity
        result = 31 * result + categories.hashCode()
        result = 31 * result + company.hashCode()
        result = 31 * result + purchasePrice.hashCode()
        result = 31 * result + salePrice.hashCode()
        result = 31 * result + isOrderedByCustomer.hashCode()
        result = 31 * result + isPaid.hashCode()
        result = 31 * result + timestamp.hashCode()
        return result
    }
}
