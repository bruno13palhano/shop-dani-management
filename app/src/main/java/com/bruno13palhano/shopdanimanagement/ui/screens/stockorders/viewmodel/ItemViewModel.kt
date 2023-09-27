package com.bruno13palhano.shopdanimanagement.ui.screens.stockorders.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.CategoryData
import com.bruno13palhano.core.data.ProductData
import com.bruno13palhano.core.data.StockOrderData
import com.bruno13palhano.core.data.di.CategoryRep
import com.bruno13palhano.core.data.di.ProductRep
import com.bruno13palhano.core.data.di.StockOrderRep
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.model.Company
import com.bruno13palhano.core.model.Product
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
class ItemViewModel @Inject constructor(
    @ProductRep private val productRepository: ProductData<Product>,
    @CategoryRep private val categoryRepository: CategoryData<Category>,
    @StockOrderRep private val stockRepository: StockOrderData<StockOrder>
) : ViewModel() {
    private val companiesCheck = listOf(
        CompanyCheck(Company.AVON, true),
        CompanyCheck(Company.NATURA, false)
    )
    private var productId by mutableLongStateOf(0L)
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
    private var categories by mutableStateOf(listOf<Category>())
    private var allCategories by mutableStateOf((listOf<CategoryCheck>()))
    private var allCompanies by mutableStateOf(companiesCheck)
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

    private fun updateCategories(categories: List<CategoryCheck>) {
        val catList = mutableListOf<Category>()
        categories
            .filter { it.isChecked }
            .map { catList.add(Category(it.id, it.category)) }
        this.categories = catList
        category = this.categories.joinToString(", ") { category ->
            category.name
        }
    }

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
        viewModelScope.launch {
            stockRepository.insertItems(
                stockOrder = createStockOrder(productId, isOrderedByCustomer),
                isPaid = isPaid
            )
        }
    }

    fun getStockOrder(stockOrderItemId: Long) {
        viewModelScope.launch {
            stockRepository.getById(stockOrderItemId).collect {
                productId = it.productId
                name = it.name
                photo = it.photo
                quantity = it.quantity.toString()
                company = it.company
                updateDate(it.date)
                updateValidity(it.validity)
                setCategoriesChecked(it.categories)
                setCompanyChecked(it.company)
                purchasePrice = it.purchasePrice.toString()
                salePrice = it.salePrice.toString()
                isPaid = it.isPaid
            }
        }
    }

    fun updateStockOrderItem(stockOrderItemId: Long, isOrderedByCustomer: Boolean) {
        viewModelScope.launch {
            stockRepository.update(updateStockOrder(stockOrderItemId, isOrderedByCustomer))
        }
    }

    fun deleteStockOrderItem(stockOrderId: Long) {
        viewModelScope.launch {
            stockRepository.deleteById(id = stockOrderId)
        }
    }

    private fun createStockOrder(productId: Long, isOrderedByCustomer: Boolean) = StockOrder(
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
        isOrderedByCustomer = isOrderedByCustomer,
        isPaid = isPaid
    )

    private fun updateStockOrder(stockOrderId: Long, isOrderedByCustomer: Boolean) = StockOrder(
        id = stockOrderId,
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
        isOrderedByCustomer = isOrderedByCustomer,
        isPaid = isPaid
    )
}