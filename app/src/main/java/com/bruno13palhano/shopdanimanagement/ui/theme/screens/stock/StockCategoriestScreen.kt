package com.bruno13palhano.shopdanimanagement.ui.theme.screens.stock

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
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme
import com.bruno13palhano.shopdanimanagement.ui.theme.components.StockItem

@Composable
fun StockCategoriesScreen(
    onItemClick: (categoryId: String) -> Unit,
    navigateBack: () -> Unit,
) {
    val categories = listOf(
        StockCategory.Gits,
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
                    onItemClick(stockCategory.categoryId)
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
                StockCategory.Gits,
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

sealed class StockCategory(val categoryId: String, @StringRes val category: Int, val icon: ImageVector) {
    object Gits: StockCategory(CategoryId.GIFTS_ID, R.string.gifts_label, Icons.Filled.Image)
    object Infant: StockCategory(CategoryId.INFANT_ID, R.string.infant_label, Icons.Filled.Image)
    object Perfumes: StockCategory(CategoryId.PERFUMES_ID, R.string.perfumes_label, Icons.Filled.Image)
    object Soaps: StockCategory(CategoryId.SOAPS_ID, R.string.soaps_label, Icons.Filled.Image)
    object AntiperspirantDeodorants: StockCategory(CategoryId.ANTIPERSPIRANT_DEODORANTS_ID, R.string.antiperspirant_deodorants_label, Icons.Filled.Image)
    object DeodorantsCologne: StockCategory(CategoryId.DEODORANTS_COLOGNE_ID, R.string.deodorants_cologne_label, Icons.Filled.Image)
    object Moisturizers: StockCategory(CategoryId.MOISTURIZERS_ID, R.string.moisturizers_label, Icons.Filled.Image)
    object Sunscreens: StockCategory(CategoryId.SUNSCREENS_ID, R.string.sunscreens_label, Icons.Filled.Image)
    object Makeup: StockCategory(CategoryId.MAKEUP_ID, R.string.makeup_label, Icons.Filled.Image)
    object Face: StockCategory(CategoryId.FACE_ID, R.string.face_label, Icons.Filled.Image)
    object Skin: StockCategory(CategoryId.SKIN_ID, R.string.skin_label, Icons.Filled.Image)
    object Hair: StockCategory(CategoryId.HAIR_ID, R.string.hair_label, Icons.Filled.Image)
}

object CategoryId {
    const val GIFTS_ID = "gifts_products"
    const val INFANT_ID = "infant_products"
    const val PERFUMES_ID = "perfumes_products"
    const val SOAPS_ID = "soaps_products"
    const val ANTIPERSPIRANT_DEODORANTS_ID = "antiperspirant_deodorants_products"
    const val DEODORANTS_COLOGNE_ID = "deodorants_cologne_products"
    const val MOISTURIZERS_ID = "moisturizers_products"
    const val SUNSCREENS_ID = "sunscreens_products"
    const val MAKEUP_ID = "makeup_products"
    const val FACE_ID = "face_products"
    const val SKIN_ID = "skin_products"
    const val HAIR_ID = "hair_products"
}