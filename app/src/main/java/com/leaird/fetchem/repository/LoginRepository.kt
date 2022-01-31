package com.leaird.fetchem.repository

import android.util.Log
import com.leaird.fetchem.R
import com.leaird.fetchem.login.LoginCredentials
import com.leaird.fetchem.login.LoginProvider
import com.leaird.fetchem.model.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LoginRepository @Inject constructor(private val loginProvider: LoginProvider) {
    suspend fun login(email: String, password: String) = flow {
        emit(Result.Loading())
        val user = loginProvider.login(LoginCredentials(email, password))
        if (user != null) {
            emit(Result.Success(user))
        } else {
            emit(Result.Error(R.string.error_message_failed_to_login))
        }
    }.catch {
        Log.e(TAG, it.message, it)
        emit(Result.Error(R.string.error_message_invalid_credentials))
    }.flowOn(Dispatchers.IO)

    suspend fun getCurrentUser() = loginProvider.getCurrentUser()

    suspend fun logout() = loginProvider.logout()

    companion object {
        const val TAG = "FetchEm-LoginRepository"
    }
}