package com.bruno13palhano.core.model

data class SaleInfo(
    val saleId: Long,
    val customerId: Long,
    val productName: String,
    val customerName: String,
    val productPhoto: ByteArray,
    val customerPhoto: ByteArray,
    val address: String,
    val phoneNumber: String,
    val email: String,
    val salePrice: Float,
    val deliveryPrice: Float,
    val quantity: Int,
    val dateOfSale: Long
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SaleInfo

        if (saleId != other.saleId) return false
        if (customerId != other.customerId) return false
        if (productName != other.productName) return false
        if (customerName != other.customerName) return false
        if (!productPhoto.contentEquals(other.productPhoto)) return false
        if (!customerPhoto.contentEquals(other.customerPhoto)) return false
        if (address != other.address) return false
        if (phoneNumber != other.phoneNumber) return false
        if (email != other.email) return false
        if (salePrice != other.salePrice) return false
        if (deliveryPrice != other.deliveryPrice) return false
        if (quantity != other.quantity) return false
        return dateOfSale == other.dateOfSale
    }

    override fun hashCode(): Int {
        var result = saleId.hashCode()
        result = 31 * result + customerId.hashCode()
        result = 31 * result + productName.hashCode()
        result = 31 * result + customerName.hashCode()
        result = 31 * result + productPhoto.contentHashCode()
        result = 31 * result + customerPhoto.contentHashCode()
        result = 31 * result + address.hashCode()
        result = 31 * result + phoneNumber.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + salePrice.hashCode()
        result = 31 * result + deliveryPrice.hashCode()
        result = 31 * result + quantity
        result = 31 * result + dateOfSale.hashCode()
        return result
    }

    companion object {
        fun emptySaleInfo() = SaleInfo(
            saleId = 0L,
            customerId = 0L,
            productName = "",
            customerName = "",
            productPhoto = byteArrayOf(),
            customerPhoto = byteArrayOf(),
            address = "",
            phoneNumber = "",
            email = "",
            salePrice = 0F,
            deliveryPrice = 0F,
            quantity = 0,
            dateOfSale = 0L
        )
    }
}