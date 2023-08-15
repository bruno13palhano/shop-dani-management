package com.bruno13palhano.shopdanimanagement.ui.screens.shopping

import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.icu.text.DecimalFormat
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.PriceCheck
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.CategoryBottomSheet
import com.bruno13palhano.shopdanimanagement.ui.components.CategoryCheck
import com.bruno13palhano.shopdanimanagement.ui.components.CompanyBottomSheet
import com.bruno13palhano.shopdanimanagement.ui.components.CompanyCheck
import com.bruno13palhano.shopdanimanagement.ui.components.clearFocusOnKeyboardDismiss
import com.bruno13palhano.shopdanimanagement.ui.components.clickableNoEffect
import com.bruno13palhano.shopdanimanagement.ui.screens.shopping.viewmodel.NewShoppingItemViewModel
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun NewShoppingItemScreen(
    productId: Long,
    navigateUp: () -> Unit,
    viewModel: NewShoppingItemViewModel = hiltViewModel()
) {
   LaunchedEffect(key1 = Unit) {
       viewModel.getProduct(productId)
   }

    val isItemNotEmpty by viewModel.isItemNotEmpty.collectAsStateWithLifecycle()
    val configuration = LocalConfiguration.current
    val orientation = configuration.orientation
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var datePickerState = rememberDatePickerState()
    var showDatePickerDialog by remember { mutableStateOf(false) }
    var validityPickerState = rememberDatePickerState()
    var showValidityPickerDialog by remember { mutableStateOf(false) }

    if (showDatePickerDialog) {
        DatePickerDialog(
            onDismissRequest = {
                showDatePickerDialog = false
                focusManager.clearFocus(force = true)
            },
            confirmButton = {
                Button(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            viewModel.updateDate(it)
                        }
                        showDatePickerDialog = false
                        focusManager.clearFocus(force = true)
                    }
                ) {
                    Text(text = stringResource(id = R.string.date_label))
                }
            }
        ) {
            datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = viewModel.dateInMillis,
                initialDisplayMode = if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    DisplayMode.Picker
                } else {
                    DisplayMode.Input
                }
            )
            DatePicker(
                state = datePickerState,
                showModeToggle = orientation == Configuration.ORIENTATION_PORTRAIT
            )
        }
    }

    if (showValidityPickerDialog) {
        DatePickerDialog(
            onDismissRequest = {
                showValidityPickerDialog = false
                focusManager.clearFocus(force = true)
            },
            confirmButton = {
                Button(
                    onClick = {
                        validityPickerState.selectedDateMillis?.let {
                            viewModel.updateValidity(it)
                        }
                        showValidityPickerDialog = false
                        focusManager.clearFocus(force = true)
                    }
                ) {
                    Text(text = stringResource(id = R.string.validity_label))
                }
            }
        ) {
            validityPickerState = rememberDatePickerState(
                initialSelectedDateMillis = viewModel.validityInMillis,
                initialDisplayMode = if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    DisplayMode.Picker
                } else {
                    DisplayMode.Input
                }
            )
            DatePicker(
                state = validityPickerState,
                showModeToggle = orientation == Configuration.ORIENTATION_PORTRAIT
            )
        }
    }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val errorMessage = stringResource(id = R.string.empty_fields_error)

    NewShoppingItemContent(
        snackbarHostState = snackbarHostState,
        name = viewModel.name,
        photo = viewModel.photo,
        quantity = viewModel.quantity,
        date = viewModel.date,
        purchasePrice = viewModel.purchasePrice,
        salePrice = viewModel.salePrice,
        validity = viewModel.validity,
        category = viewModel.category,
        company = viewModel.company,
        isPaid = viewModel.isPaid,
        onNameChange = viewModel::updateName,
        onQuantityChange = viewModel::updateQuantity,
        onPurchasePriceChange = viewModel::updatePurchasePrice,
        onSalePriceChange = viewModel::updateSalePrice,
        onIsPaidChange = viewModel::updateIsPaid,
        onDateClick = { showDatePickerDialog = true },
        onValidityClick = { showValidityPickerDialog = true},
        categories = viewModel.allCategories,
        companies = viewModel.allCompanies,
        onDismissCategory = {
            viewModel.updateCategories(viewModel.allCategories)
            focusManager.clearFocus(force = true)
        },
        onDismissCompany = { focusManager.clearFocus(force = true) },
        onCompanySelected = viewModel::updateCompany,
        onOutsideClick = {
            keyboardController?.hide()
            focusManager.clearFocus(force = true)
        },
        onDoneButtonClick = {
            if (isItemNotEmpty) {
                viewModel.insertItems(productId)
                navigateUp()
            } else {
                scope.launch {
                    snackbarHostState.showSnackbar(errorMessage)
                }
            }
        },
        navigateUp = navigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewShoppingItemContent(
    snackbarHostState: SnackbarHostState,
    name: String,
    photo: String,
    quantity: String,
    date: String,
    purchasePrice: String,
    salePrice: String,
    validity: String,
    category: String,
    company: String,
    isPaid: Boolean,
    onNameChange: (name: String) -> Unit,
    onQuantityChange: (quantity: String) -> Unit,
    onPurchasePriceChange: (purchasePrice: String) -> Unit,
    onSalePriceChange: (salePrice: String) -> Unit,
    onIsPaidChange: (isPaid: Boolean) -> Unit,
    onDateClick: () -> Unit,
    onValidityClick: () -> Unit,
    categories: List<CategoryCheck>,
    companies: List<CompanyCheck>,
    onDismissCategory: () -> Unit,
    onDismissCompany: () -> Unit,
    onCompanySelected: (selected: String) -> Unit,
    onOutsideClick: () -> Unit,
    onDoneButtonClick: () -> Unit,
    navigateUp: () -> Unit
) {
    var openCategorySheet by rememberSaveable { mutableStateOf(false) }
    var openCompanySheet by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.clickableNoEffect { onOutsideClick() },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopAppBar(
                title = { Text(text = "New Shopping Item") },
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
            FloatingActionButton(onClick = onDoneButtonClick) {
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
            .fillMaxHeight()
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
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                ElevatedCard(
                    modifier = Modifier
                        .padding(start = 8.dp),
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
                        label = {
                            Text(
                                text = stringResource(id = R.string.quantity_label),
                                fontStyle = FontStyle.Italic
                            )
                        },
                        placeholder = {
                            Text(
                                text = stringResource(id = R.string.enter_quantity_label),
                                fontStyle = FontStyle.Italic
                            )
                        },
                    )
                }
            }
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
                        text = stringResource(id = R.string.enter_purchase_price_label),
                        fontStyle = FontStyle.Italic
                    )
                },
            )
            OutlinedTextField(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 2.dp)
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
                    .padding(horizontal = 8.dp, vertical = 2.dp)
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
                    .padding(horizontal = 8.dp, vertical = 2.dp)
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
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isPaid,
                    onCheckedChange = onIsPaidChange
                )
                Text(text = stringResource(id = R.string.is_paid_label))
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun NewShoppingItemDynamicPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            NewShoppingItemContent(
                snackbarHostState = remember { SnackbarHostState() },
                name = "",
                photo = "",
                quantity = "",
                date = "",
                purchasePrice = "",
                salePrice = "",
                validity = "",
                category = "",
                company = "",
                isPaid = false,
                onNameChange = {},
                onQuantityChange = {},
                onPurchasePriceChange = {},
                onSalePriceChange = {},
                onIsPaidChange = {},
                onDateClick = {},
                onValidityClick = {},
                categories = emptyList(),
                companies = emptyList(),
                onDismissCategory = {},
                onDismissCompany = {},
                onCompanySelected = {},
                onOutsideClick = {},
                onDoneButtonClick = {},
                navigateUp = {}
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun NewShoppingItemPreview() {
    ShopDaniManagementTheme(
        dynamicColor = false
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            NewShoppingItemContent(
                snackbarHostState = remember { SnackbarHostState() },
                name = "",
                photo = "",
                quantity = "",
                date = "",
                purchasePrice = "",
                salePrice = "",
                validity = "",
                category = "",
                company = "",
                isPaid = true,
                onNameChange = {},
                onQuantityChange = {},
                onPurchasePriceChange = {},
                onSalePriceChange = {},
                onIsPaidChange = {},
                onDateClick = {},
                onValidityClick = {},
                categories = emptyList(),
                companies = emptyList(),
                onDismissCategory = {},
                onDismissCompany = {},
                onCompanySelected = {},
                onOutsideClick = {},
                onDoneButtonClick = {},
                navigateUp = {}
            )
        }
    }
}