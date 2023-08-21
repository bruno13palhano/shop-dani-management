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

    @ColumnInfo(name = "photo")
    val photo: String,

    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "address")
    val address: String,

    @ColumnInfo(name = "phone_number")
    val phoneNumber: String
)

internal fun CustomerEntity.asExternalModel() = Customer(
    id = id,
    name = name,
    photo = photo,
    email = email,
    address = address,
    phoneNumber = phoneNumber
)

internal fun Customer.asInternalModel() = CustomerEntity(
    id = id,
    name = name,
    photo = photo,
    email = email,
    address = address,
    phoneNumber = phoneNumber
)