package com.example.homerentalapp.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HouseDao {
    @Query("SELECT * FROM houses ORDER BY createdAt DESC")
    fun observeHouses(): Flow<List<HouseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHouse(house: HouseEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHouses(houses: List<HouseEntity>)

    @Query("SELECT * FROM houses WHERE id = :houseId LIMIT 1")
    suspend fun getHouseById(houseId: String): HouseEntity?

    @Query("SELECT * FROM houses WHERE id = :houseId LIMIT 1")
    fun observeHouseById(houseId: String): Flow<HouseEntity?>

    @Query("SELECT COUNT(*) FROM houses")
    suspend fun countHouses(): Int
}

