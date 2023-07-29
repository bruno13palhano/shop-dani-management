package com.bruno13palhano.core.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bruno13palhano.core.model.Category

/**
 * [Category] as an Entity.
 *
 * An entity to persist categories in database.
 * @property id the category id.
 * @property name the category name.
 */
@Entity(tableName = "category_table")
internal data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @ColumnInfo(name = "name")
    val name: String
)

/**
 * Transforms [CategoryEntity] into [Category].
 * @return [Category].
 */
internal fun CategoryEntity.asExternalModel() = Category(
    id = id,
    name = name
)

/**
 * Transforms [Category] into [CategoryEntity].
 * @return [CategoryEntity].
 */
internal fun Category.asInternalModel() = CategoryEntity(
    id = id,
    name = name
)

