package com.example.homerentalapp.navigation

/**
 * Sealed class representing all screens in the app
 */
sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Signup : Screen("signup")
    object Home : Screen("home")
    object Landing : Screen("landing")
    object Search : Screen("search")
    object HouseDetails : Screen("house_details/{houseId}") {
        fun createRoute(houseId: String) = "house_details/$houseId"
    }
    object AddHouse : Screen("add_house")
    object NearbySearch : Screen("nearby_search")
}

