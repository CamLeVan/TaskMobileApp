package com.example.taskapplication.data.auth

import android.content.Context
import android.content.SharedPreferences
import com.example.taskapplication.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    init {
        _isLoggedIn.value = getToken() != null
    }

    fun saveToken(token: String) {
        prefs.edit().putString(KEY_TOKEN, token).apply()
        _isLoggedIn.value = true
    }

    fun getToken(): String? {
        return prefs.getString(KEY_TOKEN, null)
    }

    fun saveUser(user: User) {
        prefs.edit()
            .putLong(KEY_USER_ID, user.id)
            .putString(KEY_USER_NAME, user.name)
            .putString(KEY_USER_EMAIL, user.email)
            .apply()
    }

    fun getUser(): User? {
        val id = prefs.getLong(KEY_USER_ID, -1)
        if (id == -1L) return null

        return User(
            id = id,
            name = prefs.getString(KEY_USER_NAME, "") ?: "",
            email = prefs.getString(KEY_USER_EMAIL, "") ?: "",
            email_verified_at = null
        )
    }

    fun logout() {
        prefs.edit().clear().apply()
        _isLoggedIn.value = false
    }

    companion object {
        private const val PREFS_NAME = "auth_prefs"
        private const val KEY_TOKEN = "token"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_EMAIL = "user_email"
    }
} 