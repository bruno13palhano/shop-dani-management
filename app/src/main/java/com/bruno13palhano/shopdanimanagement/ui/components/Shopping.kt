package com.bruno13palhano.shopdanimanagement.ui.components

import android.icu.text.DecimalFormat
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.bruno13palhano.shopdanimanagement.R
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingContent(
    screenTitle: String,
    snackbarHostState: SnackbarHostState,
    name: String,
    purchasePrice: String,
    quantity: String,
    isPaid: Boolean,
    photo: String,
    date: String,
    onNameChange: (name: String) -> Unit,
    onPurchasePriceChange: (purchasePrice: String) -> Unit,
    onQuantityChange: (quantity: String) -> Unit,
    onIsPaidChange: (isPaid: Boolean) -> Unit,
    onDateClick: () -> Unit,
    onOutsideClick: () -> Unit,
    onDoneClick: () -> Unit,
    navigateUp: () -> Unit
) {
    Scaffold(
        modifier = Modifier.clickableNoEffect { onOutsideClick() },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(text = screenTitle) },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.up_button_label)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onDoneClick) {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = stringResource(id = R.string.done_label)
                )
            }
        }
    ) {
        val decimalFormat = DecimalFormat.getInstance(Locale.getDefault()) as DecimalFormat
        val decimalSeparator = decimalFormat.decimalFormatSymbols.decimalSeparator
        val pattern = remember { Regex("^\\d*\\$decimalSeparator?\\d*\$") }
        val patternInt = remember { Regex("^\\d*") }

        Column(
            modifier = Modifier
                .padding(it)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ElevatedCard(
                modifier = Modifier
                    .padding(start = 16.dp)
            ) {
                if (photo.isEmpty()) {
                    Image(
                        modifier = Modifier
                            .size(200.dp)
                            .padding(8.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        imageVector = Icons.Filled.Image,
                        contentDescription = stringResource(id = R.string.product_image_label),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        modifier = Modifier
                            .size(200.dp)
                            .padding(8.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        painter = rememberAsyncImagePainter(model = photo),
                        contentDescription = stringResource(id = R.string.product_image_label),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            OutlinedTextField(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 2.dp)
                    .fillMaxWidth()
                    .clearFocusOnKeyboardDismiss(),
                value = name,
                onValueChange = onNameChange,
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
                value = purchasePrice,
                onValueChange = { purchasePriceValue ->
                    if (purchasePriceValue.isEmpty() || purchasePriceValue.matches(pattern)) {
                        onPurchasePriceChange(purchasePriceValue)
                    }
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Paid,
                        contentDescription = stringResource(id = R.string.purchase_price_label)
                    )
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Decimal
                ),
                keyboardActions = KeyboardActions(onDone = {
                    defaultKeyboardAction(ImeAction.Done)
                }),
                singleLine = true,
                label = {
                    Text(
                        text = stringResource(id = R.string.purchase_price_label),
                        fontStyle = FontStyle.Italic
                    )
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.purchase_price_label),
                        fontStyle = FontStyle.Italic
                    )
                }
            )
            OutlinedTextField(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 2.dp)
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        if (focusState.hasFocus) {
                            onDateClick()
                        }
                    },
                value = date,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.EditCalendar,
                        contentDescription = stringResource(id = R.string.date_label)
                    )
                },
                onValueChange = {},
                singleLine = true,
                readOnly = true,
                label = {
                    Text(
                        text = stringResource(id = R.string.date_label),
                        fontStyle = FontStyle.Italic
                    )
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.enter_date_label),
                        fontStyle = FontStyle.Italic
                    )
                }
            )
            OutlinedTextField(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 2.dp)
                    .fillMaxWidth()
                    .clearFocusOnKeyboardDismiss(),
                value = quantity,
                onValueChange = { quantityValue ->
                    if (quantityValue.isEmpty() || quantityValue.matches(patternInt)) {
                        onQuantityChange(quantityValue)
                    }
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.ShoppingBag,
                        contentDescription = stringResource(id = R.string.quantity_label)
                    )
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Number
                ),
                keyboardActions = KeyboardActions(onDone = {
                    defaultKeyboardAction(ImeAction.Done)
                }),
                singleLine = true,
                label = { Text(text = stringResource(id = R.string.quantity_label)) },
                placeholder = { Text(text = stringResource(id = R.string.enter_quantity_label)) }
            )
            Row(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 2.dp)
                    .align(Alignment.Start),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = stringResource(id = R.string.is_paid_label))
                Checkbox(
                    checked = isPaid,
                    onCheckedChange = onIsPaidChange
                )
            }
        }
    }
}