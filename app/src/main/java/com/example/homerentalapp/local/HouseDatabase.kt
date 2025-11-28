package com.example.homerentalapp.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [HouseEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class HouseDatabase : RoomDatabase() {
    abstract fun houseDao(): HouseDao
}

