package com.csci448.le1.weeklywrapped.presentation.navigation.specs

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.csci448.le1.weeklywrapped.R
import com.csci448.le1.weeklywrapped.presentation.camera.CameraScreen
import com.csci448.le1.weeklywrapped.presentation.viewmodel.WrappedViewModel
import kotlinx.coroutines.CoroutineScope


object PhotosScreenSpec : IScreenSpec {

    override val route = "camera"
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
        CameraScreen(
            wvm = wvm,
            onDeletePhotograph = { photograph -> wvm.deletePhotograph(photograph = photograph) }
        )
    }

    @Composable
    override fun TopAppBarActions(
        wvm: WrappedViewModel,
        navHostController: NavHostController,
        navBackStackEntry: NavBackStackEntry?,
        context: Context
    ) {
        IconButton(onClick = {
            navHostController.navigate("info")
        }) {
            Icon(
                imageVector = Icons.Filled.Info,
                contentDescription = stringResource(id = R.string.info_button_desc)
            )
        }
    }
}