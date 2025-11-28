package com.example.homerentalapp.ui.screens.nearby

import android.Manifest
import android.content.Context
import android.location.Geocoder
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.homerentalapp.viewmodel.HomeViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NearbyMapScreen(
    onBack: () -> Unit,
    onHouseSelected: (String) -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var hasPermission by remember { mutableStateOf(false) }
    val fusedClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(-1.2921, 36.8219), 12f)
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        hasPermission = perms[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                perms[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (hasPermission) {
            fetchLocationForMap(fusedClient, context) { latLng, label ->
                viewModel.updateUserLocation(latLng.latitude, latLng.longitude, label)
                cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 13f)
            }
        }
    }

    LaunchedEffect(uiState.userLatitude, uiState.userLongitude) {
        val lat = uiState.userLatitude
        val lng = uiState.userLongitude
        if (lat != null && lng != null) {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(LatLng(lat, lng), 13f)
        }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Nearby Homes", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0E7C7B))
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            val properties = MapProperties(isMyLocationEnabled = hasPermission)
            val uiSettings = MapUiSettings(myLocationButtonEnabled = hasPermission)
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = properties,
                uiSettings = uiSettings
            ) {
                uiState.houses.filter { it.latitude != 0.0 && it.longitude != 0.0 }.forEach { house ->
                    Marker(
                        state = MarkerState(position = LatLng(house.latitude, house.longitude)),
                        title = house.title.ifBlank { "House" },
                        onClick = {
                            onHouseSelected(house.id)
                            true
                        }
                    )
                }
            }
            if (!hasPermission) {
                Text(
                    text = "Grant location permission to center on your area",
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                )
            }
        }
    }
}

@RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
private fun fetchLocationForMap(
    client: FusedLocationProviderClient,
    context: Context,
    onLocation: (LatLng, String) -> Unit
) {
    val tokenSource = CancellationTokenSource()
    client.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, tokenSource.token)
        .addOnSuccessListener { location ->
            location ?: return@addOnSuccessListener
            val geocoder = Geocoder(context, Locale.getDefault())
            val label = runCatching {
                geocoder.getFromLocation(location.latitude, location.longitude, 1)
            }.getOrNull()?.firstOrNull()?.getAddressLine(0) ?: ""
            onLocation(LatLng(location.latitude, location.longitude), label)
        }
}

