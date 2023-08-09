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
    dateOfSale = dateOfSale,
    dateOfPayment = dateOfPayment,
    isPaidByClient = isPaidByClient
)

internal fun Sale.asInternalModel() = SaleEntity(
    id = id,
    productId = productId,
    dateOfSale = dateOfSale,
    dateOfPayment = dateOfPayment,
    isPaidByClient = isPaidByClient
)