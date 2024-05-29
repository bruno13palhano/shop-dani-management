package com.bruno13palhano.shopdanimanagement.ui.screens.previews.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.shopdanimanagement.ui.components.CategoriesContent
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

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
                snackbarHostState = SnackbarHostState(),
                newCategory = "",
                showCategoryDialog = true,
                categories = categories,
                onAddButtonClick = {},
                onCategoryChange = {},
                onDismissRequest = {},
                onOkClick = {},
                onItemClick = {},
                onIconMenuClick = {}
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
                snackbarHostState = SnackbarHostState(),
                newCategory = "",
                showCategoryDialog = false,
                categories = categories,
                onAddButtonClick = {},
                onCategoryChange = {},
                onDismissRequest = {},
                onOkClick = {},
                onItemClick = {},
                onIconMenuClick = {}
            )
        }
    }
}

private val categories =
    listOf(
        Category(
            1L,
            "Gifts",
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(OffsetDateTime.now(ZoneOffset.UTC))
        ),
        Category(
            2L,
            "Infant",
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(OffsetDateTime.now(ZoneOffset.UTC))
        ),
        Category(
            3L,
            "Perfumes",
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(OffsetDateTime.now(ZoneOffset.UTC))
        ),
        Category(
            4L,
            "Soaps",
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(OffsetDateTime.now(ZoneOffset.UTC))
        ),
        Category(
            5L,
            "Antiperspirant Deodorants",
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(OffsetDateTime.now(ZoneOffset.UTC))
        ),
        Category(
            6L,
            "Deodorants Cologne",
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(OffsetDateTime.now(ZoneOffset.UTC))
        ),
        Category(
            7L,
            "Sunscreens",
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(OffsetDateTime.now(ZoneOffset.UTC))
        ),
        Category(
            8L,
            "Makeup",
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(OffsetDateTime.now(ZoneOffset.UTC))
        ),
        Category(
            9L,
            "Face",
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(OffsetDateTime.now(ZoneOffset.UTC))
        ),
        Category(
            10L,
            "Skin",
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(OffsetDateTime.now(ZoneOffset.UTC))
        ),
        Category(
            11L,
            "Hair",
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(OffsetDateTime.now(ZoneOffset.UTC))
        ),
        Category(
            12L,
            "Moisturizers",
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(OffsetDateTime.now(ZoneOffset.UTC))
        )
    )