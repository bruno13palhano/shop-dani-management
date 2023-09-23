package com.bruno13palhano.shopdanimanagement.ui.screens.products.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.CategoryData
import com.bruno13palhano.core.data.ProductData
import com.bruno13palhano.core.data.di.CategoryRep
import com.bruno13palhano.core.data.di.ProductRep
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.model.Company
import com.bruno13palhano.core.model.Product
import com.bruno13palhano.shopdanimanagement.ui.components.CategoryCheck
import com.bruno13palhano.shopdanimanagement.ui.components.CompanyCheck
import com.bruno13palhano.shopdanimanagement.ui.screens.currentDate
import com.bruno13palhano.shopdanimanagement.ui.screens.dateFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    @ProductRep private val productRepository: ProductData<Product>,
    @CategoryRep private val categoryRepository: CategoryData<Category>
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
    var photo by mutableStateOf("")
        private set
    var dateInMillis by mutableLongStateOf(currentDate)
    var date: String by mutableStateOf(dateFormat.format(dateInMillis))
        private set
    var category by mutableStateOf("")
        private set
    var company by mutableStateOf(companiesCheck[0].name.company)
        private set
    private var categories by mutableStateOf(listOf(""))
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

    init {
        viewModelScope.launch {
            categoryRepository.getAll()
                .map {
                    it.map { category -> CategoryCheck(category.id, category.name, false) }
                }.collect {
                    allCategories = it
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

    fun updatePhoto(photo: String) {
        this.photo = photo
    }

    fun updateDate(date: Long) {
        dateInMillis = date
        this.date = dateFormat.format(dateInMillis)
    }

    fun updateCategories(categories: List<CategoryCheck>) {
        val catList = mutableListOf<Category>()
        categories
            .filter { it.isChecked }
            .map { catList.add(Category(it.id, it.category)) }
        this.categories = catList.map { it.name }
        category = this.categories.toString().replace("[", "").replace("]", "")
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
        categories = listOf(this.category)
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
        val product = initProduct(id = 0L)

        viewModelScope.launch {
            productRepository.insert(product)
        }
    }

    fun getProduct(id: Long) {
        viewModelScope.launch {
            productRepository.getById(id).collect {
                name = it.name
                code = it.code
                description = it.description
                photo = it.photo
                updateDate(it.date)
                categories = it.categories
                company = it.company
//                setCategoriesChecked(categories)
                category = categories.joinToString(", ")
                setCompanyChecked(it.company)
            }
        }
    }

    //Sets all current categories
    private fun setCategoriesChecked(allCategories: List<String>) {
        this.allCategories.forEach { categoryCheck ->
            allCategories.forEach {
                if (categoryCheck.category == it.replace("[","" ).replace("]", "")) {
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
        val product = initProduct(id = id)

        viewModelScope.launch {
            productRepository.update(product)
        }
    }

    fun deleteProduct(id: Long) {
        viewModelScope.launch {
            productRepository.deleteById(id)
        }
    }

    private fun initProduct(id: Long) = Product(
        id = id,
        name = name,
        code = code,
        description = description,
        photo = photo,
        date = dateInMillis,
        categories = categories,
        company = company
    )
}