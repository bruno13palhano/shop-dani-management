package com.bruno13palhano.shopdanimanagement.ui.screens.insights

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.graphics.Color
import android.graphics.Typeface
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.MoreOptionsMenu
import com.bruno13palhano.shopdanimanagement.ui.components.rememberMarker
import com.bruno13palhano.shopdanimanagement.ui.screens.common.DateChartEntry
import com.bruno13palhano.shopdanimanagement.ui.screens.insights.viewmodel.ShoppingVsSalesViewModel
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
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
import com.patrykandpatrick.vico.core.component.shape.ShapeComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.legend.VerticalLegend
import com.patrykandpatrick.vico.core.scroll.InitialScroll

@Composable
fun ShoppingVsSalesScreen(
    navigateUp: () -> Unit,
    viewModel: ShoppingVsSalesViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.getChartByRange(7)
    }
    val chartEntry by viewModel.chartEntry.collectAsStateWithLifecycle()
    val menuOptions = arrayOf(
        stringResource(id = R.string.last_7_days_label),
        stringResource(id = R.string.last_21_days_label),
        stringResource(id = R.string.last_31_days_label)
    )
    val sevenDaysTitle = stringResource(id = R.string.last_7_days_label)
    val twentyOneDaysTitle = stringResource(id = R.string.last_21_days_label)
    val thirtyOneDaysTitle = stringResource(id = R.string.last_31_days_label)
    var chartTitle by remember { mutableStateOf(sevenDaysTitle) }

    ShoppingVsSalesContent(
        chartTitle = chartTitle,
        chartEntry = chartEntry,
        menuOptions = menuOptions,
        onMenuItemClick = { index ->
            chartTitle = when (index) {
                1 -> {
                    viewModel.getChartByRange(21)
                    twentyOneDaysTitle
                }
                2 -> {
                    viewModel.getChartByRange(31)
                    thirtyOneDaysTitle
                }
                else -> {
                    viewModel.getChartByRange(7)
                    sevenDaysTitle
                }
            }
        },
        navigateUp = navigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingVsSalesContent(
    chartTitle: String,
    chartEntry: ChartEntryModelProducer,
    menuOptions: Array<String>,
    onMenuItemClick: (index: Int) -> Unit,
    navigateUp: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.shopping_vs_sales_label)) },
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
        Column(modifier = Modifier.padding(it)) {
            ProvideChartStyle(
                chartStyle = m3ChartStyle(
                    entityColors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.tertiary
                    )
                )
            ) {
                Chart(
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                        .fillMaxSize(),
                    chart = lineChart(),
                    runInitialAnimation = true,
                    chartModelProducer = chartEntry,
                    marker = rememberMarker(),
                    legend = rememberLegend(),
                    fadingEdges = rememberFadingEdges(),
                    startAxis = startAxis(
                        titleComponent = textComponent(
                            color = MaterialTheme.colorScheme.onBackground,
                            background = shapeComponent(Shapes.pillShape, MaterialTheme.colorScheme.primaryContainer),
                            padding = dimensionsOf(horizontal = 8.dp, vertical = 2.dp),
                            margins = dimensionsOf(end = 8.dp),
                            typeface = Typeface.MONOSPACE
                        ),
                        title = stringResource(id = R.string.amount_of_items_sales_label)
                    ),
                    bottomAxis = if (chartEntry.getModel().entries.isEmpty()) {
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
                            title = chartTitle
                        )
                    },
                    chartScrollSpec = rememberChartScrollSpec(initialScroll = InitialScroll.End)
                )
            }
        }
    }
}

@Composable
private fun rememberLegend(): VerticalLegend {
    val legends = listOf(
        Pair(
            stringResource(id = R.string.sales_label),
            Color.toArgb(MaterialTheme.colorScheme.primary.value.toLong())
        ),
        Pair(
            stringResource(id = R.string.shopping_label),
            Color.toArgb(MaterialTheme.colorScheme.tertiary.value.toLong())
        )
    )
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

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun ShoppingVsSalesDynamicPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ShoppingVsSalesContent(
                chartTitle = stringResource(id = R.string.last_7_days_label),
                chartEntry = ChartEntryModelProducer(),
                menuOptions = emptyArray(),
                onMenuItemClick = {},
                navigateUp = {}
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun ShoppingVsSalesPreview() {
    ShopDaniManagementTheme(
        dynamicColor = false
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ShoppingVsSalesContent(
                chartTitle = stringResource(id = R.string.last_21_days_label),
                chartEntry = ChartEntryModelProducer(),
                menuOptions = emptyArray(),
                onMenuItemClick = {},
                navigateUp = {}
            )
        }
    }
}