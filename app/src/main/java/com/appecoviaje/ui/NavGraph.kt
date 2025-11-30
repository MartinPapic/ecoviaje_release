package com.appecoviaje.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController) }
        composable("registration") { RegistrationScreen(navController) }
        composable("home") { HomeScreen(navController) }
        composable("planning") { TripPlanningScreen(navController) }
        composable("settings") { SettingsScreen(navController) }
        composable("experiences") { ExperienceScreen(navController) }
        composable("reservations") { ReservationScreen(navController) }
        composable(
            route = "payment/{tripId}/{date}/{type}",
            arguments = listOf(
                androidx.navigation.navArgument("tripId") { type = androidx.navigation.NavType.IntType },
                androidx.navigation.navArgument("date") { type = androidx.navigation.NavType.LongType },
                androidx.navigation.navArgument("type") { type = androidx.navigation.NavType.StringType }
            )
        ) { backStackEntry ->
            val tripId = backStackEntry.arguments?.getInt("tripId") ?: 0
            val date = backStackEntry.arguments?.getLong("date") ?: 0L
            val type = backStackEntry.arguments?.getString("type") ?: ""
            PaymentScreen(navController, tripId, date, type)
        }
    }
}
