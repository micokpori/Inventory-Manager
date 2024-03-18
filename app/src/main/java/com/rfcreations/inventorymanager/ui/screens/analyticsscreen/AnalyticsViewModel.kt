package com.rfcreations.inventorymanager.ui.screens.analyticsscreen

import androidx.lifecycle.ViewModel
import com.rfcreations.inventorymanager.database.inventorydb.InventoryItemEntity
import com.rfcreations.inventorymanager.database.salesdb.SoldItemsEntity
import com.rfcreations.inventorymanager.repository.inventoryrepository.InventoryRepository
import com.rfcreations.inventorymanager.repository.soldItemsRepository.SoldItemsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val soldItemsRepository: SoldItemsRepository,
    private val inventoryRepository: InventoryRepository
) : ViewModel() {

    private val getAllSoldItems: Flow<List<SoldItemsEntity>> by lazy { soldItemsRepository.getAllSoldItems() }
    private val inventoryItems: Flow<List<InventoryItemEntity>> by lazy { inventoryRepository.getAllInventoryItems() }

    private val _analyticUiState = MutableStateFlow(AnalyticUiState())
    val analyticUiState = _analyticUiState.asStateFlow()


    suspend fun calcAnalytics() {
        coroutineScope {

            val today = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            val todaySoldItems = getAllSoldItems.first().filter {
                val transactionDate = Calendar.getInstance().apply {
                    timeInMillis = it.timeOfTransaction
                }
                transactionDate.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                        transactionDate.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                        transactionDate.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH)
            }

            //calc total revenue
            launch {
                _analyticUiState.update { uiState ->
                    val totalRevenue = getAllSoldItems.first().sumOf { it.sellingPrice }
                    uiState.copy(totalRevenue = totalRevenue)
                }
            }
            //calc today's revenue
            launch {
                _analyticUiState.update { uiState ->
                    val revenueToday = todaySoldItems.sumOf { it.sellingPrice }
                    uiState.copy(revenueToday = revenueToday)
                }
            }
            //calc total profit made
            launch {
                _analyticUiState.update { uiState ->
                    val totalProfit =
                        getAllSoldItems.first().sumOf { it.sellingPrice - it.costPrice }
                    uiState.copy(totalProfit = totalProfit)
                }

            }
            //calc profit today
            launch {
                _analyticUiState.update { uiState ->
                    val profitToday =
                        todaySoldItems.sumOf { it.sellingPrice - it.costPrice }
                    uiState.copy(profitToday = profitToday)
                }
            }
            //calc total sales
            launch {
                _analyticUiState.update {
                    val totalSales = getAllSoldItems.first().size
                    it.copy(totalSales = totalSales)

                }
            }
            //calc sales made today
            launch {
                _analyticUiState.update { uiState ->
                    val salesToday = todaySoldItems.size
                    uiState.copy(salesToday = salesToday)
                }
            }

        }
    }
}