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
import com.bruno13palhano.shopdanimanagement.ui.components.ProductContent
import com.bruno13palhano.shopdanimanagement.ui.components.ProductListContent
import com.bruno13palhano.shopdanimanagement.ui.screens.common.CommonItem
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun ProductDynamicPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ProductContent(
                isEditable = false,
                screenTitle = stringResource(id = R.string.new_product_label),
                snackbarHostState = remember { SnackbarHostState() },
                categories = listOf(),
                companies = listOf(),
                name = "",
                code = "",
                description = "",
                photo = byteArrayOf(),
                date = "",
                category = "",
                company = "",
                onNameChange = {},
                onCodeChange = {},
                onDescriptionChange = {},
                onDismissCategory = {},
                onCompanySelected = {},
                onDismissCompany = {},
                onImageClick = {},
                onDateClick = {},
                onMoreOptionsItemClick = {},
                onOutsideClick = {},
                onActionButtonClick = {},
                navigateUp = {}
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun ProductPreview() {
    ShopDaniManagementTheme(
        dynamicColor = false
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ProductContent(
                isEditable = false,
                screenTitle = stringResource(id = R.string.new_product_label),
                snackbarHostState = remember { SnackbarHostState() },
                categories = listOf(),
                companies = listOf(),
                name = "",
                code = "",
                description = "",
                photo = byteArrayOf(),
                date = "",
                category = "",
                company = "",
                onNameChange = {},
                onCodeChange = {},
                onDescriptionChange = {},
                onDismissCategory = {},
                onCompanySelected = {},
                onDismissCompany = {},
                onImageClick = {},
                onDateClick = {},
                onMoreOptionsItemClick = {},
                onOutsideClick = {},
                onActionButtonClick = {},
                navigateUp = {}
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun ProductOrderedDynamicPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ProductContent(
                isEditable = true,
                screenTitle = stringResource(id = R.string.edit_product_label),
                snackbarHostState = remember { SnackbarHostState() },
                categories = listOf(),
                companies = listOf(),
                name = "",
                code = "",
                description = "",
                photo = byteArrayOf(),
                date = "",
                category = "",
                company = "",
                onNameChange = {},
                onCodeChange = {},
                onDescriptionChange = {},
                onDismissCategory = {},
                onCompanySelected = {},
                onDismissCompany = {},
                onImageClick = {},
                onDateClick = {},
                onMoreOptionsItemClick = {},
                onOutsideClick = {},
                onActionButtonClick = {},
                navigateUp = {}
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun ProductOrderedPreview() {
    ShopDaniManagementTheme(
        dynamicColor = false
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ProductContent(
                isEditable = true,
                screenTitle = stringResource(id = R.string.edit_product_label),
                snackbarHostState = remember { SnackbarHostState() },
                categories = listOf(),
                companies = listOf(),
                name = "",
                code = "",
                description = "",
                photo = byteArrayOf(),
                date = "",
                category = "",
                company = "",
                onNameChange = {},
                onCodeChange = {},
                onDescriptionChange = {},
                onDismissCategory = {},
                onCompanySelected = {},
                onDismissCompany = {},
                onImageClick = {},
                onDateClick = {},
                onMoreOptionsItemClick = {},
                onOutsideClick = {},
                onActionButtonClick = {},
                navigateUp = {}
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
private fun ProductListDynamicPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ProductListContent(
                isEditable = false,
                showCategoryDialog = true,
                categoryId = "Perfume",
                itemList = items,
                menuOptions = emptyArray(),
                onCategoryChange = {},
                onOkClick = {},
                onDismissRequest = {},
                onItemClick = {},
                onEditItemClick = {},
                onMenuItemClick = {},
                onAddButtonClick = {},
                navigateUp = {}
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
private fun ProductListPreview() {
    ShopDaniManagementTheme(
        dynamicColor = false
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ProductListContent(
                isEditable = true,
                showCategoryDialog = true,
                categoryId = "Perfume",
                itemList = items,
                menuOptions = emptyArray(),
                onCategoryChange = {},
                onOkClick = {},
                onDismissRequest = {},
                onItemClick = {},
                onEditItemClick = {},
                onMenuItemClick = {},
                onAddButtonClick = {},
                navigateUp = {}
            )
        }
    }
}

private val items = listOf(
    CommonItem(id= 1L, title = "Essencial", photo = byteArrayOf(), subtitle = "Natura", description = ""),
    CommonItem(id= 2L, title = "Kaiak", photo = byteArrayOf(), subtitle = "Natura", description = ""),
    CommonItem(id= 3L, title = "Homem", photo = byteArrayOf(), subtitle = "Natura", description = ""),
    CommonItem(id= 4L, title = "Florata", photo = byteArrayOf(), subtitle = "Avon", description = ""),
    CommonItem(id= 5L, title = "Essential", photo = byteArrayOf(), subtitle = "Avon", description = ""),
    CommonItem(id= 6L, title = "Luna", photo = byteArrayOf(), subtitle = "Natura", description = ""),
    CommonItem(id= 7L, title = "Homem", photo = byteArrayOf(), subtitle = "Natura", description = ""),
    CommonItem(id= 8L, title = "Florata", photo = byteArrayOf(), subtitle = "Avon", description = "")
)