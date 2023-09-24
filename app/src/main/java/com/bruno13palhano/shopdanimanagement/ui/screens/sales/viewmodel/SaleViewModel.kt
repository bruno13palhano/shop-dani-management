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
import com.bruno13palhano.core.data.CustomerData
import com.bruno13palhano.core.data.ProductData
import com.bruno13palhano.core.data.SaleData
import com.bruno13palhano.core.data.StockOrderData
import com.bruno13palhano.core.data.di.CategoryRep
import com.bruno13palhano.core.data.di.CustomerRep
import com.bruno13palhano.core.data.di.ProductRep
import com.bruno13palhano.core.data.di.SaleRep
import com.bruno13palhano.core.data.di.StockOrderRep
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.model.Company
import com.bruno13palhano.core.model.Customer
import com.bruno13palhano.core.model.Delivery
import com.bruno13palhano.core.model.Product
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.core.model.StockOrder
import com.bruno13palhano.shopdanimanagement.ui.components.CategoryCheck
import com.bruno13palhano.shopdanimanagement.ui.components.CompanyCheck
import com.bruno13palhano.shopdanimanagement.ui.components.CustomerCheck
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
    @ProductRep private val productRepository: ProductData<Product>,
    @CategoryRep private val categoryRepository: CategoryData<Category>,
    @SaleRep private val saleRepository: SaleData<Sale>,
    @StockOrderRep private val stockOrderRepository: StockOrderData<StockOrder>,
    @CustomerRep private val customerRepository: CustomerData<Customer>,
) : ViewModel() {
    private val companiesCheck = listOf(
        CompanyCheck(Company.AVON, true),
        CompanyCheck(Company.NATURA, false)
    )
    private var stockItemId by mutableLongStateOf(0L)
    private var productId by mutableLongStateOf(0L)
    private var customerId by mutableLongStateOf(0L)
    private var isOrderedByCustomer by mutableStateOf(false)
    var productName by mutableStateOf("")
        private set
    var customerName by mutableStateOf("")
        private set
    private var address by mutableStateOf("")
    private var phoneNumber by mutableStateOf("")
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
    private var categories by mutableStateOf(listOf<Category>())
    var allCategories by mutableStateOf(listOf<CategoryCheck>())
        private set
    var allCompanies by mutableStateOf(listOf<CompanyCheck>())
        private set
    var allCustomers by mutableStateOf(listOf<CustomerCheck>())
        private set
    var isPaidByCustomer by mutableStateOf(false)
        private set

    val isSaleNotEmpty = snapshotFlow {
        productName.isNotEmpty() && quantity.isNotEmpty() && purchasePrice.isNotEmpty() && salePrice.isNotEmpty()
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
            }.collect { allCategories = it }
        }
        viewModelScope.launch {
            customerRepository.getAll().map {
                it.map { customer ->
                    CustomerCheck(
                        id = customer.id,
                        name = customer.name,
                        address = customer.address,
                        phoneNumber = customer.phoneNumber,
                        isChecked = false
                    )
                }
            }.collect { allCustomers = it }
        }
    }

    fun updateProductName(productName: String) {
        this.productName = productName
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
        this.categories = catList
        category = this.categories.joinToString(", ") { it.name }
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

    fun updateCustomerName(customerName: String) {
        this.customerName = customerName
        allCustomers
            .map {
                it.isChecked = false
                it
            }
            .filter { it.name == customerName }
            .map {
                address = it.address
                phoneNumber = it.phoneNumber
                customerId = it.id
                it.isChecked = true
                it
            }
    }

    fun getProduct(id: Long) {
        viewModelScope.launch {
            productRepository.getById(id).collect {
                productName = it.name
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
            stockOrderRepository.getById(stockId).collect {
                stockItemId = stockId
                productName = it.name
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

    //Sets the current customer
    private fun setCustomerChecked(customerId: Long) {
        allCustomers.forEach {
            if (customerId == it.id) {
                it.isChecked = true
            }
        }
    }

    fun insertSale(
        productId: Long,
        isOrderedByCustomer: Boolean,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        val sale = initSale(productId = productId, isOrderedByCustomer = isOrderedByCustomer)
        val order = initStockOrder(productId = productId, isOrderedByCustomer = isOrderedByCustomer)
        val delivery = initDelivery()
        viewModelScope.launch {
            saleRepository.insertItems(
                sale = sale,
                stockOrder = order,
                delivery = delivery,
                onSuccess = onSuccess,
                onError = onError
            )
        }
    }

    fun getSale(saleId: Long) {
        viewModelScope.launch {
            saleRepository.getById(saleId).collect {
                productId = it.productId
                customerId = it.customerId
                productName = it.name
                customerName = it.customerName
                photo = it.photo
                quantity = it.quantity.toString()
                purchasePrice = it.purchasePrice.toString()
                salePrice = it.salePrice.toString()
                categories = it.categories
                company = it.company
                isPaidByCustomer = it.isPaidByCustomer
                isOrderedByCustomer = it.isOrderedByCustomer
                updateDateOfSale(it.dateOfSale)
                updateDateOfPayment(it.dateOfPayment)
                setCategoriesChecked(it.categories)
                setCompanyChecked(it.company)
                setCustomerChecked(it.customerId)
            }
        }
    }

    fun updateSale(saleId: Long) {
        val sale = Sale(
            id = saleId,
            productId = productId,
            customerId = customerId,
            name = productName,
            customerName = customerName,
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

    private fun initSale(productId: Long, isOrderedByCustomer: Boolean) = Sale(
        id = 0L,
        productId = productId,
        customerId = customerId,
        name = productName,
        customerName = customerName,
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

    private fun initStockOrder(productId: Long, isOrderedByCustomer: Boolean) = StockOrder(
        id = stockItemId,
        productId = productId,
        name = productName,
        photo = photo,
        date = currentDate,
        validity = currentDate,
        quantity = stockQuantity,
        categories = categories,
        company = company,
        purchasePrice = stringToFloat(purchasePrice),
        salePrice = stringToFloat(salePrice),
        isOrderedByCustomer = isOrderedByCustomer
    )

    private fun initDelivery() = Delivery(
        id = 0L,
        saleId = 0L,
        customerName = customerName,
        address = address,
        phoneNumber = phoneNumber,
        productName = productName,
        price = stringToFloat(salePrice),
        shippingDate = dateOfSaleInMillis,
        deliveryDate = dateOfSaleInMillis,
        delivered = false
    )
}