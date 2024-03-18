package com.rfcreations.inventorymanager.ui.screens.homescreen

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.rfcreations.inventorymanager.R
import com.rfcreations.inventorymanager.database.cartdb.CartEntity
import com.rfcreations.inventorymanager.ui.commons.ExitDialog
import com.rfcreations.inventorymanager.ui.navigation.Screens
import com.rfcreations.inventorymanager.ui.screens.homescreen.components.AddItemFabButton
import com.rfcreations.inventorymanager.ui.screens.homescreen.components.InventoryHomeAppBar
import com.rfcreations.inventorymanager.ui.screens.homescreen.components.InventoryItem

@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    // State to control whether to show the exit dialog
    val showExitDialog = rememberSaveable { mutableStateOf(false) }

    // Handle back button press to show exit dialog
    BackHandler {
        showExitDialog.value = true
    }

    // Show exit dialog if necessary
    ExitDialog(showExitDialog)

    // Collecting data from the ViewModel
    val inventoryItems by homeViewModel.getAllInventoryItems.collectAsState(emptyList())
    val selectedSortMethod by homeViewModel.sortMethod.collectAsState()

    // State for search query
    val searchQuery = rememberSaveable { mutableStateOf("") }

    // Retrieve sort options from resources
    val sortOptions = stringArrayResource(id = R.array.sort_options)

    // Sort inventory items based on selected sort method and filter by search query
    val sortedAndFilteredItems = inventoryItems.let { items ->
        val sortedItems = when (selectedSortMethod) {
            0 -> items.sortedBy { it.name.lowercase() } //Name ascending
            1 -> items.sortedByDescending { it.name.lowercase() } //Name descending
            2 -> items.sortedBy { it.availableStock } //Stock ascending
            3 -> items.sortedByDescending { it.availableStock } //Stock descending
            4 -> items.sortedBy { it.sellingPrice } //Selling price ascending
            5 -> items.sortedByDescending { it.sellingPrice } //Selling price descending
            6 -> items.sortedByDescending { it.lastModified } //last modified
            7 -> items.sortedBy { it.timeAdded } //Date added
            8 -> items.filter { it.expiryDate != null }.sortedBy { it.expiryDate } // Expiry date
            else -> items.sortedBy { it.name }
        }
        sortedItems.filter { it.name.contains(searchQuery.value, ignoreCase = true) }
    }

    // Retrieve cart items from ViewModel
    val cartItems by homeViewModel.cartItems.collectAsState(emptyList())

    // Composable Scaffold representing the layout structure of the screen
    Scaffold(
        topBar = {
            // Display the app bar with search functionality and navigation to cart screen
            InventoryHomeAppBar(
                stringResource(id = R.string.search_inventory),
                searchQuery,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 6.dp, top = 8.dp, bottom = 6.dp),
                cartSize = cartItems.size,
                navToCartScreen = {
                    navController.navigate(Screens.CartScreen.route)
                }
            )
        },
        floatingActionButton = {
            // Floating action button to navigate to add item screen
            AddItemFabButton {
                navController.navigate(Screens.AddItemScreen.route)
            }
        }
    ) { paddingValues ->
        // LazyColumn to display the list of inventory items
        LazyColumn(modifier = Modifier.padding(paddingValues)) {

            // Display scrollable tab row for selecting sort options
            item {
                ScrollableTabRow(
                    selectedTabIndex = selectedSortMethod
                ) {
                    sortOptions.forEachIndexed { index, string ->
                        Tab(
                            selected = index == selectedSortMethod,
                            text = { Text(text = string) },
                            onClick = {
                                homeViewModel.onEvent(HomeUiEvent.UpdateSortMethod(index))
                                Log.d("SortMethod",index.toString())
                            }
                        )
                    }
                }
            }

            // Display inventory items

            items(sortedAndFilteredItems, key = { key -> key.itemId }) { item ->
                val isItemInCart = cartItems.any { it.itemId == item.itemId }
                InventoryItem(
                    item = item,
                    addToCartAction = {
                        Log.d("cartItem", item.sellingPrice.toString())

                        homeViewModel.onEvent(
                            HomeUiEvent.AddItemToCart(
                                CartEntity(
                                    itemId = item.itemId,
                                    productName = item.name,
                                    availableStock = item.availableStock,
                                    quantityToSale = 1,
                                    costPrice = item.costPrice,
                                    sellingPrice = item.sellingPrice,
                                    imagePath = item.imagePath
                                )
                            )
                        )
                    },
                    removeItemFromCart = {
                        homeViewModel.onEvent(
                            HomeUiEvent.RemoveItemFromCart(
                                CartEntity(
                                    itemId = item.itemId,
                                    productName = item.name,
                                    availableStock = item.availableStock,
                                    quantityToSale = 0,
                                    costPrice = item.costPrice,
                                    sellingPrice = item.sellingPrice,
                                    imagePath = item.imagePath
                                )
                            )
                        )
                    },
                    navToEditItemScreen = {
                        navController.navigate(Screens.EditStockScreen.route + "/${item.itemId}")
                    },
                    deleteItemAction = { homeViewModel.onEvent(HomeUiEvent.DeleteItem(item)) },
                    isItemInCart = isItemInCart,
                    isExpiryTabSelected = selectedSortMethod == 8,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

