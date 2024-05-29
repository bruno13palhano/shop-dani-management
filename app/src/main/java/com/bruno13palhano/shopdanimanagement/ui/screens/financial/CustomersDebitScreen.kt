package com.bruno13palhano.shopdanimanagement.ui.screens.financial

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.bruno13palhano.core.model.SaleInfo
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.MoreOptionsMenu
import com.bruno13palhano.shopdanimanagement.ui.screens.dateFormat
import com.bruno13palhano.shopdanimanagement.ui.screens.financial.viewmodel.CustomersDebitViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun CustomersDebitRoute(
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    showBottomMenu: (show: Boolean) -> Unit,
    gesturesEnabled: (enabled: Boolean) -> Unit,
    onItemClick: (saleId: Long, productId: Long) -> Unit,
    navigateUp: () -> Unit
) {
    showBottomMenu(false)
    gesturesEnabled(true)
    CustomersDebitScreen(
        sharedTransitionScope = sharedTransitionScope,
        animatedContentScope = animatedContentScope,
        onItemClick = onItemClick,
        navigateUp = navigateUp
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun CustomersDebitScreen(
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onItemClick: (saleId: Long, productId: Long) -> Unit,
    navigateUp: () -> Unit,
    viewModel: CustomersDebitViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.getDebits()
    }

    val debits by viewModel.debits.collectAsStateWithLifecycle()
    val menuItems =
        arrayOf(
            stringResource(id = R.string.ordered_by_name_label),
            stringResource(id = R.string.ordered_by_price_label),
            stringResource(id = R.string.ordered_by_last_label)
        )

    var orderedByName by remember { mutableStateOf(false) }
    var orderedByPrice by remember { mutableStateOf(false) }

    CustomersDebitContent(
        debits = debits,
        menuItems = menuItems,
        sharedTransitionScope = sharedTransitionScope,
        animatedContentScope = animatedContentScope,
        onItemClick = onItemClick,
        onMoreOptionsItemClick = { index ->
            when (index) {
                CustomersDebitMenu.DEBITS_BY_NAME -> {
                    viewModel.getDebitByCustomerName(orderedByName)
                    orderedByName = toggleOrdered(orderedByName)
                }
                CustomersDebitMenu.DEBITS_BY_PRICE -> {
                    viewModel.getDebitBySalePrice(orderedByPrice)
                    orderedByPrice = toggleOrdered(orderedByPrice)
                }
                CustomersDebitMenu.ALL_DEBITS -> {
                    viewModel.getDebits()
                }
            }
        },
        navigateUp = navigateUp
    )
}

private fun toggleOrdered(ordered: Boolean) = !ordered

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun CustomersDebitContent(
    debits: List<SaleInfo>,
    menuItems: Array<String>,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onItemClick: (saleId: Long, productId: Long) -> Unit,
    onMoreOptionsItemClick: (index: Int) -> Unit,
    navigateUp: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.customers_debit_label)) },
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
                                    onDismissRequest = { expandedValue -> expanded = expandedValue },
                                    onClick = onMoreOptionsItemClick
                                )
                            }
                        }
                    }
                }
            )
        }
    ) {
        LazyColumn(
            modifier =
                Modifier
                    .semantics { contentDescription = "List of debits" }
                    .padding(it),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
        ) {
            items(items = debits, key = { item -> item.saleId }) { item ->
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
                                modifier =
                                    Modifier
                                        .sharedElement(
                                            sharedTransitionScope.rememberSharedContentState(
                                                key = "product-${item.productId}"
                                            ),
                                            animatedVisibilityScope = animatedContentScope
                                        )
                                        .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
                                        .size(64.dp)
                                        .clip(RoundedCornerShape(5)),
                                model =
                                    coil.request.ImageRequest.Builder(LocalContext.current)
                                        .data(item.productPhoto)
                                        .crossfade(true)
                                        .placeholderMemoryCacheKey("product-${item.productId}")
                                        .memoryCacheKey("product-${item.productId}")
                                        .build(),
                                contentDescription = stringResource(id = R.string.item_image),
                                contentScale = ContentScale.Crop
                            )

                            Column(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .weight(1F, true)
                            ) {
                                Text(
                                    modifier =
                                        Modifier
                                            .padding(start = 16.dp, top = 16.dp, end = 16.dp),
                                    text = item.customerName,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                                    text =
                                        stringResource(
                                            id = R.string.product_price_text_tag,
                                            item.productName,
                                            item.salePrice.toString()
                                        ),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    modifier =
                                        Modifier
                                            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                                    text =
                                        stringResource(
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
    }
}

private object CustomersDebitMenu {
    const val DEBITS_BY_NAME = 0
    const val DEBITS_BY_PRICE = 1
    const val ALL_DEBITS = 3
}