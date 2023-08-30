package com.bruno13palhano.core.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bruno13palhano.core.data.SaleData
import com.bruno13palhano.core.data.database.model.SaleEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface SaleDao : SaleData<SaleEntity> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(model: SaleEntity): Long

    @Update
    override suspend fun update(model: SaleEntity)

    @Delete
    override suspend fun delete(model: SaleEntity)

    @Query("DELETE FROM sale_table WHERE id = :id")
    override suspend fun deleteById(id: Long)

    @Query("SELECT * FROM sale_table")
    override fun getAll(): Flow<List<SaleEntity>>

    @Query("SELECT * FROM sale_table WHERE id = :id")
    override fun getById(id: Long): Flow<SaleEntity>

    @Query("SELECT * FROM sale_table WHERE id = (SELECT max(id) FROM sale_table)")
    override fun getLast(): Flow<SaleEntity>

    @Query("SELECT * FROM sale_table WHERE customer_id = :customerId")
    override fun getByCustomerId(customerId: Long): Flow<List<SaleEntity>>

    @Query("SELECT * FROM sale_table ORDER BY id DESC LIMIT :offset, :limit")
    override fun getLastSales(offset: Int, limit: Int): Flow<List<SaleEntity>>
}