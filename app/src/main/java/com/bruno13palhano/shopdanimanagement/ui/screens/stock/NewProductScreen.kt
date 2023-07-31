package com.bruno13palhano.shopdanimanagement.ui.screens.stock

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.icu.text.DecimalFormat
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.PriceCheck
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.screens.stock.viewmodel.NewProductViewModel
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun NewProductScreen(
    categoryId: String,
    navigateUp: () -> Unit,
    viewModel: NewProductViewModel = hiltViewModel()
) {
    val allCategories by viewModel.allCategories.collectAsStateWithLifecycle()
    val galleryLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            viewModel.updatePhoto(it.toString())
        }
    }
    NewProductContent(
        categories = allCategories,
        name = viewModel.name,
        description = viewModel.description,
        photo = viewModel.photo,
        category = viewModel.category,
        company = viewModel.company,
        purchasePrice = viewModel.purchasePrice,
        salePrice = viewModel.salePrice,
        isPaid = viewModel.isPaid,
        onNameChange = viewModel::updateName,
        onDescriptionChange = viewModel::updateDescription,
        onCategoryChange = viewModel::updateCategory,
        onCompanyChange = viewModel::updateCompany,
        onPurchasePriceChange = viewModel::updatePurchasePrice,
        onSalePriceChange = viewModel::updateSalePrice,
        onIsPaidChange = viewModel::updateIsPaid,
        onImageClick = {
            galleryLauncher.launch("image/*")
        },
        onDoneButtonClick = {
            navigateUp()
        },
        navigateUp = navigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewProductContent(
    categories: List<Category>,
    name: String,
    description: String,
    photo: String,
    category: String,
    company: String,
    purchasePrice: String,
    salePrice: String,
    isPaid: Boolean,
    onNameChange: (name: String) -> Unit,
    onDescriptionChange: (description: String) -> Unit,
    onCategoryChange: (category: String) -> Unit,
    onCompanyChange: (company: String) -> Unit,
    onPurchasePriceChange: (purchasePrice: String) -> Unit,
    onSalePriceChange: (salePrice: String) -> Unit,
    onIsPaidChange: (isPaid: Boolean) -> Unit,
    onImageClick: () -> Unit,
    onDoneButtonClick: () -> Unit,
    navigateUp: () -> Unit
) {
    var openSheet by rememberSaveable { mutableStateOf(false) }

    Scaffold(
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

        Column(modifier = Modifier
            .padding(it)
            .verticalScroll(rememberScrollState())
        ) {
            ModalBottomSheetSample(
                categories = categories,
                openBottomSheet = openSheet
            ) { show ->
                openSheet = show
            }
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
                    .fillMaxWidth(),
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
                label = { Text(text = stringResource(id = R.string.name_label)) },
                placeholder = { Text(text = stringResource(id = R.string.enter_name_label)) },
            )
            OutlinedTextField(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 2.dp)
                    .fillMaxWidth(),
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
                label = { Text(text = stringResource(id = R.string.description_label)) },
                placeholder = { Text(text = stringResource(id = R.string.enter_description_label)) },
            )
            OutlinedTextField(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 2.dp)
                    .fillMaxWidth()
                    .clickable {
                        openSheet = true
                    },
                value = category,
                onValueChange = onCategoryChange,
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
                label = { Text(text = stringResource(id = R.string.categories_label)) },
                placeholder = { Text(text = stringResource(id = R.string.enter_description_label)) },
            )
            OutlinedTextField(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 2.dp)
                    .fillMaxWidth(),
                value = company,
                onValueChange = onCompanyChange,
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
                label = { Text(text = stringResource(id = R.string.company_label)) },
                placeholder = { Text(text = stringResource(id = R.string.enter_company_label)) },
            )
            OutlinedTextField(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 2.dp)
                    .fillMaxWidth(),
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
                label = { Text(text = stringResource(id = R.string.purchase_price_label)) },
                placeholder = { Text(text = stringResource(id = R.string.enter_purchase_price_label)) },
            )
            OutlinedTextField(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 2.dp)
                    .fillMaxWidth(),
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
                label = { Text(text = stringResource(id = R.string.sale_price_label)) },
                placeholder = { Text(text = stringResource(id = R.string.enter_sale_price_label)) },
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

@Preview(showBackground = true)
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun NewProductPreview() {
    val categories = listOf(
        Category(1L, "Gifts"),
        Category(2L, "Infant"),
        Category(3L, "Perfumes"),
        Category(4L, "Soaps"),
        Category(5L, "Antiperspirant Deodorants"),
        Category(6L, "Deodorants Cologne"),
        Category(7L, "Sunscreens"),
        Category(8L, "Makeup"),
        Category(9L, "Face"),
        Category(10L, "Skin"),
        Category(11L, "Hair"),
        Category(12L, "Moisturizers")
    )
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            NewProductContent(
                categories = categories,
                name = "",
                description = "",
                photo = "",
                category = "",
                company = "",
                purchasePrice = "",
                salePrice = "",
                isPaid = true,
                onNameChange = {},
                onDescriptionChange = {},
                onCategoryChange = {},
                onCompanyChange = {},
                onPurchasePriceChange = {},
                onSalePriceChange = {},
                onImageClick = {},
                onIsPaidChange = {},
                onDoneButtonClick = { /*TODO*/ },
                navigateUp = {}
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalBottomSheetSample(
    categories: List<Category>,
    openBottomSheet: Boolean,
    onBottomSheetChange: (close: Boolean) -> Unit
) {
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState()

    if (openBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { onBottomSheetChange(false) },
            sheetState = bottomSheetState,
        ) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Button(
                    onClick = {
                        scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                            if (!bottomSheetState.isVisible) {
                                onBottomSheetChange(false)
                            }
                        }
                    }
                ) {
                    Text("Hide Bottom Sheet")
                }
            }
            LazyColumn(
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {
                items(categories) {
                    ListItem(
                        headlineContent = { Text("${it.name} ${it.id}") },
                        leadingContent = {
                            var checked by rememberSaveable { mutableStateOf(false) }

                            Checkbox(checked = checked, onCheckedChange = {
                                checked = !checked
                            })
                        }
                    )
                }
            }
        }
    }
}