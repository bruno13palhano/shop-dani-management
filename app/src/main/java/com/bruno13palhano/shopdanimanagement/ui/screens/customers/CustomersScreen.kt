package com.bruno13palhano.shopdanimanagement.ui.screens.customers

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
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
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.CommonPhotoItemList
import com.bruno13palhano.shopdanimanagement.ui.screens.common.CommonItem
import com.bruno13palhano.shopdanimanagement.ui.screens.customers.viewmodel.CustomersViewModel
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme

@Composable
fun CustomersScreen(
    onItemClick: (id: Long) -> Unit,
    onAddButtonClick: () -> Unit,
    navigateUp: () -> Unit,
    viewModel: CustomersViewModel = hiltViewModel()
) {
    val customerList by viewModel.customerList.collectAsStateWithLifecycle()

    CustomersContent(
        customerList = customerList,
        onItemClick = onItemClick,
        onAddButtonClick = onAddButtonClick,
        navigateUp = navigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomersContent(
    customerList: List<CommonItem>,
    onItemClick: (id: Long) -> Unit,
    onAddButtonClick: () -> Unit,
    navigateUp: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.customers_label)) },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.up_button_label)
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
            items(items = customerList, key = { item -> item.id }) { item ->
                CommonPhotoItemList(
                    modifier = Modifier.padding(vertical = 4.dp),
                    title = item.title,
                    subtitle = item.subtitle,
                    photo = item.photo,
                    onClick = { onItemClick(item.id) }
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
                onItemClick = {},
                onAddButtonClick = {},
                navigateUp = {}
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
                onItemClick = {},
                onAddButtonClick = {},
                navigateUp = {}
            )
        }
    }
}

private val customerList = listOf(
    CommonItem(1L, "", "Bruno", "Rua 15 de novembro", description = ""),
    CommonItem(2L, "", "Brenda",  "13 de maio", description = ""),
    CommonItem(3L, "", "Daniela", "Rua do serrote", description = ""),
    CommonItem(4L, "", "Josué", "Rua 15 de novembro", description = ""),
    CommonItem(5L, "", "Helena", "Rua 13 de maio", description = ""),
    CommonItem(6L, "","Socorro","Rua do serrote", description = ""),
    CommonItem(7L, "","Fernando","Rua do serrote", description = ""),
    CommonItem(8L, "","Henrique","Carão", description = ""),
    CommonItem(9L, "", "Bruno","Rua 15 de novembro", description = ""),
)