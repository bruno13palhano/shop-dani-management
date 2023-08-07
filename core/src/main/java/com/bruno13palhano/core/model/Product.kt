package com.bruno13palhano.core.model

/**
* Product model.
*
* A class to model a product.
* @property id product's id.
* @property name product's name.
* @property code product's code.
* @property description product's description.
* @property quantity the quantity of this product.
* @property date the day that the product was registered.
* @property validity the validity of the product.
* @property photo product's photo Uri.
* @property categories a list of categories related with this product.
* @property company the company that produces the product.
* @property purchasePrice product purchase price.
* @property salePrice product sale price.
* @property isPaid defines if the product is paid.
* @property isSold defines if the product is sold.
* @property isPaidByCustomer defines whether the customer has already paid for the product.
* @property isOrderedByCustomer defines whether the customer ordered the product.
* @property dateOfSale the date the product was sold.
*/
data class Product(
    val id: Long,
    val name: String,
    val code: String,
    val description: String,
    val photo: String,
    val quantity: Int,
    val date: Long,
    val validity: Long,
    val categories: List<String>,
    val company: String,
    val purchasePrice: Float,
    val salePrice: Float,
    val isPaid: Boolean,
    val isSold: Boolean,
    val isPaidByCustomer: Boolean,
    val isOrderedByCustomer: Boolean,
    val dateOfSale: Long
)
