package com.bruno13palhano.shopdanimanagement.ui.components

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Carousel(
    modifier: Modifier = Modifier,
    pageCount: Int,
    initialPage: Int = 0,
    pageDuration: Long = 1750,
    transitionDuration: Int = 450,
    pageContent: @Composable (page: Int) -> Unit
) {
    val pagerState =
        rememberPagerState(
            initialPage = initialPage,
            initialPageOffsetFraction = 0F
        ) { pageCount }

    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()
    if (isDragged.not()) {
        var currentKey by remember { mutableIntStateOf(0) }
        if (pageCount > 0) {
            LaunchedEffect(key1 = currentKey) {
                delay(pageDuration)
                val nextPage = (pagerState.currentPage + 1) % pageCount
                pagerState.animateScrollToPage(
                    page = nextPage,
                    animationSpec =
                        tween(
                            durationMillis = transitionDuration,
                            easing = LinearOutSlowInEasing
                        )
                )
                currentKey = nextPage
            }
        }
    }

    HorizontalPager(
        modifier = modifier,
        state = pagerState,
        pageSpacing = 8.dp
    ) { page -> pageContent(page) }
}