package com.example.homerentalapp.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import com.example.homerentalapp.ui.theme.PrimaryBlue
import com.example.homerentalapp.ui.theme.PrimaryBlueLight
import com.example.homerentalapp.ui.theme.PrimaryBlueLighter

/**
 * Animated gradient background for auth screens
 * Uses a stable static gradient to prevent graphics layer crashes
 */
@Composable
fun AnimatedBackground(modifier: Modifier = Modifier) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp
    
    // Use static gradient with fixed, valid endpoints
    // This prevents graphics layer crashes from dynamic gradient changes
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        PrimaryBlue,
                        PrimaryBlueLight,
                        PrimaryBlueLighter
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(
                        screenWidth.toFloat(),
                        screenHeight.toFloat()
                    )
                )
            )
    )
}

