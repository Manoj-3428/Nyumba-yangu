package com.example.homerentalapp.ui.screens.landing

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.homerentalapp.R
import com.example.homerentalapp.ui.components.AnimatedBackground
import com.example.homerentalapp.ui.components.FilterBottomSheet
import com.example.homerentalapp.ui.components.FilterChip
import com.example.homerentalapp.viewmodel.LandingViewModel

/**
 * Landing Screen - Main entry point after authentication
 * Features: Search Houses button, Filters, Quick Actions
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandingScreen(
    onNavigateToSearch: () -> Unit,
    onNavigateToNearby: () -> Unit,
    onNavigateToAddHouse: () -> Unit,
    viewModel: LandingViewModel = viewModel()
) {
    val filterState by viewModel.filterState.collectAsState()
    val isFilterSheetOpen by viewModel.isFilterSheetOpen.collectAsState()

    // Animate card entrance
    val cardAlpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(600, easing = FastOutSlowInEasing),
        label = "cardAlpha"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Nyumba Yangu",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0E7C7B)
                )
            )
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            // Animated background
            AnimatedBackground()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Welcome message
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(animationSpec = tween(600)) + slideInVertically(
                        initialOffsetY = { -it / 2 },
                        animationSpec = tween(600)
                    )
                ) {
                    Column {
                        Text(
                            text = "Welcome back!",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White
                        )
                        Text(
                            text = "Find your perfect home",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }

            // Main Search Card
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(animationSpec = tween(800, delayMillis = 200)) +
                        slideInVertically(
                            initialOffsetY = { it / 4 },
                            animationSpec = tween(800, delayMillis = 200)
                        ),
                modifier = Modifier.graphicsLayer { alpha = cardAlpha }
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        // Search Houses Button
                        var buttonPressed by remember { mutableStateOf(false) }
                        val buttonScale by animateFloatAsState(
                            targetValue = if (buttonPressed) 0.95f else 1f,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessHigh
                            ),
                            label = "buttonScale"
                        )

                        Button(
                            onClick = {
                                buttonPressed = true
                                onNavigateToSearch()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .graphicsLayer { scaleX = buttonScale; scaleY = buttonScale },
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF0E7C7B)
                            )
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Search,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Search Houses",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                )
                            }
                        }

                        // Quick Filters
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Quick Filters",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    color = Color.Black
                                )
                                TextButton(onClick = { viewModel.openFilterSheet() }) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "More",
                                            color = Color(0xFF0E7C7B),
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_filter),
                                            contentDescription = "Filter",
                                            tint = Color(0xFF0E7C7B),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }

                            // Active filters display
                            if (viewModel.hasActiveFilters()) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    if (filterState.location.isNotBlank()) {
                                        FilterChip(
                                            text = filterState.location,
                                            isSelected = true,
                                            onClick = { viewModel.updateLocation("") }
                                        )
                                    }
                                    filterState.amenities.take(2).forEach { amenity ->
                                        FilterChip(
                                            text = amenity,
                                            isSelected = true,
                                            onClick = { viewModel.toggleAmenity(amenity) }
                                        )
                                    }
                                    if (filterState.amenities.size > 2) {
                                        FilterChip(
                                            text = "+${filterState.amenities.size - 2}",
                                            isSelected = true,
                                            onClick = { viewModel.openFilterSheet() }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Quick Actions
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(animationSpec = tween(1000, delayMillis = 400)) +
                        slideInHorizontally(
                            initialOffsetX = { it },
                            animationSpec = tween(1000, delayMillis = 400)
                        )
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Nearby Search
                        QuickActionButton(
                            icon = Icons.Rounded.LocationOn,
                            text = "Nearby",
                            onClick = onNavigateToNearby,
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        // Add House
                        QuickActionButton(
                            icon = Icons.Rounded.Add,
                            text = "Add House",
                            onClick = onNavigateToAddHouse,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
        }

        // Filter Bottom Sheet
        FilterBottomSheet(
            isOpen = isFilterSheetOpen,
            filterState = filterState,
            viewModel = viewModel,
            onDismiss = { viewModel.closeFilterSheet() },
            onApplyFilters = { /* Filters applied, will be used in search */ }
        )
    }
}

/**
 * Quick action button component
 */
@Composable
private fun QuickActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "quickActionScale"
    )

    Column(
        modifier = modifier
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .clickable(
                onClick = { isPressed = true; onClick(); isPressed = false },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(
                    color = Color(0xFF0E7C7B).copy(alpha = 0.1f),
                    shape = RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = Color(0xFF0E7C7B),
                modifier = Modifier.size(32.dp)
            )
        }
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = Color.Black,
            textAlign = TextAlign.Center
        )
    }
}

