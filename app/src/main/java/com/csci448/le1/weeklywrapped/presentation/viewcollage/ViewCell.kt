package com.csci448.le1.weeklywrapped.presentation.viewcollage

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.csci448.le1.weeklywrapped.R
import com.csci448.le1.weeklywrapped.presentation.viewmodel.WrappedViewModel

@Composable
fun ViewCell(
    wvm: WrappedViewModel
) {
        Image(
            painter = painterResource(id = R.drawable.donkeys),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
}