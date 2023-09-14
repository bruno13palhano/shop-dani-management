package com.bruno13palhano.shopdanimanagement.ui.screens.previews.components

import android.content.res.Configuration
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

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun SaleDynamicPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SaleContent(
                isEdit = false,
                screenTitle = stringResource(id = R.string.new_sale_label),
                snackbarHostState = remember { SnackbarHostState() },
                menuItems = emptyArray(),
                productName = "",
                customerName = "",
                photo = "",
                quantity = "",
                dateOfSale = "",
                dateOfPayment = "",
                purchasePrice = "",
                salePrice = "",
                category = "",
                company = "",
                isPaidByCustomer = true,
                onProductNameChange = {},
                onQuantityChange = {},
                onPurchasePriceChange = {},
                onSalePriceChange = {},
                onIsPaidByCustomerChange = {},
                onDateOfSaleClick = {},
                onDateOfPaymentClick = {},
                categories = emptyList(),
                companies = emptyList(),
                customers = emptyList(),
                onDismissCategory = {},
                onDismissCompany = {},
                onDismissCustomer = {},
                onCompanySelected = {},
                onCustomerSelected = {},
                onOutsideClick = {},
                onMoreOptionsItemClick = {},
                onDoneButtonClick = {},
                navigateUp = {}
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun SalePreview() {
    ShopDaniManagementTheme(
        dynamicColor = false
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SaleContent(
                isEdit = true,
                screenTitle = stringResource(id = R.string.edit_sale_label),
                snackbarHostState = remember { SnackbarHostState() },
                menuItems = emptyArray(),
                productName = "",
                customerName = "",
                photo = "",
                quantity = "",
                dateOfSale = "",
                dateOfPayment = "",
                purchasePrice = "",
                salePrice = "",
                category = "",
                company = "",
                isPaidByCustomer = true,
                onProductNameChange = {},
                onQuantityChange = {},
                onPurchasePriceChange = {},
                onSalePriceChange = {},
                onIsPaidByCustomerChange = {},
                onDateOfSaleClick = {},
                onDateOfPaymentClick = {},
                categories = emptyList(),
                companies = emptyList(),
                customers = emptyList(),
                onDismissCategory = {},
                onDismissCompany = {},
                onDismissCustomer = {},
                onCompanySelected = {},
                onCustomerSelected = {},
                onOutsideClick = {},
                onMoreOptionsItemClick = {},
                onDoneButtonClick = {},
                navigateUp = {}
            )
        }
    }
}