package com.bruno13palhano.shopdanimanagement.ui.screens.financial

import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Typeface
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.rememberMarker
import com.bruno13palhano.shopdanimanagement.ui.screens.common.DateChartEntry
import com.bruno13palhano.shopdanimanagement.ui.screens.financial.viewmodel.FinancialInfoViewModel
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.chart.edges.rememberFadingEdges
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
import com.patrykandpatrick.vico.core.component.shape.ShapeComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.legend.VerticalLegend
import com.patrykandpatrick.vico.core.scroll.InitialScroll

@Composable
fun FinancialInfoScreen(
    navigateUp: () -> Unit,
    viewModel: FinancialInfoViewModel = hiltViewModel()
) {
    val financialInfo by viewModel.financial.collectAsStateWithLifecycle()
    val entry by viewModel.entry.collectAsStateWithLifecycle()

    FinancialInfoContent(
        allSales = financialInfo.allSales,
        stockSales = financialInfo.stockSales,
        ordersSales = financialInfo.ordersSales,
        profit = financialInfo.profit,
        shopping = financialInfo.shopping,
        entry = entry,
        navigateUp = navigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinancialInfoContent(
    allSales: Float,
    stockSales: Float,
    ordersSales: Float,
    profit: Float,
    shopping: Float,
    entry: ChartEntryModelProducer,
    navigateUp: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.financial_info_label)) },
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
        val orientation = LocalConfiguration.current.orientation

        Column(
            modifier = if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                Modifier.padding(it)
            } else {
                Modifier
                    .padding(it)
                    .verticalScroll(rememberScrollState())
            }
        ) {
            ElevatedCard(modifier = Modifier.padding(8.dp)) {
                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    text = stringResource(id = R.string.all_sales_tag, allSales)
                )
                Text(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                        .fillMaxWidth(),
                    text = stringResource(id = R.string.stock_sales_tag, stockSales)
                )
                Text(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                        .fillMaxWidth(),
                    text = stringResource(id = R.string.orders_sales_tag, ordersSales)
                )
                Text(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                        .fillMaxWidth(),
                    text = stringResource(id = R.string.profit_tag, profit)
                )
                Text(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                        .fillMaxWidth(),
                    text = stringResource(id = R.string.shopping_tag, shopping)
                )
            }
            val axisValuesFormatter = AxisValueFormatter<AxisPosition.Horizontal.Bottom> { value, chartValues ->
                try {
                    (chartValues.chartEntryModel.entries.first()
                        .getOrNull(value.toInt()) as? DateChartEntry)
                        ?.date.orEmpty()
                } catch (ignored: Exception) { "0" }
            }
            ProvideChartStyle(chartStyle =
                m3ChartStyle(
                    entityColors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.secondary,
                        MaterialTheme.colorScheme.tertiary,
                        MaterialTheme.colorScheme.outline,
                        MaterialTheme.colorScheme.error
                    )
                )
            ) {
                Chart(
                    modifier = if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                        Modifier
                            .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                            .fillMaxWidth()
                            .fillMaxHeight()
                    } else {
                        Modifier
                            .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                            .fillMaxWidth()
                            .height(304.dp)
                    },
                    chart = columnChart(),
                    runInitialAnimation = true,
                    chartModelProducer = entry,
                    marker = rememberMarker(),
                    legend = rememberLegend(
                        listOf(
                            Pair(
                                stringResource(id = R.string.sales_label),
                                Color.toArgb(MaterialTheme.colorScheme.primary.value.toLong())
                            ),
                            Pair(
                                stringResource(id = R.string.stock_label),
                                Color.toArgb(MaterialTheme.colorScheme.secondary.value.toLong())
                            ),
                            Pair(
                                stringResource(id = R.string.orders_label),
                                Color.toArgb(MaterialTheme.colorScheme.tertiary.value.toLong())
                            ),
                            Pair(
                                stringResource(id = R.string.profit_label),
                                Color.toArgb(MaterialTheme.colorScheme.outline.value.toLong())
                            ),
                            Pair(
                                stringResource(id = R.string.shopping_label),
                                Color.toArgb(MaterialTheme.colorScheme.error.value.toLong())
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
                        title = stringResource(id = R.string.amount_of_money_label)
                    ),
                    bottomAxis = if (entry.getModel().entries.isEmpty()) {
                        bottomAxis()
                    } else {
                        bottomAxis(
                            guideline = null,
                            valueFormatter = axisValuesFormatter,
                            titleComponent = textComponent(
                                color = MaterialTheme.colorScheme.onBackground,
                                background = shapeComponent(Shapes.pillShape, MaterialTheme.colorScheme.primaryContainer),
                                padding = dimensionsOf(horizontal = 8.dp, vertical = 2.dp),
                                margins = dimensionsOf(top = 8.dp, start = 8.dp),
                                typeface = Typeface.MONOSPACE
                            )
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