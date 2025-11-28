package com.example.homerentalapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homerentalapp.model.FilterState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for Landing Screen
 * Manages filter state and UI interactions
 */
class LandingViewModel : ViewModel() {

    private val _filterState = MutableStateFlow(FilterState())
    val filterState: StateFlow<FilterState> = _filterState.asStateFlow()

    private val _isFilterSheetOpen = MutableStateFlow(false)
    val isFilterSheetOpen: StateFlow<Boolean> = _isFilterSheetOpen.asStateFlow()

    /**
     * Update location filter
     */
    fun updateLocation(location: String) {
        _filterState.value = _filterState.value.copy(location = location)
    }

    /**
     * Update price range
     */
    fun updatePriceRange(minPrice: Int, maxPrice: Int) {
        _filterState.value = _filterState.value.copy(
            minPrice = minPrice,
            maxPrice = maxPrice
        )
    }

    /**
     * Toggle amenity selection
     */
    fun toggleAmenity(amenity: String) {
        val currentAmenities = _filterState.value.amenities.toMutableList()
        if (currentAmenities.contains(amenity)) {
            currentAmenities.remove(amenity)
        } else {
            currentAmenities.add(amenity)
        }
        _filterState.value = _filterState.value.copy(amenities = currentAmenities)
    }

    /**
     * Clear all filters
     */
    fun clearFilters() {
        _filterState.value = FilterState()
    }

    /**
     * Open filter bottom sheet
     */
    fun openFilterSheet() {
        _isFilterSheetOpen.value = true
    }

    /**
     * Close filter bottom sheet
     */
    fun closeFilterSheet() {
        _isFilterSheetOpen.value = false
    }

    /**
     * Check if any filters are active
     */
    fun hasActiveFilters(): Boolean {
        val state = _filterState.value
        return state.location.isNotBlank() ||
                state.minPrice > 0 ||
                state.maxPrice < 100000 ||
                state.amenities.isNotEmpty()
    }
}

