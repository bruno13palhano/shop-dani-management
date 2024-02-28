package com.bruno13palhano.shopdanimanagement.ui.screens.products

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.CircularProgress
import com.bruno13palhano.shopdanimanagement.ui.components.ProductListContent
import com.bruno13palhano.shopdanimanagement.ui.components.BarcodeReader
import com.bruno13palhano.shopdanimanagement.ui.screens.common.UiState
import com.bruno13palhano.shopdanimanagement.ui.screens.common.getErrors
import com.bruno13palhano.shopdanimanagement.ui.screens.products.viewmodel.ProductListViewModel
import kotlinx.coroutines.launch

@Composable
fun SalesProductListRoute(
    showBottomMenu: (show: Boolean) -> Unit,
    gesturesEnabled: (enabled: Boolean) -> Unit,
    onItemClick: (id: Long) -> Unit,
    onSearchClick: () -> Unit,
    navigateUp: () -> Unit
) {
    gesturesEnabled(true)
    ProductListScreen(
        isEditable = false,
        categoryId = 0L,
        onItemClick = onItemClick,
        onSearchClick = onSearchClick,
        onAddButtonClick = {},
        showBottomMenu = showBottomMenu,
        navigateUp = navigateUp
    )
}

@Composable
fun ProductListRoute(
    showBottomMenu: (show: Boolean) -> Unit,
    gesturesEnabled: (enabled: Boolean) -> Unit,
    categoryId: Long,
    onItemClick: (id: Long) -> Unit,
    onSearchClick: () -> Unit,
    onAddButtonClick: () -> Unit,
    navigateUp: () -> Unit
) {
    gesturesEnabled(true)
    ProductListScreen(
        isEditable = true,
        categoryId = categoryId,
        onItemClick = onItemClick,
        onSearchClick = onSearchClick,
        onAddButtonClick = onAddButtonClick,
        showBottomMenu = showBottomMenu,
        navigateUp = navigateUp
    )
}

@Composable
fun ProductListScreen(
    isEditable: Boolean,
    categoryId: Long,
    onItemClick: (id: Long) -> Unit,
    onSearchClick: () -> Unit,
    onAddButtonClick: () -> Unit,
    showBottomMenu: (show: Boolean) -> Unit,
    navigateUp: () -> Unit,
    viewModel: ProductListViewModel = hiltViewModel()
) {
    if (isEditable) {
        LaunchedEffect(key1 = Unit) {
            viewModel.getCategory(categoryId)
        }
        LaunchedEffect(key1 = viewModel.name) {
            viewModel.getProductsByCategory(viewModel.name)
        }
    } else {
        LaunchedEffect(key1 = Unit) {
            viewModel.getAllProducts()
        }
    }

    val menuOptions = mutableListOf(stringResource(id = R.string.all_products_label))
    val categoryState by viewModel.categoryState.collectAsStateWithLifecycle()
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    val orderList by viewModel.orders.collectAsStateWithLifecycle()
    var showCategoryDialog by remember { mutableStateOf(false) }
    var showContent by remember { mutableStateOf(true) }
    var showBarcodeReader by remember { mutableStateOf(false) }
    menuOptions.addAll(categories)

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val errors = getErrors()

    when (categoryState) {
        UiState.Fail -> {
            showContent = !showBarcodeReader
        }

        UiState.InProgress -> {
            showContent = false
            CircularProgress()
        }

        UiState.Success -> {
            showContent = !showBarcodeReader
        }
    }

    AnimatedVisibility(
        visible = showBarcodeReader,
        enter = fadeIn(animationSpec = spring(stiffness = Spring.StiffnessLow)),
        exit = fadeOut(animationSpec = spring(stiffness = Spring.StiffnessLow))
    ) {
        showBottomMenu(false)
        BarcodeReader(
            onBarcodeClick = { code ->
                if(code.isNotEmpty()) {
                    viewModel.getProductsByCode(code = code)
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
        ProductListContent(
            snackbarHostState = snackbarHostState,
            isEditable = isEditable,
            categoryId = viewModel.name,
            showCategoryDialog = showCategoryDialog,
            itemList = orderList,
            menuOptions = menuOptions.toTypedArray(),
            onCategoryChange = viewModel::updateName,
            onOkClick = {
                viewModel.updateCategory(id = categoryId) {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = errors[it],
                            withDismissAction = true
                        )
                    }
                }
            },
            onDismissRequest = { showCategoryDialog = false },
            onItemClick = onItemClick,
            onSearchClick = onSearchClick,
            onEditItemClick = { showCategoryDialog = true },
            onBarcodeClick = { showBarcodeReader = true; showContent = false },
            onMenuItemClick = { index ->
                if (index == 0) {
                    viewModel.getAllProducts()
                } else {
                    viewModel.getProductsByCategory(menuOptions[index])
                }
            },
            onAddButtonClick = onAddButtonClick,
            navigateUp = navigateUp
        )
    }
}