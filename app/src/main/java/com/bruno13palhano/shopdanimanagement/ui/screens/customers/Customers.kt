package com.bruno13palhano.shopdanimanagement.ui.screens.customers

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.core.model.Customer
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.PhotoItem
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme

@Composable
fun CustomersScreen(
    onMenuClick: () -> Unit,
    onItemClick: (id: Long) -> Unit,
    onAddButtonClick: () -> Unit,
    viewModel: CustomersViewModel = hiltViewModel()
) {
    val customerList by viewModel.customerList.collectAsStateWithLifecycle()

    CustomersContent(
        customerList = customerList,
        onMenuClick = onMenuClick,
        onItemClick = onItemClick,
        onAddButtonClick = onAddButtonClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomersContent(
    customerList: List<Customer>,
    onMenuClick: () -> Unit,
    onItemClick: (id: Long) -> Unit,
    onAddButtonClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.customers_label)) },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = stringResource(id = R.string.drawer_menu_label)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddButtonClick) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.add_label)
                )
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier.padding(it),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
        ) {
            items(items = customerList, key = { customer -> customer.id }) { customer ->
                PhotoItem(
                    modifier = Modifier.padding(vertical = 4.dp),
                    title = customer.name,
                    subtitle = customer.address,
                    photo = customer.photo,
                    onClick = { onItemClick(customer.id) }
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun CustomerDynamicPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            CustomersContent(
                customerList = customerList,
                onMenuClick = {},
                onItemClick = {},
                onAddButtonClick = {}
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun CustomerPreview() {
    ShopDaniManagementTheme(
        dynamicColor = false
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            CustomersContent(
                customerList = customerList,
                onMenuClick = {},
                onItemClick = {},
                onAddButtonClick = {}
            )
        }
    }
}

private val customerList = listOf(
    Customer(1L, "Bruno", "", "", "Rua 15 de novembro", ""),
    Customer(2L, "Brenda", "", "", "13 de maio", ""),
    Customer(3L, "Daniela", "", "", "Rua do serrote", ""),
    Customer(4L, "Josué", "", "", "Rua 15 de novembro", ""),
    Customer(5L, "Helena", "", "", "Rua 13 de maio", ""),
    Customer(6L, "Socorro", "", "", "Rua do serrote", ""),
    Customer(7L, "Fernando", "", "", "Rua do serrote", ""),
    Customer(8L, "Henrique", "", "", "Carão", ""),
    Customer(9L, "Bruno", "", "", "Rua 15 de novembro", ""),
    Customer(10L, "Brenda", "", "", "13 de maio", ""),
    Customer(11L, "Daniela", "", "", "Rua do serrote", ""),
    Customer(12L, "Josué", "", "", "Rua 15 de novembro", ""),
    Customer(13L, "Helena", "", "", "Rua 13 de maio", ""),
    Customer(14L, "Socorro", "", "", "Rua do serrote", ""),
    Customer(15L, "Fernando", "", "", "Rua do serrote", ""),
    Customer(16L, "Henrique", "", "", "Carão", "")
)