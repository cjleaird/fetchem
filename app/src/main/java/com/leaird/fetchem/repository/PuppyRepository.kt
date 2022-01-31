package com.leaird.fetchem.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.leaird.fetchem.R
import com.leaird.fetchem.model.Result
import com.leaird.fetchem.model.db.PuppyItem
import com.leaird.fetchem.model.db.PuppyItemDao
import com.leaird.fetchem.model.prefs.NetworkPreferences
import com.leaird.fetchem.model.remote.NetworkService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class PuppyRepository @Inject constructor(
    private val dao: PuppyItemDao,
    private val networkService: NetworkService
) {
    suspend fun getPuppyItems() = flow {
        emit(Result.Loading())
        if (!NetworkPreferences.hasRetrievedData) {
            Log.d(TAG, "Retrieving puppy items from network...")
            val response = networkService.getPuppyItems()
            if (response.isSuccessful) {
                NetworkPreferences.hasRetrievedData = true
                var body = response.body()
                body = body?.filter { !it.name.isNullOrBlank() }
                body?.let {
                    dao.insert(body)
                    emit(Result.Success(it))
                } ?: emit(Result.Error(R.string.error_message_failed_get_puppy_items))
            } else {
                emit(Result.Error(R.string.error_message_failed_get_puppy_items))
            }
        } else {
            Log.d(TAG, "Retrieving puppy items from local database...")
            emit(Result.Success(dao.getPuppyItems()))
        }
    }.catch {
        Log.e(TAG, "Failed to get puppy items", it)
        emit(Result.Error(R.string.error_message_failed_get_puppy_items))
    }.flowOn(Dispatchers.IO)

    fun getPagingPuppyItems(): Flow<PagingData<PuppyItem>> = Pager(
        config = PagingConfig(pageSize = 20, prefetchDistance = 2)
    ) {
        dao.pagingSource()
    }.flow

    companion object {
        private const val TAG = "FetchEm-PuppyRepository"
    }
}