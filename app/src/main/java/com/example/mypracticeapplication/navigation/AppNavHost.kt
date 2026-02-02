package com.example.mypracticeapplication.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.IntOffset
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
import com.example.mypracticeapplication.ui.screens.practice.ui_architecture.Level4Screen
import com.example.mypracticeapplication.ui.screens.practice.ui_architecture.SelfPracticeScreen
import com.example.mypracticeapplication.ui.screens.practice.ui_architecture.SharedFlowScreen
import com.example.mypracticeapplication.ui.screens.practice.ui_architecture.Level1UiState
import com.example.mypracticeapplication.ui.screens.practice.ui_architecture.Level1ViewModel
import com.example.mypracticeapplication.ui.screens.library.LibraryScreen
import com.example.mypracticeapplication.ui.screens.library.ZoomImageScreen
import com.example.mypracticeapplication.ui.screens.animation.AnimationScreen
import com.example.mypracticeapplication.ui.screens.animation.AnimatedGraphsScreen
import com.example.mypracticeapplication.ui.screens.animation.MapsScreen
import com.example.mypracticeapplication.ui.screens.animation.AnimationTypeScreen
import com.example.mypracticeapplication.ui.screens.animation.ConfettiScreen
import com.example.mypracticeapplication.ui.screens.animation.FireworksScreen
import com.example.mypracticeapplication.ui.screens.animation.ConfettiBurstScreen
import com.example.mypracticeapplication.ui.screens.animation.JsonAnimScreen
import com.example.mypracticeapplication.ui.screens.animation.FestiveConfettiScreen
import com.example.mypracticeapplication.ui.screens.animation.LottieConfettiScreen
import com.example.mypracticeapplication.ui.screens.animation.CoinHarvestScreen
import com.example.mypracticeapplication.ui.screens.animation.LoadingCircleScreen
import com.example.mypracticeapplication.ui.screens.animation.SuccessAnimationScreen
import com.example.mypracticeapplication.ui.screens.practice.CoroutineScreen



// Custom Spring Specs for Varied Smoothness
private val LuxuriousSpec = spring<IntOffset>(
    stiffness = 200f,
    dampingRatio = 1.0f
)

private val PlayfulSpec = spring<IntOffset>(
    stiffness = 400f,
    dampingRatio = 0.6f
)

private val ModernSpec = spring<IntOffset>(
    stiffness = 350f,
    dampingRatio = 0.85f
)

// Float specs for Scale/Fade if needed, matching the stiffness
private val ModernSpecFloat = spring<Float>(
    stiffness = 350f,
    dampingRatio = 0.85f
)

@Composable
fun AppNavHost(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = HomeRoute,
        // 1. Enter: Slide in from Right (Modern)
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = ModernSpec
            ) + fadeIn(animationSpec = ModernSpecFloat)
        },
        // 2. Exit: Slide out to Left (Modern)
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = ModernSpec
            ) + fadeOut(animationSpec = ModernSpecFloat) + scaleOut(
                targetScale = 0.92f,
                animationSpec = ModernSpecFloat
            )
        },
        // 3. Pop Enter (Back): Slide in from Left (Modern)
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = ModernSpec
            ) + fadeIn(animationSpec = ModernSpecFloat) + scaleIn(
                initialScale = 0.92f,
                animationSpec = ModernSpecFloat
            )
        },
        // 4. Pop Exit (Back): Slide out to Right (Modern)
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = ModernSpec
            ) + fadeOut(animationSpec = ModernSpecFloat)
        }
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
                onNavigateToCoroutine = { navController.navigate(CoroutineRoute) }
            )
        }

composable<ProfileRoute>(
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(400)
                ) + fadeIn(animationSpec = tween(400))
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(400)
                ) + fadeOut(animationSpec = tween(400))
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(400)
                ) + fadeIn(animationSpec = tween(400))
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(400)
                ) + fadeOut(animationSpec = tween(400))
            }
        ) {
            PremiumScreen(
                onClose = { navController.popBackStack() },
                onNavigateToTrial = { navController.navigate(SettingsRoute) }
            )
        }

composable<SettingsRoute>(
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(400)
                ) + fadeIn(animationSpec = tween(400))
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(400)
                ) + fadeOut(animationSpec = tween(400))
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(400)
                ) + fadeIn(animationSpec = tween(400))
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(400)
                ) + fadeOut(animationSpec = tween(400))
            }
        ) {
            SettingsScreen(
            )
        }

composable<CanvasRoute>(
            enterTransition = {
                scaleIn(initialScale = 0.8f, animationSpec = tween(300)) + fadeIn(animationSpec = tween(300))
            },
            exitTransition = {
                scaleOut(targetScale = 0.8f, animationSpec = tween(300)) + fadeOut(animationSpec = tween(300))
            },
            popEnterTransition = {
                scaleIn(initialScale = 0.8f, animationSpec = tween(300)) + fadeIn(animationSpec = tween(300))
            },
            popExitTransition = {
                scaleOut(targetScale = 0.8f, animationSpec = tween(300)) + fadeOut(animationSpec = tween(300))
            }
        ) {
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

        composable<ExperimentRoute>(
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = PlayfulSpec
                ) + fadeIn(animationSpec = tween(300))
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = PlayfulSpec
                ) + fadeOut(animationSpec = tween(300))
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = PlayfulSpec
                ) + fadeIn(animationSpec = tween(300))
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = PlayfulSpec
                ) + fadeOut(animationSpec = tween(300))
            }
        ) {
            ExperimentScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToHeavyComputation = { navController.navigate(HeavyComputationRoute) },
                onNavigateToComplexUi = { navController.navigate(ComplexUiRoute) }
            )
        }

        composable<ComplexUiRoute>(
             enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = ModernSpec
                ) + fadeIn(animationSpec = ModernSpecFloat)
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = ModernSpec
                ) + fadeOut(animationSpec = ModernSpecFloat) + scaleOut(
                    targetScale = 0.92f,
                    animationSpec = ModernSpecFloat
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = ModernSpec
                ) + fadeIn(animationSpec = ModernSpecFloat) + scaleIn(
                    initialScale = 0.92f,
                    animationSpec = ModernSpecFloat
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = ModernSpec
                ) + fadeOut(animationSpec = ModernSpecFloat)
            }
        ) {
            com.example.mypracticeapplication.ui.screens.experiment.ComplexUiScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<HeavyComputationRoute>(
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = ModernSpec
                ) + fadeIn(animationSpec = ModernSpecFloat)
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = ModernSpec
                ) + fadeOut(animationSpec = ModernSpecFloat) + scaleOut(
                    targetScale = 0.92f,
                    animationSpec = ModernSpecFloat
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = ModernSpec
                ) + fadeIn(animationSpec = ModernSpecFloat) + scaleIn(
                    initialScale = 0.92f,
                    animationSpec = ModernSpecFloat
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = ModernSpec
                ) + fadeOut(animationSpec = ModernSpecFloat)
            }
        ) {
            com.example.mypracticeapplication.ui.screens.experiment.HeavyComputationScreen(
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

composable<LibraryRoute>(
            enterTransition = {
                scaleIn(initialScale = 0.8f, animationSpec = tween(300)) + fadeIn(animationSpec = tween(300))
            },
            exitTransition = {
                scaleOut(targetScale = 0.8f, animationSpec = tween(300)) + fadeOut(animationSpec = tween(300))
            },
            popEnterTransition = {
                scaleIn(initialScale = 0.8f, animationSpec = tween(300)) + fadeIn(animationSpec = tween(300))
            },
            popExitTransition = {
                scaleOut(targetScale = 0.8f, animationSpec = tween(300)) + fadeOut(animationSpec = tween(300))
            }
        ) {
            LibraryScreen(
                onNavigateToZoomImage = { navController.navigate(ZoomImageRoute) }
            )
        }

composable<ZoomImageRoute>(
            enterTransition = {
                scaleIn(initialScale = 0.8f, animationSpec = tween(300)) + fadeIn(animationSpec = tween(300))
            },
            exitTransition = {
                scaleOut(targetScale = 0.8f, animationSpec = tween(300)) + fadeOut(animationSpec = tween(300))
            },
            popEnterTransition = {
                scaleIn(initialScale = 0.8f, animationSpec = tween(300)) + fadeIn(animationSpec = tween(300))
            },
            popExitTransition = {
                scaleOut(targetScale = 0.8f, animationSpec = tween(300)) + fadeOut(animationSpec = tween(300))
            }
        ) {
            ZoomImageScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<AnimationRoute>(
             enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = PlayfulSpec
                ) + fadeIn(animationSpec = tween(300))
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = PlayfulSpec
                ) + fadeOut(animationSpec = tween(300))
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = PlayfulSpec
                ) + fadeIn(animationSpec = tween(300))
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = PlayfulSpec
                ) + fadeOut(animationSpec = tween(300))
            }
        ) {
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
                onNavigateToOnboardingElement = { navController.navigate(OnboardingElementRoute) }
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
            com.example.mypracticeapplication.ui.screens.animation.OnboardingPage1Screen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<OnboardingElementRoute> {
            com.example.mypracticeapplication.ui.screens.animation.OnboardingElementScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
