package com.example.emergen_app.presentation.components.snackbar

import android.content.res.Resources
import androidx.annotation.StringRes
import com.example.emergen_app.R

/**
 * A sealed class representing different types of snackBar messages.
 * This can be either a simple string or a message loaded from a string resource.
 */
sealed class SnackBarMessage {

    /**
     * Represents a snackBar message that is loaded from a plain text string.
     * @property message The message text to be displayed.
     */
    class StringSnackBar(val message: String) : SnackBarMessage()

    /**
     * Represents a snackBar message that is loaded from a string resource.
     * @property message The resource ID for the message text.
     */
    class ResourceSnackBar(@StringRes val message: Int) : SnackBarMessage()

    companion object {

        /**
         * Converts the [SnackBarMessage] into a string message.
         * If the message is a resource ID, it will fetch the string using the provided [resources].
         *
         * @param resources The [Resources] instance to fetch the string resource.
         * @return A string representation of the snackBar message.
         */
        fun SnackBarMessage.toMessage(resources: Resources): String {
            return when (this) {
                is StringSnackBar -> this.message
                is ResourceSnackBar -> resources.getString(this.message)
            }
        }

        /**
         * Converts a [Throwable] to a [SnackBarMessage].
         * If the exception has a message, it will be used as a [StringSnackBar].
         * If the message is blank or null, a default generic error message will be used.
         *
         * @return A [SnackBarMessage] representing the throwable's message.
         */
        fun Throwable.toSnackBarMessage(): SnackBarMessage {
            val message = this.message.orEmpty()
            return if (message.isNotBlank()) StringSnackBar(message)
            else ResourceSnackBar(R.string.generic_error)
        }
    }
}