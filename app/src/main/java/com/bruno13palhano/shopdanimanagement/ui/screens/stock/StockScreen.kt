package com.bruno13palhano.shopdanimanagement.ui.screens.stock

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.BarcodeReader
import com.bruno13palhano.shopdanimanagement.ui.components.StockListContent
import com.bruno13palhano.shopdanimanagement.ui.screens.stock.viewmodel.StockViewModel

@Composable
fun SalesStockRoute(
    showBottomMenu: (show: Boolean) -> Unit,
    gesturesEnabled: (enabled: Boolean) -> Unit,
    onItemClick: (id: Long) -> Unit,
    onSearchClick: () -> Unit,
    navigateUp: () -> Unit
) {
    gesturesEnabled(true)
    StockScreen(
        isAddButtonEnabled = false,
        screenTitle = stringResource(id = R.string.stock_list_label),
        onItemClick = onItemClick,
        onSearchClick = onSearchClick,
        onAddButtonClick = {},
        showBottomMenu = showBottomMenu,
        navigateUp = navigateUp
    )
}

@Composable
fun StockRoute(
    showBottomMenu: (show: Boolean) -> Unit,
    gesturesEnabled: (enabled: Boolean) -> Unit,
    onItemClick: (id: Long) -> Unit,
    onSearchClick: () -> Unit,
    onAddButtonClick: () -> Unit,
    navigateUp: () -> Unit
) {
    gesturesEnabled(true)
    StockScreen(
        isAddButtonEnabled = true,
        screenTitle = stringResource(id = R.string.stock_list_label),
        onItemClick = onItemClick,
        onSearchClick = onSearchClick,
        onAddButtonClick = onAddButtonClick,
        showBottomMenu = showBottomMenu,
        navigateUp = navigateUp
    )
}

@Composable
fun StockScreen(
    isAddButtonEnabled: Boolean,
    screenTitle: String,
    onItemClick: (id: Long) -> Unit,
    onSearchClick: () -> Unit,
    onAddButtonClick: () -> Unit,
    showBottomMenu: (show: Boolean) -> Unit,
    navigateUp: () -> Unit,
    viewModel: StockViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) { viewModel.getItems() }

    val menuOptions =
        mutableListOf(
            stringResource(id = R.string.all_products_label),
            stringResource(id = R.string.out_of_stock_label)
        )

    val stockList by viewModel.stockList.collectAsStateWithLifecycle()
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    var showContent by remember { mutableStateOf(true) }
    var showBarcodeReader by remember { mutableStateOf(false) }
    menuOptions.addAll(categories)

    AnimatedVisibility(
        visible = showBarcodeReader,
        enter = fadeIn(animationSpec = spring(stiffness = Spring.StiffnessLow)),
        exit = fadeOut(animationSpec = spring(stiffness = Spring.StiffnessLow))
    ) {
        showBottomMenu(false)

        BarcodeReader(
            onBarcodeClick = { code ->
                if (code.isNotEmpty()) {
                    viewModel.getItemsByCode(code = code)
                    showBarcodeReader = false
                    showContent = true
                }
            },
            onClose = {
                showBarcodeReader = false
                showContent = true
            }
        )
    }

    AnimatedVisibility(
        visible = showContent,
        enter = fadeIn(animationSpec = spring(stiffness = Spring.StiffnessLow)),
        exit = fadeOut(animationSpec = spring(stiffness = Spring.StiffnessLow))
    ) {
        showBottomMenu(true)
        StockListContent(
            isAddButtonEnabled = isAddButtonEnabled,
            screenTitle = screenTitle,
            itemList = stockList,
            menuOptions = menuOptions.toTypedArray(),
            onItemClick = onItemClick,
            onSearchClick = onSearchClick,
            onBarcodeClick = {
                showContent = false
                showBarcodeReader = true
            },
            onMenuItemClick = { index ->
                when (index) {
                    StockMenuItems.ALL_ITEMS -> {
                        viewModel.getItems()
                    }

                    StockMenuItems.OUT_OF_STOCK -> {
                        viewModel.getOutOfStock()
                    }

                    StockMenuItems.ITEMS_BY_CATEGORY -> {
                        viewModel.getItemsByCategories(menuOptions[index])
                    }
                }
            },
            onAddButtonClick = onAddButtonClick,
            navigateUp = navigateUp
        )
    }
}

private object StockMenuItems {
    const val ALL_ITEMS = 0
    const val OUT_OF_STOCK = 1
    const val ITEMS_BY_CATEGORY = 2
}