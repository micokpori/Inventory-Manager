package com.rfcreations.inventorymanager.ui.screens.solditemsscreen

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.rfcreations.inventorymanager.R
import com.rfcreations.inventorymanager.database.salesdb.SoldItemsEntity
import com.rfcreations.inventorymanager.ui.commons.ImageCard
import com.rfcreations.inventorymanager.ui.commons.NoImageCard
import com.rfcreations.inventorymanager.ui.navigation.Screens
import com.rfcreations.inventorymanager.utils.DateConverter

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SoldItemsScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    soldItemsViewModel: SoldItemsViewModel = hiltViewModel(),
) {

    // Scroll behavior for the top app bar
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    val soldItems by soldItemsViewModel.getAllSoldItems.collectAsState(initial = emptyList())
    val groupedSoldItems = soldItems.reversed()
        .groupBy { DateConverter.convertLongToDate(it.timeOfTransaction, "hh:mm a dd/MMM/Y") }

    Scaffold(
        topBar = {
            AppBar(scrollBehavior = scrollBehavior) {
                navController.navigate(Screens.AnalyticsScreen.route)
            }
        },
    ) {
        LazyColumn(
            modifier
                .padding(it)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            groupedSoldItems.forEach { (date, items) ->
                stickyHeader {
                    DateHeader(date)
                }
                items(items, key = { key ->key.itemId}) { item ->
                    ItemCard(item, modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    navBackAction: () -> Unit
) {
    LargeTopAppBar(
        title = { Text(text = "Sales") },
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.largeTopAppBarColors(),
        navigationIcon = {
            IconButton(navBackAction) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
            }
        }
    )
}

@Composable
private fun ItemCard(
    item: SoldItemsEntity,
    modifier: Modifier = Modifier
) {

    val profit = (item.sellingPrice - item.costPrice) * item.quantitySold

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
      //  colors = CardDefaults.elevatedCardColors(MaterialTheme.colorScheme.surfaceContainerLowest),
       // elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(4.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            ItemImage(
                item.imagePath,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier.weight(0.6f)) {
                Text(
                    text = item.name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = DateConverter.convertLongToDate(item.timeOfTransaction, "hh mm a"),
                    fontWeight = FontWeight(120),
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Column(modifier.weight(0.4f), horizontalAlignment = Alignment.End) {
                val isProfitPositive = profit > 1
                Text(
                    text = if (isProfitPositive) "+₦$profit" else "₦$profit",
                    color = if (isProfitPositive) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.outline,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Qty:${item.quantitySold} @₦${item.sellingPrice}",
                    fontWeight = FontWeight(120),
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun ItemImage(imagePath: String?, modifier: Modifier = Modifier) {
    if (imagePath != null) {
        ImageCard(imagePath = Uri.parse(imagePath), modifier)
    } else {
        NoImageCard(painter = R.drawable.no_stock_img, modifier)
    }
}

@Composable
private fun DateHeader(date: String) {
    Text(
        text = date,
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surfaceVariant) // Header background color
            .padding(vertical = 8.dp, horizontal = 16.dp), // Header padding
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
    )
}


