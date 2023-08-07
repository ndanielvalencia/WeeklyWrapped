package com.csci448.le1.weeklywrapped.presentation.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.csci448.le1.weeklywrapped.presentation.navigation.specs.IScreenSpec
import com.csci448.le1.weeklywrapped.presentation.viewmodel.WrappedViewModel

@Composable
fun WrappedTopBar(
    navHostController: NavHostController,
    wvm: WrappedViewModel,
    context: Context
){
    val navBackStackEntryState = navHostController.currentBackStackEntryAsState()
    IScreenSpec.TopBar(
        wvm = wvm,
        navHostController = navHostController,
        navBackStackEntry = navBackStackEntryState.value,
        context = context
    )
}