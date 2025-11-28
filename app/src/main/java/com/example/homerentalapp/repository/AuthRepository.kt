package com.example.homerentalapp.repository

import com.example.homerentalapp.model.User
import com.example.homerentalapp.util.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

/**
 * Repository for handling authentication operations
 * Follows repository pattern for clean architecture
 */
class AuthRepository {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    /**
     * Get current user
     */
    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    /**
     * Check if user is logged in
     */
    fun isUserLoggedIn(): Boolean = auth.currentUser != null

    /**
     * Sign in with email and password
     */
    suspend fun signIn(email: String, password: String): AuthResult<FirebaseUser> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            AuthResult.Success(result.user!!)
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Sign in failed")
        }
    }

    /**
     * Sign up with email and password
     */
    suspend fun signUp(
        email: String,
        password: String,
        fullName: String,
        phoneNumber: String
    ): AuthResult<FirebaseUser> {
        return try {
            // Create user in Firebase Auth
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user ?: return AuthResult.Error("User creation failed")

            // Create user document in Firestore
            val userData = User(
                id = user.uid,
                email = email,
                fullName = fullName,
                phoneNumber = phoneNumber
            )

            firestore.collection("users")
                .document(user.uid)
                .set(userData)
                .await()

            AuthResult.Success(user)
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Sign up failed")
        }
    }

    /**
     * Sign out current user
     */
    fun signOut() {
        auth.signOut()
    }

    /**
     * Get user data from Firestore
     */
    suspend fun getUserData(userId: String): AuthResult<User> {
        return try {
            val document = firestore.collection("users")
                .document(userId)
                .get()
                .await()

            if (document.exists()) {
                val user = document.toObject(User::class.java)
                AuthResult.Success(user!!)
            } else {
                AuthResult.Error("User data not found")
            }
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Failed to fetch user data")
        }
    }
}

