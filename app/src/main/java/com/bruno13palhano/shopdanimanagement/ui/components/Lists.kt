package com.bruno13palhano.shopdanimanagement.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.bruno13palhano.shopdanimanagement.R

@Composable
fun StockItem(
    modifier: Modifier,
    name: String,
    photo: ByteArray,
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
                        modifier =
                            Modifier
                                .size(200.dp)
                                .padding(16.dp)
                                .clip(RoundedCornerShape(5)),
                        imageVector = Icons.Filled.Image,
                        contentDescription = stringResource(id = R.string.product_image_label),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        modifier =
                            Modifier
                                .size(200.dp)
                                .padding(16.dp)
                                .clip(RoundedCornerShape(5)),
                        painter = rememberAsyncImagePainter(model = photo),
                        contentDescription = stringResource(id = R.string.product_image_label),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier =
                    Modifier
                        .padding(horizontal = 8.dp),
                text = name,
                style = MaterialTheme.typography.titleLarge,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Text(
                modifier =
                    Modifier
                        .padding(horizontal = 8.dp),
                text = stringResource(id = R.string.price_tag, price),
                style = MaterialTheme.typography.bodyLarge,
                fontStyle = FontStyle.Italic,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Text(
                modifier =
                    Modifier
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
            modifier =
                Modifier
                    .fillMaxWidth()
                    .sizeIn(minHeight = 68.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier =
                    Modifier
                        .weight(1F, true)
                        .padding(16.dp),
                text = itemName,
                style = MaterialTheme.typography.titleMedium
            )
            Icon(
                modifier = Modifier.padding(16.dp),
                imageVector = imageVector,
                contentDescription = null
            )
        }
    }
}

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
                modifier =
                    Modifier
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
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null
            )
        }
    }
}

@Composable
fun CommonPhotoItemList(
    title: String,
    subtitle: String,
    photo: ByteArray,
    modifier: Modifier = Modifier,
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
            if (photo.isEmpty()) {
                Image(
                    modifier =
                        Modifier
                            .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
                            .size(64.dp)
                            .clip(RoundedCornerShape(5)),
                    imageVector = Icons.Filled.Image,
                    contentDescription = stringResource(id = R.string.item_image),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    modifier =
                        Modifier
                            .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
                            .size(64.dp)
                            .clip(RoundedCornerShape(5)),
                    painter = rememberAsyncImagePainter(model = photo),
                    contentDescription = stringResource(id = R.string.item_image),
                    contentScale = ContentScale.Crop
                )
            }
            Column(
                modifier =
                    Modifier
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

@Composable
fun HorizontalItemList(
    title: String,
    subtitle: String,
    description: String,
    photo: ByteArray,
    modifier: Modifier = Modifier,
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
            if (photo.isEmpty()) {
                Image(
                    modifier =
                        Modifier
                            .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
                            .size(64.dp)
                            .clip(RoundedCornerShape(5)),
                    imageVector = Icons.Filled.Image,
                    contentDescription = stringResource(id = R.string.item_image),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    modifier =
                        Modifier
                            .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
                            .size(64.dp)
                            .clip(RoundedCornerShape(5)),
                    painter = rememberAsyncImagePainter(model = photo),
                    contentDescription = stringResource(id = R.string.item_image),
                    contentScale = ContentScale.Crop
                )
            }
            Column(
                modifier =
                    Modifier
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

@Composable
fun CircularItemList(
    modifier: Modifier = Modifier,
    title: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ElevatedCard(
            modifier =
                Modifier
                    .padding(2.dp)
                    .size(104.dp),
            shape = CircleShape,
            onClick = onClick
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(48.dp),
                    imageVector = icon,
                    contentDescription = null
                )
            }
        }
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = title,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun InfoItemList(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    title: String,
    subtitle: String,
    description: String,
    onEditClick: () -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier =
            Modifier
                .clickable { expanded = !expanded }
                .padding(contentPadding)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier =
                    modifier
                        .weight(1F, true),
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontStyle = FontStyle.Italic
            )

            if (expanded) {
                IconButton(onClick = onEditClick) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = stringResource(id = R.string.edit_label)
                    )
                }
            } else {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = stringResource(id = R.string.expand_item_label)
                    )
                }
            }
        }

        AnimatedVisibility(visible = expanded) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .weight(1F, true)
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = subtitle,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun CatalogItemList(
    modifier: Modifier = Modifier,
    photo: ByteArray,
    title: String,
    firstSubtitle: String,
    secondSubtitle: String,
    description: String,
    footer: String,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = modifier,
        onClick = onClick
    ) {
        if (photo.isEmpty()) {
            Image(
                modifier =
                    Modifier
                        .padding(16.dp)
                        .size(144.dp)
                        .clip(RoundedCornerShape(5))
                        .align(Alignment.CenterHorizontally),
                imageVector = Icons.Filled.Image,
                contentDescription = stringResource(id = R.string.item_image),
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                modifier =
                    Modifier
                        .padding(16.dp)
                        .size(144.dp)
                        .clip(RoundedCornerShape(5))
                        .align(Alignment.CenterHorizontally),
                painter = rememberAsyncImagePainter(model = photo),
                contentDescription = stringResource(id = R.string.item_image),
                contentScale = ContentScale.Crop
            )
        }
        Text(
            modifier =
                Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .fillMaxWidth(),
            text = title,
            maxLines = 1,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            modifier =
                Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .fillMaxWidth(),
            text = firstSubtitle,
            maxLines = 1,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleSmall,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            modifier =
                Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .fillMaxWidth(),
            text = secondSubtitle,
            maxLines = 1,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            fontStyle = FontStyle.Italic,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            modifier =
                Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .fillMaxWidth(),
            text = description,
            maxLines = 1,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            fontStyle = FontStyle.Italic,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            modifier =
                Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                    .fillMaxWidth(),
            text = footer,
            maxLines = 1,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun SimpleExpandedItem(
    modifier: Modifier = Modifier,
    title: String,
    item1: String,
    item2: String,
    item3: String,
    item4: String,
    item5: String,
    onEditClick: () -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    ElevatedCard(
        modifier = modifier,
        onClick = { expanded = !expanded }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1F, true)) {
                Text(
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    text = item1,
                    style = MaterialTheme.typography.bodyMedium,
                    fontStyle = FontStyle.Italic
                )
            }
            if (expanded) {
                IconButton(onClick = onEditClick) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = stringResource(id = R.string.edit_item_label)
                    )
                }
            } else {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = stringResource(id = R.string.expand_item_label)
                    )
                }
            }
        }

        AnimatedVisibility(visible = expanded) {
            Column {
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface)

                Row(
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null
                    )
                    Text(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        text = item2,
                        fontStyle = FontStyle.Italic,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Row(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null
                    )
                    Text(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        text = item3,
                        fontStyle = FontStyle.Italic,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Row(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null
                    )
                    Text(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        text = item4,
                        fontStyle = FontStyle.Italic,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Row(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null
                    )
                    Text(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        text = item5,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                HorizontalDivider()
            }
        }
    }
}

@Composable
fun ExpandedItem(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    description: String,
    photo: ByteArray,
    onEditClick: () -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    ElevatedCard(
        modifier = modifier,
        onClick = { expanded = !expanded },
        colors =
            if (expanded) {
                CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            } else {
                CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            }
    ) {
        AnimatedVisibility(expanded) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (photo.isEmpty()) {
                    Image(
                        modifier =
                            Modifier
                                .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
                                .size(64.dp)
                                .clip(RoundedCornerShape(5)),
                        imageVector = Icons.Filled.Image,
                        contentDescription = stringResource(id = R.string.item_image),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        modifier =
                            Modifier
                                .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
                                .size(64.dp)
                                .clip(RoundedCornerShape(5)),
                        painter = rememberAsyncImagePainter(model = photo),
                        contentDescription = stringResource(id = R.string.item_image),
                        contentScale = ContentScale.Crop
                    )
                }
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .weight(1F, true)
                ) {
                    Text(
                        modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                        text = title,
                        maxLines = 1,
                        style = MaterialTheme.typography.titleMedium,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                        text = subtitle,
                        maxLines = 1,
                        style = MaterialTheme.typography.bodyMedium,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                        text = description,
                        maxLines = 1,
                        style = MaterialTheme.typography.bodyMedium,
                        fontStyle = FontStyle.Italic,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                IconButton(onClick = onEditClick) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = stringResource(id = R.string.edit_label)
                    )
                }
            }
        }

        AnimatedVisibility(!expanded) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .sizeIn(minHeight = 68.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier =
                        Modifier
                            .weight(1F, true)
                            .padding(16.dp),
                    text = title,
                    maxLines = 1,
                    style = MaterialTheme.typography.titleMedium,
                    overflow = TextOverflow.Ellipsis
                )
                Icon(
                    modifier = Modifier.padding(16.dp),
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = stringResource(id = R.string.expand_item_label)
                )
            }
        }
    }
}