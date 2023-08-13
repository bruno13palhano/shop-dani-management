package com.bruno13palhano.core.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bruno13palhano.core.data.StockOrderData
import com.bruno13palhano.core.data.database.model.StockOrderEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface StockOrderDao : StockOrderData<StockOrderEntity> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(model: StockOrderEntity): Long

    @Update
    override suspend fun update(model: StockOrderEntity)

    @Delete
    override suspend fun delete(model: StockOrderEntity)

    @Query("DELETE FROM stock_order_table WHERE id = :id")
    override suspend fun deleteById(id: Long)

    @Query("SELECT * FROM stock_order_table")
    override fun getAll(): Flow<List<StockOrderEntity>>

    @Query("SELECT * FROM stock_order_table WHERE id = :id")
    override fun getById(id: Long): Flow<StockOrderEntity>

    @Query("SELECT * FROM stock_order_table WHERE id = (SELECT max(id) FROM stock_order_table)")
    override fun getLast(): Flow<StockOrderEntity>

    @Query(
        "SELECT * FROM stock_order_table WHERE name LIKE '%'||:value||'%' " +
                "OR company LIKE '%'||:value||'%' " +
                "OR categories LIKE '%'||:value||'%'"
    )
    override fun search(value: String): Flow<List<StockOrderEntity>>

    @Query("SELECT * FROM stock_order_table WHERE categories LIKE '%'||:category||'%'")
    override fun getByCategory(category: String): Flow<List<StockOrderEntity>>
}