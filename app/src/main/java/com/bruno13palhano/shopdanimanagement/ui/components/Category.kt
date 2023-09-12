package com.bruno13palhano.shopdanimanagement.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.shopdanimanagement.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesContent(
    newCategory: String,
    showCategoryDialog: Boolean,
    categories: List<Category>,
    onAddButtonClick: () -> Unit,
    onCategoryChange: (newCategory: String) -> Unit,
    onDismissRequest: () -> Unit,
    onOkClick: () -> Unit,
    onItemClick: (categoryId: String) -> Unit,
    onIconMenuClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.product_categories_label)) },
                navigationIcon = {
                    IconButton(onClick = onIconMenuClick) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = stringResource(id = R.string.drawer_menu_label)
                        )
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
            modifier = Modifier.padding(it),
            contentPadding = PaddingValues(8.dp, vertical = 4.dp),
        ) {
            items(categories) { category ->
                SimpleItemList(
                    modifier = Modifier.padding(vertical = 4.dp),
                    itemName = category.name,
                    imageVector = Icons.Filled.ArrowForward
                ) {
                    onItemClick(category.id.toString())
                }
            }
        }
    }
    if(showCategoryDialog) {
        CategoryDialog(
            newCategory = newCategory,
            onCategoryChange = onCategoryChange,
            onOkClick = onOkClick,
            onDismissRequest = onDismissRequest
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDialog(
    newCategory: String,
    onCategoryChange: (newCategory: String) -> Unit,
    onOkClick: () -> Unit,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        modifier = Modifier
            .clip(RoundedCornerShape(10)),
        onDismissRequest = onDismissRequest
    ) {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth(),
                    text = stringResource(id = R.string.new_category_label),
                    textAlign = TextAlign.Start
                )
                OutlinedTextField(
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                        .fillMaxWidth(),
                    value = newCategory,
                    onValueChange = onCategoryChange,
                    label = { Text(text = stringResource(id = R.string.name_label)) },
                    placeholder = { Text(text = stringResource(id = R.string.enter_name_label)) }
                )
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Button(
                        modifier = Modifier.padding(end = 4.dp),
                        onClick = {
                            onDismissRequest()
                            onOkClick()
                        }
                    ) {
                        Text(text = stringResource(id = R.string.ok_label))
                    }
                    Button(
                        modifier = Modifier.padding(start = 4.dp),
                        onClick = onDismissRequest
                    ) {
                        Text(text = stringResource(id = R.string.cancel_label))
                    }
                }
            }
        }
    }
}