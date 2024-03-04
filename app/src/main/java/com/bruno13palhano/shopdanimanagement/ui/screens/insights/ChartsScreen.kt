package com.bruno13palhano.shopdanimanagement.ui.screens.insights

import android.graphics.Color
import android.graphics.Typeface
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.MoreOptionsMenu
import com.bruno13palhano.shopdanimanagement.ui.components.rememberMarker
import com.bruno13palhano.shopdanimanagement.ui.screens.common.DateChartEntry
import com.bruno13palhano.shopdanimanagement.ui.screens.insights.viewmodel.ChartsViewModel
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.chart.edges.rememberFadingEdges
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.scroll.rememberChartScrollSpec
import com.patrykandpatrick.vico.compose.component.shapeComponent
import com.patrykandpatrick.vico.compose.component.textComponent
import com.patrykandpatrick.vico.compose.dimensions.dimensionsOf
import com.patrykandpatrick.vico.compose.legend.verticalLegend
import com.patrykandpatrick.vico.compose.legend.verticalLegendItem
import com.patrykandpatrick.vico.compose.m3.style.m3ChartStyle
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.compose.style.currentChartStyle
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.chart.composed.plus
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.component.shape.ShapeComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.entry.ChartEntryModel
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.composed.ComposedChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.composed.plus
import com.patrykandpatrick.vico.core.legend.VerticalLegend
import com.patrykandpatrick.vico.core.scroll.InitialScroll

@Composable
fun ChartsRoute(
    showBottomMenu: (show: Boolean) -> Unit,
    gesturesEnabled: (enabled: Boolean) -> Unit,
    navigateUp: () -> Unit
) {
    showBottomMenu(false)
    gesturesEnabled(true)
    ChartsScreen(navigateUp = navigateUp)
}

@Composable
fun ChartsScreen(
    navigateUp: () -> Unit,
    viewModel: ChartsViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.setItemsDaysRange(7)
    }
    val lastSalesEntry by viewModel.lastSalesEntries.collectAsStateWithLifecycle()

    val menuOptions = arrayOf(
        stringResource(id = R.string.last_7_days_label),
        stringResource(id = R.string.last_21_days_label),
        stringResource(id = R.string.last_31_days_label)
    )

    var chartTitle by remember { mutableStateOf(menuOptions[0]) }
    val lastSalesChart by remember { mutableStateOf(ChartEntryModelProducer()) }
    val stockChart by remember { mutableStateOf(ChartEntryModelProducer()) }
    val ordersChart by remember { mutableStateOf(ChartEntryModelProducer()) }
    val stockOrdersChart by remember {
        mutableStateOf(stockChart + ordersChart)
    }

    LaunchedEffect(key1 = lastSalesEntry) {
        lastSalesChart.setEntries(
            lastSalesEntry.lastSalesEntries.mapIndexed { index, (date, y) ->
                DateChartEntry(date, index.toFloat(), y)
            }
        )
        stockChart.setEntries(
            lastSalesEntry.stockEntries.mapIndexed { index, (date, y) ->
                DateChartEntry(date, index.toFloat(), y)
            }
        )
        ordersChart.setEntries(
            lastSalesEntry.orderEntries.mapIndexed { index, (date, y) ->
                DateChartEntry(date, index.toFloat(), y)
            }
        )
    }

    ChartsContent(
        bottomAxisTitle = chartTitle,
        lastSalesEntry = lastSalesChart,
        stockVsOrderEntry = stockOrdersChart,
        menuOptions = menuOptions,
        onMenuItemClick = { index ->
            chartTitle = when (index) {
                ChartsMenu.DAYS_7 -> {
                    viewModel.setItemsDaysRange(7)
                    menuOptions[0]
                }

                ChartsMenu.DAYS_21 -> {
                    viewModel.setItemsDaysRange(21)
                    menuOptions[1]
                }

                ChartsMenu.DAYS_31 -> {
                    viewModel.setItemsDaysRange(31)
                    menuOptions[2]
                }

                else -> {
                    viewModel.setItemsDaysRange(7)
                    menuOptions[0]
                }
            }
        },
        navigateUp = navigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChartsContent(
    bottomAxisTitle: String,
    lastSalesEntry: ChartEntryModelProducer,
    stockVsOrderEntry: ComposedChartEntryModelProducer<ChartEntryModel>,
    menuOptions: Array<String>,
    onMenuItemClick: (index: Int) -> Unit,
    navigateUp: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.charts_label)) },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.up_button_label)
                        )
                    }
                },
                actions = {
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
                                    items = menuOptions,
                                    expanded = expanded,
                                    onDismissRequest = { expanded = it },
                                    onClick = onMenuItemClick
                                )
                            }
                        }
                    }
                }
            )
        }
    ) {
        val axisValuesFormatter = AxisValueFormatter<AxisPosition.Horizontal.Bottom> { value, chartValues ->
            try {
                (chartValues.chartEntryModel.entries.first()
                    .getOrNull(value.toInt()) as? DateChartEntry)
                    ?.date.orEmpty()
            } catch (ignored: Exception) { "0" }
        }

        Column(
            modifier = Modifier
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {
            ProvideChartStyle(
                chartStyle = m3ChartStyle(entityColors = listOf(MaterialTheme.colorScheme.tertiary))
            ) {
                Chart(
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                        .fillMaxWidth()
                        .height(264.dp),
                    chart = lineChart(),
                    runInitialAnimation = true,
                    chartModelProducer = lastSalesEntry,
                    marker = rememberMarker(),
                    legend = rememberLegend(
                        listOf(
                            Pair(
                                stringResource(id = R.string.sales_label),
                                Color.toArgb(MaterialTheme.colorScheme.primary.value.toLong())
                            )
                        )
                    ),
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
                            title = bottomAxisTitle
                        )
                    },
                    chartScrollSpec = rememberChartScrollSpec(initialScroll = InitialScroll.End)
                )
            }
            ProvideChartStyle(
                chartStyle = m3ChartStyle(entityColors = listOf(MaterialTheme.colorScheme.primary))
            ) {
                val columnChart = columnChart()
                val lineChart = lineChart(lines = listOf(
                    LineChart.LineSpec(
                        lineColor = Color.toArgb(MaterialTheme.colorScheme.tertiary.value.toLong())
                    )
                ))
                val composedChart = remember(columnChart, lineChart) { columnChart + lineChart }
                Chart(
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                        .fillMaxWidth()
                        .height(280.dp),
                    chart = composedChart,
                    runInitialAnimation = true,
                    chartModelProducer = stockVsOrderEntry,
                    marker = rememberMarker(),
                    legend = rememberLegend(
                        listOf(
                            Pair(
                                stringResource(id = R.string.stock_label),
                                Color.toArgb(MaterialTheme.colorScheme.primary.value.toLong())
                            ),
                            Pair(
                                stringResource(id = R.string.orders_label),
                                Color.toArgb(MaterialTheme.colorScheme.tertiary.value.toLong())
                            )
                        )
                    ),
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
                    bottomAxis = if (stockVsOrderEntry.getModel().entries.isEmpty()) {
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
                            title = bottomAxisTitle
                        )
                    },
                    chartScrollSpec = rememberChartScrollSpec(initialScroll = InitialScroll.End)
                )
            }
        }
    }
}

@Composable
private fun rememberLegend(legends: List<Pair<String, Int>>): VerticalLegend {
    return verticalLegend(
        items = legends.map { legend ->
            verticalLegendItem(
                icon = ShapeComponent(Shapes.pillShape, legend.second),
                label = textComponent(
                    color = currentChartStyle.axis.axisLabelColor,
                    textSize = 12.sp,
                    typeface = Typeface.MONOSPACE
                ),
                labelText = legend.first
            )
        },
        iconSize = 8.dp,
        iconPadding = 10.dp,
        spacing = 4.dp
    )
}

private object ChartsMenu {
    const val DAYS_7 = 0
    const val DAYS_21 = 1
    const val DAYS_31 = 2
}