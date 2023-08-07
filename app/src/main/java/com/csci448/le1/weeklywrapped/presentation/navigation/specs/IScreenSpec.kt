package com.csci448.le1.weeklywrapped.presentation.navigation.specs

import com.csci448.le1.weeklywrapped.presentation.viewmodel.WrappedViewModel

import android.content.Context
import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope


sealed interface IScreenSpec {
    companion object {
        private const val LOG_TAG = "448.IScreenSpec"

        val allScreens = IScreenSpec::class.sealedSubclasses.associate {
            Log.d(LOG_TAG, "allScreens: mapping route \"${it.objectInstance?.route ?: ""}\" to object \"${it.objectInstance}\"")
            it.objectInstance?.route to it.objectInstance
        }
        const val root = "wrapped"
        val startDestination = DefaultScreenSpec.route

        @Composable
        fun TopBar(
            wvm: WrappedViewModel,
            navHostController: NavHostController,
            navBackStackEntry: NavBackStackEntry?,
            context: Context
        ) {
            val route = navBackStackEntry?.destination?.route ?: ""
            allScreens[route]?.TopAppBarContent(
                wvm = wvm,
                navHostController = navHostController,
                navBackStackEntry = navBackStackEntry,
                context = context
            )
        }
    }

    val route: String
    val arguments: List<NamedNavArgument>
    fun buildRoute(vararg args: String?): String

    @get:StringRes
    val title: Int

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun TopAppBarContent(
        wvm: WrappedViewModel,
        navHostController: NavHostController,
        navBackStackEntry: NavBackStackEntry?,
        context: Context
    ) {
        val context =
            TopAppBar(
                navigationIcon = if (navHostController.previousBackStackEntry != null) {
                    {
                        IconButton(onClick = { navHostController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = stringResource(com.csci448.le1.weeklywrapped.R.string.menu_back_desc)
                            )
                        }
                    }
                } else {
                    { }
                },
                title = {
                    Text(
                        text = stringResource(id = title)
                    )
                },
                actions = {
                    TopAppBarActions(
                        wvm = wvm,
                        navHostController = navHostController,
                        navBackStackEntry = navBackStackEntry,
                        context = context
                    )
                }
            );
    }

    @Composable
    fun TopAppBarActions(
        wvm: WrappedViewModel,
        navHostController: NavHostController,
        navBackStackEntry: NavBackStackEntry?,
        context: Context
    ) {

    }
    @Composable
    fun Content(
        wvm: WrappedViewModel,
        navController: NavHostController,
        navBackStackEntry: NavBackStackEntry,
        context: Context,
        takePicture: () -> Unit,
        scheduleNotification: () -> Unit,
        coroutineScope: CoroutineScope,
        snackbarCoroutineScope: CoroutineScope,
    )

}