package com.example.homerentalapp.repository

import android.content.Context
import androidx.room.Room
import com.example.homerentalapp.local.HouseDao
import com.example.homerentalapp.local.HouseDatabase
import com.example.homerentalapp.local.toEntity
import com.example.homerentalapp.model.House
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

object HouseRepository {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var houseDao: HouseDao
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun initialize(context: Context) {
        if (::houseDao.isInitialized) return
        firestore = FirebaseFirestore.getInstance()
        val db = Room.databaseBuilder(
            context.applicationContext,
            HouseDatabase::class.java,
            "houses.db"
        ).fallbackToDestructiveMigration().build()
        houseDao = db.houseDao()
        scope.launch { seedLocalDataIfEmpty() }
        listenForRemoteChanges()
    }

    private suspend fun seedLocalDataIfEmpty() {
        if (houseDao.countHouses() == 0) {
            houseDao.insertHouses(sampleHomes.map { it.copy(id = it.id.ifBlank { UUID.randomUUID().toString() }) }.map { it.toEntity() })
        }
    }

    private fun listenForRemoteChanges() {
        firestore.collection("houses")
            .addSnapshotListener { snapshot, _ ->
                if (snapshot == null || !::houseDao.isInitialized) return@addSnapshotListener
                val remoteHouses = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(House::class.java)?.copy(id = doc.id)
                }
                scope.launch {
                    houseDao.insertHouses(remoteHouses.map { it.toEntity() })
                }
            }
    }

    fun observeHouses(): Flow<List<House>> =
        houseDao.observeHouses().map { entities -> entities.map { it.toModel() } }

    fun observeHouse(houseId: String): Flow<House?> =
        houseDao.observeHouseById(houseId).map { it?.toModel() }

    suspend fun addHouse(house: House) {
        val finalHouse = house.copy(
            id = if (house.id.isBlank()) UUID.randomUUID().toString() else house.id,
            createdAt = house.createdAt
        )
        houseDao.insertHouse(finalHouse.toEntity())
        firestore.collection("houses")
            .document(finalHouse.id)
            .set(finalHouse)
    }

    suspend fun getHouseById(houseId: String): House? =
        houseDao.getHouseById(houseId)?.toModel()

    suspend fun refreshFromRemote() {
        val snapshot = firestore.collection("houses").get().await()
        val remoteHouses = snapshot.documents.mapNotNull { doc ->
            doc.toObject(House::class.java)?.copy(id = doc.id)
        }
        houseDao.insertHouses(remoteHouses.map { it.toEntity() })
    }

    private val sampleHomes = listOf(
        House(
            id = "1",
            title = "Skyline Luxury Suites",
            description = "2 Bedroom, 2 Bathroom • Furnished • Rooftop deck",
            location = "Kilimani, Nairobi",
            address = "Lenana Road, Nairobi",
            price = 85000.0,
            size = "2BR • 1100sqft",
            type = "Apartment",
            amenities = listOf("Pool", "Lift", "24/7 Security"),
            vacantUnits = 3,
            totalUnits = 12,
            caretakerName = "Mr. Otieno",
            caretakerPhone = "+254 701 555 777",
            imageUrls = listOf(
                "https://images.unsplash.com/photo-1505691723518-36a5ac3be353?w=800"
            ),
            latitude = -1.2921,
            longitude = 36.8219
        ),
        House(
            id = "2",
            title = "Green Haven Villas",
            description = "Spacious maisonette with private garden",
            location = "Runda Estate",
            address = "Cherry Lane, Runda",
            price = 150000.0,
            size = "4BR • 2400sqft",
            type = "Villa",
            amenities = listOf("Garden", "Garage", "Backup Power"),
            vacantUnits = 1,
            totalUnits = 4,
            caretakerName = "Ms. Wanjiru",
            caretakerPhone = "+254 700 222 999",
            imageUrls = listOf(
                "https://images.unsplash.com/photo-1493663284031-b7e3aefcae8e?w=800"
            ),
            latitude = -1.2121,
            longitude = 36.8771
        ),
        House(
            id = "3",
            title = "CBD Micro-Lofts",
            description = "Stylish studio with skyline city views",
            location = "Nairobi CBD",
            address = "Kenyatta Avenue",
            price = 45000.0,
            size = "Studio • 450sqft",
            type = "Studio",
            amenities = listOf("High-Speed WiFi", "Remote Access", "Concierge"),
            vacantUnits = 6,
            totalUnits = 20,
            caretakerName = "Caretaker Anne",
            caretakerPhone = "+254 723 123 456",
            imageUrls = listOf(
                "https://images.unsplash.com/photo-1484154218962-a197022b5858?w=800"
            ),
            latitude = -1.2833,
            longitude = 36.8167
        )
    )
}
