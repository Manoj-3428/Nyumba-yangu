package com.example.homerentalapp.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.homerentalapp.R
import com.example.homerentalapp.model.House

/**
 * Alibaba style house card with hero image, price tag, and amenities.
 */
@Composable
fun HouseCard(
    house: House,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
 ) {
    var isPressed by remember { mutableStateOf(false) }
    val elevation by animateFloatAsState(
        targetValue = if (isPressed) 2f else 8f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 300f),
        label = "cardElevation"
    )
    val borderColor by animateColorAsState(
        targetValue = if (isPressed) Color(0xFF0E7C7B) else Color.Transparent,
        label = "cardBorder"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                isPressed = true
                onClick()
                isPressed = false
            },
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Hero Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(20.dp))
            ) {
                AsyncImage(
                    model = house.imageUrls.firstOrNull()
                        ?: "https://images.unsplash.com/photo-1505691938895-1758d7feb511?w=800",
                    contentDescription = house.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                                startY = 200f
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = house.type.ifEmpty { "Modern Home" },
                        color = Color.White.copy(alpha = 0.85f),
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(
                        text = house.description.ifEmpty { "Spacious living with city skyline view" },
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        maxLines = 2
                    )
                }

                // Price Tag
                Text(
                    text = "KSh ${house.price.toInt()} / month",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF0E7C7B))
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                // Location
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_place),
                        contentDescription = null,
                        tint = Color(0xFF0E7C7B),
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = house.location.ifEmpty { "Prime Location" },
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = Color.Black
                    )
                }
                Text(
                    text = house.address.ifEmpty { "Luxury Apartments, Westlands, Nairobi" },
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            // Stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatBadge(label = "Vacant", value = house.vacantUnits.toString())
                StatBadge(label = "Total", value = house.totalUnits.toString())
                StatBadge(label = "Caretaker", value = house.caretakerName.ifEmpty { "Assigned" })
            }

            // Amenities chips
            if (house.amenities.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        house.amenities.take(2).forEach { amenity ->
                            AssistChip(
                                onClick = {},
                                label = { Text(amenity, color = Color(0xFF0E7C7B)) },
                                colors = AssistChipDefaults.assistChipColors(
                                    containerColor = Color(0xFF0E7C7B).copy(alpha = 0.08f)
                                ),
                                shape = RoundedCornerShape(18.dp)
                            )
                        }
                    }
                    if (house.amenities.size > 2) {
                        Text(
                            text = "+${house.amenities.size - 2} more",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color(0xFF0E7C7B)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatBadge(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = Color.Black
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray
        )
    }
}

