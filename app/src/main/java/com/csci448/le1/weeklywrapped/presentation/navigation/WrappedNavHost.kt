package com.csci448.le1.weeklywrapped.presentation.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.csci448.le1.weeklywrapped.presentation.navigation.specs.IScreenSpec
import com.csci448.le1.weeklywrapped.presentation.viewmodel.WrappedViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun WrappedNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    wvm: WrappedViewModel,
    context: Context,
    takePicture: () -> Unit,
    scheduleNotification: () -> Unit,
    coroutineScope: CoroutineScope,
    snackbarCoroutineScope: CoroutineScope,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = IScreenSpec.root
    ) {
        navigation(
            route = IScreenSpec.root,
            startDestination = IScreenSpec.startDestination
        ) {
            IScreenSpec.allScreens.forEach { (_, screen) ->
                if(screen != null) {
                    composable(
                        route = screen.route,
                        arguments = screen.arguments
                    ) { navBackStackEntry ->
                        screen.Content(
                            navController = navController,
                            navBackStackEntry = navBackStackEntry,
                            wvm = wvm,
                            context = context,
                            takePicture = takePicture,
                            scheduleNotification = scheduleNotification,
                            coroutineScope = coroutineScope,
                            snackbarCoroutineScope = coroutineScope,
                        )
                    }
                }
            }
        }
    }
}