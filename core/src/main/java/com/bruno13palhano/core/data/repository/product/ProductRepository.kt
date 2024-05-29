package com.bruno13palhano.core.data.repository.product

import com.bruno13palhano.core.data.RepositoryOperations
import com.bruno13palhano.core.model.Product
import com.bruno13palhano.core.sync.Syncable
import kotlinx.coroutines.flow.Flow

interface ProductRepository : RepositoryOperations<Product>, Syncable {
    /**
     * Searches for products of type [Product] with this value.
     * @param value the searching value.
     * @return a [Flow] containing a [List] of all the products referring to the search.
     */
    fun search(value: String): Flow<List<Product>>

    fun searchPerCategory(
        value: String,
        categoryId: Long,
    ): Flow<List<Product>>

    /**
     * Gets products by category.
     * @param category the category name.
     * @return a [Flow] containing a [List] of all the products referring to the category.
     */
    fun getByCategory(category: String): Flow<List<Product>>

    fun getByCode(code: String): Flow<List<Product>>
}
