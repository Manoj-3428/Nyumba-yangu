package com.example.homerentalapp.ui.screens.addhouse

import android.Manifest
import android.location.Geocoder
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.homerentalapp.R
import com.example.homerentalapp.model.Amenities
import com.example.homerentalapp.model.PopularLocations
import com.example.homerentalapp.ui.components.AnimatedBackground
import com.example.homerentalapp.ui.components.AuthTextField
import com.example.homerentalapp.ui.components.FilterChip
import com.example.homerentalapp.ui.components.ImageCarousel
import com.example.homerentalapp.util.LocationHelper
import com.example.homerentalapp.viewmodel.AddHouseViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import java.util.Locale

/**
 * Add House Screen - Professional step-by-step form
 * Includes image upload, location permission, and all house details
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHouseScreen(
    onNavigateBack: () -> Unit,
    onSuccess: () -> Unit,
    viewModel: AddHouseViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    
    val selectedImageUris = uiState.imageUris.mapNotNull { runCatching { Uri.parse(it) }.getOrNull() }
    
    // Location permission state
    var hasLocationPermission by remember { 
        mutableStateOf(LocationHelper.hasLocationPermission(context)) 
    }
    var currentLocationText by remember { mutableStateOf(uiState.location) }
    LaunchedEffect(uiState.location) {
        currentLocationText = uiState.location
    }

    val fusedClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    // Permission launcher
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasLocationPermission = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (hasLocationPermission) {
            fetchCurrentLocation(
                client = fusedClient,
                context = context,
                onLocationText = { text ->
                    currentLocationText = text
                    viewModel.updateLocation(text)
                },
                onCoordinates = { lat, lng ->
                    viewModel.updateCoordinates(lat, lng)
                }
            )
        }
    }

    LaunchedEffect(hasLocationPermission) {
        if (hasLocationPermission && currentLocationText.isBlank()) {
            fetchCurrentLocation(
                client = fusedClient,
                context = context,
                onLocationText = { text ->
                    currentLocationText = text
                    viewModel.updateLocation(text)
                },
                onCoordinates = { lat, lng -> viewModel.updateCoordinates(lat, lng) }
            )
        }
    }

    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        viewModel.updateImages(uris.map { it.toString() })
    }

    // Handle success
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onSuccess()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Animated background
        AnimatedBackground()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Add New House",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.White
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                imageVector = Icons.Rounded.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF0E7C7B)
                    )
                )
            },
            containerColor = Color.Transparent
        ) { paddingValues ->
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Step 1: Images Section
                    SectionHeader("Step 1: House Images")
                    
                    ImageCarousel(
                        imageUris = selectedImageUris,
                        onAddImage = {
                            imagePickerLauncher.launch("image/*")
                        },
                        onRemoveImage = { index ->
                            val mutable = uiState.imageUris.toMutableList()
                            if (index in mutable.indices) {
                                mutable.removeAt(index)
                                viewModel.updateImages(mutable)
                            }
                        }
                    )

                    // Step 2: Description
                    SectionHeader("Step 2: Description")
                    
                    OutlinedTextField(
                        value = uiState.description,
                        onValueChange = { viewModel.updateDescription(it) },
                        label = { Text("Description *", style = MaterialTheme.typography.bodyMedium) },
                        textStyle = MaterialTheme.typography.bodyLarge,
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_description),
                                contentDescription = null,
                                tint = Color(0xFF0E7C7B)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = false,
                        maxLines = 10,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF0E7C7B),
                            unfocusedBorderColor = Color.LightGray,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedLabelColor = Color(0xFF0E7C7B),
                            unfocusedLabelColor = Color.Black
                        )
                    )

                    // Step 3: Location Section
                    SectionHeader("Step 3: Location")
                    
                    Button(
                        onClick = {
                            if (hasLocationPermission) {
                                fetchCurrentLocation(
                                    client = fusedClient,
                                    context = context,
                                    onLocationText = {
                                        currentLocationText = it
                                        viewModel.updateLocation(it)
                                    },
                                    onCoordinates = { lat, lng ->
                                        viewModel.updateCoordinates(lat, lng)
                                    }
                                )
                            } else {
                                locationPermissionLauncher.launch(
                                    arrayOf(
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION
                                    )
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF0E7C7B).copy(alpha = 0.1f)
                        )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_my_location),
                            contentDescription = null,
                            tint = Color(0xFF0E7C7B),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (hasLocationPermission) "Use Live Location" else "Grant Location Access",
                            color = Color(0xFF0E7C7B),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    if (currentLocationText.isNotBlank()) {
                        Text(
                            text = "Live location set: $currentLocationText",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                    
                    // Location Dropdown
                    var locationExpanded by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = locationExpanded,
                        onExpandedChange = { locationExpanded = !locationExpanded }
                    ) {
                        OutlinedTextField(
                            value = uiState.location,
                            onValueChange = { viewModel.updateLocation(it) },
                            label = { Text("Select Location *", style = MaterialTheme.typography.bodyMedium) },
                            textStyle = MaterialTheme.typography.bodyLarge,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Rounded.LocationOn,
                                    contentDescription = null,
                                    tint = Color(0xFF0E7C7B)
                                )
                            },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Rounded.ArrowDropDown,
                                    contentDescription = null,
                                    tint = Color(0xFF0E7C7B)
                                )
                            },
                            readOnly = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            singleLine = false,
                            maxLines = 3,
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF0E7C7B),
                                unfocusedBorderColor = Color.LightGray,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black,
                                focusedLabelColor = Color(0xFF0E7C7B),
                                unfocusedLabelColor = Color.Black
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = locationExpanded,
                            onDismissRequest = { locationExpanded = false }
                        ) {
                            PopularLocations.LOCATIONS.forEach { location ->
                                DropdownMenuItem(
                                    text = { Text(location) },
                                    onClick = {
                                        viewModel.updateLocation(location)
                                        locationExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = uiState.address,
                        onValueChange = { viewModel.updateAddress(it) },
                        label = { Text("Full Address", style = MaterialTheme.typography.bodyMedium) },
                        textStyle = MaterialTheme.typography.bodyLarge,
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_place),
                                contentDescription = null,
                                tint = Color(0xFF0E7C7B)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = false,
                        maxLines = 5,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF0E7C7B),
                            unfocusedBorderColor = Color.LightGray,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedLabelColor = Color(0xFF0E7C7B),
                            unfocusedLabelColor = Color.Black
                        )
                    )

                    // Step 4: Price & Details
                    SectionHeader("Step 4: Price & Details")
                    
                    OutlinedTextField(
                        value = uiState.price,
                        onValueChange = { viewModel.updatePrice(it) },
                        label = { Text("Price (KSh) *", style = MaterialTheme.typography.bodyMedium) },
                        textStyle = MaterialTheme.typography.bodyLarge,
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_money),
                                contentDescription = null,
                                tint = Color(0xFF0E7C7B)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = false,
                        maxLines = 3,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF0E7C7B),
                            unfocusedBorderColor = Color.LightGray,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedLabelColor = Color(0xFF0E7C7B),
                            unfocusedLabelColor = Color.Black
                        )
                    )

                    OutlinedTextField(
                        value = uiState.size,
                        onValueChange = { viewModel.updateSize(it) },
                        label = { Text("Size (e.g., 2 Bedroom, 1 Bathroom)", style = MaterialTheme.typography.bodyMedium) },
                        textStyle = MaterialTheme.typography.bodyLarge,
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_size),
                                contentDescription = null,
                                tint = Color(0xFF0E7C7B)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = false,
                        maxLines = 3,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF0E7C7B),
                            unfocusedBorderColor = Color.LightGray,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedLabelColor = Color(0xFF0E7C7B),
                            unfocusedLabelColor = Color.Black
                        )
                    )

                    // House Type
                    var typeExpanded by remember { mutableStateOf(false) }
                    val houseTypes = listOf("Apartment", "House", "Studio", "Condo", "Villa")
                    
                    ExposedDropdownMenuBox(
                        expanded = typeExpanded,
                        onExpandedChange = { typeExpanded = !typeExpanded }
                    ) {
                        OutlinedTextField(
                            value = uiState.type,
                            onValueChange = { viewModel.updateType(it) },
                            label = { Text("House Type", style = MaterialTheme.typography.bodyMedium) },
                            textStyle = MaterialTheme.typography.bodyLarge,
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_category),
                                    contentDescription = null,
                                    tint = Color(0xFF0E7C7B)
                                )
                            },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Rounded.ArrowDropDown,
                                    contentDescription = null,
                                    tint = Color(0xFF0E7C7B)
                                )
                            },
                            readOnly = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            singleLine = false,
                            maxLines = 2,
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF0E7C7B),
                                unfocusedBorderColor = Color.LightGray,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black,
                                focusedLabelColor = Color(0xFF0E7C7B),
                                unfocusedLabelColor = Color.Black
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = typeExpanded,
                            onDismissRequest = { typeExpanded = false }
                        ) {
                            houseTypes.forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(type) },
                                    onClick = {
                                        viewModel.updateType(type)
                                        typeExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // Step 5: Availability
                    SectionHeader("Step 5: Availability")
                    
                    OutlinedTextField(
                        value = uiState.vacantUnits,
                        onValueChange = { viewModel.updateVacantUnits(it) },
                        label = { Text("Vacant Units", style = MaterialTheme.typography.bodyMedium) },
                        textStyle = MaterialTheme.typography.bodyLarge,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.Home,
                                contentDescription = null,
                                tint = Color(0xFF0E7C7B)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = false,
                        maxLines = 3,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF0E7C7B),
                            unfocusedBorderColor = Color.LightGray,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedLabelColor = Color(0xFF0E7C7B),
                            unfocusedLabelColor = Color.Black
                        )
                    )

                    OutlinedTextField(
                        value = uiState.totalUnits,
                        onValueChange = { viewModel.updateTotalUnits(it) },
                        label = { Text("Total Units", style = MaterialTheme.typography.bodyMedium) },
                        textStyle = MaterialTheme.typography.bodyLarge,
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_apartment),
                                contentDescription = null,
                                tint = Color(0xFF0E7C7B)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = false,
                        maxLines = 3,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF0E7C7B),
                            unfocusedBorderColor = Color.LightGray,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedLabelColor = Color(0xFF0E7C7B),
                            unfocusedLabelColor = Color.Black
                        )
                    )

                    // Step 6: Amenities
                    SectionHeader("Step 6: Amenities")
                    
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Amenities.ALL.forEach { amenity ->
                            FilterChip(
                                text = amenity,
                                isSelected = uiState.amenities.contains(amenity),
                                onClick = { viewModel.toggleAmenity(amenity) }
                            )
                        }
                    }

                    // Step 7: Caretaker Information
                    SectionHeader("Step 7: Caretaker Information")
                    
                    OutlinedTextField(
                        value = uiState.caretakerName,
                        onValueChange = { viewModel.updateCaretakerName(it) },
                        label = { Text("Caretaker Name", style = MaterialTheme.typography.bodyMedium) },
                        textStyle = MaterialTheme.typography.bodyLarge,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.Person,
                                contentDescription = null,
                                tint = Color(0xFF0E7C7B)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = false,
                        maxLines = 3,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF0E7C7B),
                            unfocusedBorderColor = Color.LightGray,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedLabelColor = Color(0xFF0E7C7B),
                            unfocusedLabelColor = Color.Black
                        )
                    )

                    OutlinedTextField(
                        value = uiState.caretakerPhone,
                        onValueChange = { viewModel.updateCaretakerPhone(it) },
                        label = { Text("Phone Number", style = MaterialTheme.typography.bodyMedium) },
                        textStyle = MaterialTheme.typography.bodyLarge,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.Phone,
                                contentDescription = null,
                                tint = Color(0xFF0E7C7B)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = false,
                        maxLines = 3,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF0E7C7B),
                            unfocusedBorderColor = Color.LightGray,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedLabelColor = Color(0xFF0E7C7B),
                            unfocusedLabelColor = Color.Black
                        )
                    )

                    OutlinedTextField(
                        value = uiState.caretakerEmail,
                        onValueChange = { viewModel.updateCaretakerEmail(it) },
                        label = { Text("Email", style = MaterialTheme.typography.bodyMedium) },
                        textStyle = MaterialTheme.typography.bodyLarge,
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_email),
                                contentDescription = null,
                                tint = Color(0xFF0E7C7B)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = false,
                        maxLines = 3,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF0E7C7B),
                            unfocusedBorderColor = Color.LightGray,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedLabelColor = Color(0xFF0E7C7B),
                            unfocusedLabelColor = Color.Black
                        )
                    )

                    // Error message
                    if (uiState.errorMessage != null) {
                        Text(
                            text = uiState.errorMessage!!,
                            color = Color.Red,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Submit Button
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
                            viewModel.submitHouse(
                                onSuccess = { onSuccess() },
                                onError = { }
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .graphicsLayer { scaleX = buttonScale; scaleY = buttonScale },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF0E7C7B)
                        ),
                        enabled = !uiState.isLoading
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White
                            )
                        } else {
                            Text(
                                text = "Submit Listing",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

/**
 * Section header component
 */
@Composable
private fun SectionHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold
        ),
        color = Color(0xFF0E7C7B),
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

private fun fetchCurrentLocation(
    client: FusedLocationProviderClient,
    context: android.content.Context,
    onLocationText: (String) -> Unit,
    onCoordinates: (Double, Double) -> Unit
) {
    val tokenSource = CancellationTokenSource()
    client.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, tokenSource.token)
        .addOnSuccessListener { location ->
            location ?: return@addOnSuccessListener
            val geocoder = Geocoder(context, Locale.getDefault())
            val address = runCatching {
                geocoder.getFromLocation(location.latitude, location.longitude, 1)
            }.getOrNull()?.firstOrNull()?.getAddressLine(0)
            val text = address ?: "${String.format("%.4f", location.latitude)}, ${String.format("%.4f", location.longitude)}"
            onCoordinates(location.latitude, location.longitude)
            onLocationText(text)
        }
}
