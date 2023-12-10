package com.bruno13palhano.shopdanimanagement.ui.screens.products.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.repository.category.CategoryRepository
import com.bruno13palhano.core.data.repository.product.ProductRepository
import com.bruno13palhano.core.data.di.CategoryRep
import com.bruno13palhano.core.data.di.ProductRep
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.model.Company
import com.bruno13palhano.core.model.Product
import com.bruno13palhano.shopdanimanagement.ui.components.CategoryCheck
import com.bruno13palhano.shopdanimanagement.ui.components.CompanyCheck
import com.bruno13palhano.shopdanimanagement.ui.screens.getCurrentTimestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    @ProductRep private val productRepository: ProductRepository,
    @CategoryRep private val categoryRepository: CategoryRepository
) : ViewModel() {
    private val companiesCheck = listOf(
        CompanyCheck(Company.AVON, true),
        CompanyCheck(Company.NATURA, false)
    )
    var name by mutableStateOf("")
        private set
    var code by mutableStateOf("")
        private set
    var description by mutableStateOf("")
        private set
    var photo by mutableStateOf(byteArrayOf())
        private set
    var date by mutableLongStateOf(0L)
        private set
    var category by mutableStateOf("")
        private set
    var company by mutableStateOf(companiesCheck[0].name.company)
        private set
    private var categories by mutableStateOf(listOf<Category>())
    var allCategories by mutableStateOf((listOf<CategoryCheck>()))
        private set
    var allCompanies by mutableStateOf(companiesCheck)
        private set

    val isProductValid = snapshotFlow { name != "" && code != "" && category != "" }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = false
        )

    fun getAllCategories(onCategoriesDone: () -> Unit) {
        viewModelScope.launch {
            categoryRepository.getAll()
                .map {
                    it.map { category -> CategoryCheck(category.id, category.category, false) }
                }.collect {
                    allCategories = it
                    onCategoriesDone()
                }
        }
    }

    fun updateName(name: String) {
        this.name = name
    }

    fun updateCode(code: String) {
        this.code = code
    }

    fun updateDescription(description: String) {
        this.description = description
    }

    fun updatePhoto(photo: ByteArray) {
        this.photo = photo
    }

    fun updateDate(date: Long) {
        this.date = date
    }

    fun updateCategories(categories: List<CategoryCheck>) {
        this.categories = categories
            .filter { it.isChecked }
            .map { Category(it.id, it.category, getCurrentTimestamp()) }
        category = this.categories.joinToString(", ") { it.category }
    }

    fun setCategoryChecked(category: Long) {
        allCategories.forEach { categoryCheck ->
            if (categoryCheck.id == category) {
                categoryCheck.isChecked = true
            }
        }
        this.category = allCategories
            .filter { it.isChecked }
            .map { it.category }.toString()
            .replace("[", "").replace("]", "")
        categories = allCategories
            .filter { it.isChecked }
            .map { categoryChecked ->
                Category(
                    id = categoryChecked.id,
                    category = categoryChecked.category,
                    timestamp = getCurrentTimestamp()
                )
            }
    }

    fun updateCompany(company: String) {
        this.company = company
        allCompanies
            .map {
                it.isChecked = false
                it
            }
            .filter { it.name.company == company }
            .map {
                it.isChecked = true
                it
            }
    }

    fun insertProduct() {
        viewModelScope.launch {
            productRepository.insert(createProduct(id = 0L))
        }
    }

    fun getProduct(id: Long) {
        viewModelScope.launch {
            productRepository.getById(id).collect {
                name = it.name
                code = it.code
                description = it.description
                photo = it.photo
                date = it.date
                categories = it.categories
                company = it.company
                setCategoriesChecked(it.categories)
                category = it.categories.joinToString(", ") { category -> category.category }
                setCompanyChecked(it.company)
            }
        }
    }

    //Sets all current categories
    private fun setCategoriesChecked(allCategories: List<Category>) {
        this.allCategories.forEach { categoryCheck ->
            allCategories.forEach {
                if (categoryCheck.id == it.id) {
                    categoryCheck.isChecked = true
                }
            }
        }
        updateCategories(this.allCategories)
    }

    //Sets the current company
    private fun setCompanyChecked(company: String) {
        companiesCheck.forEach { companyCheck ->
            if (companyCheck.name.company == company) {
                companyCheck.isChecked = true
            }
        }
        allCompanies = companiesCheck
    }

    fun updateProduct(id: Long) {
        viewModelScope.launch {
            productRepository.update(createProduct(id = id))
        }
    }

    fun deleteProduct(id: Long) {
        viewModelScope.launch {
            productRepository.deleteById(
                id = id,
                timestamp = getCurrentTimestamp()
            )
        }
    }

    private fun createProduct(id: Long) = Product(
        id = id,
        name = name,
        code = code,
        description = description,
        photo = photo,
        date = date,
        categories = allCategories
            .filter { it.isChecked }
            .map { Category(id = it.id, category = it.category, timestamp = getCurrentTimestamp()) },
        company = company,
        timestamp = getCurrentTimestamp()
    )
}