package com.example.homerentalapp.util

import android.content.Context
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.locationDataStore by preferencesDataStore(name = "location_prefs")

data class LocationSnapshot(
    val latitude: Double? = null,
    val longitude: Double? = null,
    val label: String = ""
)

object LocationStorage {
    private lateinit var appContext: Context

    private val KEY_LAT = doublePreferencesKey("last_lat")
    private val KEY_LNG = doublePreferencesKey("last_lng")
    private val KEY_LABEL = stringPreferencesKey("last_label")

    fun initialize(context: Context) {
        if (::appContext.isInitialized) return
        appContext = context.applicationContext
    }

    val snapshotFlow: Flow<LocationSnapshot>
        get() {
            check(::appContext.isInitialized) { "LocationStorage not initialized" }
            return appContext.locationDataStore.data.map { prefs ->
                LocationSnapshot(
                    latitude = prefs[KEY_LAT],
                    longitude = prefs[KEY_LNG],
                    label = prefs[KEY_LABEL] ?: ""
                )
            }
        }

    suspend fun save(lat: Double, lng: Double, label: String) {
        if (!::appContext.isInitialized) return
        appContext.locationDataStore.edit { prefs ->
            prefs[KEY_LAT] = lat
            prefs[KEY_LNG] = lng
            prefs[KEY_LABEL] = label
        }
    }
}

