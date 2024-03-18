package com.rfcreations.inventorymanager.ui.screens.cartscreen

import android.net.Uri
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rfcreations.inventorymanager.R
import com.rfcreations.inventorymanager.database.cartdb.CartEntity
import com.rfcreations.inventorymanager.ui.commons.ImageCard
import com.rfcreations.inventorymanager.ui.commons.NoImageCard
import com.rfcreations.inventorymanager.ui.commons.OutOfStockCard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

@Composable
fun CartScreen(
    modifier: Modifier = Modifier,
    cartViewModel: CartViewModel = hiltViewModel(),
    navBackAction: () -> Unit
) {

    val cartItems by cartViewModel.cartItems.collectAsState(emptyList())
    val showCartDropDownMenu = rememberSaveable { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(0.5f),
        topBar = {
            CartAppBar(
                showCartDropDownMenu,
                clearCart = {
                    CoroutineScope(IO).launch {
                        cartViewModel.clearCart()
                    }
                },
                navBackAction
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier.padding(paddingValues)
        ) {
            if (cartItems.isEmpty()) {
                Box(modifier, contentAlignment = Alignment.Center) {
                    Text(text = "Cart is currently empty")
                }
            } else {
                LazyColumn {
                    items(cartItems) { cartItem ->

                        CartItem(
                            item = cartItem,
                            modifier = Modifier.fillMaxWidth(),
                            showSnackBar = {
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = "You can't sell more than your available stock",
                                        actionLabel = "OK",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            },
                            updateQuantityToSale = {
                                CoroutineScope(IO).launch {
                                    cartViewModel.updateQuantityToSale(
                                        CartEntity(
                                            itemId = cartItem.itemId,
                                            productName = cartItem.productName,
                                            availableStock = cartItem.availableStock,
                                            sellingPrice = cartItem.sellingPrice,
                                            quantityToSale = it,  //update with new value
                                            costPrice = cartItem.costPrice,
                                            imagePath = cartItem.imagePath
                                        )
                                    )
                                }

                            },
                            removeCartItem = {
                                CoroutineScope(IO).launch {
                                    cartViewModel.removeItemFromCart(it)

                                }
                            }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(50.dp))
                    }
                }
                Button(
                    onClick = {
                        CoroutineScope(IO).launch {
                            Log.d("cartItem",cartItems.joinToString())

                            cartViewModel.onSaleButtonClick(cartItems)
                        }
                    },
                    shape = RoundedCornerShape(4.dp),
                    contentPadding = PaddingValues(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = 8.dp)
                ) {
                    Text(text = "Sale")
                }
            }
        }
    }
}

@Composable
private fun CartItem(
    item: CartEntity,
    showSnackBar: () -> Unit,
    updateQuantityToSale: (Int) -> Unit,
    removeCartItem: (CartEntity) -> Unit,
    modifier: Modifier = Modifier
) {

    var showSelectQuantityDialog by rememberSaveable { mutableStateOf(false) }
    val itemSellingPrice by rememberSaveable { mutableIntStateOf(item.sellingPrice) }
    val quantityToSale = item.quantityToSale

    if (showSelectQuantityDialog) {
        SelectQuantityDialog(
            quantity = quantityToSale.toString(),
            stockAvailable = item.availableStock,
            updateQuantity = { /*quantityToSale = it.toInt()*/ },
            dismissRequest = { showSelectQuantityDialog = !showSelectQuantityDialog }) {
        }
    }
    Card(
        modifier = modifier.padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Top
        ) {
            if (item.imagePath == null) {
                NoImageCard(
                    R.drawable.no_stock_img,
                    modifier
                        .size(100.dp)
                        .weight(0.3f)
                )
            } else {
                ImageCard(
                    imagePath = Uri.parse(item.imagePath),
                    modifier
                        .padding(8.dp)
                        .size(100.dp)
                        .weight(0.3f)
                        .clip(RoundedCornerShape(12.dp))
                )
            }
            Column(
                modifier
                    .weight(0.7f)
                    .align(Alignment.Top)
            ) {
                Text(text = item.productName, maxLines = 3, overflow = TextOverflow.Ellipsis)
                Text(text = "₦$itemSellingPrice", fontWeight = FontWeight.Bold)

                Row {
                    Icon(painterResource(id = R.drawable.store_outlined), null)
                    Text(text = item.availableStock.toString())
                }

            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(
                modifier = Modifier
                    .padding(horizontal = 8.dp),
                onClick = { removeCartItem(item) }
            ) {
                Text(text = "Remove")
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (item.availableStock < 1) {
                    OutOfStockCard()
                } else {
                    CustomButton(icon = R.drawable.minus_sign) {
                        Log.d("oldPrice", itemSellingPrice.toString())
                        if (quantityToSale != 1) {
                            val newValue = quantityToSale - 1
                            updateQuantityToSale(newValue)
                        }
                        Log.d("newPrice", itemSellingPrice.toString())
                    }
                    Text(
                        text = "$quantityToSale",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .clickable {
                                showSelectQuantityDialog = !showSelectQuantityDialog
                            }
                    )
                    CustomButton(icon = R.drawable.add_icon) {
                        Log.d("oldPrice", itemSellingPrice.toString())
                        if (quantityToSale < item.availableStock) {
                            val newValue = quantityToSale + 1
                            updateQuantityToSale(newValue)
                        } else {
                            showSnackBar()
                        }
                        Log.d("newPrice", itemSellingPrice.toString())
                    }
                }
            }
        }
    }
}

@Composable
private fun CustomButton(
    @DrawableRes icon: Int,
    clickAction: () -> Unit
) {
    Card(
        onClick = clickAction,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        shape = RoundedCornerShape(6.dp),
        modifier = Modifier.padding(8.dp)
    ) {
        Icon(painterResource(id = icon), null,modifier = Modifier.padding(6.dp))
    }
    /*Button(
        modifier = Modifier.padding(8.dp),
        contentPadding = PaddingValues(0.dp),
        onClick = clickAction
    ) {
        Icon(painterResource(id = icon), null)
    }*/
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CartAppBar(
    showCartDropDownMenu: MutableState<Boolean>,
    clearCart: () -> Unit,
    navBackAction: () -> Unit
) {
    TopAppBar(
        title = { Text(text = "Cart") },
        navigationIcon = {
            IconButton(onClick = navBackAction) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
            }
        },
        actions = {
            IconButton(onClick = { showCartDropDownMenu.value = true }) {
                Icon(Icons.Filled.MoreVert, null)
            }
            DropdownMenu(
                expanded = showCartDropDownMenu.value,
                onDismissRequest = { showCartDropDownMenu.value = false }
            ) {
                DropdownMenuItem(
                    text = { Text(text = "Clear cart") },
                    onClick = {
                        clearCart()
                        showCartDropDownMenu.value = !showCartDropDownMenu.value
                    },
                    leadingIcon = { Icon(painterResource(R.drawable.clear_all_icon), null) }
                )
            }
        }
    )
}
