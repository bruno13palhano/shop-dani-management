package com.bruno13palhano.shopdanimanagement.ui.screens.previews

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bruno13palhano.shopdanimanagement.ui.screens.common.CommonItem
import com.bruno13palhano.shopdanimanagement.ui.screens.customers.CustomersContent
import com.bruno13palhano.shopdanimanagement.ui.screens.financial.CustomersDebitContent
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CustomerDynamicPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SharedTransitionLayout {
                AnimatedContent(targetState = 0L, label = "") {
                    CustomersContent(
                        customerList = customerList,
                        menuItems = arrayOf(),
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedContentScope = this,
                        onItemClick = {},
                        onSearchClick = {},
                        onMoreOptionsItemClick = {},
                        onAddButtonClick = { it },
                        onIconMenuClick = {}
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CustomerPreview() {
    ShopDaniManagementTheme(
        dynamicColor = false
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SharedTransitionLayout {
                AnimatedContent(targetState = 0L, label = "") {
                    CustomersContent(
                        customerList = customerList,
                        menuItems = arrayOf(),
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedContentScope = this,
                        onItemClick = {},
                        onSearchClick = {},
                        onMoreOptionsItemClick = {},
                        onAddButtonClick = {},
                        onIconMenuClick = { it }
                    )
                }
            }
        }
    }
}

private val customerList =
    listOf(
        CommonItem(1L, byteArrayOf(), "Bruno", "Rua 15 de novembro", ""),
        CommonItem(2L, byteArrayOf(), "Brenda", "13 de maio", ""),
        CommonItem(3L, byteArrayOf(), "Daniela", "Rua do serrote", ""),
        CommonItem(4L, byteArrayOf(), "Josué", "Rua 15 de novembro", ""),
        CommonItem(5L, byteArrayOf(), "Helena", "Rua 13 de maio", ""),
        CommonItem(6L, byteArrayOf(), "Socorro", "Rua do serrote", ""),
        CommonItem(7L, byteArrayOf(), "Fernando", "Rua do serrote", ""),
        CommonItem(8L, byteArrayOf(), "Henrique", "Carão", ""),
        CommonItem(9L, byteArrayOf(), "Bruno", "Rua 15 de novembro", "")
    )

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CustomersDebitDynamicPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SharedTransitionLayout {
                AnimatedContent(targetState = Unit, label = "") {
                    CustomersDebitContent(
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedContentScope = this@AnimatedContent,
                        debits = listOf(),
                        menuItems = arrayOf(),
                        onItemClick = { _, _ -> },
                        onMoreOptionsItemClick = {},
                        navigateUp = { it }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CustomersDebitPreview() {
    ShopDaniManagementTheme(
        dynamicColor = false
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SharedTransitionLayout {
                AnimatedContent(targetState = Unit, label = "") {
                    CustomersDebitContent(
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedContentScope = this@AnimatedContent,
                        debits = listOf(),
                        menuItems = arrayOf(),
                        onItemClick = { _, _ -> },
                        onMoreOptionsItemClick = {},
                        navigateUp = { it }
                    )
                }
            }
        }
    }
}