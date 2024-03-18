package com.rfcreations.inventorymanager.ui.screens.analyticsscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.rfcreations.inventorymanager.R
import com.rfcreations.inventorymanager.ui.navigation.Screens

private data class AnalyticsModel(
    val icon: ImageVector,
    val clickable: Boolean,
    val title: String,
    val value: String,
    val navAction: () -> Unit,
)

@Composable
fun AnalyticsScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    analyticsViewModel: AnalyticsViewModel = hiltViewModel()
) {
    // val getSolItems by analyticsViewModel.getAllSoldItems.collectAsState(initial = emptyList())
    LaunchedEffect(true) {
        analyticsViewModel.calcAnalytics()
    }
    val analyticUiState by analyticsViewModel.analyticUiState.collectAsState()

    Column {
        Spacer(modifier = Modifier.height(60.dp))

        Text(
            text = "Analytics",
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(50.dp))

        val models = listOf<AnalyticsModel>(

            AnalyticsModel(
                icon = ImageVector.vectorResource(R.drawable.revenue),
                clickable = true,
                title = "Total Revenue",
                value = "₦${analyticUiState.totalRevenue}",
                navAction = { navController.navigate(Screens.SoldItemsScreen.route) },
            ),
            AnalyticsModel(
                icon = ImageVector.vectorResource(R.drawable.revenue),
                clickable = true,
                title = "Today's Revenue",
                value = "₦${analyticUiState.revenueToday}",
                navAction = { navController.navigate(Screens.SoldItemsScreen.route) },
            ),
            AnalyticsModel(
                icon = ImageVector.vectorResource(R.drawable.dollar_icon),
                clickable = true,
                title = "Total Profit",
                value = "₦${analyticUiState.totalProfit}",
                navAction = { navController.navigate(Screens.SoldItemsScreen.route) },
            ),
            AnalyticsModel(
                icon = ImageVector.vectorResource(R.drawable.dollar_icon),
                clickable = true,
                title = "Profit Today",
                value = "₦${analyticUiState.profitToday}",
                navAction = { navController.navigate(Screens.SoldItemsScreen.route) },
            ),
            AnalyticsModel(
                icon = ImageVector.vectorResource(R.drawable.cart),
                clickable = true,
                title = "Total sales",
                value = "${analyticUiState.totalSales}",
                navAction = { navController.navigate(Screens.SoldItemsScreen.route) },
            ),
            AnalyticsModel(
                icon = ImageVector.vectorResource(R.drawable.cart),
                clickable = true,
                title = "Sales Today",
                value = "${analyticUiState.salesToday}",
                navAction = { navController.navigate(Screens.SoldItemsScreen.route) },
            ),
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier
        ) {

            items(models, key = { key -> key.title }) {
                AnalyticCard(
                    icon = it.icon,
                    clickable = it.clickable,
                    title = it.title,
                    value = it.value,
                    navAction = it.navAction,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun AnalyticCard(
    icon: ImageVector,
    clickable: Boolean,
    title: String,
    value: String,
    navAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier.padding(horizontal = 10.dp, vertical = 16.dp),
     //   elevation = CardDefaults.elevatedCardElevation(8.dp)
    ) {
        Column(
            modifier.then(
                if (clickable) Modifier.clickable {
                    navAction()
                } else Modifier
            ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Icon(
                icon,
                null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = value,fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = title, style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(20.dp))
        }

    }
}