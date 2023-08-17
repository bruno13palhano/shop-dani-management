package com.bruno13palhano.shopdanimanagement.ui.screens.sales

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.CategoryData
import com.bruno13palhano.core.data.ProductData
import com.bruno13palhano.core.data.di.DefaultCategoryRepository
import com.bruno13palhano.core.data.di.DefaultProductRepository
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
class SaleViewModel @Inject constructor(
    @DefaultProductRepository private val productRepository: ProductData<Product>,
    @DefaultCategoryRepository private val categoryRepository: CategoryData<Category>
) : ViewModel() {
    private val companiesCheck = listOf(
        CompanyCheck(Company.AVON, true),
        CompanyCheck(Company.NATURA, false)
    )
    var name by mutableStateOf("")
        private set
    var photo by mutableStateOf("")
        private set
    var quantity by mutableStateOf("")
        private set
    var dateOfSaleInMillis by mutableLongStateOf(currentDate)
        private set
    var dateOfSale: String by mutableStateOf(dateFormat.format(dateOfSaleInMillis))
        private set
    var dateOfPaymentInMillis by mutableLongStateOf(currentDate)
        private set
    var dateOfPayment: String by mutableStateOf(dateFormat.format(dateOfPaymentInMillis))
        private set
    var purchasePrice by mutableStateOf("")
        private set
    var salePrice by mutableStateOf("")
        private set
    var category by mutableStateOf("")
        private set
    var company by mutableStateOf(companiesCheck[0].name.company)
        private set
    private var categories by mutableStateOf(listOf(""))
    var allCategories by mutableStateOf(listOf<CategoryCheck>())
        private set
    var allCompanies by mutableStateOf(listOf<CompanyCheck>())
        private set
    var isPaidByCustomer by mutableStateOf(false)
        private set

    val isSaleNotEmpty = snapshotFlow {
        name.isNotEmpty() && quantity.isNotEmpty() && purchasePrice.isNotEmpty() && salePrice.isNotEmpty()
    }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = false
        )

    init {
        viewModelScope.launch {
            categoryRepository.getAll().map {
                it.map { category -> CategoryCheck(category.id, category.name, false) }
            }.collect {
                allCategories = it
            }
        }
    }

    fun updateName(name: String) {
        this.name = name
    }

    fun updateQuantity(quantity: String) {
        this.quantity = quantity
    }

    fun updateDateOfSale(dateOfSale: Long) {
        dateOfSaleInMillis = dateOfSale
        this.dateOfSale = dateFormat.format(dateOfSaleInMillis)
    }

    fun updateDateOfPayment(dateOfPayment: Long) {
        dateOfPaymentInMillis = dateOfPayment
        this.dateOfPayment = dateFormat.format(dateOfPaymentInMillis)
    }

    fun updatePurchasePrice(purchasePrice: String) {
        this.purchasePrice = purchasePrice
    }

    fun updateSalePrice(salePrice: String) {
        this.salePrice = salePrice
    }

    fun updateIsPaidByCustomer(isPaidByCustomer: Boolean) {
        this.isPaidByCustomer = isPaidByCustomer
    }

    fun updateCategories(categories: List<CategoryCheck>) {
        val catList = mutableListOf<Category>()
        categories
            .filter { it.isChecked }
            .map { catList.add(Category(it.id, it.category)) }
        this.categories = catList.map { it.name }
        category = this.categories.toString().replace("[", "").replace("]", "")
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

    fun getProduct(id: Long) {
        viewModelScope.launch {
            productRepository.getById(id).collect {
                name = it.name
                photo = it.photo
                categories = it.categories
                company = it.company
                setCategoriesChecked(categories)
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
}