package com.example.homerentalapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.homerentalapp.model.House
import com.example.homerentalapp.repository.HouseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class HouseDetailsUiState(
    val house: House? = null,
    val isLoading: Boolean = true
)

class HouseDetailsViewModel(
    private val houseId: String,
    private val repository: HouseRepository = HouseRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HouseDetailsUiState())
    val uiState: StateFlow<HouseDetailsUiState> = _uiState

    init {
        viewModelScope.launch {
            repository.observeHouse(houseId).collectLatest { house ->
                _uiState.value = HouseDetailsUiState(
                    house = house,
                    isLoading = false
                )
            }
        }
    }

    companion object {
        fun provideFactory(houseId: String): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return HouseDetailsViewModel(houseId) as T
                }
            }
    }
}

