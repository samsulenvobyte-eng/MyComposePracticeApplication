package com.example.mypracticeapplication.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mypracticeapplication.presentation.settings.PremiumScreen
import com.example.mypracticeapplication.presentation.image.CanvasScreen
import com.example.mypracticeapplication.presentation.image.CompareScreen
import com.example.mypracticeapplication.presentation.onboarding.CustomOnboardingScreen
import com.example.mypracticeapplication.presentation.experiment.ExperimentScreen
import com.example.mypracticeapplication.presentation.home.HomeScreen
import com.example.mypracticeapplication.presentation.experiment.OfferScreen
import com.example.mypracticeapplication.presentation.onboarding.OnboardingV2Screen
import com.example.mypracticeapplication.presentation.settings.SettingsScreen
import com.example.mypracticeapplication.presentation.image.ResultScreen
import com.example.mypracticeapplication.presentation.image.ViewBatchImageScreen
import com.example.mypracticeapplication.presentation.compose_lab.ComposeTestLapScreen
import com.example.mypracticeapplication.presentation.compose_lab.state.StateLabScreen
import com.example.mypracticeapplication.presentation.compose_lab.state.ListStateDemoScreen
import com.example.mypracticeapplication.presentation.compose_lab.state.StateMechanicsLabScreen
import com.example.mypracticeapplication.presentation.image.FitPhotoScreen
import com.example.mypracticeapplication.presentation.compose_lab.ComposeLabScreen
import com.example.mypracticeapplication.presentation.compose_lab.side_effects.SideEffectApisScreen
import com.example.mypracticeapplication.presentation.compose_lab.side_effects.LaunchedEffectExampleScreen
import com.example.mypracticeapplication.presentation.compose_lab.side_effects.DisposableEffectExampleScreen
import com.example.mypracticeapplication.presentation.practice.PracticeScreen
import com.example.mypracticeapplication.presentation.practice.UiArchitectureScreen
import com.example.mypracticeapplication.presentation.practice.ui_architecture.Level1Screen
import com.example.mypracticeapplication.presentation.practice.ui_architecture.Level2Screen
import com.example.mypracticeapplication.presentation.practice.ui_architecture.Level3Screen
import com.example.mypracticeapplication.presentation.practice.ui_architecture.Level4Screen
import com.example.mypracticeapplication.presentation.practice.ui_architecture.SelfPracticeScreen
import com.example.mypracticeapplication.presentation.practice.ui_architecture.SharedFlowScreen
import com.example.mypracticeapplication.presentation.library.LibraryScreen
import com.example.mypracticeapplication.presentation.library.ZoomImageScreen
import com.example.mypracticeapplication.presentation.animation.AnimationScreen
import com.example.mypracticeapplication.presentation.animation.AnimatedGraphsScreen
import com.example.mypracticeapplication.presentation.animation.MapsScreen
import com.example.mypracticeapplication.presentation.animation.AnimationTypeScreen
import com.example.mypracticeapplication.presentation.animation.ConfettiScreen
import com.example.mypracticeapplication.presentation.animation.FireworksScreen
import com.example.mypracticeapplication.presentation.animation.ConfettiBurstScreen
import com.example.mypracticeapplication.presentation.animation.JsonAnimScreen
import com.example.mypracticeapplication.presentation.animation.FestiveConfettiScreen
import com.example.mypracticeapplication.presentation.animation.LottieConfettiScreen
import com.example.mypracticeapplication.presentation.animation.CoinHarvestScreen
import com.example.mypracticeapplication.presentation.animation.LoadingCircleScreen
import com.example.mypracticeapplication.presentation.animation.SuccessAnimationScreen
import com.example.mypracticeapplication.presentation.animation.BoardingCompScreen
import com.example.mypracticeapplication.presentation.ttboost_animation.TtBoostOnboardingScreen
import com.example.mypracticeapplication.presentation.practice.CoroutineScreen
import com.example.mypracticeapplication.presentation.notification.NotificationScreen
import com.example.mypracticeapplication.presentation.notification.NotificationViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = HomeRoute,
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
                onNavigateToLibrary = { navController.navigate(LibraryRoute) },
                onNavigateToAnimation = { navController.navigate(AnimationRoute) },
                onNavigateToCoroutine = { navController.navigate(CoroutineRoute) },
                onNavigateToNotification = { navController.navigate(NotificationRoute) }
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
                onNavigateBack = { navController.popBackStack() },
                onNavigateToHeavyComputation = { navController.navigate(HeavyComputationRoute) },
                onNavigateToComplexUi = { navController.navigate(ComplexUiRoute) }
            )
        }

        composable<ComplexUiRoute> {
            com.example.mypracticeapplication.presentation.experiment.ComplexUiScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<HeavyComputationRoute> {
            com.example.mypracticeapplication.presentation.experiment.HeavyComputationScreen(
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
                onNavigateToLevel3 = { navController.navigate(Level3Route) },
                onNavigateToLevel4 = { navController.navigate(Level4Route) },
                onNavigateToSelfPractice = { navController.navigate(SelfPracticeRoute) },
                onNavigateToSharedFlow = { navController.navigate(SharedFlowRoute) }
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

        composable<Level4Route> {
            Level4Screen()
        }

        composable<SelfPracticeRoute> {
            SelfPracticeScreen()
        }

        composable<SharedFlowRoute> {
            SharedFlowScreen()
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

        composable<AnimationRoute> {
            AnimationScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToGraphs = { navController.navigate(AnimatedGraphsRoute) },
                onNavigateToMaps = { navController.navigate(MapsRoute) },
                onNavigateToAnimationType = { navController.navigate(AnimationTypeRoute) },
                onNavigateToConfetti = { navController.navigate(ConfettiRoute) },
                onNavigateToFireworks = { navController.navigate(FireworksRoute) },
                onNavigateToConfettiBurst = { navController.navigate(ConfettiBurstRoute) },
                onNavigateToJsonAnim = { navController.navigate(JsonAnimRoute) },
                onNavigateToFestiveConfetti = { navController.navigate(FestiveConfettiRoute) },
                onNavigateToLottieConfetti = { navController.navigate(LottieConfettiRoute) },
                onNavigateToCoinHarvest = { navController.navigate(CoinHarvestRoute) },
                onNavigateToLoadingCircle = { navController.navigate(LoadingCircleRoute) },
                onNavigateToSuccessAnimation = { navController.navigate(SuccessAnimationRoute) },
                onNavigateToOnboardingPage1 = { navController.navigate(OnboardingPage1Route) },
                onNavigateToOnboardingPage2 = { navController.navigate(OnboardingPage2Route) },
                onNavigateToOnboardingElement = { navController.navigate(OnboardingElementRoute) },
                onNavigateToBoardingComp = { navController.navigate(BoardingCompRoute) },
                onNavigateToTtBoostOnboarding = { navController.navigate(TtBoostOnboardingRoute) }
            )
        }

        composable<AnimatedGraphsRoute> {
            AnimatedGraphsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<MapsRoute> {
            MapsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<AnimationTypeRoute> {
            AnimationTypeScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<ConfettiRoute> {
            ConfettiScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<FireworksRoute> {
            FireworksScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<ConfettiBurstRoute> {
            ConfettiBurstScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<JsonAnimRoute> {
            JsonAnimScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<FestiveConfettiRoute> {
            FestiveConfettiScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<CoroutineRoute> {
            CoroutineScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<LottieConfettiRoute> {
            LottieConfettiScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

            composable<CoinHarvestRoute> {
            CoinHarvestScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<LoadingCircleRoute> {
            LoadingCircleScreen(
                navController = navController
            )
        }

        composable<SuccessAnimationRoute> {
            SuccessAnimationScreen(
                navController = navController
            )
        }

        composable<OnboardingPage1Route> {
            com.example.mypracticeapplication.presentation.animation.OnboardingPage1Screen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<OnboardingElementRoute> {
            com.example.mypracticeapplication.presentation.animation.OnboardingElementScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<OnboardingPage2Route> {
            com.example.mypracticeapplication.presentation.animation.OnboardingPage2Screen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<BoardingCompRoute> {
            BoardingCompScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<TtBoostOnboardingRoute> {
            TtBoostOnboardingScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable<NotificationRoute> {
            val viewModel: NotificationViewModel = viewModel(factory = NotificationViewModel.Factory)
            NotificationScreen(
                onBackClick = { navController.popBackStack() },
                viewModel = viewModel
            )
        }
    }
}



