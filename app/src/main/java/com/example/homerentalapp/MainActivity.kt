 package com.example.homerentalapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.homerentalapp.navigation.NavGraph
import com.example.homerentalapp.navigation.Screen
import com.example.homerentalapp.ui.theme.HomeRentalAppTheme
import com.example.homerentalapp.util.SessionManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HomeRentalAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    // Check if user is logged in, navigate to Landing if true
                    val startDestination = if (SessionManager.isUserLoggedIn()) {
                        Screen.Landing.route
                    } else {
                        Screen.Login.route
                    }
                    NavGraph(navController = navController, startDestination = startDestination)
                }
            }
        }
    }
}
