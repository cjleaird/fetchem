package com.leaird.fetchem.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {
    private val _isFinished = MutableStateFlow(false)
    val isFinished: StateFlow<Boolean> = _isFinished

    init {
        viewModelScope.launch {
            delay(DURATION_SPLASH)
            _isFinished.emit(true)
        }
    }

    companion object {
        const val DURATION_SPLASH = 1750L
    }
}