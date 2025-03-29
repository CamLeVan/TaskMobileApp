package com.example.taskapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskapplication.data.model.ChatMessage
import com.example.taskapplication.data.model.UnreadMessageCount
import com.example.taskapplication.data.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel(private val chatRepository: ChatRepository) : ViewModel() {

    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _chatState = MutableStateFlow<ChatState>(ChatState.Initial)
    val chatState: StateFlow<ChatState> = _chatState.asStateFlow()

    private val _unreadMessageCount = MutableStateFlow<UnreadMessageCount?>(null)
    val unreadMessageCount: StateFlow<UnreadMessageCount?> = _unreadMessageCount.asStateFlow()

    fun getChatMessages(teamId: Long) {
        viewModelScope.launch {
            _chatState.value = ChatState.Loading
            chatRepository.getChatMessages(teamId)
                .onSuccess { messages ->
                    _chatMessages.value = messages
                    _chatState.value = ChatState.Success
                }
                .onFailure { error ->
                    _chatState.value = ChatState.Error(error.message ?: "Failed to get chat messages")
                }
        }
    }

    fun sendChatMessage(teamId: Long, message: ChatMessage) {
        viewModelScope.launch {
            _chatState.value = ChatState.Loading
            chatRepository.sendChatMessage(teamId, message)
                .onSuccess {
                    getChatMessages(teamId)
                    _chatState.value = ChatState.Success
                }
                .onFailure { error ->
                    _chatState.value = ChatState.Error(error.message ?: "Failed to send chat message")
                }
        }
    }

    fun markMessageAsRead(teamId: Long, messageId: Long) {
        viewModelScope.launch {
            _chatState.value = ChatState.Loading
            chatRepository.markMessageAsRead(teamId, messageId)
                .onSuccess {
                    getChatMessages(teamId)
                    _chatState.value = ChatState.Success
                }
                .onFailure { error ->
                    _chatState.value = ChatState.Error(error.message ?: "Failed to mark message as read")
                }
        }
    }

    fun getUnreadMessageCount(teamId: Long) {
        viewModelScope.launch {
            _chatState.value = ChatState.Loading
            chatRepository.getUnreadMessageCount(teamId)
                .onSuccess { count ->
                    _unreadMessageCount.value = count
                    _chatState.value = ChatState.Success
                }
                .onFailure { error ->
                    _chatState.value = ChatState.Error(error.message ?: "Failed to get unread message count")
                }
        }
    }
}

sealed class ChatState {
    object Initial : ChatState()
    object Loading : ChatState()
    object Success : ChatState()
    data class Error(val message: String) : ChatState()
} 