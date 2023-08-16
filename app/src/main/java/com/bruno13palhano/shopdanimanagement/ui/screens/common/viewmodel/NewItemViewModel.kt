package com.bruno13palhano.shopdanimanagement.ui.screens.common.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.CategoryData
import com.bruno13palhano.core.data.ProductData
import com.bruno13palhano.core.data.ShoppingData
import com.bruno13palhano.core.data.StockOrderData
import com.bruno13palhano.core.data.di.DefaultCategoryRepository
import com.bruno13palhano.core.data.di.DefaultProductRepository
import com.bruno13palhano.core.data.di.DefaultShoppingRepository
import com.bruno13palhano.core.data.di.DefaultStockOrderRepository
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.model.Company
import com.bruno13palhano.core.model.Product
import com.bruno13palhano.core.model.Shopping
import com.bruno13palhano.core.model.StockOrder
import com.bruno13palhano.shopdanimanagement.ui.components.CategoryCheck
import com.bruno13palhano.shopdanimanagement.ui.components.CompanyCheck
import com.bruno13palhano.shopdanimanagement.ui.screens.currentDate
import com.bruno13palhano.shopdanimanagement.ui.screens.dateFormat
import com.bruno13palhano.shopdanimanagement.ui.screens.stringToFloat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewItemViewModel @Inject constructor(
    @DefaultProductRepository private val productRepository: ProductData<Product>,
    @DefaultCategoryRepository private val categoryRepository: CategoryData<Category>,
    @DefaultShoppingRepository private val shoppingRepository: ShoppingData<Shopping>,
    @DefaultStockOrderRepository private val stockRepository: StockOrderData<StockOrder>
) : ViewModel() {
    private val companiesCheck = listOf(
        CompanyCheck(Company.AVON, true),
        CompanyCheck(Company.NATURA, false)
    )
    var name by mutableStateOf("")
        private set
    var photo by mutableStateOf("")
        private set
    var dateInMillis by mutableLongStateOf(0L)
        private set
    var quantity by mutableStateOf("")
        private set
    var date by mutableStateOf("")
        private set
    var validityInMillis by mutableLongStateOf(currentDate)
        private set
    var validity: String by mutableStateOf(dateFormat.format(validityInMillis))
        private set
    var purchasePrice by mutableStateOf("")
        private set
    var salePrice by mutableStateOf("")
        private set
    var category by mutableStateOf("")
        private set
    var company by mutableStateOf("")
        private set
    private var categories by mutableStateOf(listOf(""))
    var allCategories by mutableStateOf((listOf<CategoryCheck>()))
        private set
    var allCompanies by mutableStateOf(companiesCheck)
        private set
    var isPaid by mutableStateOf(false)
        private set

    val isItemNotEmpty = snapshotFlow {
        name.isNotEmpty() && quantity.isNotEmpty() && purchasePrice.isNotEmpty() && salePrice.isNotEmpty()
    }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = false
        )

    fun updateName(name: String) {
        this.name = name
    }

    fun updateQuantity(quantity: String) {
        this.quantity = quantity
    }

    fun updateDate(date: Long) {
        dateInMillis = date
        this.date = dateFormat.format(dateInMillis)
    }

    fun updateValidity(validity: Long) {
        validityInMillis = validity
        this.validity = dateFormat.format(validityInMillis)
    }

    fun updatePurchasePrice(purchasePrice: String) {
        this.purchasePrice = purchasePrice
    }

    fun updateSalePrice(salePrice: String) {
        this.salePrice = salePrice
    }

    fun updateIsPaid(isPaid: Boolean) {
        this.isPaid = isPaid
    }

    fun updateCategories(categories: List<CategoryCheck>) {
        val catList = mutableListOf<Category>()
        categories
            .filter { it.isChecked }
            .map { catList.add(Category(it.id, it.category)) }
        this.categories = catList.map { it.name }
        category = this.categories.toString().replace("[", "")
            .replace("]", "")
    }

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

    private fun setCompanyChecked(company: String) {
        companiesCheck.forEach { companyCheck ->
            if (companyCheck.name.company == company) {
                companyCheck.isChecked = true
            }
        }
        allCompanies = companiesCheck
    }

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

    fun getProduct(id: Long) {
        viewModelScope.launch {
            productRepository.getById(id).collect {
                name = it.name
                photo = it.photo
                updateDate(it.date)
                categories = it.categories
                company = it.company
                setCategoriesChecked(categories)
                setCompanyChecked(it.company)
            }
        }
    }

    fun insertItems(productId: Long, isOrderedByCustomer: Boolean) {
        val shoppingItem = Shopping(
            id = 0L,
            productId = productId,
            name = name,
            purchasePrice = stringToFloat(purchasePrice),
            quantity = quantity.toInt(),
            date = dateInMillis,
            isPaid = false
        )
        val stockItem = StockOrder(
            id = 0L,
            productId = productId,
            name = name,
            photo = photo,
            quantity = quantity.toInt(),
            date = dateInMillis,
            validity = validityInMillis,
            categories = categories,
            company = company,
            purchasePrice = stringToFloat(purchasePrice),
            salePrice = stringToFloat(salePrice),
            isOrderedByCustomer = isOrderedByCustomer

        )
        if (!isOrderedByCustomer) {
            viewModelScope.launch {
                shoppingRepository.insert(shoppingItem)
            }
        }
        viewModelScope.launch {
            stockRepository.insert(stockItem)
        }
    }
}