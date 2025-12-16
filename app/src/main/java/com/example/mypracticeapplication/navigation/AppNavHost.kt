package com.example.mypracticeapplication.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mycomposablepractice.ui.screens.PremiumScreen
import com.example.mypracticeapplication.ui.screens.CanvasScreen
import com.example.mypracticeapplication.ui.screens.CompareScreen
import com.example.mypracticeapplication.ui.screens.CustomOnboardingScreen
import com.example.mypracticeapplication.ui.screens.ExperimentScreen
import com.example.mypracticeapplication.ui.screens.HomeScreen
import com.example.mypracticeapplication.ui.screens.OfferScreen
import com.example.mypracticeapplication.ui.screens.OnboardingV2Screen
import com.example.mypracticeapplication.ui.screens.SettingsScreen
import com.example.mypracticeapplication.ui.screens.ViewBatchImageScreen

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
                onNavigateToCustomOnboarding = { navController.navigate(CustomOnboardingRoute) },
                onNavigateToExperiment = { navController.navigate(ExperimentRoute) },
                onNavigateToOnboardingV2 = { navController.navigate(OnboardingV2Route) },
                onNavigateToCompare = { navController.navigate(CompareRoute) },
                onNavigateToViewBatchImage = { navController.navigate(ViewBatchImageRoute) }
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

        composable<CustomOnboardingRoute> {
            CustomOnboardingScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<ExperimentRoute> {
            ExperimentScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<OnboardingV2Route> {
            OnboardingV2Screen(
                onNavigateBack = { navController.popBackStack() },
                onComplete = { navController.popBackStack() }
            )
        }

        composable<CompareRoute> {
            CompareScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<ViewBatchImageRoute> {
            ViewBatchImageScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
