package com.csci448.le1.weeklywrapped.presentation.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.csci448.le1.weeklywrapped.R
import com.csci448.le1.weeklywrapped.data.Collage


@Composable
fun CollageList(
    collages: List<Collage>,
    onSelectCollage: (selectedCollage: Collage) -> Unit,
    onDeleteCollage: (collageToDelete: Collage) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxWidth(),
        columns = GridCells.Fixed(3)
    ) {
        items(collages) { collage ->
            CollageRow(
                collage = collage,
                onSelectCollage = onSelectCollage,
                onDeleteCollage = onDeleteCollage
            )
        }
    }
}

@Composable
private fun CollageRow(
    collage: Collage,
    onSelectCollage: (selectedCollage: Collage) -> Unit,
    onDeleteCollage: (collageToDelete: Collage) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelectCollage(collage) },
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImage(
                model = collage.collageURI,
                contentDescription = collage.collageFileName,
                modifier = Modifier
                    .size(100.dp, 100.dp)
                    .fillMaxWidth()
            )
            IconButton(
                onClick = { onDeleteCollage(collage) }
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = stringResource(id = R.string.content_description_delete_collage)
                )
            }
        }
    }
}