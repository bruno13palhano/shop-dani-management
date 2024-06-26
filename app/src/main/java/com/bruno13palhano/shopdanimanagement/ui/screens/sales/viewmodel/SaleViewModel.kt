package com.bruno13palhano.shopdanimanagement.ui.screens.sales.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.di.CustomerRep
import com.bruno13palhano.core.data.di.ProductRep
import com.bruno13palhano.core.data.di.SaleRep
import com.bruno13palhano.core.data.di.StockRep
import com.bruno13palhano.core.data.repository.customer.CustomerRepository
import com.bruno13palhano.core.data.repository.product.ProductRepository
import com.bruno13palhano.core.data.repository.sale.SaleRepository
import com.bruno13palhano.core.data.repository.stock.StockRepository
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.model.Errors
import com.bruno13palhano.core.model.Sale
import com.bruno13palhano.shopdanimanagement.ui.components.CustomerCheck
import com.bruno13palhano.shopdanimanagement.ui.screens.common.UiState
import com.bruno13palhano.shopdanimanagement.ui.screens.getCurrentTimestamp
import com.bruno13palhano.shopdanimanagement.ui.screens.stringToFloat
import com.bruno13palhano.shopdanimanagement.ui.screens.stringToInt
import com.bruno13palhano.shopdanimanagement.ui.screens.stringToLong
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SaleViewModel
    @Inject
    constructor(
        @SaleRep private val saleRepository: SaleRepository,
        @StockRep private val stockRepository: StockRepository,
        @ProductRep private val productRepository: ProductRepository,
        @CustomerRep private val customerRepository: CustomerRepository
    ) : ViewModel() {
        private var _saleState = MutableStateFlow<UiState>(UiState.Fail)
        val saleState = _saleState.asStateFlow()

        private var itemId by mutableLongStateOf(0L)
        private var productId by mutableLongStateOf(0L)
        private var customerId by mutableLongStateOf(0L)
        private var shippingDate by mutableLongStateOf(0L)
        private var deliveryDate by mutableLongStateOf(0L)
        private var isOrderedByCustomer by mutableStateOf(false)
        private var delivered by mutableStateOf(false)

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
        var amazonCode by mutableStateOf("")
            private set
        var amazonRequestNumber by mutableStateOf("")
            private set
        var amazonPrice by mutableStateOf("")
            private set
        var amazonTax by mutableStateOf("")
            private set
        var amazonSKU by mutableStateOf("")
            private set
        var resaleProfit by mutableStateOf("")
            private set
        var allCustomers by mutableStateOf(listOf<CustomerCheck>())
            private set
        var isPaidByCustomer by mutableStateOf(false)
            private set
        private var isPaid by mutableStateOf(false)
        var isAmazon by mutableStateOf(false)
            private set

        private var _amazonProfit = snapshotFlow { calcAmazonProfit() }
        val amazonProfit =
            _amazonProfit
                .stateIn(
                    scope = viewModelScope,
                    started = WhileSubscribed(5_000),
                    initialValue = "0"
                )

        private var _totalProfit = snapshotFlow { calcTotalProfit() }
        var totalProfit =
            _totalProfit
                .stateIn(
                    scope = viewModelScope,
                    started = WhileSubscribed(5_000),
                    initialValue = "0"
                )

        val notifySale =
            snapshotFlow { !isPaidByCustomer }
                .stateIn(
                    scope = viewModelScope,
                    started = WhileSubscribed(),
                    initialValue = true
                )

        val isSaleNotEmpty =
            snapshotFlow {
                if (isAmazon) {
                    isSaleParamsNotEmpty() && isAmazonParamsNotEmpty()
                } else {
                    isSaleParamsNotEmpty()
                }
            }
                .stateIn(
                    scope = viewModelScope,
                    started = WhileSubscribed(5_000),
                    initialValue = false
                )

        fun getAllCustomers() =
            viewModelScope.launch {
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
            if (isAmazon) this.amazonPrice = salePrice
        }

        fun updateDeliveryPrice(deliveryPrice: String) {
            this.deliveryPrice = deliveryPrice
        }

        fun updateIsPaidByCustomer(isPaidByCustomer: Boolean) {
            this.isPaidByCustomer = isPaidByCustomer
        }

        fun updateAmazonCode(amazonCode: String) {
            this.amazonCode = amazonCode
        }

        fun updateAmazonRequestNumber(amazonRequestNumber: String) {
            this.amazonRequestNumber = amazonRequestNumber
        }

        fun updateAmazonPrice(amazonPrice: String) {
            this.amazonPrice = amazonPrice
            if (isAmazon) this.salePrice = amazonPrice
        }

        fun updateAmazonTax(amazonTax: String) {
            this.amazonTax = amazonTax
        }

        fun updateAmazonSKU(amazonSKU: String) {
            this.amazonSKU = amazonSKU
        }

        fun updateResaleProfit(resaleProfit: String) {
            this.resaleProfit = resaleProfit
        }

        fun updateIsAmazon(isAmazon: Boolean) {
            this.isAmazon = isAmazon
            if (isAmazon) amazonPrice = salePrice
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

        fun getStockItem(stockItemId: Long) {
            viewModelScope.launch {
                stockRepository.getById(stockItemId).collect {
                    itemId = stockItemId
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

        private fun setCategories(categories: List<Category>) = categories.joinToString(", ") { category -> category.category }

        // Sets the current customer
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
            onError: (error: Int) -> Unit
        ) {
            _saleState.value = UiState.InProgress
            viewModelScope.launch {
                if ((stockQuantity < stringToInt(quantity)) && !isOrderedByCustomer) {
                    onError(Errors.INSUFFICIENT_ITEMS_STOCK)
                    _saleState.value = UiState.Fail
                } else {
                    saleRepository.insert(
                        model =
                            createSale(
                                id = 0L,
                                productId = productId,
                                shippingDate = currentDate,
                                deliveryDate = currentDate,
                                isOrderedByCustomer = isOrderedByCustomer,
                                canceled = false
                            ),
                        onError = {
                            onError(it)
                            _saleState.value = UiState.Fail
                        },
                        onSuccess = { _saleState.value = UiState.Success }
                    )
                }
            }
        }

        fun getSale(saleId: Long) {
            viewModelScope.launch {
                saleRepository.getById(saleId).collect {
                    productId = it.productId
                    customerId = it.customerId
                    itemId = it.stockId
                    productName = it.name
                    customerName = it.customerName
                    photo = it.photo
                    address = it.address
                    phoneNumber = it.phoneNumber
                    quantity = it.quantity.toString()
                    purchasePrice = it.purchasePrice.toString()
                    salePrice = it.salePrice.toString()
                    deliveryPrice = it.deliveryPrice.toString()
                    amazonCode = it.amazonCode
                    amazonRequestNumber = it.amazonRequestNumber.toString()
                    amazonPrice = if (it.isAmazon) it.salePrice.toString() else ""
                    amazonTax = it.amazonTax.toString()
                    amazonSKU = it.amazonSKU
                    resaleProfit = it.resaleProfit.toString()
                    shippingDate = it.shippingDate
                    deliveryDate = it.deliveryDate
                    category = setCategories(it.categories)
                    company = it.company
                    isPaidByCustomer = it.isPaidByCustomer
                    isOrderedByCustomer = it.isOrderedByCustomer
                    isAmazon = it.isAmazon
                    delivered = it.delivered
                    updateDateOfSale(it.dateOfSale)
                    updateDateOfPayment(it.dateOfPayment)
                    setCustomerChecked(it.customerId)
                }
            }
        }

        fun updateSale(
            saleId: Long,
            canceled: Boolean,
            shippingDate: Long = this.shippingDate,
            deliveryDate: Long = this.deliveryDate,
            onError: (error: Int) -> Unit
        ) {
            _saleState.value = UiState.InProgress
            viewModelScope.launch {
                saleRepository.update(
                    model =
                        createSale(
                            id = saleId,
                            productId = productId,
                            shippingDate = shippingDate,
                            deliveryDate = deliveryDate,
                            isOrderedByCustomer = isOrderedByCustomer,
                            canceled = canceled
                        ),
                    onError = {
                        onError(it)
                        _saleState.value = UiState.Fail
                    },
                    onSuccess = { _saleState.value = UiState.Success }
                )
            }
        }

        fun deleteSale(
            saleId: Long,
            onError: (error: Int) -> Unit
        ) {
            _saleState.value = UiState.InProgress
            viewModelScope.launch {
                saleRepository.deleteById(
                    id = saleId,
                    timestamp = getCurrentTimestamp(),
                    onError = {
                        onError(it)
                        _saleState.value = UiState.Fail
                    },
                    onSuccess = { _saleState.value = UiState.Success }
                )
            }
        }

        fun cancelSale(saleId: Long) {
            viewModelScope.launch {
                saleRepository.cancelSale(saleId = saleId)
            }
        }

        private fun createSale(
            id: Long,
            productId: Long,
            shippingDate: Long,
            deliveryDate: Long,
            isOrderedByCustomer: Boolean,
            canceled: Boolean
        ) = Sale(
            id = id,
            productId = productId,
            customerId = customerId,
            stockId = itemId,
            name = productName,
            customerName = customerName,
            photo = photo,
            address = address,
            phoneNumber = phoneNumber,
            quantity = stringToInt(quantity),
            purchasePrice = stringToFloat(purchasePrice),
            salePrice = stringToFloat(salePrice),
            deliveryPrice = stringToFloat(deliveryPrice),
            categories = emptyList(),
            company = company,
            amazonCode = amazonCode,
            amazonRequestNumber = stringToLong(amazonRequestNumber),
            amazonTax = stringToInt(amazonTax),
            amazonProfit = stringToFloat(amazonProfit.value),
            amazonSKU = amazonSKU,
            resaleProfit = stringToFloat(resaleProfit),
            totalProfit = stringToFloat(totalProfit.value),
            dateOfSale = dateOfSale,
            dateOfPayment = dateOfPayment,
            shippingDate = shippingDate,
            deliveryDate = deliveryDate,
            isOrderedByCustomer = isOrderedByCustomer,
            isPaidByCustomer = isPaidByCustomer,
            delivered = delivered,
            canceled = canceled,
            isAmazon = isAmazon,
            timestamp = getCurrentTimestamp()
        )

        private fun isSaleParamsNotEmpty(): Boolean {
            return quantity.isNotEmpty() && stringToInt(quantity) != 0 && purchasePrice.isNotEmpty() &&
                salePrice.isNotEmpty()
        }

        private fun isAmazonParamsNotEmpty(): Boolean {
            return amazonCode.isNotEmpty() && amazonRequestNumber.isNotEmpty() &&
                amazonPrice.isNotEmpty() && (amazonTax.isNotEmpty() && stringToInt(amazonTax) != 0) &&
                amazonSKU.isNotEmpty() && resaleProfit.isNotEmpty()
        }

        private fun calcAmazonProfit(): String {
            return (
                if (amazonPrice.isNotEmpty()) {
                    val totalAmazonPrice = (stringToFloat(amazonPrice) * stringToFloat(quantity))
                    totalAmazonPrice - ((stringToFloat(amazonTax) * totalAmazonPrice) / 100)
                } else {
                    0F
                }
            ).toString()
        }

        private fun calcTotalProfit(): String {
            return (
                stringToFloat(amazonProfit.value) -
                    (stringToFloat(purchasePrice) * stringToFloat(quantity)) +
                    stringToFloat(resaleProfit)
            ).toString()
        }
    }