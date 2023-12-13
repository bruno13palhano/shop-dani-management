package com.bruno13palhano.core.model

data class Sale(
    override val id: Long,
    val productId: Long,
    val stockId: Long,
    val customerId: Long,
    val name: String,
    val customerName: String,
    val photo: ByteArray,
    val address: String,
    val phoneNumber: String,
    val quantity: Int,
    val purchasePrice: Float,
    val salePrice: Float,
    val deliveryPrice: Float,
    val categories: List<Category>,
    val company: String,
    val dateOfSale: Long,
    val dateOfPayment: Long,
    val shippingDate: Long,
    val deliveryDate: Long,
    val isOrderedByCustomer: Boolean,
    val isPaidByCustomer: Boolean,
    val delivered: Boolean,
    val canceled: Boolean,
    override val timestamp: String
) : Model(id = id, timestamp = timestamp) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Sale

        if (id != other.id) return false
        if (productId != other.productId) return false
        if (stockId != other.stockId) return false
        if (customerId != other.customerId) return false
        if (name != other.name) return false
        if (customerName != other.customerName) return false
        if (!photo.contentEquals(other.photo)) return false
        if (address != other.address) return false
        if (phoneNumber != other.phoneNumber) return false
        if (quantity != other.quantity) return false
        if (purchasePrice != other.purchasePrice) return false
        if (salePrice != other.salePrice) return false
        if (deliveryPrice != other.deliveryPrice) return false
        if (categories != other.categories) return false
        if (company != other.company) return false
        if (dateOfSale != other.dateOfSale) return false
        if (dateOfPayment != other.dateOfPayment) return false
        if (shippingDate != other.shippingDate) return false
        if (deliveryDate != other.deliveryDate) return false
        if (isOrderedByCustomer != other.isOrderedByCustomer) return false
        if (isPaidByCustomer != other.isPaidByCustomer) return false
        if (delivered != other.delivered) return false
        if (canceled != other.canceled) return false
        if (timestamp != other.timestamp) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + productId.hashCode()
        result = 31 * result + stockId.hashCode()
        result = 31 * result + customerId.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + customerName.hashCode()
        result = 31 * result + photo.contentHashCode()
        result = 31 * result + address.hashCode()
        result = 31 * result + phoneNumber.hashCode()
        result = 31 * result + quantity
        result = 31 * result + purchasePrice.hashCode()
        result = 31 * result + salePrice.hashCode()
        result = 31 * result + deliveryPrice.hashCode()
        result = 31 * result + categories.hashCode()
        result = 31 * result + company.hashCode()
        result = 31 * result + dateOfSale.hashCode()
        result = 31 * result + dateOfPayment.hashCode()
        result = 31 * result + shippingDate.hashCode()
        result = 31 * result + deliveryDate.hashCode()
        result = 31 * result + isOrderedByCustomer.hashCode()
        result = 31 * result + isPaidByCustomer.hashCode()
        result = 31 * result + delivered.hashCode()
        result = 31 * result + canceled.hashCode()
        result = 31 * result + timestamp.hashCode()
        return result
    }
}
