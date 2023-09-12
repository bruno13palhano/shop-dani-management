package com.bruno13palhano.shopdanimanagement.ui.screens.previews.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.shopdanimanagement.ui.components.CategoriesContent
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CategoriesListDynamicPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            CategoriesContent(
                newCategory = "",
                showCategoryDialog = true,
                categories = categories,
                onAddButtonClick = {},
                onCategoryChange = {},
                onDismissRequest = {},
                onOkClick = {},
                onItemClick = {},
                onIconMenuClick = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CategoriesListPreview() {
    ShopDaniManagementTheme(
        dynamicColor = false
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            CategoriesContent(
                newCategory = "",
                showCategoryDialog = false,
                categories = categories,
                onAddButtonClick = {},
                onCategoryChange = {},
                onDismissRequest = {},
                onOkClick = {},
                onItemClick = {},
                onIconMenuClick = {},
            )
        }
    }
}

private val categories = listOf(
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