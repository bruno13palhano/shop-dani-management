package com.bruno13palhano.core.model

/**
* Product model.
*
* A class to model a product.
* @property id product's id.
* @property name product's name.
* @property description product's description.
* @property photo product's photo Uri.
* @property categories a list of categories related with this product.
* @property company the company that produces the product.
* @property purchasePrice product purchase price.
* @property salePrice product sale price.
* @property isPaid defines if the product is paid.
* @property isSold defines if the product is sold.
*/
data class Product(
    val id: Long,
    val name: String,
    val description: String,
    val photo: String,
    val categories: List<String>,
    val company: String,
    val purchasePrice: Float,
    val salePrice: Float,
    val isPaid: Boolean,
    val isSold: Boolean
)
