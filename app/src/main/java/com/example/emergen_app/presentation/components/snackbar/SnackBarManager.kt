package com.example.emergen_app.presentation.components.snackbar

import androidx.annotation.StringRes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * A singleton object that manages snackBar messages to be shown in the UI.
 * It exposes a [StateFlow] to observe snackBar messages and provides methods
 * to show and clear them.
 */
object SnackBarManager {
    // Internal MutableStateFlow holding the current snackBar message, initialized to null.
    private var _messages: MutableStateFlow<SnackBarMessage?> = MutableStateFlow(null)

    // Public read-only StateFlow to observe snackBar messages.
    val snackBarMessages: StateFlow<SnackBarMessage?> get() = _messages.asStateFlow()

    /**
     * Displays a snackBar message using a string resource.
     * This method sets the snackBar message to a resource-based message.
     *
     * @param message The string resource ID to display in the snackBar.
     */
    fun showMessage(@StringRes message: Int) {
        _messages.value = SnackBarMessage.ResourceSnackBar(message)
    }

    /**
     * Displays a custom snackBar message.
     * This method allows the display of any [SnackBarMessage] object.
     *
     * @param snackBarMessage The custom [SnackBarMessage] to display.
     */
    fun showMessage(snackBarMessage: SnackBarMessage) {
        _messages.value = snackBarMessage
    }

    /**
     * Clears the current snackBar message.
     * This will hide any currently visible snackBar message.
     */
    fun clearSnackBarState() {
        _messages.value = null
    }

    fun showMessage(s: String) {

    }
}