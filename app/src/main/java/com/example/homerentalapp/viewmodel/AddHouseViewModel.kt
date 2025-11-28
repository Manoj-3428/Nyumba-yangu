package com.example.homerentalapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homerentalapp.model.House
import com.example.homerentalapp.repository.HouseRepository
import com.example.homerentalapp.util.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for Add House Screen
 * Manages house listing form state
 */
class AddHouseViewModel(
    private val repository: HouseRepository = HouseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddHouseUiState())
    val uiState: StateFlow<AddHouseUiState> = _uiState.asStateFlow()

    /**
     * Update description
     */
    fun updateDescription(description: String) {
        _uiState.value = _uiState.value.copy(description = description)
    }

    /**
     * Update location
     */
    fun updateLocation(location: String) {
        _uiState.value = _uiState.value.copy(location = location)
    }

    /**
     * Update address
     */
    fun updateAddress(address: String) {
        _uiState.value = _uiState.value.copy(address = address)
    }

    /**
     * Update price
     */
    fun updatePrice(price: String) {
        _uiState.value = _uiState.value.copy(price = price)
    }

    /**
     * Update size
     */
    fun updateSize(size: String) {
        _uiState.value = _uiState.value.copy(size = size)
    }

    /**
     * Update house type
     */
    fun updateType(type: String) {
        _uiState.value = _uiState.value.copy(type = type)
    }

    /**
     * Toggle amenity
     */
    fun toggleAmenity(amenity: String) {
        val currentAmenities = _uiState.value.amenities.toMutableList()
        if (currentAmenities.contains(amenity)) {
            currentAmenities.remove(amenity)
        } else {
            currentAmenities.add(amenity)
        }
        _uiState.value = _uiState.value.copy(amenities = currentAmenities)
    }

    /**
     * Update vacant units
     */
    fun updateVacantUnits(units: String) {
        _uiState.value = _uiState.value.copy(vacantUnits = units)
    }

    /**
     * Update total units
     */
    fun updateTotalUnits(units: String) {
        _uiState.value = _uiState.value.copy(totalUnits = units)
    }

    /**
     * Update caretaker name
     */
    fun updateCaretakerName(name: String) {
        _uiState.value = _uiState.value.copy(caretakerName = name)
    }

    /**
     * Update caretaker phone
     */
    fun updateCaretakerPhone(phone: String) {
        _uiState.value = _uiState.value.copy(caretakerPhone = phone)
    }

    /**
     * Update caretaker email
     */
    fun updateCaretakerEmail(email: String) {
        _uiState.value = _uiState.value.copy(caretakerEmail = email)
    }

    /**
     * Submit house listing
     */
    fun updateImages(uris: List<String>) {
        _uiState.value = _uiState.value.copy(imageUris = uris)
    }

    fun updateCoordinates(lat: Double?, lng: Double?) {
        _uiState.value = _uiState.value.copy(latitude = lat, longitude = lng)
    }

    fun submitHouse(onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            val state = _uiState.value
            val userId = SessionManager.getCurrentUserId()

            if (userId == null) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "User not logged in"
                )
                onError("User not logged in")
                return@launch
            }

            // Validate required fields
            if (state.description.isBlank() || state.location.isBlank() || state.price.isBlank()) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Please fill all required fields (Description, Location, Price)"
                )
                onError("Please fill all required fields")
                return@launch
            }

            val house = House(
                ownerId = userId,
                title = state.description.take(30),
                description = state.description,
                location = state.location,
                address = state.address,
                price = state.price.toDoubleOrNull() ?: 0.0,
                size = state.size,
                type = state.type,
                amenities = state.amenities,
                vacantUnits = state.vacantUnits.toIntOrNull() ?: 0,
                totalUnits = state.totalUnits.toIntOrNull() ?: 0,
                caretakerName = state.caretakerName,
                caretakerPhone = state.caretakerPhone,
                caretakerEmail = state.caretakerEmail,
                imageUrls = state.imageUris,
                latitude = state.latitude ?: 0.0,
                longitude = state.longitude ?: 0.0,
                isVerified = false
            )

            repository.addHouse(house)

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                isSuccess = true
            )
            onSuccess()
        }
    }

    /**
     * Reset form
     */
    fun resetForm() {
        _uiState.value = AddHouseUiState()
    }
}

/**
 * UI State for Add House Screen
 */
data class AddHouseUiState(
    val description: String = "",
    val location: String = "",
    val address: String = "",
    val price: String = "",
    val size: String = "",
    val type: String = "",
    val amenities: List<String> = emptyList(),
    val vacantUnits: String = "",
    val totalUnits: String = "",
    val caretakerName: String = "",
    val caretakerPhone: String = "",
    val caretakerEmail: String = "",
    val imageUris: List<String> = emptyList(),
    val latitude: Double? = null,
    val longitude: Double? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)

