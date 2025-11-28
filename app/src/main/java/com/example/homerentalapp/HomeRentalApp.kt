package com.example.homerentalapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import com.example.homerentalapp.repository.HouseRepository
import com.example.homerentalapp.util.LocationStorage

@HiltAndroidApp
class HomeRentalApp : Application() {
    override fun onCreate() {
        super.onCreate()
        HouseRepository.initialize(this)
        LocationStorage.initialize(this)
    }
}

