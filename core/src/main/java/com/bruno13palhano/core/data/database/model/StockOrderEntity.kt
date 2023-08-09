package com.bruno13palhano.core.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bruno13palhano.core.model.StockOrder

@Entity(tableName = "stock_order_table")
data class StockOrderEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "product_id")
    val productId: Long,

    @ColumnInfo(name = "quantity")
    val quantity: Int,

    @ColumnInfo(name = "is_ordered_by_customer")
    val isOrderedByCustomer: Boolean
)

internal fun StockOrderEntity.asExternalModel() = StockOrder(
    id = id,
    productId = productId,
    quantity = quantity,
    isOrderedByCustomer = isOrderedByCustomer
)

internal fun StockOrder.asInternalModel() = StockOrderEntity(
    id = id,
    productId = productId,
    quantity = quantity,
    isOrderedByCustomer = isOrderedByCustomer
)