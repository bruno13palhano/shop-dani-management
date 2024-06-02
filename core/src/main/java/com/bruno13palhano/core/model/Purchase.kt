package com.bruno13palhano.core.model

class Purchase(
    val id: Long,
    val stockId: Long,
    val customerId: Long,
    val productName: String,
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
    val amazonCode: String,
    val amazonRequestNumber: Long,
    val amazonTax: Int,
    val amazonProfit: Float,
    val amazonSKU: String,
    val resaleProfit: Float,
    val totalProfit: Float,
    val dateOfSale: Long,
    val dateOfPayment: Long,
    val shippingDate: Long,
    val deliveryDate: Long,
    val isOrderedByCustomer: Boolean,
    val isPaidByCustomer: Boolean,
    val delivered: Boolean,
    val cancelled: Boolean,
    val isAmazon: Boolean,
    val timestamp: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Purchase

        if (id != other.id) return false
        if (stockId != other.stockId) return false
        if (customerId != other.customerId) return false
        if (productName != other.productName) return false
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
        if (amazonCode != other.amazonCode) return false
        if (amazonRequestNumber != other.amazonRequestNumber) return false
        if (amazonTax != other.amazonTax) return false
        if (amazonProfit != other.amazonProfit) return false
        if (amazonSKU != other.amazonSKU) return false
        if (resaleProfit != other.resaleProfit) return false
        if (totalProfit != other.totalProfit) return false
        if (dateOfSale != other.dateOfSale) return false
        if (dateOfPayment != other.dateOfPayment) return false
        if (shippingDate != other.shippingDate) return false
        if (deliveryDate != other.deliveryDate) return false
        if (isOrderedByCustomer != other.isOrderedByCustomer) return false
        if (isPaidByCustomer != other.isPaidByCustomer) return false
        if (delivered != other.delivered) return false
        if (cancelled != other.cancelled) return false
        if (isAmazon != other.isAmazon) return false
        if (timestamp != other.timestamp) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + stockId.hashCode()
        result = 31 * result + customerId.hashCode()
        result = 31 * result + productName.hashCode()
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
        result = 31 * result + amazonCode.hashCode()
        result = 31 * result + amazonRequestNumber.hashCode()
        result = 31 * result + amazonTax
        result = 31 * result + amazonProfit.hashCode()
        result = 31 * result + amazonSKU.hashCode()
        result = 31 * result + resaleProfit.hashCode()
        result = 31 * result + totalProfit.hashCode()
        result = 31 * result + dateOfSale.hashCode()
        result = 31 * result + dateOfPayment.hashCode()
        result = 31 * result + shippingDate.hashCode()
        result = 31 * result + deliveryDate.hashCode()
        result = 31 * result + isOrderedByCustomer.hashCode()
        result = 31 * result + isPaidByCustomer.hashCode()
        result = 31 * result + delivered.hashCode()
        result = 31 * result + cancelled.hashCode()
        result = 31 * result + isAmazon.hashCode()
        result = 31 * result + timestamp.hashCode()
        return result
    }
}
