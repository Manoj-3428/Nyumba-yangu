package com.example.homerentalapp.viewmodel

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homerentalapp.model.House
import com.example.homerentalapp.repository.HouseRepository
import com.example.homerentalapp.util.LocationStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = false,
    val houses: List<House> = emptyList(),
    val filteredHouses: List<House> = emptyList(),
    val userLatitude: Double? = null,
    val userLongitude: Double? = null,
    val useLocationFilter: Boolean = false,
    val radiusMeters: Float = 500f,
    val lastLocationLabel: String = "",
    val errorMessage: String? = null
)

class HomeViewModel(
    private val repository: HouseRepository = HouseRepository,
    private val locationStorage: LocationStorage = LocationStorage
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observeHouses().collect { houses ->
                _uiState.value = _uiState.value.copy(
                    houses = houses,
                    isLoading = false
                )
                applyFilters()
            }
        }
        viewModelScope.launch {
            locationStorage.snapshotFlow.collect { snapshot ->
                if (snapshot.label.isNotBlank()) {
                    _uiState.value = _uiState.value.copy(
                        lastLocationLabel = snapshot.label,
                        userLatitude = _uiState.value.userLatitude ?: snapshot.latitude,
                        userLongitude = _uiState.value.userLongitude ?: snapshot.longitude
                    )
                }
            }
        }
    }

    fun updateUserLocation(lat: Double, lng: Double, label: String) {
        _uiState.value = _uiState.value.copy(
            userLatitude = lat,
            userLongitude = lng,
            lastLocationLabel = label
        )
        viewModelScope.launch {
            locationStorage.save(lat, lng, label)
        }
        applyFilters()
    }

    fun updateRadius(radiusMeters: Float) {
        _uiState.value = _uiState.value.copy(radiusMeters = radiusMeters)
        applyFilters()
    }

    fun toggleLocationFilter(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(useLocationFilter = enabled)
        applyFilters()
    }

    private fun applyFilters() {
        val state = _uiState.value
        if (!state.useLocationFilter || state.userLatitude == null || state.userLongitude == null) {
            _uiState.value = state.copy(filteredHouses = state.houses)
            return
        }

        val filtered = state.houses.filter { house ->
            withinRadius(
                userLat = state.userLatitude,
                userLng = state.userLongitude,
                houseLat = house.latitude,
                houseLng = house.longitude,
                radiusMeters = state.radiusMeters
            )
        }
        _uiState.value = state.copy(filteredHouses = filtered)
    }

    private fun withinRadius(
        userLat: Double,
        userLng: Double,
        houseLat: Double,
        houseLng: Double,
        radiusMeters: Float
    ): Boolean {
        val result = FloatArray(1)
        Location.distanceBetween(userLat, userLng, houseLat, houseLng, result)
        if (houseLat == 0.0 && houseLng == 0.0) return false
        return result[0] <= radiusMeters
    }
}

