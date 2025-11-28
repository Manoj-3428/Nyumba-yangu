package com.example.homerentalapp.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.homerentalapp.ui.screens.addhouse.AddHouseScreen
import com.example.homerentalapp.ui.screens.details.HouseDetailsScreen
import com.example.homerentalapp.ui.screens.home.HomeScreen
import com.example.homerentalapp.ui.screens.landing.LandingScreen
import com.example.homerentalapp.ui.screens.login.LoginScreen
import com.example.homerentalapp.ui.screens.nearby.NearbyMapScreen
import com.example.homerentalapp.ui.screens.signup.SignupScreen
import com.example.homerentalapp.viewmodel.HouseDetailsViewModel

/**
 * Navigation graph for the app
 * Handles all screen transitions with animations
 */
@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Login.route,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Login Screen
        composable(
            route = Screen.Login.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(400)
                ) + fadeIn(animationSpec = tween(400))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(400)
                ) + fadeOut(animationSpec = tween(400))
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(400)
                ) + fadeIn(animationSpec = tween(400))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(400)
                ) + fadeOut(animationSpec = tween(400))
            }
        ) {
            LoginScreen(
                onNavigateToSignup = {
                    navController.navigate(Screen.Signup.route) {
                        popUpTo(Screen.Login.route) { inclusive = false }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Landing.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // Signup Screen
        composable(
            route = Screen.Signup.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(400)
                ) + fadeIn(animationSpec = tween(400))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(400)
                ) + fadeOut(animationSpec = tween(400))
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(400)
                ) + fadeIn(animationSpec = tween(400))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(400)
                ) + fadeOut(animationSpec = tween(400))
            }
        ) {
            SignupScreen(
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Landing.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // Landing Screen
        composable(
            route = Screen.Landing.route,
            enterTransition = {
                fadeIn(animationSpec = tween(400)) + slideInVertically(
                    initialOffsetY = { it / 2 },
                    animationSpec = tween(400)
                )
            },
            exitTransition = {
                fadeOut(animationSpec = tween(400)) + slideOutVertically(
                    targetOffsetY = { it / 2 },
                    animationSpec = tween(400)
                )
            }
        ) {
            LandingScreen(
                onNavigateToSearch = {
                    navController.navigate(Screen.Home.route)
                },
                onNavigateToNearby = {
                    navController.navigate(Screen.NearbySearch.route)
                },
                onNavigateToAddHouse = {
                    navController.navigate(Screen.AddHouse.route)
                }
            )
        }

        // Add House Screen
        composable(
            route = Screen.AddHouse.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(400)
                ) + fadeIn(animationSpec = tween(400))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(400)
                ) + fadeOut(animationSpec = tween(400))
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(400)
                ) + fadeIn(animationSpec = tween(400))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(400)
                ) + fadeOut(animationSpec = tween(400))
            }
        ) {
            AddHouseScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.AddHouse.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        // Home Screen - list of houses
        composable(
            route = Screen.Home.route,
            enterTransition = {
                fadeIn(animationSpec = tween(400)) + slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(400)
                )
            },
            exitTransition = {
                fadeOut(animationSpec = tween(400)) + slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(400)
                )
            }
        ) {
            HomeScreen(
                onNavigateBack = { navController.popBackStack() },
                onAddHouse = { navController.navigate(Screen.AddHouse.route) },
                onOpenDetails = { houseId ->
                    navController.navigate(Screen.HouseDetails.createRoute(houseId))
                }
            )
        }

        // Nearby map screen
        composable(route = Screen.NearbySearch.route) {
            NearbyMapScreen(
                onBack = { navController.popBackStack() },
                onHouseSelected = { houseId ->
                    navController.navigate(Screen.HouseDetails.createRoute(houseId))
                }
            )
        }

        // House details
        composable(
            route = Screen.HouseDetails.route,
            arguments = listOf(navArgument("houseId") { type = NavType.StringType })
        ) { backStackEntry ->
            val houseId = backStackEntry.arguments?.getString("houseId") ?: ""
            val detailsViewModel: HouseDetailsViewModel = viewModel(
                viewModelStoreOwner = backStackEntry,
                key = "house_details_$houseId",
                factory = HouseDetailsViewModel.provideFactory(houseId)
            )
            HouseDetailsScreen(
                onBack = { navController.popBackStack() },
                viewModel = detailsViewModel
            )
        }
    }
}

