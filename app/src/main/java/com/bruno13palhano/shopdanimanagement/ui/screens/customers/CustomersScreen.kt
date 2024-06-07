package com.bruno13palhano.shopdanimanagement.ui.screens.customers

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
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
import com.bruno13palhano.core.model.CustomerInfo
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.MoreOptionsMenu
import com.bruno13palhano.shopdanimanagement.ui.screens.common.CommonItem
import com.bruno13palhano.shopdanimanagement.ui.screens.common.DateChartEntry
import com.bruno13palhano.shopdanimanagement.ui.screens.customers.viewmodel.CustomersViewModel
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun CustomersRoute(
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    showBottomMenu: (show: Boolean) -> Unit,
    gesturesEnabled: (enabled: Boolean) -> Unit,
    onItemClick: (id: Long) -> Unit,
    onSearchClick: () -> Unit,
    onAddButtonClick: () -> Unit,
    onIconMenuClick: () -> Unit
) {
    showBottomMenu(true)
    gesturesEnabled(true)
    CustomersScreen(
        sharedTransitionScope = sharedTransitionScope,
        animatedContentScope = animatedContentScope,
        onItemClick = onItemClick,
        onSearchClick = onSearchClick,
        onAddButtonClick = onAddButtonClick,
        onIconMenuClick = onIconMenuClick
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun CustomersScreen(
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onItemClick: (id: Long) -> Unit,
    onSearchClick: () -> Unit,
    onAddButtonClick: () -> Unit,
    onIconMenuClick: () -> Unit,
    viewModel: CustomersViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) { viewModel.getAllCustomers() }

    val entries by viewModel.entry.collectAsStateWithLifecycle()
    val customerInfo by viewModel.customerInfo.collectAsStateWithLifecycle()

    val chart by remember { mutableStateOf(ChartEntryModelProducer()) }

    LaunchedEffect(key1 = entries) {
        chart.setEntries(
            entries.mapIndexed { index, (date, y) ->
                DateChartEntry(date, index.toFloat(), y)
            }
        )
    }

    val customerList by viewModel.customerList.collectAsStateWithLifecycle()
    val menuItems =
        arrayOf(
            stringResource(id = R.string.ordered_by_name_label),
            stringResource(id = R.string.ordered_by_address_label),
            stringResource(id = R.string.ordered_by_last_label)
        )

    var orderedByName by remember { mutableStateOf(false) }
    var orderedByAddress by remember { mutableStateOf(false) }
    var openCustomerBottomSheet by remember { mutableStateOf(false) }

    CustomersContent(
        customerList = customerList,
        customerInfo = customerInfo,
        openCustomerBottomSheet = openCustomerBottomSheet,
        chartEntries = chart,
        menuItems = menuItems,
        sharedTransitionScope = sharedTransitionScope,
        animatedContentScope = animatedContentScope,
        onItemClick = {
            openCustomerBottomSheet = true
            viewModel.getCustomerInfo(it)
            viewModel.getCustomerPurchases(it)
        },
        onSearchClick = onSearchClick,
        onMoreOptionsItemClick = { index ->
            when (index) {
                MoreOptions.ORDERED_BY_NAME -> {
                    viewModel.getOrderedByName(isOrderedAsc = orderedByName)
                    orderedByName = !orderedByName
                }
                MoreOptions.ORDERED_BY_ADDRESS -> {
                    viewModel.getOrderedByAddress(isOrderedAsc = orderedByAddress)
                    orderedByAddress = !orderedByAddress
                }
                else -> {
                    viewModel.getAllCustomers()
                }
            }
        },
        onEditCustomerClick = onItemClick,
        onDismissCustomerBottomSheet = { openCustomerBottomSheet = false },
        onAddButtonClick = onAddButtonClick,
        onIconMenuClick = onIconMenuClick
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun CustomersContent(
    customerList: List<CommonItem>,
    customerInfo: CustomerInfo,
    openCustomerBottomSheet: Boolean,
    chartEntries: ChartEntryModelProducer,
    menuItems: Array<String>,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onItemClick: (id: Long) -> Unit,
    onSearchClick: () -> Unit,
    onMoreOptionsItemClick: (index: Int) -> Unit,
    onEditCustomerClick: (id: Long) -> Unit,
    onDismissCustomerBottomSheet: () -> Unit,
    onAddButtonClick: () -> Unit,
    onIconMenuClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.customers_label)) },
                navigationIcon = {
                    IconButton(onClick = onIconMenuClick) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = stringResource(id = R.string.drawer_menu_label)
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
                    IconButton(onClick = { expanded = true }) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = stringResource(id = R.string.more_options_label)
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
            FloatingActionButton(onClick = onAddButtonClick) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.add_label)
                )
            }
        }
    ) {
        LazyColumn(
            modifier =
                Modifier
                    .semantics { contentDescription = "List of customers" }
                    .padding(it),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
        ) {
            items(items = customerList, key = { item -> item.id }) { item ->
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onEditCustomerClick(item.id) }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        with(sharedTransitionScope) {
                            AsyncImage(
                                modifier =
                                    Modifier
                                        .sharedElement(
                                            sharedTransitionScope.rememberSharedContentState(
                                                key = "customer-${item.description}"
                                            ),
                                            animatedVisibilityScope = animatedContentScope
                                        )
                                        .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
                                        .size(64.dp)
                                        .clip(RoundedCornerShape(5)),
                                model =
                                    ImageRequest.Builder(LocalContext.current)
                                        .data(item.photo)
                                        .crossfade(true)
                                        .placeholderMemoryCacheKey("customer-${item.description}")
                                        .memoryCacheKey("customer-${item.description}")
                                        .build(),
                                contentDescription = stringResource(id = R.string.item_image),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Column(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .weight(1F, true)
                        ) {
                            Text(
                                modifier =
                                    Modifier.padding(
                                        start = 16.dp,
                                        top = 16.dp,
                                        end = 16.dp
                                    ),
                                text = item.title,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                modifier =
                                    Modifier.padding(
                                        start = 16.dp,
                                        end = 16.dp,
                                        bottom = 16.dp
                                    ),
                                text = item.subtitle,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

private object MoreOptions {
    const val ORDERED_BY_NAME = 0
    const val ORDERED_BY_ADDRESS = 1
}