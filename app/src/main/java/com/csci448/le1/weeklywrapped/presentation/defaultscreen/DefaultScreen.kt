package com.csci448.le1.weeklywrapped.presentation.defaultscreen


import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PhotoAlbum
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.csci448.le1.weeklywrapped.presentation.viewmodel.WrappedViewModel
import com.csci448.le1.weeklywrapped.R

@Composable
fun DefaultScreen(
    wvm: WrappedViewModel,
    onPhotosButton: () -> Unit,
    onCollagesButton: () -> Unit,
    onNotify: () -> Unit,
    takePicture: () -> Unit,
    context: Context,
    cameraEnabled: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            if (!cameraEnabled){
                Box() {
                    Text(stringResource(id = R.string.default_prompt), textAlign = TextAlign.Center)
                }
                Box() {
                    Text(stringResource(id = R.string.default_prompt2), textAlign = TextAlign.Center)
                }
                Button(
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    enabled = true,
                    onClick = { onNotify() }
                ) {
                    Text(text = stringResource(id = R.string.notify_me_later))
                }
            } else {
                Box() {
                    Text(stringResource(id = R.string.default_prompt3), textAlign = TextAlign.Center)
                }
                Box() {
                    Text(stringResource(id = R.string.default_prompt4), textAlign = TextAlign.Center)
                }
                Box() {
                    Text(stringResource(id = R.string.default_prompt5), textAlign = TextAlign.Center)
                }
            }

        }
        Row() {

            val backgroundColor = if (cameraEnabled) R.color.dark_blue else R.color.gray
            val tintColor = if (cameraEnabled) R.color.white else R.color.light_gray

            IconButton(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(colorResource(id = backgroundColor)),
                onClick = takePicture,
                enabled = cameraEnabled
            ) {
                Icon(
                    imageVector = Icons.Default.PhotoCamera,
                    contentDescription = stringResource(id = R.string.camera_desc),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(all = 10.dp),
                    tint = colorResource(id = tintColor),
                )
            }
        }

        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {

            Button(
                onClick = onPhotosButton,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier.padding(all = 5.dp)
            ){
                Icon(imageVector = Icons.Default.PhotoAlbum, modifier = Modifier.size(30.dp), contentDescription = stringResource(
                    id = R.string.photo_album_desc
                ))
                Text(text = stringResource(id = R.string.photos_button_text))
            }
            Button(
                onClick = onCollagesButton,
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                modifier = Modifier.padding(all = 5.dp)
            ) {
                Icon(imageVector = Icons.Default.PhotoLibrary, modifier = Modifier.size(30.dp), contentDescription = stringResource(
                    id = R.string.collage_icon_desc
                ))
                Text(text = stringResource(id = R.string.collages_button_text))
            }
        }
    }
}