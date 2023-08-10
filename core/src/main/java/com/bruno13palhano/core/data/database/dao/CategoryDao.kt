package com.bruno13palhano.core.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bruno13palhano.core.data.CategoryData
import com.bruno13palhano.core.data.database.model.CategoryEntity
import kotlinx.coroutines.flow.Flow

/**
 * [CategoryEntity] Dao interface.
 *
 * A Data Access Object for the [CategoryEntity].
 * This interface is responsible for handling [CategoryEntity] access to Room database.
 */
@Dao
internal interface CategoryDao : CategoryData<CategoryEntity> {

    /**
     * Inserts a [CategoryEntity] into the database.
     * @param model the new [CategoryEntity].
     * @return the id of the new [CategoryEntity].
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(model: CategoryEntity): Long

    /**
     * Updates the [CategoryEntity] int the database.
     * @param model the [CategoryEntity] to be updated.
     */
    @Update
    override suspend fun update(model: CategoryEntity)

    /**
     * Deletes the [CategoryEntity] int the database.
     * @param model the [CategoryEntity] to be deleted.
     */
    @Delete
    override suspend fun delete(model: CategoryEntity)

    /**
     * Deletes the [CategoryEntity] specified by this [id].
     * @param id the [id] for this [CategoryEntity].
     */
    @Query("DELETE FROM category_table WHERE id = :id")
    override suspend fun deleteById(id: Long)

    /**
     * Gets all [CategoryEntity].
     * @return a [Flow] containing a [List] of all [CategoryEntity].
     */
    @Query("SELECT * FROM category_table")
    override fun getAll(): Flow<List<CategoryEntity>>

    /**
     * Gets the [CategoryEntity] specified by this [id].
     * @param id the [id] for this [CategoryEntity].
     * @return a [Flow] of [CategoryEntity].
     */
    @Query("SELECT * FROM category_table WHERE id = :id")
    override fun getById(id: Long): Flow<CategoryEntity>

    /**
     * Gets the last [CategoryEntity] inserted into the database.
     * @return a [Flow] of [CategoryEntity].
     */
    @Query("SELECT * FROM category_table WHERE id = (SELECT max(id) FROM category_table)")
    override fun getLast(): Flow<CategoryEntity>

    @Query("SELECT * FROM category_table WHERE name LIKE '%'||:value||'%'")
    override fun search(value: String): Flow<List<CategoryEntity>>
}