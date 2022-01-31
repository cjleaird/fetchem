package com.leaird.fetchem.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leaird.fetchem.model.prefs.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

class ActivityViewModel : ViewModel() {
    private val _showAdminView = MutableStateFlow(UserPreferences.isAdmin)
    val showAdminView = _showAdminView

    init {
        viewModelScope.launch {
            UserPreferences.currentUser
                .filter { it != null } // If null, there is no logged in user
                .collect {
                    _showAdminView.emit(it!!.isAdmin)
                }
        }
    }
}