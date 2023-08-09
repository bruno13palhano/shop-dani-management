package com.bruno13palhano.core.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bruno13palhano.core.model.Shopping

@Entity(tableName = "shopping_entity")
data class ShoppingEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "product_id")
    val productId: Long,

    @ColumnInfo(name = "quantity")
    val quantity: Int,

    @ColumnInfo(name = "date")
    val date: Long,

    @ColumnInfo(name = "is_paid_by_client")
    val isPaid: Boolean
)

internal fun ShoppingEntity.asExternalModel() = Shopping(
    id = id,
    productId = productId,
    quantity = quantity,
    date = date,
    isPaid = isPaid
)

internal fun ShoppingEntity.asInternalModel() = ShoppingEntity(
    id = id,
    productId = productId,
    quantity = quantity,
    date = date,
    isPaid = isPaid
)