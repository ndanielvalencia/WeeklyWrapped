package com.csci448.le1.weeklywrapped.presentation.navigation.specs

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.csci448.le1.weeklywrapped.R
import com.csci448.le1.weeklywrapped.presentation.info.InfoScreen
import com.csci448.le1.weeklywrapped.presentation.viewmodel.WrappedViewModel
import kotlinx.coroutines.CoroutineScope


object InfoScreenSpec : IScreenSpec {

    override val route = "info"
    override val arguments: List<NamedNavArgument> = emptyList()
    override fun buildRoute(vararg args: String?) = route
    override val title = R.string.top_bar_title

    @Composable
    override fun Content(
        wvm: WrappedViewModel,
        navController: NavHostController,
        navBackStackEntry: NavBackStackEntry,
        context: Context,
        takePicture: () -> Unit,
        scheduleNotification: () -> Unit,
        coroutineScope: CoroutineScope,
        snackbarCoroutineScope: CoroutineScope,
    ) {
        InfoScreen(wvm,
        onTextButtonClick = {
            navController.navigate("default")
        });
    }

    @Composable
    override fun TopAppBarActions(
        wvm: WrappedViewModel,
        navHostController: NavHostController,
        navBackStackEntry: NavBackStackEntry?,
        context: Context
    ) {

    }
}