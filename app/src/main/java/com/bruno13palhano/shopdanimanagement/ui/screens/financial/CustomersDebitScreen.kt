package com.bruno13palhano.shopdanimanagement.ui.screens.financial

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.CommonItemList
import com.bruno13palhano.shopdanimanagement.ui.screens.common.CommonItem
import com.bruno13palhano.shopdanimanagement.ui.screens.financial.viewmodel.CustomersDebitViewModel
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme

@Composable
fun CustomersDebitScreen(
    onItemClick: (id: Long) -> Unit,
    navigateUp: () -> Unit,
    viewModel: CustomersDebitViewModel = hiltViewModel()
) {
    val debits by viewModel.debits.collectAsStateWithLifecycle()
    CustomersDebitContent(
        debits = debits,
        onItemClick = onItemClick,
        navigateUp = navigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomersDebitContent(
    debits: List<CommonItem>,
    onItemClick: (id: Long) -> Unit,
    navigateUp: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.customers_debit_label)) },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.up_button_label)
                        )
                    }
                }
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier.padding(it),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
        ) {
            items(items = debits, key = { item -> item.id }) { item ->
                CommonItemList(
                    modifier = Modifier.padding(vertical = 4.dp),
                    title = item.title,
                    subtitle = stringResource(id = R.string.customer_debit_tag, item.subtitle),
                    description = item.description,
                    onClick = { onItemClick(item.id) }
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun CustomersDebitDynamicPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            CustomersDebitContent(
                debits = debits,
                onItemClick = {},
                navigateUp = {}
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun CustomersDebitPreview() {
    ShopDaniManagementTheme(
        dynamicColor = false
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            CustomersDebitContent(
                debits = debits,
                onItemClick = {},
                navigateUp = {}
            )
        }
    }
}

private val debits = listOf(
    CommonItem(id = 1L, photo = "", title = "Bruno", subtitle = "R$12.50", description = "Feb 4, 2023"),
    CommonItem(id = 2L, photo = "", title = "Daniela", subtitle = "R$170.90", description = "Feb 7, 2023"),
    CommonItem(id = 3L, photo = "", title = "Josué", subtitle = "R$165.99", description = "Feb 8, 2023"),
    CommonItem(id = 4L, photo = "", title = "Helena", subtitle = "R$9.90", description = "Feb 11, 2023"),
    CommonItem(id = 5L, photo = "", title = "Fernando", subtitle = "R$160.50", description = "Feb 12, 2023"),
    CommonItem(id = 6L, photo = "", title = "Socorro", subtitle = "R$122.50", description = "Feb 14, 2023"),
    CommonItem(id = 7L, photo = "", title = "Brenda", subtitle = "R$282.99", description = "Feb 14, 2023"),
)