package com.rfcreations.inventorymanager.ui.screens.homescreen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rfcreations.inventorymanager.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortBottomSheet(
    selectedSortMethod: Int,
    changeSortMethod: (Int) -> Unit,
    dismissRequest: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = dismissRequest
    ) {
        Title(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .align(Alignment.Start)
        )
        SortItem(
            selectedSortMethod,
            modifier = Modifier.fillMaxWidth()
        ) {
            changeSortMethod(it)
        }
        Spacer(modifier = Modifier.height(20.dp))

    }
}

@Composable
private fun Title(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(id = R.string.sort_by),
        style = typography.bodySmall,
        modifier = modifier,
        color = colorScheme.tertiary
    )
}

@Composable
private fun SortItem(
    selectedSortMethod: Int,
    modifier: Modifier = Modifier,
    changeSortMethod: (Int) -> Unit,
) {
    val sortOptions = stringArrayResource(id = R.array.sort_options)
    sortOptions.forEachIndexed { index, sortMethod ->
        Row(
            modifier = modifier
                .clickable {
                    changeSortMethod(index)
                }
                .padding(horizontal = 12.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,

            ) {
            Text(text = sortMethod, modifier.weight(0.8f))
            if (selectedSortMethod == index) {
                Icon(
                    modifier = modifier.weight(0.2f),
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = colorScheme.primary
                )
            }
        }
    }
}