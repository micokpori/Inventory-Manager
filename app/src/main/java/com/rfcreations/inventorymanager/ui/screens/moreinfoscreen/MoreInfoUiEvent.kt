package com.rfcreations.inventorymanager.ui.screens.moreinfoscreen

sealed class MoreInfoUiEvent {
    data object ToggleShowAppThemeDialog : MoreInfoUiEvent()
    data object ToggleShowBackUpDialog : MoreInfoUiEvent()
    data object ToggleShowRestoreBackUpDialog : MoreInfoUiEvent()
    data object ClearCache : MoreInfoUiEvent()
}