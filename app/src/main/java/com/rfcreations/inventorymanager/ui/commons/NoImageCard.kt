package com.rfcreations.inventorymanager.ui.commons

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

@Composable
fun NoImageCard(
    @DrawableRes painter:Int,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = painter),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        alignment = Alignment.Center,
        modifier = modifier
    )
}
