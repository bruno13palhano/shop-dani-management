package com.bruno13palhano.core.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bruno13palhano.core.model.StockOrder

@Entity(tableName = "stock_order_table")
internal data class StockOrderEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "product_id")
    val productId: Long,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "photo")
    val photo: String,

    @ColumnInfo(name = "date")
    val date: Long,

    @ColumnInfo(name = "validity")
    val validity: Long,

    @ColumnInfo(name = "quantity")
    val quantity: Int,

    @ColumnInfo(name = "categories")
    val categories: List<String>,

    @ColumnInfo(name = "company")
    val company: String,

    @ColumnInfo(name = "purchase_price")
    val purchasePrice: Float,

    @ColumnInfo(name = "sale_price")
    val salePrice: Float,

    @ColumnInfo(name = "is_ordered_by_customer")
    val isOrderedByCustomer: Boolean
)

internal fun StockOrderEntity.asExternalModel() = StockOrder(
    id = id,
    productId = productId,
    name = name,
    photo = photo,
    date = date,
    validity = validity,
    quantity = quantity,
    categories = categories,
    company = company,
    purchasePrice = purchasePrice,
    salePrice = salePrice,
    isOrderedByCustomer = isOrderedByCustomer
)

internal fun StockOrder.asInternalModel() = StockOrderEntity(
    id = id,
    productId = productId,
    name = name,
    photo = photo,
    date = date,
    validity = validity,
    quantity = quantity,
    categories = categories,
    company = company,
    purchasePrice = purchasePrice,
    salePrice = salePrice,
    isOrderedByCustomer = isOrderedByCustomer
)