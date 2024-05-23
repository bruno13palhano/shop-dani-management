package com.bruno13palhano.shopdanimanagement.ui.screens.sales

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bruno13palhano.core.model.SaleInfo
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.MoreOptionsMenu
import com.bruno13palhano.shopdanimanagement.ui.components.SingleInputDialog
import com.bruno13palhano.shopdanimanagement.ui.screens.dateFormat
import com.bruno13palhano.shopdanimanagement.ui.screens.sales.viewmodel.SalesViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SalesRoute(
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    showBottomMenu: (show: Boolean) -> Unit,
    gesturesEnabled: (enabled: Boolean) -> Unit,
    onItemClick: (saleId: Long, productId: Long) -> Unit,
    onAddButtonClick: () -> Unit,
    navigateUp: () -> Unit
) {
    showBottomMenu(true)
    gesturesEnabled(true)
    SalesScreen(
        isOrders = false,
        screenTitle = stringResource(id = R.string.sales_label),
        sharedTransitionScope = sharedTransitionScope,
        animatedContentScope = animatedContentScope,
        onItemClick = onItemClick,
        onAddButtonClick = onAddButtonClick,
        navigateUp = navigateUp
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun OrdersRoute(
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    showBottomMenu: (show: Boolean) -> Unit,
    gesturesEnabled: (enabled: Boolean) -> Unit,
    onItemClick: (saleId: Long, productId: Long) -> Unit,
    navigateUp: () -> Unit
) {
    showBottomMenu(true)
    gesturesEnabled(true)
    SalesScreen(
        isOrders = true,
        screenTitle = stringResource(id = R.string.orders_label),
        sharedTransitionScope = sharedTransitionScope,
        animatedContentScope = animatedContentScope,
        onItemClick = onItemClick,
        onAddButtonClick = {},
        navigateUp = navigateUp
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SalesScreen(
    isOrders: Boolean,
    screenTitle: String,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onItemClick: (saleId: Long, productId: Long) -> Unit,
    onAddButtonClick: () -> Unit,
    navigateUp: () -> Unit,
    viewModel: SalesViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) {
        if (isOrders) {
            viewModel.getOrders()
        } else {
            viewModel.getSales()
        }
    }

    val saleList by viewModel.saleList.collectAsStateWithLifecycle()

    val menuItems = arrayOf(
        stringResource(id = R.string.ordered_by_name_label),
        stringResource(id = R.string.ordered_by_price_label),
        stringResource(id = R.string.ordered_by_last_label),
        stringResource(id = R.string.create_spreadsheet_label)
    )

    var orderedByName by remember { mutableStateOf(false) }
    var orderedByPrice by remember { mutableStateOf(false) }
    var showSpreadsheetDialog by remember { mutableStateOf(false) }

    SalesContent(
        isOrders = isOrders,
        screenTitle = screenTitle,
        sheetName = viewModel.sheetName,
        showSpreadsheetDialog = showSpreadsheetDialog,
        saleList = saleList,
        menuItems = menuItems,
        sharedTransitionScope = sharedTransitionScope,
        animatedContentScope = animatedContentScope,
        onItemClick = onItemClick,
        onSheetNameChange = viewModel::updateSheetName,
        onDialogOkClick = { viewModel.exportSalesSheet() },
        onMoreOptionsItemClick = { index ->
            when (index) {
                MoreOptions.ORDERED_BY_NAME -> {
                    if (isOrders) {
                        viewModel.getOrdersByCustomerName(isOrderedAsc = orderedByName)
                    } else {
                        viewModel.getSalesByCustomerName(isOrderedAsc = orderedByName)
                    }
                    orderedByName = !orderedByName
                }
                MoreOptions.ORDERED_BY_PRICE -> {
                    if (isOrders) {
                        viewModel.getOrdersBySalePrice(isOrderedAsc = orderedByPrice)
                    } else {
                        viewModel.getSalesBySalePrice(isOrderedAsc = orderedByPrice)
                    }
                    orderedByPrice = !orderedByPrice
                }
                MoreOptions.ORDERED_BY_LAST -> {
                    if (isOrders) {
                        viewModel.getOrders()
                    } else {
                        viewModel.getSales()
                    }
                }
                MoreOptions.CREATE_SPREADSHEET -> { showSpreadsheetDialog = true }
            }
        },
        onDismissDialog = { showSpreadsheetDialog = false },
        onAddButtonClick = onAddButtonClick,
        navigateUp = navigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SalesContent(
    isOrders: Boolean,
    screenTitle: String,
    sheetName: String,
    showSpreadsheetDialog: Boolean,
    saleList: List<SaleInfo>,
    menuItems: Array<String>,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onItemClick: (saleId: Long, productId: Long) -> Unit,
    onSheetNameChange: (sheetName: String) -> Unit,
    onDialogOkClick: () -> Unit,
    onMoreOptionsItemClick: (index: Int) -> Unit,
    onDismissDialog: () -> Unit,
    onAddButtonClick: () -> Unit,
    navigateUp: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = screenTitle) },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.up_button_label)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { expanded = true }) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = stringResource(id = R.string.drawer_menu_label)
                            )
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                MoreOptionsMenu(
                                    items = menuItems,
                                    expanded = expanded,
                                    onDismissRequest = { expandedValue ->
                                        expanded = expandedValue
                                    },
                                    onClick = onMoreOptionsItemClick
                                )
                            }
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            if (!isOrders) {
                FloatingActionButton(onClick = onAddButtonClick) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(id = R.string.add_label)
                    )
                }
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .semantics { contentDescription = "List of items" }
                .padding(it),
            contentPadding = PaddingValues(vertical = 4.dp, horizontal = 8.dp),
            reverseLayout = true
        ) {
            items(items = saleList, key = { item -> item.saleId }) { item ->
                with(sharedTransitionScope) {
                    ElevatedCard(
                        modifier = Modifier.padding(vertical = 4.dp),
                        onClick = { onItemClick(item.saleId, item.productId) }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                modifier = Modifier
                                    .sharedElement(
                                        sharedTransitionScope.rememberSharedContentState(
                                            key = "product-${item.productId}"
                                        ),
                                        animatedVisibilityScope = animatedContentScope
                                    )
                                    .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
                                    .size(64.dp)
                                    .clip(RoundedCornerShape(5)),
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(item.productPhoto)
                                    .crossfade(true)
                                    .placeholderMemoryCacheKey("product-${item.productId}")
                                    .memoryCacheKey("product-${item.productId}")
                                    .build(),
                                contentDescription = stringResource(id = R.string.item_image),
                                contentScale = ContentScale.Crop
                            )

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1F, true)
                            ) {
                                Text(
                                    modifier = Modifier
                                        .padding(start = 16.dp, top = 16.dp, end = 16.dp),
                                    text = item.customerName,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                                    text = stringResource(
                                        id = R.string.product_price_text_tag,
                                        item.productName,
                                        item.salePrice.toString()
                                     ),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    modifier = Modifier
                                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                                    text = stringResource(
                                        id = R.string.date_of_sale_tag,
                                        dateFormat.format(item.dateOfSale)
                                    ),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = showSpreadsheetDialog,
            enter = fadeIn(animationSpec = spring(stiffness = Spring.StiffnessLow)),
            exit = fadeOut(animationSpec = spring(stiffness = Spring.StiffnessLow))
        ) {
            SingleInputDialog(
                dialogTitle = stringResource(id = R.string.spreadsheet_name_label),
                label = stringResource(id = R.string.name_label),
                placeholder = stringResource(id = R.string.enter_name_label),
                input = sheetName,
                onInputChange = onSheetNameChange,
                onOkClick = onDialogOkClick,
                onDismissRequest = onDismissDialog
            )
        }
    }
}

private object MoreOptions {
    const val ORDERED_BY_NAME = 0
    const val ORDERED_BY_PRICE = 1
    const val ORDERED_BY_LAST = 2
    const val CREATE_SPREADSHEET = 3
}