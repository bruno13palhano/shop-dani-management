package com.bruno13palhano.shopdanimanagement.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductItem(
    name: String,
    price: String,
    quantity: Int,
    isSold: Boolean,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier
                        .size(128.dp),
                    imageVector = Icons.Filled.Image,
                    contentDescription = stringResource(id = R.string.product_image_label)
                )
                if (isSold) {
                    Text(
                        modifier = Modifier
                            .rotate(-45F),
                        text = "Product Sold",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            Column {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    text = name,
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    text = price,
                    style = MaterialTheme.typography.bodyLarge,
                    fontStyle = FontStyle.Italic
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    text = quantity.toString(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockItem(
    modifier: Modifier,
    name: String,
    price: String,
    quantity: Int,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = modifier,
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier
                    .size(128.dp),
                imageVector = Icons.Filled.Image,
                contentDescription = stringResource(id = R.string.product_image_label),
            )
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .padding(horizontal = 8.dp),
                text = name,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                modifier = Modifier
                    .padding(horizontal = 8.dp),
                text = price,
                style = MaterialTheme.typography.bodyLarge,
                fontStyle = FontStyle.Italic
            )
            Text(
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                text = quantity.toString(),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockItem(
    category: String,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .weight(1F, true)
                    .padding(8.dp),
                text = category,
                style = MaterialTheme.typography.titleMedium
            )
            Icon(
                modifier = Modifier.padding(8.dp),
                imageVector = Icons.Filled.ArrowForward,
                contentDescription = null
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductItemPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ProductItem(
                name = "Essencial",
                price = "178.99R$",
                quantity = 10,
                isSold = true,
                onClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductItemPreview2() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            StockItem(
                modifier = Modifier
                    .fillMaxSize(),
                name = "Essencial",
                price = "178.99R$",
                quantity = 10,
                onClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StockListPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            StockItem(
                category = "Perfumes",
                onClick = {}
            )
        }
    }
}