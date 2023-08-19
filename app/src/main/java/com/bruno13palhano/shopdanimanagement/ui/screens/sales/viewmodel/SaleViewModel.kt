package com.bruno13palhano.shopdanimanagement.ui.screens.sales.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.CategoryData
import com.bruno13palhano.core.data.ProductData
import com.bruno13palhano.core.data.SaleData
import com.bruno13palhano.core.data.StockOrderData
import com.bruno13palhano.core.data.di.DefaultCategoryRepository
import com.bruno13palhano.core.data.di.DefaultProductRepository
import com.bruno13palhano.core.data.di.DefaultSaleRepository
import com.bruno13palhano.core.data.di.DefaultStockOrderRepository
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.model.Company
import com.bruno13palhano.core.model.Product
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.core.model.StockOrder
import com.bruno13palhano.shopdanimanagement.ui.components.CategoryCheck
import com.bruno13palhano.shopdanimanagement.ui.components.CompanyCheck
import com.bruno13palhano.shopdanimanagement.ui.screens.currentDate
import com.bruno13palhano.shopdanimanagement.ui.screens.dateFormat
import com.bruno13palhano.shopdanimanagement.ui.screens.stringToFloat
import com.bruno13palhano.shopdanimanagement.ui.screens.stringToInt
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SaleViewModel @Inject constructor(
    @DefaultProductRepository private val productRepository: ProductData<Product>,
    @DefaultCategoryRepository private val categoryRepository: CategoryData<Category>,
    @DefaultSaleRepository private val saleRepository: SaleData<Sale>,
    @DefaultStockOrderRepository private val stockRepository: StockOrderData<StockOrder>
) : ViewModel() {
    private val companiesCheck = listOf(
        CompanyCheck(Company.AVON, true),
        CompanyCheck(Company.NATURA, false)
    )
    private var stockItemId by mutableLongStateOf(0L)
    private var productId by mutableLongStateOf(0L)
    var name by mutableStateOf("")
        private set
    var photo by mutableStateOf("")
        private set
    var quantity by mutableStateOf("")
        private set
    var stockQuantity by mutableIntStateOf(0)
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
                setCategoriesChecked(it.categories)
                setCompanyChecked(it.company)
            }
        }
    }

    fun getStockItem(stockId: Long) {
        viewModelScope.launch {
            stockRepository.getById(stockId).collect {
                stockItemId = stockId
                name = it.name
                photo = it.photo
                purchasePrice = it.purchasePrice.toString()
                salePrice = it.salePrice.toString()
                categories = it.categories
                company = it.company
                stockQuantity = it.quantity
                setCategoriesChecked(it.categories)
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

    fun insertSale(
        productId: Long,
        isOrderedByCustomer: Boolean,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        val sale = Sale(
            id = 0L,
            productId = productId,
            name = name,
            photo = photo,
            quantity = stringToInt(quantity),
            purchasePrice = stringToFloat(purchasePrice),
            salePrice = stringToFloat(salePrice),
            categories = categories,
            company = company,
            dateOfSale = dateOfSaleInMillis,
            dateOfPayment = dateOfPaymentInMillis,
            isOrderedByCustomer = isOrderedByCustomer,
            isPaidByCustomer = isPaidByCustomer
        )
        if (isOrderedByCustomer) {
            viewModelScope.launch {
                saleRepository.insert(sale)
                onSuccess()
            }
        } else {
            val finalQuantity = (stockQuantity - stringToInt(quantity))
            if (finalQuantity >= 0) {
                viewModelScope.launch {
                    stockRepository.updateStockOrderQuantity(stockItemId, finalQuantity)
                }
                viewModelScope.launch {
                    saleRepository.insert(sale)
                }
                onSuccess()
            } else { onError() }
        }
    }

    fun getSale(saleId: Long) {
        viewModelScope.launch {
            saleRepository.getById(saleId).collect {
                productId = it.productId
                name = it.name
                photo = it.photo
                quantity = it.quantity.toString()
                purchasePrice = it.purchasePrice.toString()
                salePrice = it.salePrice.toString()
                categories = it.categories
                company = it.company
                updateDateOfSale(it.dateOfSale)
                updateDateOfPayment(it.dateOfPayment)
                setCategoriesChecked(it.categories)
                setCompanyChecked(it.company)
            }
        }
    }

    fun updateSale(saleId: Long, isOrderedByCustomer: Boolean) {
        val sale = Sale(
            id = saleId,
            productId = productId,
            name = name,
            photo = photo,
            quantity = stringToInt(quantity),
            purchasePrice = stringToFloat(purchasePrice),
            salePrice = stringToFloat(salePrice),
            categories = categories,
            company = company,
            dateOfSale = dateOfSaleInMillis,
            dateOfPayment = dateOfPaymentInMillis,
            isOrderedByCustomer = isOrderedByCustomer,
            isPaidByCustomer = isPaidByCustomer

        )
        viewModelScope.launch {
            saleRepository.update(sale)
        }
    }

    fun deleteSale(saleId: Long) {
        viewModelScope.launch {
            saleRepository.deleteById(saleId)
        }
    }
}