package com.bruno13palhano.shopdanimanagement.ui.screens.customers

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.clearFocusOnKeyboardDismiss
import com.bruno13palhano.shopdanimanagement.ui.screens.customers.viewmodel.CustomerInfoViewModel
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme

@Composable
fun CustomerInfoScreen(
    customerId: Long,
    navigateUp: () -> Unit,
    viewModel: CustomerInfoViewModel = hiltViewModel()
) {

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerInfoContent(
    name: String,
    address: String,
    photo: String,
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
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun CustomerDynamicPrev() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            CustomerInfoContent(
                name = "",
                address = "",
                photo = "",
                onEditIconClick = {},
                navigateUp = {}
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun CustomerPrev() {
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
                onEditIconClick = {},
                navigateUp = {}
            )
        }
    }
}