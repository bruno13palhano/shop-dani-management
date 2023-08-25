package com.bruno13palhano.shopdanimanagement.ui.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HorizontalProductItem(
    modifier: Modifier,
    name: String,
    photo: String,
    price: Float,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = modifier,
        onClick = onClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                if(photo.isEmpty()) {
                    Image(
                        modifier = Modifier
                            .size(128.dp)
                            .padding(8.dp)
                            .clip(RoundedCornerShape(5)),
                        imageVector = Icons.Filled.Image,
                        contentDescription = stringResource(id = R.string.product_image_label),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        modifier = Modifier
                            .size(128.dp)
                            .padding(8.dp)
                            .clip(RoundedCornerShape(5)),
                        painter = rememberAsyncImagePainter(model = Uri.parse(photo)),
                        contentDescription = stringResource(id = R.string.product_image_label),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Column {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    text = name,
                    style = MaterialTheme.typography.titleLarge,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    text = stringResource(id = R.string.price_tag, price),
                    style = MaterialTheme.typography.bodyLarge,
                    fontStyle = FontStyle.Italic,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
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
    photo: String,
    price: Float,
    quantity: Int,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = modifier,
        onClick = onClick
    ) {
        Surface(
            color = MaterialTheme.colorScheme.secondaryContainer
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                if (photo.isEmpty()) {
                    Image(
                        modifier = Modifier
                            .size(200.dp)
                            .padding(16.dp)
                            .clip(RoundedCornerShape(5)),
                        imageVector = Icons.Filled.Image,
                        contentDescription = stringResource(id = R.string.product_image_label),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        modifier = Modifier
                            .size(200.dp)
                            .padding(16.dp)
                            .clip(RoundedCornerShape(5)),
                        painter = rememberAsyncImagePainter(model = Uri.parse(photo)),
                        contentDescription = stringResource(id = R.string.product_image_label),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .padding(horizontal = 8.dp),
                text = name,
                style = MaterialTheme.typography.titleLarge,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Text(
                modifier = Modifier
                    .padding(horizontal = 8.dp),
                text = stringResource(id = R.string.price_tag, price),
                style = MaterialTheme.typography.bodyLarge,
                fontStyle = FontStyle.Italic,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Text(
                modifier = Modifier
                    .padding(horizontal = 8.dp),
                text = stringResource(id = R.string.quantity_tag, quantity),
                style = MaterialTheme.typography.bodyLarge,
                fontStyle = FontStyle.Italic,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleItemList(
    modifier: Modifier,
    itemName: String,
    imageVector: ImageVector,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = modifier,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .sizeIn(minHeight = 68.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .weight(1F, true)
                    .padding(16.dp),
                text = itemName,
                style = MaterialTheme.typography.titleMedium
            )
            Icon(
                modifier = Modifier.padding(8.dp),
                imageVector = imageVector,
                contentDescription = null
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonItemList(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    description: String,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = modifier,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F, true)
            ) {
                Text(
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                    text = subtitle,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    text = description,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Icon(
                modifier = Modifier.padding(8.dp),
                imageVector = Icons.Filled.ArrowForward,
                contentDescription = null
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonPhotoItem(
    title: String,
    subtitle: String,
    photo: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    ElevatedCard(
        modifier = modifier,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (photo.isEmpty()) {
                Image(
                    modifier = Modifier
                        .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
                        .size(64.dp)
                        .clip(RoundedCornerShape(5)),
                    imageVector = Icons.Filled.Image,
                    contentDescription = stringResource(id = R.string.item_image),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    modifier = Modifier
                        .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
                        .size(64.dp)
                        .clip(RoundedCornerShape(5)),
                    painter = rememberAsyncImagePainter(model = photo),
                    contentDescription = stringResource(id = R.string.item_image),
                    contentScale = ContentScale.Crop
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F, true)
            ) {
                Text(
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HorizontalItemList(
    title: String,
    subtitle: String,
    description: String,
    photo: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    ElevatedCard(
        modifier = modifier,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (photo.isEmpty()) {
                Image(
                    modifier = Modifier
                        .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
                        .size(64.dp)
                        .clip(RoundedCornerShape(5)),
                    imageVector = Icons.Filled.Image,
                    contentDescription = stringResource(id = R.string.item_image),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    modifier = Modifier
                        .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
                        .size(64.dp)
                        .clip(RoundedCornerShape(5)),
                    painter = rememberAsyncImagePainter(model = photo),
                    contentDescription = stringResource(id = R.string.item_image),
                    contentScale = ContentScale.Crop
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F, true)
            ) {
                Text(
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    text = description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
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
            HorizontalProductItem(
                modifier = Modifier.fillMaxSize(),
                name = "Essencial",
                price = 178.99F,
                photo = "",
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
                photo = "",
                price = 178.99f,
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
            SimpleItemList(
                modifier = Modifier.fillMaxWidth(),
                itemName = "Perfumes",
                imageVector = Icons.Filled.ArrowForward,
                onClick = {}
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
private fun SaleItemListPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            CommonItemList(
                title = "Bruno",
                subtitle = "Essencial",
                description = "Feb 19, 2003",
                onClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PhotoItemPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            CommonPhotoItem(
                title = "Bruno",
                subtitle = "Rua 15 de novembro",
                photo = "",
                onClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HorizontalItemListPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            HorizontalItemList(
                title = "Bruno",
                subtitle = "Rua 15 de novembro",
                description = "Test",
                photo = "",
                onClick = {}
            )
        }
    }
}