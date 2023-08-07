package com.csci448.le1.weeklywrapped.presentation.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.csci448.le1.weeklywrapped.BuildConfig
import com.csci448.le1.weeklywrapped.R
import com.csci448.le1.weeklywrapped.data.Photograph
import java.io.File
import java.util.*

@Composable
fun PhotographList(
    photographs: List<Photograph>,
    onSelectPhotograph: (selectedPhotograph: Photograph) -> Unit,
    onDeletePhotograph: (photographToDelete: Photograph) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxWidth(),
        columns = GridCells.Fixed(3)
    ) {
        items(photographs) { photograph ->
            PhotographRow(
                photograph = photograph,
                onSelectPhotograph = onSelectPhotograph,
                onDeletePhotograph = onDeletePhotograph
            )
        }
    }
}

@Composable
private fun PhotographRow(
    photograph: Photograph,
    onSelectPhotograph: (selectedPhotograph: Photograph) -> Unit,
    onDeletePhotograph: (photographToDelete: Photograph) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelectPhotograph(photograph) },
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImage(
                model = photograph.photoURI,
                contentDescription = photograph.photographFileName,
                modifier = Modifier
                    .size(100.dp, 100.dp)
                    .fillMaxWidth()
            )
            IconButton(
                onClick = { onDeletePhotograph(photograph) }
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = stringResource(id = R.string.content_description_delete_photograph)
                )
            }
        }
    }
}