package com.bruno13palhano.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bruno13palhano.core.data.database.dao.CategoryDao
import com.bruno13palhano.core.data.database.dao.ProductDao
import com.bruno13palhano.core.data.database.dao.SaleDao
import com.bruno13palhano.core.data.database.dao.SearchCacheDao
import com.bruno13palhano.core.data.database.dao.ShoppingDao
import com.bruno13palhano.core.data.database.dao.StockOrderDao
import com.bruno13palhano.core.data.database.model.CategoryEntity
import com.bruno13palhano.core.data.database.model.ProductEntity
import com.bruno13palhano.core.data.database.model.SaleEntity
import com.bruno13palhano.core.data.database.model.SearchCacheEntity
import com.bruno13palhano.core.data.database.model.ShoppingEntity
import com.bruno13palhano.core.data.database.model.StockOrderEntity

@Database(
    entities = [
        ProductEntity::class,
        CategoryEntity::class,
        SearchCacheEntity::class,
        SaleEntity::class,
        ShoppingEntity::class,
        StockOrderEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(CategoryConverter::class)
internal abstract class AppDatabase : RoomDatabase() {
    abstract val productDao: ProductDao
    abstract val categoryDao: CategoryDao
    abstract val searchCacheDao: SearchCacheDao
    abstract val saleDao: SaleDao
    abstract val shoppingDao: ShoppingDao
    abstract val stockOrderDao: StockOrderDao
}