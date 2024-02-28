package com.bruno13palhano.shopdanimanagement.ui.screens.amazon

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.core.model.SaleInfo
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.HorizontalItemList
import com.bruno13palhano.shopdanimanagement.ui.components.MoreOptionsMenu
import com.bruno13palhano.shopdanimanagement.ui.components.SingleInputDialog
import com.bruno13palhano.shopdanimanagement.ui.screens.amazon.viewmodel.AmazonViewModel
import com.bruno13palhano.shopdanimanagement.ui.screens.dateFormat

@Composable
fun AmazonRoute(
    showBottomMenu: (show: Boolean) -> Unit,
    gesturesEnabled: (enabled: Boolean) -> Unit,
    onItemClick: (saleId: Long) -> Unit,
    onSearchClick: () -> Unit,
    navigateUp: () -> Unit
) {
    showBottomMenu(true)
    gesturesEnabled(true)
    AmazonScreen(
        onItemClick = onItemClick,
        onSearchClick = onSearchClick,
        navigateUp = navigateUp,
    )
}

@Composable
fun AmazonScreen(
    onItemClick: (saleId: Long) -> Unit,
    onSearchClick: () -> Unit,
    navigateUp: () -> Unit,
    viewModel: AmazonViewModel = hiltViewModel()
) {
    val amazonSales by viewModel.amazonSale.collectAsStateWithLifecycle()
    val menuItems = arrayOf(
        stringResource(id = R.string.create_spreadsheet_label)
    )

    var showSpreadsheetDialog by remember { mutableStateOf(false) }

    AmazonContent(
        amazonSales = amazonSales,
        menuItems = menuItems,
        sheetName = viewModel.sheetName,
        showSpreadsheetDialog = showSpreadsheetDialog,
        onSheetNameChange = viewModel::updateSheetName,
        onItemClick = { saleId ->
            onItemClick(saleId)
        },
        onSearchClick = onSearchClick,
        onMoreOptionsItemClick = { index ->
            when (index) {
                MoreOptions.CREATE_SPREADSHEET -> { showSpreadsheetDialog = true }
                else -> {}
            }
        },
        onDialogOkClick = { viewModel.createSpreadsheet() },
        onDismissDialog = { showSpreadsheetDialog = false },
        navigateUp = navigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmazonContent(
    amazonSales: List<SaleInfo>,
    menuItems: Array<String>,
    sheetName: String,
    showSpreadsheetDialog: Boolean,
    onSheetNameChange: (sheetName: String) -> Unit,
    onItemClick: (saleId: Long) -> Unit,
    onSearchClick: () -> Unit,
    onMoreOptionsItemClick: (index: Int) -> Unit,
    onDialogOkClick: () -> Unit,
    onDismissDialog: () -> Unit,
    navigateUp: () -> Unit
) {
    var expandedMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.amazon_label)) },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.up_button_label)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onSearchClick) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = stringResource(id = R.string.search_label)
                        )
                    }
                    IconButton(onClick = { expandedMenu = true }) {
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
                                    expanded = expandedMenu,
                                    onDismissRequest = { expandedMenu = false },
                                    onClick = { onMoreOptionsItemClick(it) }
                                )
                            }
                        }
                    }
                }
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier.padding(it),
            contentPadding = PaddingValues(vertical = 4.dp, horizontal = 8.dp),
            reverseLayout = true
        ) {
            items(items = amazonSales, key = { item -> item.saleId }) { item ->
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
                    onClick = {
                        onItemClick(item.saleId)
                    }
                )
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
    const val CREATE_SPREADSHEET = 0
}