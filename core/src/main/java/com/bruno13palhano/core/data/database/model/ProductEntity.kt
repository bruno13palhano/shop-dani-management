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
 * @property date the day that the product was registered.
 * @property photo product's photo Uri.
 * @property categories a list of categories related with this product.
 * @property company the company that produces the product.
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

    @ColumnInfo(name = "date")
    val date: Long,

    @ColumnInfo(name = "categories")
    val categories: List<String>,

    @ColumnInfo(name = "company")
    val company: String
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
    date = date,
    categories = categories,
    company = company
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
    date = date,
    categories = categories,
    company = company
)
