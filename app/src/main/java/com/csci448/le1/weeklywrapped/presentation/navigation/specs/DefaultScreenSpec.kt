package com.csci448.le1.weeklywrapped.presentation.navigation.specs

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.csci448.le1.weeklywrapped.MainActivity
import com.csci448.le1.weeklywrapped.presentation.defaultscreen.DefaultScreen
import com.csci448.le1.weeklywrapped.presentation.viewmodel.WrappedViewModel
import com.csci448.le1.weeklywrapped.R
import kotlinx.coroutines.CoroutineScope

object DefaultScreenSpec : IScreenSpec {

    override val route = "default"
    override val arguments: List<NamedNavArgument> = emptyList()
    override fun buildRoute(vararg args: String?) = route
    override val title = R.string.top_bar_title

    val LOG_TAG = "448.defaultScreenSpec"


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

        val mainActivity = context as MainActivity
        val cameraEnabled = wvm.cameraEnabled.collectAsStateWithLifecycle(lifecycle = mainActivity.lifecycle).value

        Log.d("448.MainActivity", "cameraEnabled in default screen: $cameraEnabled")
        DefaultScreen(wvm,
            onPhotosButton = {
                navController.navigate("camera")
            },
            onCollagesButton = {
                navController.navigate("view")
            },
            onNotify = {
                Toast
                    .makeText(context,
                        R.string.notification_text,
                        Toast.LENGTH_SHORT)
                    .show()
                scheduleNotification()
            },
            takePicture = takePicture,
            context = context,
            cameraEnabled = cameraEnabled.enabled);
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