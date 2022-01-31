package com.leaird.fetchem.model.prefs

import android.content.Context
import android.content.SharedPreferences
import com.leaird.fetchem.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface Preferences {
    fun init(context: Context)
}

object UserPreferences : Preferences {
    private const val PREF_NAME = "com.leaird.fetchem.pref.user"

    private const val KEY_USER_EMAIL = "KEY_USER_EMAIL"
    private const val KEY_USER_IS_ADMIN = "KEY_USER_IS_ADMIN"

    private lateinit var preferences: SharedPreferences

    @Suppress("ObjectPropertyName")
    private val _currentUser = MutableStateFlow(email?.let {
        User(it, isAdmin)
    })
    val currentUser: StateFlow<User?> = _currentUser

    override fun init(context: Context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    /**
     * Store the current user in shared preferences, presumably
     * because they have logged in.
     */
    suspend fun setUser(email: String, isAdmin: Boolean) {
        UserPreferences.email = email
        UserPreferences.isAdmin = isAdmin
        _currentUser.emit(User(email, isAdmin))
    }

    /**
     * Remove the current user from shared preferences, presumably
     * because they have been logged out.
     */
    suspend fun removeUser() {
        preferences.edit().clear().apply()
        _currentUser.emit(null)
    }

    var isAdmin: Boolean
        get() {
            if (this::preferences.isInitialized) {
                return preferences.getBoolean(KEY_USER_IS_ADMIN, false)
            }
            return false
        }
        private set(value) = preferences.edit {
            if (this::preferences.isInitialized) {
                preferences.edit {
                    it.putBoolean(KEY_USER_IS_ADMIN, value)
                }
            }
        }

    var email: String?
        get() {
            if (this::preferences.isInitialized) {
                return preferences.getString(KEY_USER_EMAIL, null)
            }
            return null
        }
        private set(value) {
            if (this::preferences.isInitialized) {
                preferences.edit {
                    it.putString(KEY_USER_EMAIL, value)
                }
            }
        }
}

object NetworkPreferences : Preferences {
    private const val PREF_NAME = "com.leaird.fetchem.pref.network"

    private const val KEY_HAS_RETRIEVED_DATA = "KEY_HAS_RETRIEVED_DATA"

    private lateinit var preferences: SharedPreferences

    override fun init(context: Context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    var hasRetrievedData: Boolean
        get() {
            if (this::preferences.isInitialized) {
                return preferences.getBoolean(KEY_HAS_RETRIEVED_DATA, false)
            }
            return false
        }
        set(value) = preferences.edit {
            if (this::preferences.isInitialized) {
                preferences.edit {
                    it.putBoolean(KEY_HAS_RETRIEVED_DATA, value)
                }
            }
        }
}

private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
    val editor = edit()
    operation(editor)
    editor.apply()
}