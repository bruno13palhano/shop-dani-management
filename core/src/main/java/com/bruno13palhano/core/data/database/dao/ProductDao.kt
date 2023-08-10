package com.bruno13palhano.core.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bruno13palhano.core.data.ProductData
import com.bruno13palhano.core.data.database.model.ProductEntity
import kotlinx.coroutines.flow.Flow

/**
 * [ProductEntity] Dao interface.
 *
 * A Data Access Object for the [ProductEntity].
 * This interface is responsible for handling [ProductEntity] access to the Room database.
 */
@Dao
internal interface ProductDao : ProductData<ProductEntity> {

    /**
     * Inserts a [ProductEntity] into the database.
     * @param model the new [ProductEntity].
     * @return the id of the new [ProductEntity].
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(model: ProductEntity): Long

    /**
     * Updates the [ProductEntity] in the database.
     * @param model the [ProductEntity] to be updated.
     */
    @Update
    override suspend fun update(model: ProductEntity)

    /**
     * Deletes the [ProductEntity] in the database.
     * @param model the [ProductEntity] to be deleted.
     */
    @Delete
    override suspend fun delete(model: ProductEntity)

    /**
     * Deletes the [ProductEntity] specified by this [id].
     * @param id the [id] for this [ProductEntity].
     */
    @Query("DELETE FROM product_table WHERE id = :id")
    override suspend fun deleteById(id: Long)

    /**
     * Gets all [ProductEntity].
     * @return a [Flow] containing a [List] of all [ProductEntity].
     */
    @Query("SELECT * FROM product_table")
    override fun getAll(): Flow<List<ProductEntity>>

    /**
     * Gets the [ProductEntity] specified by this [id].
     * @param id the [id] for this [ProductEntity].
     * @return a [Flow] of [ProductEntity].
     */
    @Query("SELECT * FROM product_table WHERE id = :id")
    override fun getById(id: Long): Flow<ProductEntity>

    /**
     * Gets the last [ProductEntity] inserted into the database.
     * @return a [Flow] of [ProductEntity].
     */
    @Query("SELECT * FROM product_table WHERE id = (SELECT max(id) FROM product_table)")
    override fun getLast(): Flow<ProductEntity>

    /**
     * Searches for [ProductEntity] with this value.
     * @param value the searching value.
     * @return a [Flow] containing a [List] of all [ProductEntity] referring to the search.
     */
    @Query(
        "SELECT * FROM product_table WHERE name LIKE '%'||:value||'%' " +
                "OR description LIKE '%'||:value||'%' " +
                "OR company LIKE '%'||:value||'%' " +
                "OR categories LIKE '%'||:value||'%'"
    )
    override fun search(value: String): Flow<List<ProductEntity>>
}