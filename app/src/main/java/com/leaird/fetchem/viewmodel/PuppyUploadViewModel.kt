package com.leaird.fetchem.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.filter
import com.leaird.fetchem.repository.PuppyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class PuppyUploadViewModel @Inject constructor(private val puppyRepository: PuppyRepository) :
    ViewModel() {
    fun loadPuppyItems(listId: Int) = puppyRepository.getPagingPuppyItems().map { pagingData ->
        pagingData.filter { item ->
            // Filter these items dependending on the provided list id.
            listId == -1 || listId == item.listId
        }
    }.cachedIn(viewModelScope)
}