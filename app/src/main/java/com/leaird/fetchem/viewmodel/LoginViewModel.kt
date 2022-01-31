package com.leaird.fetchem.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.leaird.fetchem.model.Result
import com.leaird.fetchem.model.prefs.UserPreferences
import com.leaird.fetchem.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginRepository: LoginRepository) :
    ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow(-1)
    val errorMessage: StateFlow<Int> = _errorMessage

    private val _showLogin = MutableStateFlow(false)
    val showLogin: StateFlow<Boolean> = _showLogin

    val currentUser = UserPreferences.currentUser.asLiveData()

    init {
        viewModelScope.launch {
            launch {
                val user = loginRepository.getCurrentUser()
                if (user != null) {
                    UserPreferences.setUser(user.email, user.isAdmin)
                } else {
                    UserPreferences.removeUser()
                }
                UserPreferences.currentUser.collect {
                    _showLogin.emit(it == null)
                }
            }
        }
    }

    fun login(email: String, password: String) = viewModelScope.launch(Dispatchers.IO) {
        loginRepository.login(email, password).collect {
            when (it) {
                is Result.Loading -> {
                    _isLoading.emit(true)
                    _errorMessage.emit(-1)
                }
                is Result.Success -> {
                    _isLoading.emit(false)
                    UserPreferences.setUser(it.data.email, it.data.isAdmin)
                }
                is Result.Error -> {
                    _isLoading.emit(false)
                    _errorMessage.emit(it.message)
                }
            }
        }
    }

    fun logout() = viewModelScope.launch(Dispatchers.IO) {
        loginRepository.logout()
        UserPreferences.removeUser()
    }
}