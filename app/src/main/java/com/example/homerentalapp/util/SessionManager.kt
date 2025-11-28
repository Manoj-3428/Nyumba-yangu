package com.example.homerentalapp.util

import com.google.firebase.auth.FirebaseAuth

/**
 * Session Manager to handle user authentication state
 * Persists login session across app restarts
 */
object SessionManager {
    private val auth = FirebaseAuth.getInstance()

    /**
     * Check if user is currently logged in
     */
    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    /**
     * Get current user ID
     */
    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    /**
     * Get current user email
     */
    fun getCurrentUserEmail(): String? {
        return auth.currentUser?.email
    }
}

