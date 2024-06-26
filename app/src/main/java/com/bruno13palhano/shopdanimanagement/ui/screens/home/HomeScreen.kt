package com.bruno13palhano.shopdanimanagement.ui.screens.home

import android.graphics.Typeface
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import com.bruno13palhano.shopdanimanagement.ui.components.CircularProgress
import com.bruno13palhano.shopdanimanagement.ui.components.InfoItemList
import com.bruno13palhano.shopdanimanagement.ui.components.rememberMarker
import com.bruno13palhano.shopdanimanagement.ui.navigation.HomeRoutes
import com.bruno13palhano.shopdanimanagement.ui.screens.common.DateChartEntry
import com.bruno13palhano.shopdanimanagement.ui.screens.dateFormat
import com.bruno13palhano.shopdanimanagement.ui.screens.login.LoginState
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
fun HomeRoute(
    showBottomMenu: (show: Boolean) -> Unit,
    gesturesEnabled: (enabled: Boolean) -> Unit,
    onOptionsItemClick: (route: HomeRoutes) -> Unit,
    onSalesItemClick: (id: Long) -> Unit,
    onMenuClick: () -> Unit,
    onUnauthenticated: () -> Unit
) {
    gesturesEnabled(true)
    HomeScreen(
        onOptionsItemClick = onOptionsItemClick,
        onSalesItemClick = onSalesItemClick,
        onMenuClick = onMenuClick,
        onUnauthenticated = onUnauthenticated,
        showBottomMenu = showBottomMenu
    )
}

@Composable
fun HomeScreen(
    onOptionsItemClick: (route: HomeRoutes) -> Unit,
    onSalesItemClick: (id: Long) -> Unit,
    onMenuClick: () -> Unit,
    onUnauthenticated: () -> Unit,
    showBottomMenu: (show: Boolean) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.isAuthenticated()
    }

    val state by viewModel.loginState.collectAsStateWithLifecycle()

    val lastSalesEntry by viewModel.lastSales.collectAsStateWithLifecycle()
    val homeInfo by viewModel.homeInfo.collectAsStateWithLifecycle()

    val chart by remember { mutableStateOf(ChartEntryModelProducer()) }
    var showProfit by rememberSaveable { mutableStateOf(false) }
    var showContent by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = lastSalesEntry) {
        chart.setEntries(lastSalesEntry)
    }

    AnimatedVisibility(
        visible = showContent,
        enter = fadeIn(animationSpec = spring(stiffness = Spring.StiffnessVeryLow)),
        exit = fadeOut(animationSpec = spring(stiffness = Spring.StiffnessVeryLow))
    ) {
        HomeContent(
            homeInfo = homeInfo,
            lastSalesEntry = chart,
            onOptionsItemClick = onOptionsItemClick,
            showProfit = showProfit,
            onSalesItemClick = onSalesItemClick,
            onShowProfitChange = { showProfit = it },
            onMenuClick = onMenuClick
        )
    }

    when (state) {
        LoginState.SignedIn -> {
            showBottomMenu(true)
            showContent = true
        }
        LoginState.InProgress -> {
            showContent = false
            CircularProgress()
        }
        LoginState.SignedOut -> {
            showContent = false
            onUnauthenticated()
            showBottomMenu(false)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    homeInfo: HomeViewModel.HomeInfo,
    lastSalesEntry: ChartEntryModelProducer,
    showProfit: Boolean,
    onOptionsItemClick: (route: HomeRoutes) -> Unit,
    onSalesItemClick: (id: Long) -> Unit,
    onShowProfitChange: (show: Boolean) -> Unit,
    onMenuClick: () -> Unit
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
        val infoSaleList =
            listOf(
                Pair(homeInfo.biggestSale, stringResource(id = R.string.biggest_sale_label)),
                Pair(homeInfo.smallestSale, stringResource(id = R.string.smallest_sale_label)),
                Pair(homeInfo.lastSale, stringResource(id = R.string.last_sale_label))
            )
        val options =
            listOf(
                HomeInnerScreen.Sales,
                HomeInnerScreen.Stock,
                HomeInnerScreen.Orders,
                HomeInnerScreen.Amazon,
                HomeInnerScreen.Deliveries,
                HomeInnerScreen.Catalog
            )
        val axisValuesFormatter =
            AxisValueFormatter<AxisPosition.Horizontal.Bottom> { value, chartValues ->
                try {
                    (
                        chartValues.chartEntryModel.entries.first()
                            .getOrNull(value.toInt()) as? DateChartEntry
                    )
                        ?.date
                        .orEmpty()
                } catch (ignored: Exception) {
                    "0"
                }
            }

        val dots = stringResource(id = R.string.dots_label)

        LazyColumn(modifier = Modifier.padding(it)) {
            item {
                Row(
                    modifier =
                        Modifier
                            .padding(top = 16.dp, bottom = 8.dp)
                            .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .weight(1F, true)
                    ) {
                        Text(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp),
                            text = stringResource(id = R.string.profit_label),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                            text =
                                if (showProfit) {
                                    stringResource(id = R.string.value_tag, homeInfo.profit)
                                } else {
                                    stringResource(id = R.string.value_text_tag, dots)
                                },
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            text =
                                if (showProfit) {
                                    stringResource(id = R.string.total_sales_value_tag, homeInfo.sales)
                                } else {
                                    stringResource(id = R.string.total_sales_text_value_tag, dots)
                                },
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    IconButton(
                        modifier = Modifier.padding(end = 8.dp),
                        onClick = { onShowProfitChange(!showProfit) }
                    ) {
                        if (showProfit) {
                            Icon(
                                imageVector = Icons.Filled.Visibility,
                                contentDescription = stringResource(id = R.string.show_label)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Filled.VisibilityOff,
                                contentDescription = stringResource(id = R.string.hide_label)
                            )
                        }
                    }
                }

                LazyRow(
                    modifier = Modifier.semantics { contentDescription = "Options" },
                    contentPadding = PaddingValues(vertical = 16.dp, horizontal = 4.dp)
                ) {
                    items(items = options, key = { option -> option.resourceId }) { option ->
                        val description = stringResource(id = option.resourceId)

                        CircularItemList(
                            modifier =
                                Modifier
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
                    modifier =
                        Modifier
                            .semantics { contentDescription = "List of sales" }
                            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                ) {
                    infoSaleList.forEach { info ->
                        if (info.first.value == 0F) {
                            InfoItemList(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                contentPadding = PaddingValues(vertical = 12.dp),
                                title = info.second,
                                subtitle = stringResource(id = R.string.empty_label),
                                description = "",
                                onEditClick = {}
                            )
                            HorizontalDivider()
                        } else {
                            InfoItemList(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                contentPadding = PaddingValues(vertical = 12.dp),
                                title = info.second,
                                subtitle =
                                    if (showProfit) {
                                        stringResource(
                                            id = R.string.product_price_tag,
                                            info.first.item,
                                            info.first.value
                                        )
                                    } else {
                                        stringResource(
                                            id = R.string.product_price_text_tag,
                                            info.first.item,
                                            dots
                                        )
                                    },
                                description =
                                    pluralStringResource(
                                        id = R.plurals.description_label,
                                        count = info.first.quantity,
                                        info.first.customer,
                                        info.first.quantity,
                                        dateFormat.format(info.first.date)
                                    ),
                                onEditClick = {
                                    onSalesItemClick(info.first.id)
                                }
                            )
                            HorizontalDivider()
                        }
                    }
                }

                ElevatedCard(
                    modifier = Modifier.padding(16.dp)
                ) {
                    ProvideChartStyle(
                        chartStyle =
                            m3ChartStyle(
                                entityColors = listOf(MaterialTheme.colorScheme.tertiary)
                            )
                    ) {
                        val marker = rememberMarker()
                        Chart(
                            modifier =
                                Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth()
                                    .height(264.dp),
                            chart = lineChart(),
                            runInitialAnimation = true,
                            chartModelProducer = lastSalesEntry,
                            marker = marker,
                            fadingEdges = rememberFadingEdges(),
                            startAxis =
                                startAxis(
                                    titleComponent =
                                        textComponent(
                                            color = MaterialTheme.colorScheme.onBackground,
                                            background =
                                                shapeComponent(
                                                    Shapes.pillShape,
                                                    MaterialTheme.colorScheme.primaryContainer
                                                ),
                                            padding = dimensionsOf(horizontal = 8.dp, vertical = 2.dp),
                                            margins = dimensionsOf(end = 8.dp),
                                            typeface = Typeface.MONOSPACE
                                        ),
                                    title = stringResource(id = R.string.amount_of_sales_label)
                                ),
                            bottomAxis =
                                if (lastSalesEntry.getModel().entries.isEmpty()) {
                                    bottomAxis()
                                } else {
                                    bottomAxis(
                                        guideline = null,
                                        valueFormatter = axisValuesFormatter,
                                        titleComponent =
                                            textComponent(
                                                color = MaterialTheme.colorScheme.onBackground,
                                                background =
                                                    shapeComponent(
                                                        Shapes.pillShape,
                                                        MaterialTheme.colorScheme.primaryContainer
                                                    ),
                                                padding = dimensionsOf(horizontal = 8.dp, vertical = 2.dp),
                                                margins =
                                                    dimensionsOf(
                                                        top = 8.dp,
                                                        start = 8.dp,
                                                        end = 8.dp
                                                    ),
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
}

sealed class HomeInnerScreen<T : HomeRoutes>(
    val route: T,
    val icon: ImageVector,
    @StringRes val resourceId: Int
) {
    data object Stock : HomeInnerScreen<HomeRoutes>(
        route = HomeRoutes.Stock,
        icon = Icons.AutoMirrored.Filled.List,
        resourceId = R.string.stock_label
    )

    data object Sales : HomeInnerScreen<HomeRoutes>(
        route = HomeRoutes.Sales,
        icon = Icons.Filled.PointOfSale,
        resourceId = R.string.sales_label
    )

    data object Orders : HomeInnerScreen<HomeRoutes>(
        route = HomeRoutes.Orders,
        icon = Icons.Filled.Checklist,
        resourceId = R.string.orders_label
    )

    data object Amazon : HomeInnerScreen<HomeRoutes>(
        route = HomeRoutes.Amazon,
        icon = Icons.Filled.ShoppingBag,
        resourceId = R.string.amazon_label
    )

    data object Deliveries : HomeInnerScreen<HomeRoutes>(
        route = HomeRoutes.Deliveries,
        icon = Icons.Filled.LocalShipping,
        resourceId = R.string.deliveries_label
    )

    data object Catalog : HomeInnerScreen<HomeRoutes>(
        route = HomeRoutes.Catalog,
        icon = Icons.AutoMirrored.Filled.ListAlt,
        resourceId = R.string.catalog_label
    )
}