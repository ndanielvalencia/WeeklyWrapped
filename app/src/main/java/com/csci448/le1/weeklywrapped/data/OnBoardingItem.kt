package com.csci448.le1.weeklywrapped.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class OnBoardingItem(
    @DrawableRes val imageId: Int,
    @StringRes val titleId: Int,
    @StringRes val descriptionId: Int,
    @DrawableRes val iconId: Int,
) {
}