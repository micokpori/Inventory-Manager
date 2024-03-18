package com.rfcreations.inventorymanager.ui.screens.moreinfoscreen.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Composable function representing an info card with an icon and a title.
 *
 * @param title The title of the info card.
 * @param icon The icon for the info card.
 * @param modifier Optional modifier for the card.
 * @param onClick The action to perform when the card is clicked.
 */
@Composable
fun InfoCard(
    title: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        onClick,
        modifier.padding(8.dp),
        shape = RoundedCornerShape(10.dp),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, null, modifier = Modifier.padding(6.dp))
            Text(text = title, fontWeight = FontWeight.Bold)
        }
    }
}