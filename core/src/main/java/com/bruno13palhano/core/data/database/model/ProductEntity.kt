package com.bruno13palhano.core.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bruno13palhano.core.model.Product

/**
 * [Product] as an Entity.
 *
 * An entity class to persist products in database.
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
@Entity(tableName = "product_table")
internal data class ProductEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "code")
    val code: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "photo")
    val photo: String,

    @ColumnInfo(name = "quantity")
    val quantity: Int,

    @ColumnInfo(name = "date")
    val date: Long,

    @ColumnInfo(name = "validity")
    val validity: Long,

    @ColumnInfo(name = "categories")
    val categories: List<String>,

    @ColumnInfo(name = "company")
    val company: String,

    @ColumnInfo(name = "purchase_price")
    val purchasePrice: Float,

    @ColumnInfo(name = "sale_price")
    val salePrice: Float,

    @ColumnInfo(name = "is_paid")
    val isPaid: Boolean,

    @ColumnInfo(name = "is_sold")
    val isSold: Boolean,

    @ColumnInfo(name = "is_paid_by_customer")
    val isPaidByCustomer: Boolean,

    @ColumnInfo(name = "is_ordered_by_customer")
    val isOrderedByCustomer: Boolean,

    @ColumnInfo(name = "date_of_sale")
    val dateOfSale: Long
)

/**
 * Transforms [ProductEntity] into [Product].
 * @return [Product].
 */
internal fun ProductEntity.asExternalModel() = Product(
    id = id,
    name = name,
    code = code,
    description = description,
    photo = photo,
    quantity = quantity,
    date = date,
    validity = validity,
    categories = categories,
    company = company,
    purchasePrice = purchasePrice,
    salePrice = salePrice,
    isPaid = isPaid,
    isSold = isSold,
    isPaidByCustomer = isPaidByCustomer,
    isOrderedByCustomer = isOrderedByCustomer,
    dateOfSale = dateOfSale
)

/**
 * Transforms [Product] into [ProductEntity].
 * @return [ProductEntity].
 */
internal fun Product.asInternalModel() = ProductEntity(
    id = id,
    name = name,
    code = code,
    description = description,
    photo = photo,
    quantity = quantity,
    date = date,
    validity = validity,
    categories = categories,
    company = company,
    purchasePrice = purchasePrice,
    salePrice = salePrice,
    isPaid = isPaid,
    isSold = isSold,
    isPaidByCustomer = isPaidByCustomer,
    isOrderedByCustomer = isOrderedByCustomer,
    dateOfSale = dateOfSale
)
