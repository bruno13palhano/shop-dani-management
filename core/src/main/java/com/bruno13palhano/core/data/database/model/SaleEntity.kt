package com.bruno13palhano.core.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bruno13palhano.core.model.Sale

@Entity(tableName = "sale_table")
internal data class SaleEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "product_id")
    val productId: Long,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "quantity")
    val quantity: Int,

    @ColumnInfo(name = "purchase_price")
    val purchasePrice: Float,

    @ColumnInfo(name = "sale_price")
    val salePrice: Float,

    @ColumnInfo(name = "categories")
    val categories: List<String>,

    @ColumnInfo(name = "company")
    val company: String,

    @ColumnInfo(name = "date_of_sale")
    val dateOfSale: Long,

    @ColumnInfo(name = "date_of_payment")
    val dateOfPayment: Long,

    @ColumnInfo(name = "is_paid_by_client")
    val isPaidByClient: Boolean
)

internal fun SaleEntity.asExternalModel() = Sale(
    id = id,
    productId = productId,
    name = name,
    quantity = quantity,
    purchasePrice = purchasePrice,
    salePrice = salePrice,
    categories = categories,
    company = company,
    dateOfSale = dateOfSale,
    dateOfPayment = dateOfPayment,
    isPaidByClient = isPaidByClient
)

internal fun Sale.asInternalModel() = SaleEntity(
    id = id,
    productId = productId,
    name = name,
    quantity = quantity,
    purchasePrice = purchasePrice,
    salePrice = salePrice,
    categories = categories,
    company = company,
    dateOfSale = dateOfSale,
    dateOfPayment = dateOfPayment,
    isPaidByClient = isPaidByClient
)