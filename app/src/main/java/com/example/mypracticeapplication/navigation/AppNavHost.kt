package com.example.mypracticeapplication.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mypracticeapplication.ui.screens.settings.PremiumScreen
import com.example.mypracticeapplication.ui.screens.image.CanvasScreen
import com.example.mypracticeapplication.ui.screens.image.CompareScreen
import com.example.mypracticeapplication.ui.screens.onboarding.CustomOnboardingScreen
import com.example.mypracticeapplication.ui.screens.experiment.ExperimentScreen
import com.example.mypracticeapplication.ui.screens.home.HomeScreen
import com.example.mypracticeapplication.ui.screens.experiment.OfferScreen
import com.example.mypracticeapplication.ui.screens.onboarding.OnboardingV2Screen
import com.example.mypracticeapplication.ui.screens.settings.SettingsScreen
import com.example.mypracticeapplication.ui.screens.image.ResultScreen
import com.example.mypracticeapplication.ui.screens.image.ViewBatchImageScreen
import com.example.mypracticeapplication.ui.screens.compose_lab.ComposeTestLapScreen
import com.example.mypracticeapplication.ui.screens.compose_lab.state.StateLabScreen
import com.example.mypracticeapplication.ui.screens.compose_lab.state.ListStateDemoScreen
import com.example.mypracticeapplication.ui.screens.compose_lab.state.StateMechanicsLabScreen
import com.example.mypracticeapplication.ui.screens.image.FitPhotoScreen
import com.example.mypracticeapplication.ui.screens.compose_lab.ComposeLabScreen
import com.example.mypracticeapplication.ui.screens.compose_lab.side_effects.SideEffectApisScreen
import com.example.mypracticeapplication.ui.screens.compose_lab.side_effects.LaunchedEffectExampleScreen
import com.example.mypracticeapplication.ui.screens.compose_lab.side_effects.DisposableEffectExampleScreen
import com.example.mypracticeapplication.ui.screens.practice.PracticeScreen
import com.example.mypracticeapplication.ui.screens.practice.UiArchitectureScreen
import com.example.mypracticeapplication.ui.screens.practice.ui_architecture.Level1Screen
import com.example.mypracticeapplication.ui.screens.practice.ui_architecture.Level2Screen
import com.example.mypracticeapplication.ui.screens.practice.ui_architecture.Level3Screen
import com.example.mypracticeapplication.ui.screens.practice.ui_architecture.Level1UiState
import com.example.mypracticeapplication.ui.screens.practice.ui_architecture.Level1ViewModel
import com.example.mypracticeapplication.ui.screens.library.LibraryScreen
import com.example.mypracticeapplication.ui.screens.library.ZoomImageScreen


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
                onNavigateToViewBatchImage = { navController.navigate(ViewBatchImageRoute) },
                onNavigateToResult = { navController.navigate(ResultRoute) },
                onNavigateToFitPhoto = { navController.navigate(FitPhotoRoute) },
                onNavigateToComposeTestLap = { navController.navigate(ComposeTestLapRoute) },
                onNavigateToComposeLab = { navController.navigate(ComposeLabRoute) },
                onNavigateToSideEffectApis = { navController.navigate(SideEffectApisRoute) },
                onNavigateToPractice = { navController.navigate(PracticeRoute) },
                onNavigateToLibrary = { navController.navigate(LibraryRoute) }
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
                onNavigateToResult = { navController.navigate(ResultRoute) }
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

        composable<ResultRoute> {
            ResultScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<FitPhotoRoute> {
            FitPhotoScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<ComposeTestLapRoute> {
            ComposeTestLapScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToStateLab = { navController.navigate(StateLabRoute) },
                onNavigateToStateMechanicsLab = { navController.navigate(StateMechanicsLabRoute) }
            )
        }

        composable<StateLabRoute> {
            StateLabScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToListStateDemo = { navController.navigate(ListStateDemoRoute) }
            )
        }

        composable<ListStateDemoRoute> {
            ListStateDemoScreen()
        }

        composable<StateMechanicsLabRoute> {
            StateMechanicsLabScreen()
        }

        composable<ComposeLabRoute> {
            ComposeLabScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<SideEffectApisRoute> {
            SideEffectApisScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToLaunchedEffect = { navController.navigate(LaunchedEffectExampleRoute) },
                onNavigateToDisposableEffect = { navController.navigate(DisposableEffectExampleRoute) }
            )
        }

        composable<LaunchedEffectExampleRoute> {
            LaunchedEffectExampleScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<DisposableEffectExampleRoute> {
            DisposableEffectExampleScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<PracticeRoute> {
            PracticeScreen(
                onNavigateToUiArchitecture = { navController.navigate(UiArchitectureRoute) }
            )
        }

        composable<UiArchitectureRoute> {
            UiArchitectureScreen(
                onNavigateToLevel1 = { navController.navigate(Level1Route) },
                onNavigateToLevel2 = { navController.navigate(Level2Route) },
                onNavigateToLevel3 = { navController.navigate(Level3Route) }
            )
        }

        composable<Level1Route> {
            Level1Screen()
        }

        composable<Level2Route> {
            Level2Screen()
        }

        composable<Level3Route> {
            Level3Screen()
        }

        composable<LibraryRoute> {
            LibraryScreen(
                onNavigateToZoomImage = { navController.navigate(ZoomImageRoute) }
            )
        }

        composable<ZoomImageRoute> {
            ZoomImageScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
