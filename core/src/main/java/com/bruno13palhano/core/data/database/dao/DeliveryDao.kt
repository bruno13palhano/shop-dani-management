package com.bruno13palhano.core.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bruno13palhano.core.data.DeliveryData
import com.bruno13palhano.core.data.database.model.DeliveryEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface DeliveryDao : DeliveryData<DeliveryEntity> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(model: DeliveryEntity): Long

    @Update
    override suspend fun update(model: DeliveryEntity)

    @Delete
    override suspend fun delete(model: DeliveryEntity)

    @Query("DELETE FROM delivery_table WHERE id = :id")
    override suspend fun deleteById(id: Long)

    @Query("SELECT * FROM delivery_table")
    override fun getAll(): Flow<List<DeliveryEntity>>

    @Query("SELECT * FROM delivery_table WHERE id = :id")
    override fun getById(id: Long): Flow<DeliveryEntity>

    @Query("SELECT * FROM delivery_table WHERE id = (SELECT max(id) FROM delivery_table)")
    override fun getLast(): Flow<DeliveryEntity>

    @Query("UPDATE delivery_table SET shipping_date = :shippingDate WHERE id = :id")
    override suspend fun updateShippingDate(id: Long, shippingDate: Long)

    @Query("UPDATE delivery_table SET delivery_date = :deliveryDate WHERE id = :id")
    override suspend fun updateDeliveryDate(id: Long, deliveryDate: Long)

    @Query("UPDATE delivery_table SET delivered = :delivered WHERE id = :id")
    override suspend fun updateDelivered(id: Long, delivered: Boolean)

    @Query("SELECT * FROM delivery_table WHERE delivered = :delivered")
    override fun getDeliveries(delivered: Boolean): Flow<List<DeliveryEntity>>
}