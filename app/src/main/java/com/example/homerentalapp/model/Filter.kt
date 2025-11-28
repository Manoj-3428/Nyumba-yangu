package com.example.homerentalapp.model

/**
 * Filter data models for house search
 */
data class FilterState(
    val location: String = "",
    val minPrice: Int = 0,
    val maxPrice: Int = 100000,
    val amenities: List<String> = emptyList()
)

/**
 * Available amenities
 */
object Amenities {
    const val WATER = "Water"
    const val ELECTRICITY = "Electricity"
    const val PARKING = "Parking"
    const val WIFI = "WiFi"
    const val SECURITY = "Security"
    const val GARDEN = "Garden"
    const val FURNISHED = "Furnished"
    
    val ALL = listOf(WATER, ELECTRICITY, PARKING, WIFI, SECURITY, GARDEN, FURNISHED)
}

/**
 * Popular locations
 */
object PopularLocations {
    val LOCATIONS = listOf(
        "Nairobi",
        "Mombasa",
        "Kisumu",
        "Nakuru",
        "Eldoret",
        "Thika",
        "Malindi",
        "Kitale"
    )
}

