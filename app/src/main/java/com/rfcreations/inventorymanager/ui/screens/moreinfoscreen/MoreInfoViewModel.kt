package com.rfcreations.inventorymanager.ui.screens.moreinfoscreen

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rfcreations.inventorymanager.utils.ThemeUiState
import com.rfcreations.inventorymanager.utils.BackUpManager
import com.rfcreations.inventorymanager.utils.RestoreBackUpManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoreInfoViewModel @Inject constructor(
    private val context: Application,
    val themeUiState: ThemeUiState,
    private val backUpManager: BackUpManager,
    private val restoreBackUpManager: RestoreBackUpManager
) : ViewModel() {

    val backUpState = backUpManager.backUpState
    val restoreState = restoreBackUpManager.restoreState

    private val _showAppThemeDialog = MutableStateFlow(false)
    val showAppThemeDialog = _showAppThemeDialog.asStateFlow()

    private val _showBackUpDialog = MutableStateFlow(false)
    val showBackUpDialog = _showBackUpDialog.asStateFlow()

    private val _showRestoreBackUpDialog = MutableStateFlow(false)
    val showRestoreBackUpDialog = _showRestoreBackUpDialog.asStateFlow()

    fun commenceBackup() {
        viewModelScope.launch {
            backUpManager.commenceBackUp()
        }
    }

    fun commenceRestore() {
        viewModelScope.launch {
            restoreBackUpManager.commenceRestore()
        }
    }

    fun cancelBackup() = backUpManager.cancelBackUp()
    fun cancelRestore() = restoreBackUpManager.cancelRestore()

    fun onEvent(event: MoreInfoUiEvent) {
        when (event) {
            is MoreInfoUiEvent.ToggleShowAppThemeDialog -> {
                _showAppThemeDialog.value = !_showAppThemeDialog.value
            }

            is MoreInfoUiEvent.ClearCache -> {
                viewModelScope.launch {
                    val cacheDir = context.cacheDir
                    cacheDir.deleteRecursively()
                }
            }

            is MoreInfoUiEvent.ToggleShowBackUpDialog -> {
                _showBackUpDialog.value = !_showBackUpDialog.value
            }

            is MoreInfoUiEvent.ToggleShowRestoreBackUpDialog -> {
                _showRestoreBackUpDialog.value = !_showRestoreBackUpDialog.value
            }
        }
    }
}