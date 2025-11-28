package com.example.homerentalapp.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.homerentalapp.model.Amenities
import com.example.homerentalapp.model.FilterState
import com.example.homerentalapp.model.PopularLocations
import com.example.homerentalapp.viewmodel.LandingViewModel

/**
 * Filter bottom sheet with location, price range, and amenities
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    isOpen: Boolean,
    filterState: FilterState,
    viewModel: LandingViewModel,
    onDismiss: () -> Unit,
    onApplyFilters: () -> Unit
) {
    if (isOpen) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Filter Houses",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color(0xFF0E7C7B)
                    )
                    TextButton(onClick = { viewModel.clearFilters() }) {
                        Text(
                            text = "Clear All",
                            color = Color(0xFF0E7C7B),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Location Section
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Location",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = Color.Black
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PopularLocations.LOCATIONS.forEach { location ->
                            FilterChip(
                                text = location,
                                isSelected = filterState.location == location,
                                onClick = { viewModel.updateLocation(location) }
                            )
                        }
                    }
                }

                // Price Range Section
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Price Range",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = Color.Black
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "KSh ${filterState.minPrice}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black
                        )
                        Text(
                            text = "to",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                        Text(
                            text = "KSh ${filterState.maxPrice}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black
                        )
                    }
                    Slider(
                        value = filterState.maxPrice.toFloat(),
                        onValueChange = { viewModel.updatePriceRange(filterState.minPrice, it.toInt()) },
                        valueRange = 0f..100000f,
                        colors = SliderDefaults.colors(
                            thumbColor = Color(0xFF0E7C7B),
                            activeTrackColor = Color(0xFF0E7C7B)
                        )
                    )
                }

                // Amenities Section
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Amenities",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = Color.Black
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Amenities.ALL.forEach { amenity ->
                            FilterChip(
                                text = amenity,
                                isSelected = filterState.amenities.contains(amenity),
                                onClick = { viewModel.toggleAmenity(amenity) }
                            )
                        }
                    }
                }

                // Apply Button
                Button(
                    onClick = {
                        onApplyFilters()
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0E7C7B)
                    )
                ) {
                    Text(
                        text = "Apply Filters",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

