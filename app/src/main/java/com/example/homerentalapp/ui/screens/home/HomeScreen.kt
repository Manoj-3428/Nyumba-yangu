package com.example.homerentalapp.ui.screens.home

import android.Manifest
import android.content.Context
import android.location.Geocoder
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.*
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.homerentalapp.R
import com.example.homerentalapp.ui.components.HouseCard
import com.example.homerentalapp.util.LocationHelper
import com.example.homerentalapp.viewmodel.HomeViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateBack: () -> Unit = {},
    onAddHouse: () -> Unit,
    onOpenDetails: (String) -> Unit = {},
    viewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val filters = listOf("All", "Luxury", "Budget", "Family", "Studio")
    var activeFilter by remember { mutableStateOf("All") }
    val context = LocalContext.current
    var hasLocationPermission by remember { mutableStateOf(LocationHelper.hasLocationPermission(context)) }
    var liveLocationText by remember { mutableStateOf(uiState.lastLocationLabel) }
    val fusedClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasLocationPermission = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (hasLocationPermission) {
            fetchUserLocation(
                client = fusedClient,
                context = context,
                onLocation = { lat, lng, address ->
                    liveLocationText = address
                    viewModel.updateUserLocation(lat, lng, address)
                }
            )
        }
    }

    LaunchedEffect(hasLocationPermission) {
        if (hasLocationPermission && uiState.userLatitude == null) {
            fetchUserLocation(
                client = fusedClient,
                context = context,
                onLocation = { lat, lng, address ->
                    liveLocationText = address
                    viewModel.updateUserLocation(lat, lng, address)
                }
            )
        }
    }

    var showFilterSheet by remember { mutableStateOf(false) }
    val modalSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var radiusSliderValue by remember { mutableStateOf(uiState.radiusMeters) }
    var locationFilterEnabled by remember { mutableStateOf(uiState.useLocationFilter) }

    LaunchedEffect(uiState.radiusMeters) { radiusSliderValue = uiState.radiusMeters }
    LaunchedEffect(uiState.useLocationFilter) { locationFilterEnabled = uiState.useLocationFilter }
    LaunchedEffect(uiState.lastLocationLabel) {
        if (liveLocationText.isBlank() && uiState.lastLocationLabel.isNotBlank()) {
            liveLocationText = uiState.lastLocationLabel
        }
    }
    LaunchedEffect(uiState.userLatitude, uiState.userLongitude) {
        if (uiState.userLatitude != null && uiState.userLongitude != null && liveLocationText.isBlank()) {
            liveLocationText = "${String.format(Locale.getDefault(), "%.4f", uiState.userLatitude)}, ${String.format(Locale.getDefault(), "%.4f", uiState.userLongitude)}"
        }
    }

    val displayedHouses = if (uiState.useLocationFilter && uiState.userLatitude != null) {
        uiState.filteredHouses
    } else uiState.houses
    val chipFilteredHouses = remember(displayedHouses, activeFilter) {
        when (activeFilter) {
            "Luxury" -> displayedHouses.filter { it.price >= 120000 }
            "Budget" -> displayedHouses.filter { it.price <= 60000 }
            "Family" -> displayedHouses.filter { it.size.contains("3", ignoreCase = true) || it.size.contains("4") }
            "Studio" -> displayedHouses.filter { it.type.contains("Studio", ignoreCase = true) }
            else -> displayedHouses
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Nyumba Yangu",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Featured Rentals",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showFilterSheet = true }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_filter_list),
                            contentDescription = "Filters",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0E7C7B),
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddHouse,
                containerColor = Color(0xFF0E7C7B)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Add House",
                    tint = Color.White
                )
            }
        },
        containerColor = Color(0xFFF3F6FA)
    ) { paddingValues ->
        if (showFilterSheet) {
            ModalBottomSheet(
                onDismissRequest = { showFilterSheet = false },
                sheetState = modalSheetState
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Nearby Filter",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF0E7C7B)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Enable location filter")
                        Switch(
                            checked = locationFilterEnabled,
                            onCheckedChange = { locationFilterEnabled = it }
                        )
                    }
                    Text(text = "Distance: ${(radiusSliderValue / 1000).format(1)} km")
                    Slider(
                        value = radiusSliderValue,
                        onValueChange = { radiusSliderValue = it },
                        valueRange = 500f..5000f,
                        steps = 9
                    )
                    Button(
                        onClick = {
                            viewModel.updateRadius(radiusSliderValue)
                            viewModel.toggleLocationFilter(locationFilterEnabled)
                            showFilterSheet = false
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0E7C7B))
                    ) {
                        Text("Apply")
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Filter chips
            LazyRowFilters(
                filters = filters,
                activeFilter = activeFilter,
                onFilterSelected = { activeFilter = it }
            )

            // Location row
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Your Location",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.Gray
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Rounded.LocationOn,
                                contentDescription = null,
                                tint = Color(0xFF0E7C7B)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            val locationDisplay = when {
                                hasLocationPermission && liveLocationText.isNotBlank() -> liveLocationText
                                uiState.lastLocationLabel.isNotBlank() -> uiState.lastLocationLabel
                                else -> "Permission required"
                            }
                            Text(
                                text = locationDisplay,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Black
                            )
                        }
                    }
                    OutlinedButton(
                        onClick = {
                            if (hasLocationPermission) {
                                showFilterSheet = true
                            } else {
                                locationPermissionLauncher.launch(
                                    arrayOf(
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION
                                    )
                                )
                            }
                        },
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_filter_list),
                            contentDescription = null,
                            tint = Color(0xFF0E7C7B)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Filters",
                            color = Color(0xFF0E7C7B)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            AnimatedVisibility(
                visible = uiState.isLoading,
                enter = fadeIn(tween(500)),
                exit = fadeOut(tween(300))
            ) {
                LoadingState()
            }

            AnimatedVisibility(
                visible = !uiState.isLoading && chipFilteredHouses.isNotEmpty(),
                enter = fadeIn(tween(600))
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    items(chipFilteredHouses) { house ->
                        HouseCard(
                            house = house,
                            onClick = { onOpenDetails(house.id) }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(88.dp)) }
                }
            }

            if (!uiState.isLoading && chipFilteredHouses.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No listings yet. Be the first to add one!",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
private fun LazyRowFilters(
    filters: List<String>,
    activeFilter: String,
    onFilterSelected: (String) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(filters.size) { index ->
            val filter = filters[index]
            val isActive = filter == activeFilter
            AssistChip(
                onClick = { onFilterSelected(filter) },
                label = {
                    Text(
                        text = filter,
                        color = if (isActive) Color.White else Color(0xFF0E7C7B)
                    )
                },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = if (isActive) Color(0xFF0E7C7B) else Color(0xFF0E7C7B).copy(alpha = 0.08f)
                )
            )
        }
    }
}

@Composable
private fun LoadingState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        repeat(3) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.LightGray.copy(alpha = 0.3f))
            )
        }
    }
}

private fun fetchUserLocation(
    client: FusedLocationProviderClient,
    context: Context,
    onLocation: (Double, Double, String) -> Unit
) {
    if (!LocationHelper.hasLocationPermission(context)) {
        return
    }
    val tokenSource = CancellationTokenSource()
    client.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, tokenSource.token)
        .addOnSuccessListener { location ->
            location ?: return@addOnSuccessListener
            val geocoder = Geocoder(context, Locale.getDefault())
            val address = runCatching {
                geocoder.getFromLocation(location.latitude, location.longitude, 1)
            }.getOrNull()?.firstOrNull()?.getAddressLine(0)
            val text = address ?: "${String.format(Locale.getDefault(), "%.4f", location.latitude)}, ${String.format(Locale.getDefault(), "%.4f", location.longitude)}"
            onLocation(location.latitude, location.longitude, text)
        }
}

private fun Float.format(decimals: Int): String =
    String.format(Locale.getDefault(), "%.${decimals}f", this)

