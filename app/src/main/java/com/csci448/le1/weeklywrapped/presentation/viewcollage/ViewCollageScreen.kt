package com.csci448.le1.weeklywrapped.presentation.viewcollage

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
import coil.compose.AsyncImage
import com.csci448.le1.weeklywrapped.R
import com.csci448.le1.weeklywrapped.data.Collage
import com.csci448.le1.weeklywrapped.presentation.list.CollageList
import com.csci448.le1.weeklywrapped.presentation.list.PhotographList
import com.csci448.le1.weeklywrapped.presentation.viewmodel.WrappedViewModel
import java.util.*

@Composable
fun ViewScreen(
    wvm: WrappedViewModel,
    onEditButton: () -> Unit,
    onShareButton: (Collage) -> Unit,
    onDeleteCollage: (Collage) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        val showShareDialog = remember { mutableStateOf(false) }
        val showDeleteDialog = remember { mutableStateOf(false) }
        val currentCollage = remember {mutableStateOf(Collage("", UUID.randomUUID(),""))}
        if (showShareDialog.value) {
            AlertDialog(
                onDismissRequest = { showShareDialog.value = false },
                title = { Text(stringResource(R.string.share_collage)) },
                text = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(stringResource(R.string.share_collage_confirm))
                        AsyncImage(
                            model = currentCollage.value.collageURI,
                            contentDescription = currentCollage.value.collageFileName,
                            modifier = Modifier
                                .size(100.dp, 100.dp)
                                .fillMaxWidth()
                        )
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
                            onShareButton(currentCollage.value)
                            showShareDialog.value = false
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
                        onClick = { showShareDialog.value = false }
                    ) {
                        Text("No")
                    }
                }
            )
        }
        if (showDeleteDialog.value) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog.value = false },
            title = { Text(stringResource(R.string.delete_collage)) },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(stringResource(R.string.delete_collage_confirm))
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
                        onDeleteCollage(currentCollage.value)
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

        var collageList = wvm.collageListStateFlow.collectAsState().value

        if(collageList.isNotEmpty()) {
            Column(modifier = Modifier.weight(0.9f)) {
                CollageList(
                    collages = collageList,
                    onSelectCollage = {selectedCollage ->
                        currentCollage.value = selectedCollage
                        showShareDialog.value = true
                    },
                    onDeleteCollage = {selectedCollage ->
                        currentCollage.value = selectedCollage
                        showDeleteDialog.value = true
                    },
                )
            }
        }
        else {
            Column(
                modifier = Modifier.weight(0.9f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = stringResource(id = R.string.empty_collage))
            }
        }

        Row(
            modifier = Modifier
                .weight(0.1f)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = onEditButton,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
            shape = RoundedCornerShape(4.dp)
            ) {
                Text(text = stringResource(id = R.string.edit_button_text))
            }
        }
    }
}