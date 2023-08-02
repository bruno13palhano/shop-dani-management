package com.bruno13palhano.shopdanimanagement.ui.screens.stock

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.bruno13palhano.shopdanimanagement.ui.components.ProductContent
import com.bruno13palhano.shopdanimanagement.ui.screens.stock.viewmodel.EditProductViewModel

@Composable
fun EditProductScreen(
    productId: Long,
    navigateUp: () -> Unit,
    viewModel: EditProductViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.getProduct(productId)
    }
    ProductContent(
        categories = viewModel.allCategories,
        companies = viewModel.allCompanies,
        name = viewModel.name,
        code = viewModel.code,
        description = viewModel.description,
        photo = viewModel.photo,
        quantity = viewModel.quantity,
        date = viewModel.date,
        validity = viewModel.validity,
        category = viewModel.category,
        company = viewModel.company,
        purchasePrice = viewModel.purchasePrice,
        salePrice = viewModel.salePrice,
        isPaid = viewModel.isPaid,
        onNameChange = viewModel::updateName,
        onCodeChange = viewModel::updateCode,
        onDescriptionChange = viewModel::updateDescription,
        onQuantityChange = viewModel::updateQuantity,
        onPurchasePriceChange = viewModel::updatePurchasePrice,
        onSalePriceChange = viewModel::updateSalePrice,
        onIsPaidChange = viewModel::updateIsPaid,
        onDismissCategory = {},
        onCompanySelected = {},
        onDismissCompany = {},
        onImageClick = {},
        onDateClick = {},
        onValidityClick = {},
        onOutsideClick = {},
        onActionButtonClick = {},
        navigateUp = navigateUp
    )
}