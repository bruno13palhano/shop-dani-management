package com.bruno13palhano.core.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bruno13palhano.core.data.ShoppingData
import com.bruno13palhano.core.data.database.model.ShoppingEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface ShoppingDao : ShoppingData<ShoppingEntity> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(model: ShoppingEntity): Long

    @Update
    override suspend fun update(model: ShoppingEntity)

    @Delete
    override suspend fun delete(model: ShoppingEntity)

    @Query("DELETE FROM shopping_table WHERE id = :id")
    override suspend fun deleteById(id: Long)

    @Query("SELECT * FROM shopping_table")
    override fun getAll(): Flow<List<ShoppingEntity>>

    @Query("SELECT * FROM shopping_table WHERE id = :id")
    override fun getById(id: Long): Flow<ShoppingEntity>

    @Query("SELECT * FROM shopping_table WHERE id = (SELECT max(id) FROM shopping_table)")
    override fun getLast(): Flow<ShoppingEntity>
}