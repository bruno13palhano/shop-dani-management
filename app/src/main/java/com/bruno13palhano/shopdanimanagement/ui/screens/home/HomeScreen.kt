package com.bruno13palhano.shopdanimanagement.ui.screens.home

import android.graphics.Typeface
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.CircularItemList
import com.bruno13palhano.shopdanimanagement.ui.components.InfoItemList
import com.bruno13palhano.shopdanimanagement.ui.components.rememberMarker
import com.bruno13palhano.shopdanimanagement.ui.navigation.HomeDestinations
import com.bruno13palhano.shopdanimanagement.ui.screens.common.DateChartEntry
import com.bruno13palhano.shopdanimanagement.ui.screens.dateFormat
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.edges.rememberFadingEdges
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.component.shapeComponent
import com.patrykandpatrick.vico.compose.component.textComponent
import com.patrykandpatrick.vico.compose.dimensions.dimensionsOf
import com.patrykandpatrick.vico.compose.m3.style.m3ChartStyle
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer

@Composable
fun HomeScreen(
    onOptionsItemClick: (route: String) -> Unit,
    onSalesItemClick: (id: Long, isOrderedByCustomer: Boolean) -> Unit,
    onMenuClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val lastSalesEntry by viewModel.lastSales.collectAsStateWithLifecycle()
    val homeInfo by viewModel.homeInfo.collectAsStateWithLifecycle()

    val chart by remember { mutableStateOf(ChartEntryModelProducer()) }

    LaunchedEffect(key1 = lastSalesEntry) {
        chart.setEntries(
            lastSalesEntry.mapIndexed { index, (date, y) ->
                DateChartEntry(date, index.toFloat(), y)
            }
        )
    }

    HomeContent(
        homeInfo = homeInfo,
        lastSalesEntry = chart,
        onOptionsItemClick = onOptionsItemClick,
        onSalesItemClick = onSalesItemClick,
        onMenuClick = onMenuClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    homeInfo: HomeViewModel.HomeInfo,
    lastSalesEntry: ChartEntryModelProducer,
    onOptionsItemClick: (route: String) -> Unit,
    onSalesItemClick: (id: Long, isOrderedByCustomer: Boolean) -> Unit,
    onMenuClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.semantics { contentDescription = "Home screen" },
                title = { Text(text = stringResource(id = R.string.home_label)) },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = stringResource(id = R.string.drawer_menu_label)
                        )
                    }
                }
            )
        }
    ) {
        val infoSaleList = listOf(
            Pair(homeInfo.biggestSale, stringResource(id = R.string.biggest_sale_label)),
            Pair(homeInfo.smallestSale, stringResource(id = R.string.smallest_sale_label)),
            Pair(homeInfo.lastSale, stringResource(id = R.string.last_sale_label))
        )
        val options = listOf(
            HomeInnerScreen.Sales,
            HomeInnerScreen.Stock,
            HomeInnerScreen.Orders,
            HomeInnerScreen.Deliveries,
            HomeInnerScreen.Catalog
        )
        val axisValuesFormatter = AxisValueFormatter<AxisPosition.Horizontal.Bottom> { value, chartValues ->
            try {
                (chartValues.chartEntryModel.entries.first()
                    .getOrNull(value.toInt()) as? DateChartEntry)
                    ?.date
                    .orEmpty()
            } catch (ignored: Exception) { "0" }
        }

        Column(modifier = Modifier
            .padding(it)
            .verticalScroll(rememberScrollState())
        ) {
            Text(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 2.dp),
                text = stringResource(id = R.string.profit_label),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 2.dp, bottom = 2.dp),
                text = stringResource(id = R.string.value_tag, homeInfo.profit),
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 2.dp, bottom = 8.dp),
                text = stringResource(id = R.string.total_sales_value_tag, homeInfo.sales),
                style = MaterialTheme.typography.bodyMedium
            )

            LazyRow(
                modifier = Modifier.semantics { contentDescription = "Options" },
                contentPadding = PaddingValues(vertical = 16.dp, horizontal = 4.dp)
            ) {
                items(items = options, key = { option -> option.route } ) { option ->
                    val description = stringResource(id = option.resourceId)
                    CircularItemList(
                        modifier = Modifier
                            .semantics {
                                contentDescription = description
                            }
                            .padding(horizontal = 4.dp),
                        title = stringResource(id = option.resourceId),
                        icon = option.icon,
                        onClick = { onOptionsItemClick(option.route) }
                    )
                }
            }

            ElevatedCard(
                modifier = Modifier
                    .semantics { contentDescription = "List of sales" }
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            ) {
                infoSaleList.forEach { info ->
                    InfoItemList(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        contentPadding = PaddingValues(vertical = 12.dp),
                        title = info.second,
                        subtitle = stringResource(
                            id = R.string.product_price_tag,
                            info.first.item,
                            info.first.value
                        ),
                        description = pluralStringResource(
                            id = R.plurals.description_label,
                            count = info.first.quantity,
                            info.first.customer,
                            info.first.quantity,
                            dateFormat.format(info.first.date)
                        ),
                        onEditClick = { onSalesItemClick(info.first.id, info.first.isOrderedByCustomer) }
                    )
                    HorizontalDivider()
                }
            }

            ElevatedCard(
                modifier = Modifier.padding(16.dp)
            ) {
                ProvideChartStyle(
                    chartStyle = m3ChartStyle(
                        entityColors = listOf(MaterialTheme.colorScheme.tertiary)
                    )
                ) {
                    val marker = rememberMarker()
                    Chart(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                            .height(264.dp),
                        chart = lineChart(),
                        runInitialAnimation = true,
                        chartModelProducer = lastSalesEntry,
                        marker = marker,
                        fadingEdges = rememberFadingEdges(),
                        startAxis = startAxis(
                            titleComponent = textComponent(
                                color = MaterialTheme.colorScheme.onBackground,
                                background = shapeComponent(Shapes.pillShape, MaterialTheme.colorScheme.primaryContainer),
                                padding = dimensionsOf(horizontal = 8.dp, vertical = 2.dp),
                                margins = dimensionsOf(end = 8.dp),
                                typeface = Typeface.MONOSPACE
                            ),
                            title = stringResource(id = R.string.amount_of_sales_label)
                        ),
                        bottomAxis = if (lastSalesEntry.getModel().entries.isEmpty()) {
                            bottomAxis()
                        } else {
                            bottomAxis(
                                guideline = null,
                                valueFormatter = axisValuesFormatter,
                                titleComponent = textComponent(
                                    color = MaterialTheme.colorScheme.onBackground,
                                    background = shapeComponent(Shapes.pillShape, MaterialTheme.colorScheme.primaryContainer),
                                    padding = dimensionsOf(horizontal = 8.dp, vertical = 2.dp),
                                    margins = dimensionsOf(top = 8.dp, start = 8.dp, end = 8.dp),
                                    typeface = Typeface.MONOSPACE
                                ),
                                title = stringResource(id = R.string.last_sales_label)
                            )
                        }
                    )
                }
            }
        }
    }
}

sealed class HomeInnerScreen(val route: String, val icon: ImageVector, @StringRes val resourceId: Int) {
    object Stock: HomeInnerScreen(HomeDestinations.HOME_STOCK_ROUTE, Icons.Filled.List, R.string.stock_label)
    object Sales: HomeInnerScreen(HomeDestinations.HOME_SALES_ROUTE, Icons.Filled.PointOfSale, R.string.sales_label)
    object Orders: HomeInnerScreen(HomeDestinations.HOME_ORDERS_ROUTE, Icons.Filled.Checklist, R.string.orders_label)
    object Deliveries: HomeInnerScreen(HomeDestinations.HOME_DELIVERIES_ROUTE, Icons.Filled.LocalShipping, R.string.deliveries_label)
    object Catalog: HomeInnerScreen(HomeDestinations.HOME_CATALOG_ROUTE, Icons.Filled.ListAlt, R.string.catalog_label)
}