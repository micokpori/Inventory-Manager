package com.rfcreations.inventorymanager.ui.commons

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.rfcreations.inventorymanager.R


@Composable
fun ImageCard(imagePath: Uri, modifier: Modifier = Modifier) {
    Log.d("ItemCompose", "ImageCard: Creating ImageCard with image path: $imagePath")
    AsyncImage(
        model = ImageRequest.Builder(context = LocalContext.current)
            .data(imagePath)
            .crossfade(true)
            .build(),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        error = painterResource(R.drawable.ic_broken_image),
        placeholder = painterResource(R.drawable.loading_img),
        modifier = modifier
    )
}