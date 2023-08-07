package com.csci448.le1.weeklywrapped.presentation.camera

import com.csci448.le1.weeklywrapped.R
import com.csci448.le1.weeklywrapped.presentation.viewmodel.WrappedViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.csci448.le1.weeklywrapped.data.Photograph
import com.csci448.le1.weeklywrapped.presentation.list.PhotographList
import java.util.*

@Composable
fun CameraScreen(
    wvm: WrappedViewModel,
    onDeletePhotograph: (Photograph) -> Unit,
) {

        val photographList = wvm.photographListStateFlow.collectAsState().value
        val showDeleteDialog = remember { mutableStateOf(false) }
        val currentPhoto = remember {mutableStateOf(Photograph("", UUID.randomUUID(), ""))}

    if (showDeleteDialog.value) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog.value = false },
            title = { Text(stringResource(R.string.delete_photo)) },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(stringResource(R.string.delete_photo_confirm))
                }
            },
            confirmButton = {
                Button(
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    onClick = {
                        onDeletePhotograph(currentPhoto.value)
                        showDeleteDialog.value = false
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    onClick = { showDeleteDialog.value = false }
                ) {
                    Text("No")
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 20.dp),
        contentAlignment = Alignment.Center

    ) {
        if(photographList.isNotEmpty()) {
            PhotographList(photographs = photographList,
                onSelectPhotograph = {},
                onDeletePhotograph = {
                    photograph ->
                    currentPhoto.value = photograph
                    showDeleteDialog.value = true
                })
        } else {
            Text(text = stringResource(id = R.string.empty_photo))
        }


    }
}

