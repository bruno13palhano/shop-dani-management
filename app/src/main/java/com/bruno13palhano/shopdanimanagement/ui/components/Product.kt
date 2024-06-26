package com.bruno13palhano.shopdanimanagement.ui.components

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.bruno13palhano.shopdanimanagement.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductContent(
    isEditable: Boolean,
    screenTitle: String,
    snackbarHostState: SnackbarHostState,
    categories: List<CategoryCheck>,
    companies: List<CompanyCheck>,
    name: String,
    code: String,
    description: String,
    photo: ByteArray,
    date: String,
    category: String,
    company: String,
    onNameChange: (name: String) -> Unit,
    onCodeChange: (code: String) -> Unit,
    onDescriptionChange: (description: String) -> Unit,
    onDismissCategory: () -> Unit,
    onCompanySelected: (selected: String) -> Unit,
    onDismissCompany: () -> Unit,
    onImageClick: () -> Unit,
    onDateClick: () -> Unit,
    onMoreOptionsItemClick: (index: Int) -> Unit,
    onOutsideClick: () -> Unit,
    onActionButtonClick: () -> Unit,
    navigateUp: () -> Unit
) {
    val configuration = LocalConfiguration.current
    var openCategorySheet by rememberSaveable { mutableStateOf(false) }
    var openCompanySheet by rememberSaveable { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    val items =
        arrayOf(
            stringResource(id = R.string.add_to_catalog_label),
            stringResource(id = R.string.delete_label)
        )

    Scaffold(
        modifier =
            Modifier
                .clickableNoEffect {
                    onOutsideClick()
                },
        topBar = {
            TopAppBar(
                title = { Text(text = screenTitle) },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.up_button_label)
                        )
                    }
                },
                actions = {
                    if (isEditable) {
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
                                        items = items,
                                        expanded = expanded,
                                        onDismissRequest = { expandedValue ->
                                            expanded = expandedValue
                                        },
                                        onClick = onMoreOptionsItemClick
                                    )
                                }
                            }
                        }
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
        Column(
            modifier =
                Modifier
                    .padding(it)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
        ) {
            AnimatedVisibility(visible = openCategorySheet) {
                BottomSheet(
                    onDismissBottomSheet = {
                        openCategorySheet = false
                        onDismissCategory()
                    }
                ) {
                    LazyColumn(contentPadding = PaddingValues(bottom = 32.dp)) {
                        items(categories) { categoryItem ->
                            ListItem(
                                headlineContent = { Text(text = categoryItem.category) },
                                leadingContent = {
                                    var checked by rememberSaveable { mutableStateOf(categoryItem.isChecked) }

                                    Checkbox(
                                        checked = checked,
                                        onCheckedChange = { isChecked ->
                                            categoryItem.isChecked = isChecked
                                            checked = isChecked
                                        }
                                    )
                                }
                            )
                        }
                    }
                }
            }

            AnimatedVisibility(visible = openCompanySheet) {
                val initialCompany =
                    companies
                        .filter { company -> company.isChecked }
                        .findLast { company -> company.isChecked }?.name?.company

                BottomSheet(
                    onDismissBottomSheet = {
                        openCompanySheet = false
                        onDismissCompany()
                    }
                ) {
                    val (selected, onOptionSelected) = rememberSaveable { mutableStateOf(initialCompany) }

                    Column(modifier = Modifier.padding(bottom = 32.dp)) {
                        companies.forEach { companyItem ->
                            ListItem(
                                headlineContent = { Text(text = companyItem.name.company) },
                                leadingContent = {
                                    RadioButton(
                                        selected = companyItem.name.company == selected,
                                        onClick = {
                                            onOptionSelected(companyItem.name.company)
                                            onCompanySelected(companyItem.name.company)
                                        }
                                    )
                                }
                            )
                        }
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                ElevatedCard(
                    modifier =
                        Modifier
                            .padding(start = 8.dp),
                    onClick = onImageClick
                ) {
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
                            painter = rememberAsyncImagePainter(model = photo),
                            contentDescription = stringResource(id = R.string.product_image_label)
                        )
                    }
                }

                Column {
                    CustomTextField(
                        text = name,
                        onTextChange = onNameChange,
                        icon = Icons.Filled.Title,
                        label = stringResource(id = R.string.name_label),
                        placeholder = stringResource(id = R.string.enter_name_label)
                    )
                    CustomIntegerField(
                        value = code,
                        onValueChange = onCodeChange,
                        icon = Icons.Filled.QrCode,
                        label = stringResource(id = R.string.code_label),
                        placeholder = stringResource(id = R.string.enter_code_label)
                    )
                }
            }
            CustomClickField(
                value = date,
                onClick = onDateClick,
                icon = Icons.Filled.EditCalendar,
                label = stringResource(id = R.string.date_label),
                placeholder = stringResource(id = R.string.enter_date_label)
            )
            CustomClickField(
                value = category,
                onClick = { openCategorySheet = true },
                icon = Icons.Filled.Category,
                label = stringResource(id = R.string.categories_label),
                placeholder = stringResource(id = R.string.enter_categories_label)
            )
            CustomClickField(
                value = company,
                onClick = { openCompanySheet = true },
                icon = Icons.Filled.BusinessCenter,
                label = stringResource(id = R.string.company_label),
                placeholder = stringResource(id = R.string.enter_company_label)
            )
            if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                OutlinedTextField(
                    modifier =
                        Modifier
                            .padding(start = 8.dp, top = 2.dp, end = 8.dp, bottom = 8.dp)
                            .fillMaxWidth()
                            .weight(1F, true)
                            .clearFocusOnKeyboardDismiss(),
                    value = description,
                    onValueChange = onDescriptionChange,
                    leadingIcon = {
                        Row(
                            modifier =
                                Modifier
                                    .fillMaxHeight()
                                    .padding(top = 16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Description,
                                contentDescription = stringResource(id = R.string.description_label)
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions =
                        KeyboardActions(onDone = {
                            defaultKeyboardAction(ImeAction.Done)
                        }),
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
                    }
                )
            } else {
                OutlinedTextField(
                    modifier =
                        Modifier
                            .padding(start = 8.dp, top = 2.dp, end = 8.dp, bottom = 8.dp)
                            .sizeIn(minHeight = 200.dp)
                            .fillMaxWidth()
                            .sizeIn(minHeight = 200.dp)
                            .clearFocusOnKeyboardDismiss(),
                    value = description,
                    onValueChange = onDescriptionChange,
                    leadingIcon = {
                        Row(
                            modifier =
                                Modifier
                                    .fillMaxHeight()
                                    .sizeIn(minHeight = 200.dp)
                                    .padding(top = 16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Description,
                                contentDescription = stringResource(id = R.string.description_label)
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions =
                        KeyboardActions(onDone = {
                            defaultKeyboardAction(ImeAction.Done)
                        }),
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
                    }
                )
            }
        }
    }
}

object ProductMenuItem {
    const val ADD_TO_CATALOG = 0
    const val DELETE = 1
}