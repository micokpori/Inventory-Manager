package com.rfcreations.inventorymanager.ui.screens.solditemsscreen

import androidx.lifecycle.ViewModel
import com.rfcreations.inventorymanager.repository.soldItemsRepository.SoldItemsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SoldItemsViewModel @Inject constructor(
    private val soldItemsRepository: SoldItemsRepository
) : ViewModel() {

    val getAllSoldItems by lazy { soldItemsRepository.getAllSoldItems()}
}
