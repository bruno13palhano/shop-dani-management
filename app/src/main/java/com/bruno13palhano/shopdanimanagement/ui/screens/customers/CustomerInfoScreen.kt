package com.bruno13palhano.shopdanimanagement.ui.screens.customers

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.graphics.Typeface
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.clearFocusOnKeyboardDismiss
import com.bruno13palhano.shopdanimanagement.ui.components.rememberMarker
import com.bruno13palhano.shopdanimanagement.ui.screens.common.DateChartEntry
import com.bruno13palhano.shopdanimanagement.ui.screens.customers.viewmodel.CustomerInfoViewModel
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
import com.patrykandpatrick.vico.compose.m3.style.m3ChartStyle
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.scroll.InitialScroll

@Composable
fun CustomerInfoScreen(
    customerId: Long,
    onEditIconClick: () -> Unit,
    navigateUp: () -> Unit,
    viewModel: CustomerInfoViewModel = hiltViewModel()
) {
    val entry by viewModel.entry.collectAsStateWithLifecycle()
    val customerInfo by viewModel.customerInfo.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.getCustomerInfo(customerId)
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.getCustomerPurchases(customerId)
    }

    CustomerInfoContent(
        name = customerInfo.name,
        address = customerInfo.address,
        photo = customerInfo.photo,
        owingValue = customerInfo.owingValue,
        purchasesValue = customerInfo.purchasesValue,
        lastPurchaseValue = customerInfo.lastPurchaseValue,
        entry = entry,
        onEditIconClick = onEditIconClick,
        navigateUp = navigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerInfoContent(
    name: String,
    address: String,
    photo: String,
    owingValue: String,
    purchasesValue: String,
    lastPurchaseValue: String,
    entry: ChartEntryModelProducer,
    onEditIconClick: () -> Unit,
    navigateUp: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.customer_label)) },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.up_button_label)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onEditIconClick) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = stringResource(id = R.string.edit_label)
                        )
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
               verticalAlignment = Alignment.Bottom
            ) {
                ElevatedCard(
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    if (photo.isEmpty()) {
                        Image(
                            modifier = Modifier
                                .size(128.dp)
                                .padding(8.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            imageVector = Icons.Filled.Image,
                            contentDescription = stringResource(id = R.string.product_image_label)
                        )
                    } else {
                        Image(
                            modifier = Modifier
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
                    OutlinedTextField(
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                            .fillMaxWidth()
                            .clearFocusOnKeyboardDismiss(),
                        value = name,
                        onValueChange = {},
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Title,
                                contentDescription = stringResource(id = R.string.name_label)
                            )
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            defaultKeyboardAction(ImeAction.Done)
                        }),
                        singleLine = true,
                        enabled = false,
                        label = {
                            Text(
                                text = stringResource(id = R.string.name_label),
                                fontStyle = FontStyle.Italic
                            )
                        },
                        placeholder = {
                            Text(
                                text = stringResource(id = R.string.enter_name_label),
                                fontStyle = FontStyle.Italic
                            )
                        },
                    )
                    OutlinedTextField(
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                            .fillMaxWidth()
                            .clearFocusOnKeyboardDismiss(),
                        value = address,
                        onValueChange = {},
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.LocationCity,
                                contentDescription = stringResource(id = R.string.address_label)
                            )
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            defaultKeyboardAction(ImeAction.Done)
                        }),
                        singleLine = true,
                        enabled = false,
                        label = {
                            Text(
                                text = stringResource(id = R.string.address_label),
                                fontStyle = FontStyle.Italic
                            )
                        },
                        placeholder = {
                            Text(
                                text = stringResource(id = R.string.enter_address_label),
                                fontStyle = FontStyle.Italic
                            )
                        },
                    )
                }
            }
            ElevatedCard(modifier = Modifier.padding(8.dp)) {
                Text(
                    modifier = Modifier
                        .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 8.dp)
                        .fillMaxWidth(),
                    text = stringResource(id = R.string.owing_tag, owingValue)
                )
                Text(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
                        .fillMaxWidth(),
                    text = stringResource(id = R.string.amount_of_all_purchases_tag, purchasesValue)
                )
                Text(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                        .fillMaxWidth(),
                    text = stringResource(id = R.string.last_purchase_tag, lastPurchaseValue)
                )
            }
            val axisValuesFormatter = AxisValueFormatter<AxisPosition.Horizontal.Bottom> { value, chartValues ->
                try {
                    (chartValues.chartEntryModel.entries.first()
                        .getOrNull(value.toInt()) as? DateChartEntry)
                        ?.date.orEmpty()
                } catch (ignored: Exception) { "0" }
            }
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
                    chartModelProducer = entry,
                    marker = rememberMarker(),
                    fadingEdges = rememberFadingEdges(),
                    startAxis = startAxis(
                        titleComponent = textComponent(
                            color = MaterialTheme.colorScheme.onBackground,
                            background = shapeComponent(Shapes.pillShape, MaterialTheme.colorScheme.primaryContainer),
                            padding = dimensionsOf(horizontal = 8.dp, vertical = 2.dp),
                            margins = dimensionsOf(end = 8.dp),
                            typeface = Typeface.MONOSPACE
                        ),
                        title = stringResource(id = R.string.amount_of_purchases_label)
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
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun CustomerInfoDynamicPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            CustomerInfoContent(
                name = "",
                address = "",
                photo = "",
                owingValue = "120.99",
                purchasesValue = "1590.99",
                lastPurchaseValue = "77.99",
                entry = ChartEntryModelProducer(),
                onEditIconClick = {},
                navigateUp = {}
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun CustomerInfoPreview() {
    ShopDaniManagementTheme(
        dynamicColor = false
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            CustomerInfoContent(
                name = "",
                address = "",
                photo = "",
                owingValue = "120.99",
                purchasesValue = "1590.99",
                lastPurchaseValue = "77.99",
                entry = ChartEntryModelProducer(),
                onEditIconClick = {},
                navigateUp = {}
            )
        }
    }
}