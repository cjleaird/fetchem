package com.leaird.fetchem.login

import com.leaird.fetchem.model.User
import java.io.IOException

interface LoginProvider {
    /**
     * Attempt to login to login provider. Will return the user
     * if successful. Otherwise, returns null or throws exception.
     */
    @Throws(LoginException::class)
    suspend fun login(credentials: LoginCredentials): User?

    /**
     * Logout the current user.
     */
    suspend fun logout()

    /**
     * Get the currently logged in user.
     */
    suspend fun getCurrentUser(): User?

    /**
     * Thrown when an error occurs during login.
     */
    class LoginException(message: String?) : IOException(message)
}