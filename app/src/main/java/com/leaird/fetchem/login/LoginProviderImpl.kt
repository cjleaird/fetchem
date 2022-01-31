package com.leaird.fetchem.login

import android.util.Log
import com.google.android.gms.tasks.RuntimeExecutionException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.leaird.fetchem.login.LoginProvider.LoginException
import com.leaird.fetchem.model.User
import java.lang.IllegalArgumentException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class LoginProviderImpl(private val auth: FirebaseAuth) : LoginProvider {
    override suspend fun login(credentials: LoginCredentials): User? {
        val result: AuthResult = suspendCoroutine { continuation ->
            try {
                auth.signInWithEmailAndPassword(credentials.email, credentials.password)
                    .addOnCompleteListener { task ->
                        try {
                            continuation.resume(task.result)
                        } catch (e: RuntimeExecutionException) {
                            continuation.resumeWithException(LoginException(e.message))
                        }
                    }
            } catch (e: IllegalArgumentException) {
                continuation.resumeWithException(LoginException(e.message))
            }
        }
        return result.user?.let { getCurrentUserFromFirebaseUser(it) }
    }

    override suspend fun logout() {
        auth.signOut()
    }

    override suspend fun getCurrentUser(): User? {
        return auth.currentUser?.let { getCurrentUserFromFirebaseUser(it) }
    }

    private suspend fun getCurrentUserFromFirebaseUser(firebaseUser: FirebaseUser): User =
        suspendCoroutine { continuation ->
            firebaseUser.getIdToken(false).addOnCompleteListener { task ->
                var isAdmin = false
                try {
                    isAdmin = task.result?.claims?.get(CLAIMS_ADMIN)?.let { it as Boolean } ?: false
                } catch (e: RuntimeExecutionException) {
                    Log.e(TAG, "Failed to get current user", e)
                    continuation.resumeWithException(LoginException(e.message))
                }
                val currentUser = User(firebaseUser.email!!, isAdmin)
                continuation.resume(currentUser)
            }
        }

    companion object {
        private const val TAG = "FetchEm-LoginProviderImpl"

        /**
         * Claims used to determine role of the logged in user.
         */
        private const val CLAIMS_ADMIN = "admin"
    }
}