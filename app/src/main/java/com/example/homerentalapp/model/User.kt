package com.example.homerentalapp.model

/**
 * User data model
 * Represents a user in the Nyumba Yangu app
 */
data class User(
    val id: String = "",
    val email: String = "",
    val fullName: String = "",
    val phoneNumber: String = "",
    val profileImageUrl: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

