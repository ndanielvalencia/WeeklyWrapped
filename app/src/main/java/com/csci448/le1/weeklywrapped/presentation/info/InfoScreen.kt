@file:OptIn(ExperimentalPagerApi::class)

package com.csci448.le1.weeklywrapped.presentation.info

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import com.csci448.le1.weeklywrapped.presentation.viewmodel.WrappedViewModel
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.csci448.le1.weeklywrapped.R
import com.csci448.le1.weeklywrapped.data.OnBoardingItem
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import kotlinx.coroutines.CoroutineScope


@OptIn(ExperimentalPagerApi::class)
@Composable
fun InfoScreen(
    wvm: WrappedViewModel,
    onTextButtonClick: () -> Unit = {}
) {
    val items = wvm.onboardingItems
    val scope = rememberCoroutineScope()
    val pageState = rememberPagerState()

    Column(modifier = Modifier.fillMaxSize()) {

        HorizontalPager(
            count = items.size,
            state = pageState,
            modifier = Modifier
                .fillMaxHeight(0.9f)
                .fillMaxWidth()
        ) { page ->
            OnBoardingItem(item = items[page])
        }
        BottomSection(
            size = items.size,
            index = pageState.currentPage,
            onNextButtonClick = {
                if (pageState.currentPage + 1 < items.size) scope.launch {
                    pageState.scrollToPage(pageState.currentPage + 1)
                }
            },
            onPreviousButtonClick = {
                if (pageState.currentPage + 1 == items.size) scope.launch {
                    pageState.scrollToPage(pageState.currentPage - 1)
                }
            },
            onTextButtonClick = onTextButtonClick
        )
    }

}

@Composable
fun BottomSection(
    size: Int,
    index: Int,
    onNextButtonClick: () -> Unit = {},
    onPreviousButtonClick: () -> Unit = {},
    onTextButtonClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        // Indicators
        Indicators(size, index)
        if (index + 1 < size) {
            //  Right arrow button, only shows up on the first screen
            FloatingActionButton(
                onClick = onNextButtonClick,
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clip(RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp))
            ) {
                Icon(
                    Icons.Outlined.KeyboardArrowRight,
                    tint = Color.White,
                    contentDescription = stringResource(id = R.string.info_next_button_desc)
                )
            }
        } else {
            FloatingActionButton(
                onClick = onPreviousButtonClick,
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .clip(RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp))
            ) {
                Icon(
                    Icons.Outlined.KeyboardArrowLeft,
                    tint = Color.White,
                    contentDescription = stringResource(id = R.string.info_next_button_desc)
                )
            }
            TextButton(
                onClick = onTextButtonClick,
                modifier = Modifier.align(Alignment.CenterEnd),
            ) {
                Text(
                    text = stringResource(id = R.string.info_take_me_button_desc),
                    style = TextStyle(color = MaterialTheme.colorScheme.primary, fontSize = 18.sp),
                    softWrap = true,
                    minLines = 2,
                )
            }
        }

    }
}

@Composable
fun BoxScope.Indicators(size: Int, index: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.align(Alignment.Center)
    ) {
        repeat(size) {
            Indicator(isSelected = it == index)
        }
    }
}

@Composable
fun Indicator(isSelected: Boolean) {
    val width = animateDpAsState(
        targetValue = if (isSelected) 25.dp else 10.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )

    Box(
        modifier = Modifier
            .height(10.dp)
            .width(width.value)
            .clip(CircleShape)
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
            )
    ) {

    }
}

@Composable
fun OnBoardingItem(item: OnBoardingItem) {
    val scaledWidth = LocalConfiguration.current.screenWidthDp.dp
    val scaledHeight = LocalConfiguration.current.screenHeightDp.dp


    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            contentScale = ContentScale.Crop,

            painter = painterResource(id = item.imageId),
            contentDescription = stringResource(id = R.string.info_screen_picture_desc),
            modifier = Modifier
                .blur(radius = 10.dp)
                .size(scaledWidth, scaledHeight)
                .fillMaxWidth(),
            colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply {
                setToScale(
                    0.5f,
                    0.5f,
                    0.5f,
                    1f
                )
            }),
        )

        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(all = 20.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = item.titleId),
                modifier = Modifier
                    .padding(all = 10.dp),
                style = TextStyle(
                    fontSize = 36.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    shadow = Shadow(
                        color = Color.Black,
                        offset = Offset(4f, 4f),
                        blurRadius = 8f
                    )
                )
            )
        }
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(all = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = item.descriptionId),
                modifier = Modifier

                    .padding(all = 10.dp),
                style = TextStyle(
                    fontSize = 24.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            )

            Box(modifier = Modifier.size(size = 120.dp).padding(all = 20.dp)) {
                Image(

                    painter = painterResource(id = item.iconId),
                    stringResource(id = R.string.info_icon_picture_desc),
                    modifier = Modifier
                        .fillMaxSize(),
                )
            }
        }


    }

}