package com.bruno13palhano.core.model

/**
* Product model.
*
* A class to model a product.
* @property id product's id.
* @property name product's name.
* @property code product's code.
* @property description product's description.
* @property date the day that the product was registered.
* @property photo product's photo Uri.
* @property categories a list of categories related with this product.
* @property company the company that produces the product.
*/
data class Product(
    val id: Long,
    val name: String,
    val code: String,
    val description: String,
    val photo: ByteArray,
    val date: Long,
    val categories: List<Category>,
    val company: String
)
