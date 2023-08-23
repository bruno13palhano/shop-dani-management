package com.bruno13palhano.shopdanimanagement.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.screens.common.CommonPhotoItem
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListContent(
    categoryId: String,
    showCategoryDialog: Boolean,
    itemList: List<CommonPhotoItem>,
    onCategoryChange: (category: String) -> Unit,
    onOkClick: () -> Unit,
    onDismissRequest: () -> Unit,
    onItemClick: (id: Long) -> Unit,
    onEditItemClick: () -> Unit,
    onAddButtonClick: () -> Unit,
    navigateUp: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val menuOptions = arrayOf(
        stringResource(id = R.string.edit_label)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = categoryId) },
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
                                    onClick = { index ->
                                        when (index) {
                                            0 -> {
                                                onEditItemClick()
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddButtonClick) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.add_label)
                )
            }
        }
    ) {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
            modifier = Modifier.padding(it)
        ) {
            items(itemList) { item ->
                CommonPhotoItem(
                    modifier = Modifier.padding(vertical = 4.dp),
                    photo = item.photo,
                    title = item.title,
                    subtitle = item.subtitle,
                    onClick = { onItemClick(item.id) }
                )
            }
        }
        if (showCategoryDialog) {
            CategoryDialog(
                newCategory = categoryId,
                onCategoryChange = onCategoryChange,
                onOkClick = onOkClick,
                onDismissRequest = onDismissRequest
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
private fun ProductListDynamicPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ProductListContent(
                showCategoryDialog = true,
                categoryId = "Perfume",
                itemList = items,
                onCategoryChange = {},
                onOkClick = {},
                onDismissRequest = {},
                onItemClick = {},
                onEditItemClick = {},
                onAddButtonClick = {},
                navigateUp = {}
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
private fun ProductListPreview() {
    ShopDaniManagementTheme(
        dynamicColor = false
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ProductListContent(
                showCategoryDialog = true,
                categoryId = "Perfume",
                itemList = items,
                onCategoryChange = {},
                onOkClick = {},
                onDismissRequest = {},
                onItemClick = {},
                onEditItemClick = {},
                onAddButtonClick = {},
                navigateUp = {}
            )
        }
    }
}

private val items = listOf(
    CommonPhotoItem(id= 1L, title = "Essencial", photo = "", subtitle = "Natura"),
    CommonPhotoItem(id= 2L, title = "Kaiak", photo = "", subtitle = "Natura"),
    CommonPhotoItem(id= 3L, title = "Homem", photo = "", subtitle = "Natura"),
    CommonPhotoItem(id= 4L, title = "Florata", photo = "", subtitle = "Avon"),
    CommonPhotoItem(id= 5L, title = "Essential", photo = "", subtitle = "Avon"),
    CommonPhotoItem(id= 6L, title = "Luna", photo = "", subtitle = "Natura"),
    CommonPhotoItem(id= 7L, title = "Homem", photo = "", subtitle = "Natura"),
    CommonPhotoItem(id= 8L, title = "Florata", photo = "", subtitle = "Avon")
)