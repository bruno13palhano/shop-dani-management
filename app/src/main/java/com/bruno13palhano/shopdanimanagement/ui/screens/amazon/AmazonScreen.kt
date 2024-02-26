package com.bruno13palhano.shopdanimanagement.ui.screens.amazon

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.bruno13palhano.core.model.SaleInfo
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.HorizontalItemList
import com.bruno13palhano.shopdanimanagement.ui.screens.amazon.viewmodel.AmazonViewModel
import com.bruno13palhano.shopdanimanagement.ui.screens.dateFormat

@Composable
fun AmazonScreen(
    navigateUp: () -> Unit,
    onItemClick: (saleId: Long) -> Unit,
    viewModel: AmazonViewModel = hiltViewModel()
) {
    val amazonSales by viewModel.amazonSale.collectAsStateWithLifecycle()

    AmazonContent(
        amazonSales = amazonSales,
        onItemClick = { saleId ->
            onItemClick(saleId)
        },
        navigateUp = navigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmazonContent(
    amazonSales: List<SaleInfo>,
    onItemClick: (saleId: Long) -> Unit,
    navigateUp: () -> Unit
) {
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
    }
}