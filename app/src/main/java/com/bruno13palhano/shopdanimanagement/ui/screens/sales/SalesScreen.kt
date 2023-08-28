package com.bruno13palhano.shopdanimanagement.ui.screens.sales

import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.HorizontalItemList
import com.bruno13palhano.shopdanimanagement.ui.screens.common.CommonItem
import com.bruno13palhano.shopdanimanagement.ui.screens.sales.viewmodel.SalesViewModel

@Composable
fun SalesScreen(
    onItemClick: (id: Long) -> Unit,
    onAddButtonClick: () -> Unit,
    navigateUp: () -> Unit,
    viewModel: SalesViewModel = hiltViewModel()
) {
    val saleList by viewModel.saleList.collectAsStateWithLifecycle()

    SalesContent(
        saleList = saleList,
        onItemClick = onItemClick,
        onAddButtonClick = onAddButtonClick,
        navigateUp = navigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalesContent(
    saleList: List<CommonItem>,
    onItemClick: (id: Long) -> Unit,
    onAddButtonClick: () -> Unit,
    navigateUp: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.sales_label)) },
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
            contentPadding = PaddingValues(vertical = 4.dp, horizontal = 8.dp)
        ) {
            items(items = saleList, key = { item -> item.id }) { item ->
                HorizontalItemList(
                    modifier = Modifier.padding(vertical = 4.dp),
                    photo = item.photo,
                    title = item.title,
                    subtitle = item.subtitle,
                    description = stringResource(id = R.string.date_of_sale_tag, item.description),
                    onClick = { onItemClick(item.id) }
                )
            }
        }
    }
}