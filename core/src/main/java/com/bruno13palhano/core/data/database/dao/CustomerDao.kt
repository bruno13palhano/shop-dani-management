package com.bruno13palhano.core.data.database.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bruno13palhano.core.data.CustomerData
import com.bruno13palhano.core.data.database.model.CustomerEntity
import kotlinx.coroutines.flow.Flow

internal interface CustomerDao : CustomerData<CustomerEntity> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(model: CustomerEntity): Long

    @Update
    override suspend fun update(model: CustomerEntity)

    @Delete
    override suspend fun delete(model: CustomerEntity)

    @Query("DELETE FROM customer_table WHERE id = :id")
    override suspend fun deleteById(id: Long)

    @Query("SELECT * FROM customer_table")
    override fun getAll(): Flow<List<CustomerEntity>>

    @Query("SELECT * FROM customer_table WHERE id = :id")
    override fun getById(id: Long): Flow<CustomerEntity>

    @Query("SELECT * FROM customer_table WHERE id = (SELECT max(id) FROM customer_table)")
    override fun getLast(): Flow<CustomerEntity>
}