package com.rfcreations.inventorymanager.ui.screens.homescreen

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rfcreations.inventorymanager.database.cartdb.CartEntity
import com.rfcreations.inventorymanager.repository.cartrepository.CartRepository
import com.rfcreations.inventorymanager.repository.inventoryrepository.InventoryRepository
import com.rfcreations.inventorymanager.repository.userpreferencerepository.UserPreferenceRepository
import com.rfcreations.inventorymanager.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val inventoryRepository: InventoryRepository,
    private val userPreferenceRepository: UserPreferenceRepository,
    private val context: Application
) : ViewModel() {

    val cartItems = cartRepository.getAllCartItems()
    private val prefKey = Constants.PrefKeys

    private val _sortMethod =
        MutableStateFlow(userPreferenceRepository.getIntPref(prefKey.SORT_METHOD_KEY, 0))
    val sortMethod = _sortMethod.asStateFlow()

    val getAllInventoryItems = inventoryRepository.getAllInventoryItems()

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.DeleteItem -> {
                viewModelScope.launch {
                    try {
                        inventoryRepository.deleteItem(event.item)
                        //delete image from storage
                        withContext(IO) {
                            val imgPath = event.item.imagePath
                            if (imgPath != null) {
                                val image = File(imgPath)
                                image.delete()
                            }
                        }
                        //Remove item from cart as it may present
                        cartRepository.removeItemFromCart(
                            CartEntity(
                                itemId = event.item.itemId,
                                "",
                                0,
                                0,
                                0,
                                0,
                                null
                            )
                        )
                        val imageDir = event.item.imagePath?.let { File(it) }
                        imageDir?.delete()

                    } catch (e: Exception) {

                    }
                }
            }

            is HomeUiEvent.UpdateSortMethod -> {
                _sortMethod.value = event.newValue
                userPreferenceRepository.editIntPref(prefKey.SORT_METHOD_KEY, event.newValue)
            }

            is HomeUiEvent.AddItemToCart -> {
                viewModelScope.launch {
                    cartRepository.insertItemToCart(event.item)
                }
            }

            is HomeUiEvent.RemoveItemFromCart -> {
                viewModelScope.launch {
                    cartRepository.removeItemFromCart(event.item)
                }
            }
        }
    }

}