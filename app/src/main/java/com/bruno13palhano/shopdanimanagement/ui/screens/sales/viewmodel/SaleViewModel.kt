package com.bruno13palhano.shopdanimanagement.ui.screens.sales.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.CustomerData
import com.bruno13palhano.core.data.ProductData
import com.bruno13palhano.core.data.SaleData
import com.bruno13palhano.core.data.StockOrderData
import com.bruno13palhano.core.data.di.CustomerRep
import com.bruno13palhano.core.data.di.ProductRep
import com.bruno13palhano.core.data.di.SaleRep
import com.bruno13palhano.core.data.di.StockOrderRep
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.model.Customer
import com.bruno13palhano.core.model.Delivery
import com.bruno13palhano.core.model.Product
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.core.model.StockOrder
import com.bruno13palhano.shopdanimanagement.ui.components.CustomerCheck
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
    @SaleRep private val saleRepository: SaleData<Sale>,
    @StockOrderRep private val stockOrderRepository: StockOrderData<StockOrder>,
    @ProductRep private val productRepository: ProductData<Product>,
    @CustomerRep private val customerRepository: CustomerData<Customer>,
) : ViewModel() {
    private var stockOrderId by mutableLongStateOf(0L)
    private var productId by mutableLongStateOf(0L)
    private var customerId by mutableLongStateOf(0L)
    private var isOrderedByCustomer by mutableStateOf(false)
    var productName by mutableStateOf("")
        private set
    var customerName by mutableStateOf("")
        private set
    private var address by mutableStateOf("")
    private var phoneNumber by mutableStateOf("")
    var photo by mutableStateOf(byteArrayOf())
        private set
    var quantity by mutableStateOf("")
        private set
    var stockQuantity by mutableIntStateOf(0)
        private set
    var dateOfSale by mutableLongStateOf(0L)
        private set
    var dateOfPayment by mutableLongStateOf(0L)
        private set
    var purchasePrice by mutableStateOf("")
        private set
    var salePrice by mutableStateOf("")
        private set
    var deliveryPrice by mutableStateOf("")
        private set
    var category by mutableStateOf("")
        private set
    var company by mutableStateOf("")
        private set
    var allCustomers by mutableStateOf(listOf<CustomerCheck>())
        private set
    var isPaidByCustomer by mutableStateOf(false)
        private set
    private var isPaid by mutableStateOf(false)

    val isSaleNotEmpty = snapshotFlow {
        productName.isNotEmpty() && quantity.isNotEmpty() && purchasePrice.isNotEmpty()
                && salePrice.isNotEmpty()
    }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = false
        )

    init {
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

    fun updateQuantity(quantity: String) {
        this.quantity = quantity
    }

    fun updateDateOfSale(dateOfSale: Long) {
        this.dateOfSale = dateOfSale
    }

    fun updateDateOfPayment(dateOfPayment: Long) {
        this.dateOfPayment = dateOfPayment
    }

    fun updatePurchasePrice(purchasePrice: String) {
        this.purchasePrice = purchasePrice
    }

    fun updateSalePrice(salePrice: String) {
        this.salePrice = salePrice
    }

    fun updateDeliveryPrice(deliveryPrice: String) {
        this.deliveryPrice = deliveryPrice
    }

    fun updateIsPaidByCustomer(isPaidByCustomer: Boolean) {
        this.isPaidByCustomer = isPaidByCustomer
    }

    fun updateCustomerName(customerName: String) {
        this.customerName = customerName
        allCustomers.map { customer -> customer.isChecked = false }
        for (customer in allCustomers) {
            if (customer.name == customerName) {
                address = customer.address
                phoneNumber = customer.phoneNumber
                customerId = customer.id
                customer.isChecked = true
                break
            }
        }
    }

    fun getProduct(id: Long) {
        viewModelScope.launch {
            productRepository.getById(id).collect {
                productId = it.id
                productName = it.name
                photo = it.photo
                category = setCategories(it.categories)
                company = it.company
            }
        }
    }

    fun getStockItem(stockId: Long) {
        viewModelScope.launch {
            stockOrderRepository.getById(stockId).collect {
                stockOrderId = stockId
                productId = it.productId
                productName = it.name
                photo = it.photo
                purchasePrice = it.purchasePrice.toString()
                salePrice = it.salePrice.toString()
                category = setCategories(it.categories)
                company = it.company
                stockQuantity = it.quantity
                isPaid = it.isPaid
            }
        }
    }

    private fun setCategories(categories: List<Category>) =
        categories.joinToString(", ") { category -> category.name }

    //Sets the current customer
    private fun setCustomerChecked(customerId: Long) {
        for (customer in allCustomers) {
            if (customer.id == customerId) {
                customer.isChecked = true
                break
            }
        }
    }

    fun insertSale(
        isOrderedByCustomer: Boolean,
        currentDate: Long,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        viewModelScope.launch {
            saleRepository.insertItems(
                sale = createSale(
                    id = 0L,
                    productId = productId,
                    isOrderedByCustomer = isOrderedByCustomer
                ),
                stockOrder = createStockOrder(
                    productId = productId,
                    currentDate = currentDate,
                    isOrderedByCustomer = isOrderedByCustomer
                ),
                delivery = createDelivery(),
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
                stockOrderId = it.stockOrderId
                productName = it.name
                customerName = it.customerName
                photo = it.photo
                quantity = it.quantity.toString()
                purchasePrice = it.purchasePrice.toString()
                salePrice = it.salePrice.toString()
                deliveryPrice = it.deliveryPrice.toString()
                category = setCategories(it.categories)
                company = it.company
                isPaidByCustomer = it.isPaidByCustomer
                isOrderedByCustomer = it.isOrderedByCustomer
                updateDateOfSale(it.dateOfSale)
                updateDateOfPayment(it.dateOfPayment)
                setCustomerChecked(it.customerId)
            }
        }
    }

    fun updateSale(saleId: Long) {
        viewModelScope.launch {
            saleRepository.update(
                createSale(id = saleId, productId = productId, isOrderedByCustomer = isOrderedByCustomer)
            )
        }
    }

    fun deleteSale(saleId: Long) {
        viewModelScope.launch {
            saleRepository.deleteById(saleId)
        }
    }

    fun cancelSale(saleId: Long) {
        viewModelScope.launch {
            saleRepository.cancelSale(saleId = saleId)
        }
    }

    private fun createSale(id: Long, productId: Long, isOrderedByCustomer: Boolean) = Sale(
        id = id,
        productId = productId,
        customerId = customerId,
        stockOrderId = stockOrderId,
        name = productName,
        customerName = customerName,
        photo = photo,
        quantity = stringToInt(quantity),
        purchasePrice = stringToFloat(purchasePrice),
        salePrice = stringToFloat(salePrice),
        deliveryPrice = stringToFloat(deliveryPrice),
        categories = emptyList(),
        company = company,
        dateOfSale = dateOfSale,
        dateOfPayment = dateOfPayment,
        isOrderedByCustomer = isOrderedByCustomer,
        isPaidByCustomer = isPaidByCustomer,
        canceled = false
    )

    private fun createStockOrder(
        productId: Long,
        currentDate: Long,
        isOrderedByCustomer: Boolean
    ) = StockOrder(
        id = stockOrderId,
        productId = productId,
        name = productName,
        photo = photo,
        date = currentDate,
        validity = currentDate,
        quantity = stockQuantity,
        categories = emptyList(),
        company = company,
        purchasePrice = stringToFloat(purchasePrice),
        salePrice = stringToFloat(salePrice),
        isOrderedByCustomer = isOrderedByCustomer,
        isPaid = isPaid
    )

    private fun createDelivery() = Delivery(
        id = 0L,
        saleId = 0L,
        customerName = customerName,
        address = address,
        phoneNumber = phoneNumber,
        productName = productName,
        price = stringToFloat(salePrice),
        deliveryPrice = stringToFloat(deliveryPrice),
        shippingDate = dateOfSale,
        deliveryDate = dateOfSale,
        delivered = false
    )
}