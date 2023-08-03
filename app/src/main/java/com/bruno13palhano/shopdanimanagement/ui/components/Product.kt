package com.bruno13palhano.shopdanimanagement.ui.components

import android.icu.text.DecimalFormat
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.PriceCheck
import androidx.compose.material.icons.filled.QrCode
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
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
fun ProductContent(
    snackbarHostState: SnackbarHostState,
    categories: List<CategoryCheck>,
    companies: List<CompanyCheck>,
    name: String,
    code: String,
    description: String,
    photo: String,
    quantity: String,
    date: String,
    validity: String,
    category: String,
    company: String,
    purchasePrice: String,
    salePrice: String,
    isPaid: Boolean,
    onNameChange: (name: String) -> Unit,
    onCodeChange: (code: String) -> Unit,
    onDescriptionChange: (description: String) -> Unit,
    onQuantityChange: (quantity: String) -> Unit,
    onPurchasePriceChange: (purchasePrice: String) -> Unit,
    onSalePriceChange: (salePrice: String) -> Unit,
    onIsPaidChange: (isPaid: Boolean) -> Unit,
    onDismissCategory: () -> Unit,
    onCompanySelected: (selected: String) -> Unit,
    onDismissCompany: () -> Unit,
    onImageClick: () -> Unit,
    onDateClick: () -> Unit,
    onValidityClick: () -> Unit,
    onOutsideClick: () -> Unit,
    onActionButtonClick: () -> Unit,
    navigateUp: () -> Unit
) {
    var openCategorySheet by rememberSaveable { mutableStateOf(false) }
    var openCompanySheet by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .clickableNoEffect {
                onOutsideClick()
            },
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.new_product_label)) },
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
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onActionButtonClick) {
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

        Column(modifier = Modifier
            .padding(it)
            .verticalScroll(rememberScrollState())
        ) {
            CategoryBottomSheet(
                categories = categories,
                openBottomSheet = openCategorySheet,
                onBottomSheetChange = { show -> openCategorySheet = show },
                onDismissCategory = onDismissCategory
            )
            CompanyBottomSheet(
                companies = companies,
                openBottomSheet = openCompanySheet,
                onBottomSheetChange = { show -> openCompanySheet = show },
                onDismissCompany = onDismissCompany,
                onSelectedItem = onCompanySelected
            )
            ElevatedCard(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .fillMaxWidth(),
                onClick = onImageClick
            ) {
                if (photo.isEmpty()) {
                    Image(
                        modifier = Modifier
                            .size(168.dp)
                            .align(Alignment.CenterHorizontally),
                        imageVector = Icons.Filled.Image,
                        contentDescription = stringResource(id = R.string.product_image_label)
                    )
                } else {
                    Image(
                        modifier = Modifier
                            .size(168.dp)
                            .align(Alignment.CenterHorizontally),
                        painter = rememberAsyncImagePainter(model = photo),
                        contentDescription = stringResource(id = R.string.product_image_label)
                    )
                }
            }
            OutlinedTextField(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 2.dp)
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
                    .padding(horizontal = 16.dp, vertical = 2.dp)
                    .fillMaxWidth()
                    .clearFocusOnKeyboardDismiss(),
                value = code,
                onValueChange = { codeValue ->
                    if (codeValue.isEmpty() || codeValue.matches(patternInt)) {
                        onCodeChange(codeValue)
                    }
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.QrCode,
                        contentDescription = stringResource(id = R.string.code_label)
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
                label = {
                    Text(
                        text = stringResource(id = R.string.code_label),
                        fontStyle = FontStyle.Italic
                    )
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.enter_code_label),
                        fontStyle = FontStyle.Italic
                    )
                }
            )
            OutlinedTextField(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 2.dp)
                    .fillMaxWidth()
                    .clearFocusOnKeyboardDismiss(),
                value = description,
                onValueChange = onDescriptionChange,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Description,
                        contentDescription = stringResource(id = R.string.description_label)
                    )
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    defaultKeyboardAction(ImeAction.Done)
                }),
                singleLine = true,
                label = {
                    Text(
                        text = stringResource(id = R.string.description_label),
                        fontStyle = FontStyle.Italic
                    )
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.enter_description_label),
                        fontStyle = FontStyle.Italic
                    )
                },
            )
            OutlinedTextField(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 2.dp)
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
            OutlinedTextField(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 2.dp)
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
                    .padding(horizontal = 16.dp, vertical = 2.dp)
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        if (focusState.hasFocus) {
                            onValidityClick()
                        }
                    },
                value = validity,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.CalendarMonth,
                        contentDescription = stringResource(id = R.string.validity_label)
                    )
                },
                onValueChange = {},
                singleLine = true,
                readOnly = true,
                label = {
                    Text(
                        text = stringResource(id = R.string.validity_label),
                        fontStyle = FontStyle.Italic
                    )
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.enter_validity_label),
                        fontStyle = FontStyle.Italic
                    )
                }
            )
            OutlinedTextField(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 2.dp)
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
                        text = stringResource(id = R.string.enter_purchase_price_label),
                        fontStyle = FontStyle.Italic
                    )
                },
            )
            OutlinedTextField(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 2.dp)
                    .fillMaxWidth()
                    .clearFocusOnKeyboardDismiss(),
                value = salePrice,
                onValueChange = { salePriceValue ->
                    if (salePriceValue.isEmpty() || salePriceValue.matches(pattern)) {
                        onSalePriceChange(salePriceValue)
                    }
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.PriceCheck,
                        contentDescription = stringResource(id = R.string.sale_price_label)
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
                        text = stringResource(id = R.string.sale_price_label),
                        fontStyle = FontStyle.Italic
                    )
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.enter_sale_price_label),
                        fontStyle = FontStyle.Italic
                    )
                },
            )
            OutlinedTextField(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 2.dp)
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        if (focusState.hasFocus) {
                            openCategorySheet = true
                        }
                    },
                value = category,
                onValueChange = {},
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Category,
                        contentDescription = stringResource(id = R.string.categories_label)
                    )
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    defaultKeyboardAction(ImeAction.Done)
                }),
                singleLine = true,
                readOnly = true,
                label = {
                    Text(
                        text = stringResource(id = R.string.categories_label),
                        fontStyle = FontStyle.Italic
                    )
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.enter_categories_label),
                        fontStyle = FontStyle.Italic
                    )
                },
            )
            OutlinedTextField(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 2.dp)
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        if (focusState.hasFocus) {
                            openCompanySheet = true
                        }
                    },
                value = company,
                onValueChange = {},
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.BusinessCenter,
                        contentDescription = stringResource(id = R.string.company_label)
                    )
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    defaultKeyboardAction(ImeAction.Done)
                }),
                singleLine = true,
                readOnly = true,
                label = {
                    Text(
                        text = stringResource(id = R.string.company_label),
                        fontStyle = FontStyle.Italic
                    )
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.enter_company_label),
                        fontStyle = FontStyle.Italic
                    )
                },
            )
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp),
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