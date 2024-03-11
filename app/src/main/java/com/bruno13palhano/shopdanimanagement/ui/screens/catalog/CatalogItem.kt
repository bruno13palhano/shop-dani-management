package com.bruno13palhano.shopdanimanagement.ui.screens.catalog

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Discount
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.CircularProgress
import com.bruno13palhano.shopdanimanagement.ui.components.CustomFloatField
import com.bruno13palhano.shopdanimanagement.ui.components.CustomIntegerField
import com.bruno13palhano.shopdanimanagement.ui.components.CustomTextField
import com.bruno13palhano.shopdanimanagement.ui.components.MoreOptionsMenu
import com.bruno13palhano.shopdanimanagement.ui.screens.catalog.viewmodel.CatalogItemViewModel
import com.bruno13palhano.shopdanimanagement.ui.screens.common.DataError
import com.bruno13palhano.shopdanimanagement.ui.screens.common.UiState
import com.bruno13palhano.shopdanimanagement.ui.screens.common.getErrors
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme
import kotlinx.coroutines.launch

@Composable
fun NewCatalogItemRoute(
    showBottomMenu: (show: Boolean) -> Unit,
    gesturesEnabled: (enabled: Boolean) -> Unit,
    productId: Long,
    navigateUp: () -> Unit
) {
    showBottomMenu(true)
    gesturesEnabled(true)
    CatalogItemScreen(
        productId = productId,
        catalogId = 0L,
        navigateUp = navigateUp
    )
}

@Composable
fun EditCatalogItemRoute(
    showBottomMenu: (show: Boolean) -> Unit,
    gesturesEnabled: (enabled: Boolean) -> Unit,
    catalogId: Long,
    navigateUp: () -> Unit
) {
    showBottomMenu(true)
    gesturesEnabled(true)
    CatalogItemScreen(
        productId = 0L,
        catalogId = catalogId,
        navigateUp = navigateUp
    )
}

@Composable
fun CatalogItemScreen(
    productId: Long,
    catalogId: Long,
    navigateUp: () -> Unit,
    viewModel: CatalogItemViewModel = hiltViewModel()
) {
    val editable = catalogId != 0L
    LaunchedEffect(key1 = Unit) {
        if (editable) {
            viewModel.getCatalogItem(id = catalogId)
        } else {
            viewModel.getProduct(id = productId)
        }
    }

    val catalogState by viewModel.catalogState.collectAsStateWithLifecycle()
    var showContent by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val errors = getErrors()

    val menuOptions = arrayOf(
        stringResource(id = R.string.delete_label),
    )

    when (catalogState) {
        UiState.Fail -> { showContent = true }

        UiState.InProgress -> {
            showContent = false
            CircularProgress()
        }

        UiState.Success -> { LaunchedEffect(key1 = Unit) { navigateUp() } }
    }

    AnimatedVisibility(
        visible = showContent,
        enter = fadeIn(animationSpec = spring(stiffness = Spring.StiffnessLow)),
        exit = fadeOut(animationSpec = spring(stiffness = Spring.StiffnessLow))
    ) {
        CatalogItemContent(
            editable = editable,
            name = viewModel.name,
            photo = viewModel.photo,
            title = viewModel.title,
            description = viewModel.description,
            discount = viewModel.discount,
            price = viewModel.price,
            menuOptions = menuOptions,
            onTitleChange = viewModel::updateTitle,
            onDescriptionChange = viewModel::updateDescription,
            onDiscountChange = viewModel::updateDiscount,
            onPriceChange = viewModel::updatePrice,
            onMenuOptionsItemClick = { index ->
                when (index) {
                    0 -> {
                        viewModel.delete(
                            onError = { error ->
                                scope.launch {
                                    if (error == DataError.DeleteDatabase.error) {
                                        snackbarHostState.showSnackbar(
                                            message = errors[error],
                                            withDismissAction = true
                                        )
                                    }

                                    navigateUp()
                                }
                            }
                        )
                    }
                }
            },
            onDoneButtonClick = {
                if (catalogId == 0L) {
                    viewModel.insert(
                        onError = { error ->
                            scope.launch {
                                if (error == DataError.InsertDatabase.error) {
                                    snackbarHostState.showSnackbar(
                                        message = errors[error],
                                        withDismissAction = true
                                    )
                                }

                                navigateUp()
                            }
                        }
                    )
                } else {
                    viewModel.update(
                        onError = { error ->
                            scope.launch {
                                if (error == DataError.UpdateDatabase.error) {
                                    snackbarHostState.showSnackbar(
                                        message = errors[error],
                                        withDismissAction = true
                                    )
                                }

                                navigateUp()
                            }
                        }
                    )
                }
            },
            navigateUp = navigateUp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogItemContent(
    editable: Boolean,
    name: String,
    photo: ByteArray,
    title: String,
    description: String,
    discount: String,
    price: String,
    menuOptions: Array<String>,
    onTitleChange: (title: String) -> Unit,
    onDescriptionChange: (description: String) -> Unit,
    onDiscountChange: (discount: String) -> Unit,
    onPriceChange: (price: String) -> Unit,
    onMenuOptionsItemClick: (index: Int) -> Unit,
    onDoneButtonClick: () -> Unit,
    navigateUp: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.product_label)) },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.up_button_label)
                        )
                    }
                },
                actions = {
                    if (editable) {
                        IconButton(onClick = { expanded = true }) {
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
                                    onDismissRequest = { expandedValue ->
                                        expanded = expandedValue
                                    },
                                    onClick = onMenuOptionsItemClick
                                )
                            }
                        }
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
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ElevatedCard(
                modifier = Modifier
                    .padding(16.dp),
                shape = RoundedCornerShape(5)
            ) {
                if (photo.isEmpty()) {
                    Image(
                        modifier = Modifier
                            .size(200.dp)
                            .padding(8.dp)
                            .clip(RoundedCornerShape(5)),
                        imageVector = Icons.Filled.Image,
                        contentDescription = stringResource(id = R.string.customer_photo_label),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        modifier = Modifier
                            .size(200.dp)
                            .padding(8.dp)
                            .clip(RoundedCornerShape(5)),
                        painter = rememberAsyncImagePainter(model = photo),
                        contentDescription = stringResource(id = R.string.customer_photo_label),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            CustomTextField(
                text = name,
                onTextChange = {},
                icon = Icons.Filled.Title,
                label = stringResource(id = R.string.product_name_label),
                placeholder = ""
            )
            CustomTextField(
                text = title,
                onTextChange = onTitleChange,
                icon = Icons.AutoMirrored.Filled.Label,
                label = stringResource(id = R.string.title_label),
                placeholder = stringResource(id = R.string.enter_title_label)
            )
            CustomIntegerField(
                value = discount,
                onValueChange = onDiscountChange,
                icon = Icons.Filled.Discount,
                label = stringResource(id = R.string.discount_label),
                placeholder = stringResource(id = R.string.enter_discount_label)
            )
            CustomFloatField(
                value = price,
                onValueChange = onPriceChange,
                icon = Icons.Filled.Paid,
                label = stringResource(id = R.string.price_label),
                placeholder = stringResource(id = R.string.enter_price_label)
            )
            CustomTextField(
                text = description,
                onTextChange = onDescriptionChange,
                icon = Icons.Filled.Description,
                label = stringResource(id = R.string.description_label),
                placeholder = stringResource(id = R.string.enter_description_label)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun CatalogItemDynamicPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            CatalogItemContent(
                editable = false,
                name = "",
                photo = byteArrayOf(),
                title = "",
                description = "",
                discount = "",
                price = "",
                menuOptions = arrayOf(),
                onTitleChange = {},
                onDescriptionChange = {},
                onDiscountChange = {},
                onPriceChange = {},
                onMenuOptionsItemClick = {},
                onDoneButtonClick = {},
                navigateUp = {}
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun CatalogItemPreview() {
    ShopDaniManagementTheme(
        dynamicColor = false
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            CatalogItemContent(
                editable = true,
                name = "",
                photo = byteArrayOf(),
                title = "",
                description = "",
                discount = "",
                price = "",
                menuOptions = arrayOf(),
                onTitleChange = {},
                onDescriptionChange = {},
                onDiscountChange = {},
                onPriceChange = {},
                onMenuOptionsItemClick = {},
                onDoneButtonClick = {},
                navigateUp = {}
            )
        }
    }
}