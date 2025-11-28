package com.example.homerentalapp.model

/**
 * House listing data model
 */
data class House(
    val id: String = "",
    val ownerId: String = "",
    val title: String = "",
    val description: String = "",
    val location: String = "",
    val address: String = "",
    val price: Double = 0.0,
    val size: String = "", // e.g., "2 Bedroom, 1 Bathroom"
    val type: String = "", // e.g., "Apartment", "House", "Studio"
    val amenities: List<String> = emptyList(),
    val vacantUnits: Int = 0,
    val totalUnits: Int = 0,
    val caretakerName: String = "",
    val caretakerPhone: String = "",
    val caretakerEmail: String = "",
    val imageUrls: List<String> = emptyList(),
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val createdAt: Long = System.currentTimeMillis(),
    val isVerified: Boolean = false
)

