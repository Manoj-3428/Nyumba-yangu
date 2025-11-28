package com.example.homerentalapp.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.homerentalapp.model.House

@Entity(tableName = "houses")
data class HouseEntity(
    @PrimaryKey val id: String,
    val ownerId: String,
    val title: String,
    val description: String,
    val location: String,
    val address: String,
    val price: Double,
    val size: String,
    val type: String,
    val amenities: List<String>,
    val vacantUnits: Int,
    val totalUnits: Int,
    val caretakerName: String,
    val caretakerPhone: String,
    val caretakerEmail: String,
    val imageUrls: List<String>,
    val latitude: Double,
    val longitude: Double,
    val createdAt: Long,
    val isVerified: Boolean
) {
    fun toModel(): House = House(
        id = id,
        ownerId = ownerId,
        title = title,
        description = description,
        location = location,
        address = address,
        price = price,
        size = size,
        type = type,
        amenities = amenities,
        vacantUnits = vacantUnits,
        totalUnits = totalUnits,
        caretakerName = caretakerName,
        caretakerPhone = caretakerPhone,
        caretakerEmail = caretakerEmail,
        imageUrls = imageUrls,
        latitude = latitude,
        longitude = longitude,
        createdAt = createdAt,
        isVerified = isVerified
    )
}

fun House.toEntity(): HouseEntity = HouseEntity(
    id = if (id.isBlank()) ownerId + createdAt else id,
    ownerId = ownerId,
    title = title,
    description = description,
    location = location,
    address = address,
    price = price,
    size = size,
    type = type,
    amenities = amenities,
    vacantUnits = vacantUnits,
    totalUnits = totalUnits,
    caretakerName = caretakerName,
    caretakerPhone = caretakerPhone,
    caretakerEmail = caretakerEmail,
    imageUrls = imageUrls,
    latitude = latitude,
    longitude = longitude,
    createdAt = createdAt,
    isVerified = isVerified
)

