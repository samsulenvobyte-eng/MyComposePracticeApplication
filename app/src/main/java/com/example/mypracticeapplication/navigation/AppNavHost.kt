package com.example.mypracticeapplication.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mycomposablepractice.ui.screens.PremiumScreen
import com.example.mypracticeapplication.ui.screens.HomeScreen
import com.example.mypracticeapplication.ui.screens.SettingsScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = HomeRoute
    ) {
        composable<HomeRoute> {
            HomeScreen(
                onNavigateToProfile = { navController.navigate(ProfileRoute) },
                onNavigateToSettings = { navController.navigate(SettingsRoute) }
            )
        }

        composable<ProfileRoute> {
            PremiumScreen(
                onClose = { navController.popBackStack() },
                onNavigateToTrial = { navController.navigate(SettingsRoute) }
            )
        }

        composable<SettingsRoute> {
            SettingsScreen(
            )
        }
    }
}
