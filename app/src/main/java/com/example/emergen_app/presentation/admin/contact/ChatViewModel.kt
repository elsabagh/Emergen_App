package com.example.emergen_app.presentation.admin.contact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emergen_app.data.models.Message
import com.example.emergen_app.domain.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> get() = _messages
    // إرسال رسالة جديدة
    fun sendMessage(message: Message) {
        viewModelScope.launch {
            try {
                chatRepository.sendMessage(message)

                _messages.value = _messages.value + message

            } catch (e: Exception) {
            }
        }
    }

    fun getUserMessages() {
        viewModelScope.launch {
            chatRepository.getUserMessages().collect {
                _messages.value = it
            }
        }
    }

    fun getChatWithUser(userId: String) {
        viewModelScope.launch {
            chatRepository.getChatWithUser(userId).collect {
                _messages.value = it
            }
        }
    }

    fun getChatWithCurrentUser() {
        viewModelScope.launch {
            chatRepository.getChatWithCurrentUser().collect {
                _messages.value = it
            }
        }
    }

}
