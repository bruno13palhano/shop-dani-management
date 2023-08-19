package com.bruno13palhano.core.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bruno13palhano.core.model.Customer

@Entity(tableName = "customer_table")
internal data class CustomerEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "address")
    val address: String,

    @ColumnInfo(name = "phone_number")
    val phoneNumber: String
)

internal fun CustomerEntity.asExternalModel() = Customer(
    id = id,
    name = name,
    address = address,
    phoneNumber = phoneNumber
)

internal fun Customer.asInternalModel() = CustomerEntity(
    id = id,
    name = name,
    address = address,
    phoneNumber = phoneNumber
)