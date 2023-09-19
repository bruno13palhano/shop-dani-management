package com.bruno13palhano.shopdanimanagement

import com.bruno13palhano.core.data.CategoryData
import com.bruno13palhano.core.data.ProductData
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.model.Product
import com.bruno13palhano.shopdanimanagement.ui.screens.products.viewmodel.ProductViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.junit.Assert.assertEquals
import org.junit.Test

class ProductViewModelTest {
    object CategoryRepositoryFake: CategoryData<Category> {
        override fun search(value: String): Flow<List<Category>> =
            flow { emit(emptyList()) }

        override suspend fun deleteById(id: Long) {
            TODO("Not yet implemented")
        }

        override fun getAll(): Flow<List<Category>> =
            flow { emit(emptyList()) }

        override fun getById(id: Long): Flow<Category> {
            TODO("Not yet implemented")
        }

        override fun getLast(): Flow<Category> {
            TODO("Not yet implemented")
        }

        override suspend fun delete(model: Category) {
            TODO("Not yet implemented")
        }

        override suspend fun update(model: Category) {
            TODO("Not yet implemented")
        }

        override suspend fun insert(model: Category): Long {
            TODO("Not yet implemented")
        }

    }

    object ProductRepositoryFake : ProductData<Product> {
        override suspend fun insert(model: Product): Long {
            TODO("Not yet implemented")
        }

        override suspend fun update(model: Product) {
            TODO("Not yet implemented")
        }

        override suspend fun delete(model: Product) {
            TODO("Not yet implemented")
        }

        override fun search(value: String): Flow<List<Product>> =
            flow { emit(emptyList()) }

        override fun getByCategory(category: String): Flow<List<Product>> {
            TODO("Not yet implemented")
        }

        override suspend fun deleteById(id: Long) {
            TODO("Not yet implemented")
        }

        override fun getAll(): Flow<List<Product>> =
            flow { emit(emptyList()) }

        override fun getById(id: Long): Flow<Product> {
            TODO("Not yet implemented")
        }

        override fun getLast(): Flow<Product> {
            TODO("Not yet implemented")
        }

    }

    private val viewModel = ProductViewModel(ProductRepositoryFake, CategoryRepositoryFake)

    @Test
    fun updateCategories_shouldAddCategoriesSelected() {
        assertEquals(viewModel.category, "")
    }
}