package com.bruno13palhano.core.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bruno13palhano.core.model.Delivery

@Entity(tableName = "delivery_table")
internal data class DeliveryEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "sale_id")
    val saleId: Long,

    @ColumnInfo(name = "customer_name")
    val customerName: String,

    @ColumnInfo(name = "address")
    val address: String,

    @ColumnInfo(name = "phone_number")
    val phoneNumber: String,

    @ColumnInfo(name = "product_name")
    val productName: String,

    @ColumnInfo(name = "price")
    val price: Float,

    @ColumnInfo(name = "shipping_date")
    val shippingDate: Long,

    @ColumnInfo(name = "delivery_date")
    val deliveryDate: Long,

    @ColumnInfo(name = "delivered")
    val delivered: Boolean
)

internal fun DeliveryEntity.asExternalModel() = Delivery(
    id = id,
    saleId = saleId,
    customerName = customerName,
    address = address,
    phoneNumber = phoneNumber,
    productName = productName,
    price = price,
    shippingDate = shippingDate,
    deliveryDate = deliveryDate,
    delivered = delivered
)

internal fun Delivery.asInternalModel() = DeliveryEntity(
    id = id,
    saleId = saleId,
    customerName = customerName,
    address = address,
    phoneNumber = phoneNumber,
    productName = productName,
    price = price,
    shippingDate = shippingDate,
    deliveryDate = deliveryDate,
    delivered = delivered
)