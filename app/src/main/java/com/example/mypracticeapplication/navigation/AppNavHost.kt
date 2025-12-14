package com.example.mypracticeapplication.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mycomposablepractice.ui.screens.PremiumScreen
import com.example.mypracticeapplication.ui.screens.BottomSheetScreen
import com.example.mypracticeapplication.ui.screens.CanvasScreen
import com.example.mypracticeapplication.ui.screens.CompressOnboardingScreen
import com.example.mypracticeapplication.ui.screens.HealthOnboardingScreen
import com.example.mypracticeapplication.ui.screens.HomeScreen
import com.example.mypracticeapplication.ui.screens.OfferScreen
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
                onNavigateToSettings = { navController.navigate(SettingsRoute) },
                onNavigateToCanvas = { navController.navigate(CanvasRoute) },
                onNavigateToOffer = { navController.navigate(OfferRoute) },
                onNavigateToHealthOnboarding = { navController.navigate(HealthOnboardingRoute) },
                onNavigateToBottomSheet = { navController.navigate(BottomSheetRoute) },
                onNavigateToCompressOnboarding = { navController.navigate(CompressOnboardingRoute) }
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

        composable<CanvasRoute> {
            CanvasScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<OfferRoute> {
            OfferScreen()
        }

        composable<HealthOnboardingRoute> {
            HealthOnboardingScreen()
        }

        composable<BottomSheetRoute> {
            BottomSheetScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<CompressOnboardingRoute> {
            CompressOnboardingScreen()
        }
    }
}
