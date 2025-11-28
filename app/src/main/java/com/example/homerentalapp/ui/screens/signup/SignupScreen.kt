package com.example.homerentalapp.ui.screens.signup

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.homerentalapp.R
import com.example.homerentalapp.ui.components.AnimatedBackground
import com.example.homerentalapp.ui.components.AuthTextField
import com.example.homerentalapp.ui.components.SocialAuthButton
import com.example.homerentalapp.viewmodel.AuthViewModel

/**
 * Signup Screen with beautiful animations
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    
    val uiState by viewModel.uiState.collectAsState()
    
    // Animate card entrance
    val cardAlpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(600, easing = FastOutSlowInEasing),
        label = "cardAlpha"
    )
    
    val cardOffsetY by animateFloatAsState(
        targetValue = 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "cardOffsetY"
    )

    // Handle successful signup
    LaunchedEffect(uiState.isAuthenticated) {
        if (uiState.isAuthenticated) {
            onNavigateToHome()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Animated background
        AnimatedBackground()

        // Main content card - Simple white card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .align(Alignment.Center)
                .graphicsLayer {
                    alpha = cardAlpha
                    translationY = cardOffsetY
                },
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Small logo icon
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF0E7C7B)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🏠",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }

                // Welcome text - Simple
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Create Account",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        textAlign = TextAlign.Center,
                        color = Color(0xFF0E7C7B)
                    )
                    Text(
                        text = "Sign up to get started",
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                        color = Color.Black
                    )
                }

                // Full Name field
                AuthTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = "Full name",
                    leadingIcon = Icons.Rounded.Person
                )

                // Email field
                AuthTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email address",
                    leadingIcon = Icons.Rounded.Phone,
                    isError = uiState.errorMessage != null && email.isNotEmpty()
                )

                // Phone field
                AuthTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = "Phone number",
                    leadingIcon = Icons.Rounded.Phone
                )

                // Password field
                AuthTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = "Password",
                    leadingIcon = Icons.Rounded.Lock,
                    isPassword = true,
                    isError = uiState.errorMessage != null && password.isNotEmpty(),
                    errorMessage = uiState.errorMessage
                )

                // Error message
                if (uiState.errorMessage != null) {
                    Text(
                        text = uiState.errorMessage!!,
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Sign up button - Simple blue button
                val isFormValid = fullName.isNotBlank() && 
                        email.isNotBlank() && 
                        phoneNumber.isNotBlank() && 
                        password.isNotBlank()

                Button(
                    onClick = { viewModel.signUp(email, password, fullName, phoneNumber) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0E7C7B)
                    ),
                    enabled = !uiState.isLoading && isFormValid
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White
                        )
                    } else {
                        Text(
                            text = "Create Account",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        )
                    }
                }

                Divider(color = Color.LightGray)

                // Social auth section
                Text(
                    text = "Continue with",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Black
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SocialAuthButton(
                        iconRes = R.drawable.ic_google,
                        backgroundColor = Color(0xFFDB4437),
                        onClick = { /* TODO: Google Sign In */ },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    SocialAuthButton(
                        iconRes = R.drawable.ic_call,
                        backgroundColor = Color(0xFF0E7C7B),
                        onClick = { /* TODO: Phone Sign In */ },
                        modifier = Modifier.weight(1f)
                    )
                }

                // Login link
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Already have an account? ",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Black
                    )
                    TextButton(onClick = onNavigateToLogin) {
                        Text(
                            text = "Log In",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0E7C7B)
                        )
                    }
                }
            }
        }
    }
}

