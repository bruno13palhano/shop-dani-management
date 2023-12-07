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
import java.time.OffsetDateTime

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
    Category(1L, "Gifts", OffsetDateTime.now()),
    Category(2L, "Infant",  OffsetDateTime.now()),
    Category(3L, "Perfumes",  OffsetDateTime.now()),
    Category(4L, "Soaps",  OffsetDateTime.now()),
    Category(5L, "Antiperspirant Deodorants",  OffsetDateTime.now()),
    Category(6L, "Deodorants Cologne",  OffsetDateTime.now()),
    Category(7L, "Sunscreens",  OffsetDateTime.now()),
    Category(8L, "Makeup",  OffsetDateTime.now()),
    Category(9L, "Face",  OffsetDateTime.now()),
    Category(10L, "Skin",  OffsetDateTime.now()),
    Category(11L, "Hair",  OffsetDateTime.now()),
    Category(12L, "Moisturizers",  OffsetDateTime.now())
)