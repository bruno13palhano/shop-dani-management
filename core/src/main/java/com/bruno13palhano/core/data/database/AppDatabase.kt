package com.bruno13palhano.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bruno13palhano.core.data.database.dao.ProductDao
import com.bruno13palhano.core.data.database.model.CategoryConverter
import com.bruno13palhano.core.data.database.model.ProductEntity

@Database(
    entities = [
        ProductEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(CategoryConverter::class)
internal abstract class AppDatabase : RoomDatabase() {
    abstract val productDao: ProductDao
}