package com.leaird.fetchem.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leaird.fetchem.model.Result
import com.leaird.fetchem.model.db.PuppyItem
import com.leaird.fetchem.repository.PuppyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("unused")
@HiltViewModel
class DashboardViewModel @Inject constructor(private val puppyRepository: PuppyRepository) :
    ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow(-1)
    val errorMessage: StateFlow<Int> = _errorMessage

    private val _puppyItems = MutableStateFlow<List<PuppyItem>?>(null)
    val puppyItems: StateFlow<List<PuppyItem>?> = _puppyItems

    val puppyLists = _puppyItems.transform { items ->
        // Get distinct list id items.
        val listIds = arrayListOf<Int>()
        items?.forEach {
            if (!listIds.contains(it.listId)) {
                listIds.add(it.listId)
            }
        }
        listIds.sort()
        emit(listIds)
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            puppyRepository.getPuppyItems().collect {
                when (it) {
                    is Result.Loading -> {
                        _isLoading.emit(true)
                        _errorMessage.emit(-1)
                    }
                    is Result.Success -> {
                        _isLoading.emit(false)
                        _puppyItems.emit(it.value())
                    }
                    is Result.Error -> {
                        _isLoading.emit(false)
                        _errorMessage.emit(it.message)
                    }
                }
            }
        }
    }
}