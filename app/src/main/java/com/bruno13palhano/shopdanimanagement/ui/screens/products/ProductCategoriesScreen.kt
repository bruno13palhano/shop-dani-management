package com.bruno13palhano.shopdanimanagement.ui.screens.products

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.shopdanimanagement.ui.components.CategoriesContent
import com.bruno13palhano.shopdanimanagement.ui.components.CircularProgress
import com.bruno13palhano.shopdanimanagement.ui.screens.common.UiState
import com.bruno13palhano.shopdanimanagement.ui.screens.common.getErrors
import com.bruno13palhano.shopdanimanagement.ui.screens.products.viewmodel.ProductCategoriesViewModel
import kotlinx.coroutines.launch

@Composable
fun ProductCategoriesRoute(
    showBottomMenu: (show: Boolean) -> Unit,
    gesturesEnabled: (enabled: Boolean) -> Unit,
    onIconMenuClick: () -> Unit,
    onItemClick: (categoryId: Long) -> Unit
) {
    showBottomMenu(true)
    gesturesEnabled(true)
    ProductCategoriesScreen(
        onIconMenuClick = onIconMenuClick,
        onItemClick = onItemClick
    )
}
@Composable
fun ProductCategoriesScreen(
    onIconMenuClick: () -> Unit,
    onItemClick: (categoryId: Long) -> Unit,
    viewModel: ProductCategoriesViewModel = hiltViewModel()
) {
    val categoryState by viewModel.categoryState.collectAsStateWithLifecycle()
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    var showCategoryDialog by remember { mutableStateOf(false) }
    var showContent by remember { mutableStateOf(true) }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val errors = getErrors()

    when (categoryState) {
        UiState.Fail -> { showContent = true }

        UiState.InProgress -> {
            showContent = false
            CircularProgress()
        }

        UiState.Success -> { showContent = true }
    }

    AnimatedVisibility(
        visible = showContent,
        enter = fadeIn(animationSpec = spring(stiffness = Spring.StiffnessLow)),
        exit = fadeOut(animationSpec = spring(stiffness = Spring.StiffnessLow))
    ) {
        CategoriesContent(
            snackbarHostState = snackbarHostState,
            newCategory = viewModel.newName,
            showCategoryDialog = showCategoryDialog,
            categories = categories,
            onAddButtonClick = { showCategoryDialog = true },
            onCategoryChange = viewModel::updateName,
            onDismissRequest = { showCategoryDialog = false },
            onOkClick = {
                viewModel.insertCategory(
                    onError = {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = errors[it],
                                withDismissAction = true
                            )
                        }
                    }
                )
            },
            onItemClick = onItemClick,
            onIconMenuClick = onIconMenuClick
        )
    }
}