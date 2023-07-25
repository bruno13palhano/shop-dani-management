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
fun StockListScreen(
    navigateBack: () -> Unit,
) {
    val categories = listOf(
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

    StockListContent(
        navigateBack = navigateBack,
        categories = categories
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StockListContent(
    categories: List<StockCategory>,
    navigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.stock_list_label)) },
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

                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun StockListPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val categories = listOf(
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

            StockListContent(
                navigateBack = {},
                categories = categories
            )
        }
    }
}

sealed class StockCategory(@StringRes val category: Int, val icon: ImageVector) {
    object Perfumes: StockCategory(R.string.perfumes_label, Icons.Filled.Image)
    object Soaps: StockCategory(R.string.soaps_label, Icons.Filled.Image)
    object AntiperspirantDeodorants: StockCategory(R.string.antiperspirant_deodorants_label, Icons.Filled.Image)
    object DeodorantsCologne: StockCategory(R.string.deodorants_cologne_label, Icons.Filled.Image)
    object Moisturizers: StockCategory(R.string.moisturizers_label, Icons.Filled.Image)
    object Sunscreens: StockCategory(R.string.sunscreens_label, Icons.Filled.Image)
    object Makeup: StockCategory(R.string.makeup_label, Icons.Filled.Image)
    object Face: StockCategory(R.string.face_label, Icons.Filled.Image)
    object Skin: StockCategory(R.string.skin_label, Icons.Filled.Image)
    object Hair: StockCategory(R.string.hair_label, Icons.Filled.Image)
}