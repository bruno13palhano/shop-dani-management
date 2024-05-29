package com.bruno13palhano.shopdanimanagement.ui.screens.customers

import android.graphics.Typeface
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.CustomTextField
import com.bruno13palhano.shopdanimanagement.ui.components.clickableNoEffect
import com.bruno13palhano.shopdanimanagement.ui.components.rememberMarker
import com.bruno13palhano.shopdanimanagement.ui.screens.common.DateChartEntry
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.edges.rememberFadingEdges
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.scroll.rememberChartScrollSpec
import com.patrykandpatrick.vico.compose.component.shapeComponent
import com.patrykandpatrick.vico.compose.component.textComponent
import com.patrykandpatrick.vico.compose.dimensions.dimensionsOf
import com.patrykandpatrick.vico.compose.m3.style.m3ChartStyle
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.scroll.InitialScroll

@Composable
fun CustomerInfoContent(
    name: String,
    address: String,
    photo: ByteArray,
    owingValue: String,
    purchasesValue: String,
    lastPurchaseValue: String,
    entry: ChartEntryModelProducer,
    onEditIconClick: () -> Unit,
    onOutsideClick: () -> Unit
) {
    Column(
        modifier =
            Modifier
                .padding(bottom = 48.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .clickableNoEffect { onOutsideClick() }
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(id = R.string.customer_information_label),
                style = MaterialTheme.typography.titleLarge
            )
            IconButton(
                modifier = Modifier.align(Alignment.CenterEnd),
                onClick = onEditIconClick
            ) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = stringResource(id = R.string.edit_label)
                )
            }
        }
        Row(verticalAlignment = Alignment.Bottom) {
            ElevatedCard(modifier = Modifier.padding(start = 8.dp)) {
                if (photo.isEmpty()) {
                    Image(
                        modifier =
                            Modifier
                                .size(128.dp)
                                .padding(8.dp)
                                .clip(RoundedCornerShape(8.dp)),
                        imageVector = Icons.Filled.Image,
                        contentDescription = stringResource(id = R.string.product_image_label)
                    )
                } else {
                    Image(
                        modifier =
                            Modifier
                                .size(128.dp)
                                .padding(8.dp)
                                .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop,
                        painter = rememberAsyncImagePainter(model = photo),
                        contentDescription = stringResource(id = R.string.product_image_label)
                    )
                }
            }
            Column {
                CustomTextField(
                    text = name,
                    onTextChange = {},
                    icon = Icons.Filled.Title,
                    readOnly = true,
                    label = stringResource(id = R.string.name_label),
                    placeholder = ""
                )
                CustomTextField(
                    text = address,
                    onTextChange = {},
                    icon = Icons.Filled.LocationCity,
                    readOnly = true,
                    label = stringResource(id = R.string.address_label),
                    placeholder = ""
                )
            }
        }
        ElevatedCard(modifier = Modifier.padding(8.dp)) {
            Text(
                modifier =
                    Modifier
                        .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 8.dp)
                        .fillMaxWidth(),
                text = stringResource(id = R.string.owing_tag, owingValue)
            )
            Text(
                modifier =
                    Modifier
                        .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
                        .fillMaxWidth(),
                text = stringResource(id = R.string.amount_of_all_purchases_tag, purchasesValue)
            )
            Text(
                modifier =
                    Modifier
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                        .fillMaxWidth(),
                text = stringResource(id = R.string.last_purchase_tag, lastPurchaseValue)
            )
        }
        val axisValuesFormatter =
            AxisValueFormatter<AxisPosition.Horizontal.Bottom> { value, chartValues ->
                try {
                    (
                        chartValues.chartEntryModel.entries.first()
                            .getOrNull(value.toInt()) as? DateChartEntry
                    )
                        ?.date.orEmpty()
                } catch (ignored: Exception) {
                    "0"
                }
            }
        ProvideChartStyle(
            chartStyle = m3ChartStyle(entityColors = listOf(MaterialTheme.colorScheme.tertiary))
        ) {
            Chart(
                modifier =
                    Modifier
                        .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                        .fillMaxWidth()
                        .height(264.dp),
                chart = lineChart(),
                runInitialAnimation = true,
                chartModelProducer = entry,
                marker = rememberMarker(),
                fadingEdges = rememberFadingEdges(),
                startAxis =
                    startAxis(
                        titleComponent =
                            textComponent(
                                color = MaterialTheme.colorScheme.onBackground,
                                background = shapeComponent(Shapes.pillShape, MaterialTheme.colorScheme.primaryContainer),
                                padding = dimensionsOf(horizontal = 8.dp, vertical = 2.dp),
                                margins = dimensionsOf(end = 8.dp),
                                typeface = Typeface.MONOSPACE
                            ),
                        title = stringResource(id = R.string.amount_of_purchases_label)
                    ),
                bottomAxis =
                    if (entry.getModel().entries.isEmpty()) {
                        bottomAxis()
                    } else {
                        bottomAxis(
                            guideline = null,
                            valueFormatter = axisValuesFormatter,
                            titleComponent =
                                textComponent(
                                    color = MaterialTheme.colorScheme.onBackground,
                                    background = shapeComponent(Shapes.pillShape, MaterialTheme.colorScheme.primaryContainer),
                                    padding = dimensionsOf(horizontal = 8.dp, vertical = 2.dp),
                                    margins = dimensionsOf(top = 8.dp, start = 8.dp, end = 8.dp),
                                    typeface = Typeface.MONOSPACE
                                ),
                            title = stringResource(id = R.string.last_31_days_label)
                        )
                    },
                chartScrollSpec = rememberChartScrollSpec(initialScroll = InitialScroll.End)
            )
        }
    }
}