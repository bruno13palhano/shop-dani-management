package com.bruno13palhano.shopdanimanagement.ui.screens.previews.components

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.SaleContent
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SaleDynamicPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SharedTransitionLayout {
                AnimatedContent(targetState = 0L, label = "") {
                    SaleContent(
                        isEdit = false,
                        screenTitle = stringResource(id = R.string.new_sale_label),
                        snackbarHostState = remember { SnackbarHostState() },
                        menuItems = emptyArray(),
                        productId = it,
                        productName = "",
                        customerName = "",
                        photo = byteArrayOf(),
                        quantity = "",
                        dateOfSale = "",
                        dateOfPayment = "",
                        purchasePrice = "",
                        salePrice = "",
                        deliveryPrice = "",
                        category = "",
                        company = "",
                        amazonCode = "",
                        amazonRequestNumber = "",
                        amazonPrice = "",
                        amazonTax = "",
                        amazonProfit = "",
                        amazonSKU = "",
                        resaleProfit = "",
                        totalProfit = "",
                        isPaidByCustomer = true,
                        isAmazon = true,
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedContentScope = this,
                        onQuantityChange = {},
                        onAmazonCodeChange = {},
                        onAmazonRequestNumberChange = {},
                        onAmazonPriceChange = {},
                        onAmazonTaxChange = {},
                        onAmazonSKUChange = {},
                        onResaleProfitChange = {},
                        onPurchasePriceChange = {},
                        onSalePriceChange = {},
                        onDeliveryPriceChange = {},
                        onIsPaidByCustomerChange = {},
                        onIsAmazonChange = {},
                        onDateOfSaleClick = {},
                        onDateOfPaymentClick = {},
                        customers = emptyList(),
                        onDismissCustomer = {},
                        onCustomerSelected = {},
                        onOutsideClick = {},
                        onMoreOptionsItemClick = {},
                        onDoneButtonClick = {},
                        navigateUp = {}
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SalePreview() {
    ShopDaniManagementTheme(
        dynamicColor = false
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SharedTransitionLayout {
                AnimatedContent(targetState = 0L, label = "") {
                    SaleContent(
                        isEdit = true,
                        screenTitle = stringResource(id = R.string.edit_sale_label),
                        snackbarHostState = remember { SnackbarHostState() },
                        menuItems = emptyArray(),
                        productId = it,
                        productName = "",
                        customerName = "",
                        photo = byteArrayOf(),
                        quantity = "",
                        dateOfSale = "",
                        dateOfPayment = "",
                        purchasePrice = "",
                        deliveryPrice = "",
                        salePrice = "",
                        category = "",
                        company = "",
                        amazonCode = "",
                        amazonRequestNumber = "",
                        amazonPrice = "",
                        amazonTax = "",
                        amazonProfit = "",
                        amazonSKU = "",
                        resaleProfit = "",
                        totalProfit = "",
                        isPaidByCustomer = true,
                        isAmazon = false,
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedContentScope = this,
                        onQuantityChange = {},
                        onAmazonCodeChange = {},
                        onAmazonRequestNumberChange = {},
                        onAmazonPriceChange = {},
                        onAmazonTaxChange = {},
                        onAmazonSKUChange = {},
                        onResaleProfitChange = {},
                        onPurchasePriceChange = {},
                        onSalePriceChange = {},
                        onDeliveryPriceChange = {},
                        onIsPaidByCustomerChange = {},
                        onIsAmazonChange = {},
                        onDateOfSaleClick = {},
                        onDateOfPaymentClick = {},
                        customers = emptyList(),
                        onDismissCustomer = {},
                        onCustomerSelected = {},
                        onOutsideClick = {},
                        onMoreOptionsItemClick = {},
                        onDoneButtonClick = {},
                        navigateUp = {}
                    )
                }
            }
        }
    }
}