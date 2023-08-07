package com.csci448.le1.weeklywrapped.presentation.navigation.specs

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.csci448.le1.weeklywrapped.R
import com.csci448.le1.weeklywrapped.presentation.viewmodel.WrappedViewModel
import com.csci448.le1.weeklywrapped.presentation.viewcollage.ViewScreen
import kotlinx.coroutines.CoroutineScope
import java.io.File


object ViewCollageScreenSpec : IScreenSpec {

    override val route = "view"
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
        ViewScreen(wvm = wvm,
            onEditButton = {
                navController.navigate("edit")
            },
            onShareButton = { collage ->
                //TODO Popup for sharing options
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "image/*"

                val uri = Uri.parse(collage.collageURI)

                shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Share Image")
                context.startActivity(Intent.createChooser(shareIntent, "Share image using:"))
            },
            onDeleteCollage = { collage -> wvm.deleteCollage(collage = collage) })
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