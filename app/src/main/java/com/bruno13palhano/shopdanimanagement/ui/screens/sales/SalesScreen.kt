package com.bruno13palhano.shopdanimanagement.ui.screens.sales

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.bruno13palhano.core.model.SaleInfo
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.BottomSheet
import com.bruno13palhano.shopdanimanagement.ui.components.HorizontalItemList
import com.bruno13palhano.shopdanimanagement.ui.components.MoreOptionsMenu
import com.bruno13palhano.shopdanimanagement.ui.components.SingleInputDialog
import com.bruno13palhano.shopdanimanagement.ui.screens.dateFormat
import com.bruno13palhano.shopdanimanagement.ui.screens.sales.viewmodel.SalesViewModel

@Composable
fun SalesScreen(
    isOrders: Boolean,
    screenTitle: String,
    onItemClick: (id: Long) -> Unit,
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
    val currentSale by viewModel.currentSale.collectAsStateWithLifecycle()
    val menuItems = arrayOf(
        stringResource(id = R.string.ordered_by_name_label),
        stringResource(id = R.string.ordered_by_price_label),
        stringResource(id = R.string.ordered_by_last_label),
        stringResource(id = R.string.create_spreadsheet_label)
    )

    var orderedByName by remember { mutableStateOf(false) }
    var orderedByPrice by remember { mutableStateOf(false) }
    var showSpreadsheetDialog by remember { mutableStateOf(false) }
    var openBottomSheet by remember { mutableStateOf(false) }

    SalesContent(
        isOrders = isOrders,
        screenTitle = screenTitle,
        sheetName = viewModel.sheetName,
        showSpreadsheetDialog = showSpreadsheetDialog,
        openBottomSheet = openBottomSheet,
        saleList = saleList,
        currentSale = currentSale,
        menuItems = menuItems,
        onItemClick = { saleId, customerId ->
            openBottomSheet = true
            viewModel.getCurrentSale(saleId = saleId, customerId = customerId)
        },
        onEditClick = {
            openBottomSheet = false
            onItemClick(it)
        },
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
        onDismissBottomSheet = { openBottomSheet = false },
        onAddButtonClick = onAddButtonClick,
        navigateUp = navigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalesContent(
    isOrders: Boolean,
    screenTitle: String,
    sheetName: String,
    showSpreadsheetDialog: Boolean,
    openBottomSheet: Boolean,
    saleList: List<SaleInfo>,
    currentSale: SaleInfo,
    menuItems: Array<String>,
    onItemClick: (saleId: Long, customerId: Long) -> Unit,
    onEditClick: (id: Long) -> Unit,
    onSheetNameChange: (sheetName: String) -> Unit,
    onDialogOkClick: () -> Unit,
    onMoreOptionsItemClick: (index: Int) -> Unit,
    onDismissDialog: () -> Unit,
    onDismissBottomSheet: () -> Unit,
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
                            imageVector = Icons.Filled.ArrowBack,
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
                HorizontalItemList(
                    modifier = Modifier.padding(vertical = 4.dp),
                    title = item.customerName,
                    subtitle = stringResource(
                        id = R.string.product_price_text_tag,
                        item.productName,
                        item.salePrice.toString()
                    ),
                    description = stringResource(
                        id = R.string.date_of_sale_tag,
                        dateFormat.format(item.dateOfSale)
                    ),
                    photo = item.productPhoto,
                    onClick = { onItemClick(item.saleId, item.customerId) }
                )
            }
        }

        AnimatedVisibility(visible = openBottomSheet) {
            BottomSheet(onDismissBottomSheet = onDismissBottomSheet) {
                Column(
                    modifier = Modifier
                        .padding(bottom = 48.dp)
                        .fillMaxWidth()
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = stringResource(id = R.string.sale_information_label),
                            style = MaterialTheme.typography.titleLarge
                        )
                        IconButton(
                            modifier = Modifier.align(Alignment.CenterEnd),
                            onClick = { onEditClick(currentSale.saleId) }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = stringResource(id = R.string.edit_label)
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .fillMaxWidth()
                    ) {
                        if (currentSale.productPhoto.isEmpty()) {
                            Image(
                                modifier = Modifier
                                    .size(128.dp)
                                    .padding(16.dp)
                                    .clip(RoundedCornerShape(5)),
                                imageVector = Icons.Filled.Image,
                                contentScale = ContentScale.Crop,
                                contentDescription = stringResource(id = R.string.product_image_label)
                            )
                        } else {
                            Image(
                                modifier = Modifier
                                    .size(128.dp)
                                    .padding(16.dp)
                                    .clip(RoundedCornerShape(5)),
                                painter = rememberAsyncImagePainter(
                                    model = currentSale.productPhoto
                                ),
                                contentScale = ContentScale.Crop,
                                contentDescription = stringResource(id = R.string.product_image_label)
                            )
                        }
                        Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                            Text(
                                text = pluralStringResource(
                                    id = R.plurals.simple_description_label,
                                    count = currentSale.quantity,
                                    currentSale.quantity,
                                    currentSale.productName
                                ),
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = stringResource(id = R.string.price_tag, currentSale.salePrice),
                                style = MaterialTheme.typography.bodyMedium,
                                fontStyle = FontStyle.Italic
                            )
                            Text(
                                text = stringResource(
                                    id = R.string.delivery_price_tag,
                                    currentSale.deliveryPrice.toString()
                                ),
                                style = MaterialTheme.typography.bodyMedium,
                                fontStyle = FontStyle.Italic
                            )
                            Text(
                                text = stringResource(
                                    id = R.string.date_of_sale_tag,
                                    dateFormat.format(currentSale.dateOfSale)
                                ),
                                style = MaterialTheme.typography.bodyMedium,
                                fontStyle = FontStyle.Italic
                            )
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    Text(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        text = stringResource(id = R.string.customer_information_label),
                        style = MaterialTheme.typography.titleLarge
                    )

                    Row(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .fillMaxWidth()
                    ) {
                        if (currentSale.customerName.isEmpty()) {
                            Image(
                                modifier = Modifier
                                    .size(128.dp)
                                    .padding(16.dp)
                                    .clip(RoundedCornerShape(5)),
                                imageVector = Icons.Filled.Image,
                                contentScale = ContentScale.Crop,
                                contentDescription = stringResource(id = R.string.customer_photo_label)
                            )
                        } else {
                            Image(
                                modifier = Modifier
                                    .size(128.dp)
                                    .padding(16.dp)
                                    .clip(RoundedCornerShape(5)),
                                painter = rememberAsyncImagePainter(
                                    model = currentSale.customerPhoto
                                ),
                                contentScale = ContentScale.Crop,
                                contentDescription = stringResource(id = R.string.customer_photo_label)
                            )
                        }
                        Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                            Text(
                                text = currentSale.customerName,
                                style = MaterialTheme.typography.titleMedium,
                                fontStyle = FontStyle.Italic
                            )
                            Text(
                                text = currentSale.address,
                                style = MaterialTheme.typography.bodyMedium,
                                fontStyle = FontStyle.Italic
                            )
                            Text(
                                text = currentSale.phoneNumber,
                                style = MaterialTheme.typography.bodyMedium,
                                fontStyle = FontStyle.Italic
                            )
                            Text(
                                text = currentSale.email,
                                style = MaterialTheme.typography.bodyMedium,
                                fontStyle = FontStyle.Italic
                            )
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