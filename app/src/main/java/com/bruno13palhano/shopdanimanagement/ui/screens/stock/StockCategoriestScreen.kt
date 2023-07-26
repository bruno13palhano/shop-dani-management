package com.bruno13palhano.shopdanimanagement.ui.screens.stock

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme
import com.bruno13palhano.shopdanimanagement.ui.components.StockItem

@Composable
fun StockCategoriesScreen(
    onItemClick: (categoryId: String) -> Unit,
    navigateBack: () -> Unit,
) {
    val categories = listOf(
        StockCategory.Gifts,
        StockCategory.Infant,
        StockCategory.Perfumes,
        StockCategory.Soaps,
        StockCategory.AntiperspirantDeodorants,
        StockCategory.DeodorantsCologne,
        StockCategory.Sunscreens,
        StockCategory.Makeup,
        StockCategory.Face,
        StockCategory.Skin,
        StockCategory.Hair,
        StockCategory.Moisturizers
    )

    StockCategoriesContent(
        navigateBack = navigateBack,
        categories = categories,
        onItemClick = onItemClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StockCategoriesContent(
    categories: List<StockCategory>,
    onItemClick: (categoryId: String) -> Unit,
    navigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.stock_categories_label)) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.up_button_label)
                        )
                    }
                }
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier.padding(it),
            contentPadding = PaddingValues(8.dp),
        ) {
            items(categories) { stockCategory ->
                StockItem(category = stringResource(id = stockCategory.category)) {
                    onItemClick(stockCategory.categoryId.category)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun StockCategoriesListPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val categories = listOf(
                StockCategory.Gifts,
                StockCategory.Infant,
                StockCategory.Perfumes,
                StockCategory.Soaps,
                StockCategory.AntiperspirantDeodorants,
                StockCategory.DeodorantsCologne,
                StockCategory.Sunscreens,
                StockCategory.Makeup,
                StockCategory.Face,
                StockCategory.Skin,
                StockCategory.Hair,
                StockCategory.Moisturizers
            )

            StockCategoriesContent(
                navigateBack = {},
                categories = categories,
                onItemClick = {}
            )
        }
    }
}

sealed class StockCategory(val categoryId: Category, @StringRes val category: Int, val icon: ImageVector) {
    object Gifts: StockCategory(Category.GIFTS, R.string.gifts_label, Icons.Filled.Image)
    object Infant: StockCategory(Category.INFANT, R.string.infant_label, Icons.Filled.Image)
    object Perfumes: StockCategory(Category.PERFUMES, R.string.perfumes_label, Icons.Filled.Image)
    object Soaps: StockCategory(Category.SOAPS, R.string.soaps_label, Icons.Filled.Image)
    object AntiperspirantDeodorants: StockCategory(Category.ANTIPERSPIRANT_DEODORANTS, R.string.antiperspirant_deodorants_label, Icons.Filled.Image)
    object DeodorantsCologne: StockCategory(Category.DEODORANTS_COLOGNE, R.string.deodorants_cologne_label, Icons.Filled.Image)
    object Moisturizers: StockCategory(Category.MOISTURIZERS, R.string.moisturizers_label, Icons.Filled.Image)
    object Sunscreens: StockCategory(Category.SUNSCREENS, R.string.sunscreens_label, Icons.Filled.Image)
    object Makeup: StockCategory(Category.MAKEUP, R.string.makeup_label, Icons.Filled.Image)
    object Face: StockCategory(Category.FACE, R.string.face_label, Icons.Filled.Image)
    object Skin: StockCategory(Category.SKIN, R.string.skin_label, Icons.Filled.Image)
    object Hair: StockCategory(Category.HAIR, R.string.hair_label, Icons.Filled.Image)
}