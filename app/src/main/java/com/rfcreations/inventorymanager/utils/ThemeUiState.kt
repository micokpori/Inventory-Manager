package com.rfcreations.inventorymanager.utils

import com.rfcreations.inventorymanager.repository.userpreferencerepository.UserPreferenceRepository
import com.rfcreations.inventorymanager.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * This class represents the state of the theme for the UI.
 * It uses injected dependencies and StateFlow to manage the theme data.
 *
 * @param userPreferenceRepository An instance of the UserPreferenceRepository
 *to interact with user preferences.
 */
class ThemeUiState @Inject constructor(
    private val userPreferenceRepository: UserPreferenceRepository
) {

    /**
     * Reference to the PrefKeys object for easier access to preference keys.
     */
    private val prefKeys = Constants.PrefKeys

    /**
     * A private MutableStateFlow holding the current selected theme value (0, 1, or 2).
     * The initial value is retrieved from user preferences using the injected repository.
     */
    private val _selectedTheme = MutableStateFlow(
        userPreferenceRepository.getIntPref(prefKeys.SELECTED_THEME_KEY, 2)
    )

    /**
     * An immutable StateFlow exposing the selectedTheme as a read-only stream.
     */
    val selectedTheme = _selectedTheme.asStateFlow()

    /**
     * A private MutableStateFlow holding the current dark theme preference (true/false).
     */
    private val _darkTheme = MutableStateFlow(false)

    /**
     * An immutable StateFlow exposing the darkTheme as a read-only stream.
     */
    val darkTheme = _darkTheme.asStateFlow()

    /**
     * A private MutableStateFlow holding the current dynamic theme preference (true/false).
     */
    private val _dynamicTheme = MutableStateFlow(false)

    /**
     * An immutable StateFlow exposing the dynamicTheme as a read-only stream.
     */
    val dynamicTheme = _dynamicTheme.asStateFlow()

    /**
     * Updates the selected theme value and also saves to shared preferences.
     *
     * @param newThemeValue The new integer value representing the selected theme.
     */
    fun updateSelectedTheme(newThemeValue: Int) {
        _selectedTheme.value = newThemeValue
        userPreferenceRepository.editIntPref(prefKeys.SELECTED_THEME_KEY, newThemeValue)
    }

    /**
     * Toggles the dynamic theme and also saves to shared preferences.
     */
    fun toggleDynamicTheme() {
        _dynamicTheme.value = !_dynamicTheme.value
        userPreferenceRepository.editBooleanPref(prefKeys.DYNAMIC_THEME_KEY, _dynamicTheme.value)
    }

    /**
     * Updates the dark theme value and also saves to shared preferences.
     * @param newThemeValue The new boolean value indicating the desired dark theme state.
     */
    fun toggleDarkThemeValue(newThemeValue: Boolean) {
        _darkTheme.value = newThemeValue
    }
}
