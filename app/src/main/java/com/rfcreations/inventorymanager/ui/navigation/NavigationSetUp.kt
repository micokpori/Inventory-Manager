package com.rfcreations.inventorymanager.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.rfcreations.inventorymanager.R
import com.rfcreations.inventorymanager.ui.screens.additemscreen.AddItemScreen
import com.rfcreations.inventorymanager.ui.screens.analyticsscreen.AnalyticsScreen
import com.rfcreations.inventorymanager.ui.screens.cartscreen.CartScreen
import com.rfcreations.inventorymanager.ui.screens.contactusscreen.ContactUsScreen
import com.rfcreations.inventorymanager.ui.screens.edititemscreen.EditItemScreen
import com.rfcreations.inventorymanager.ui.screens.forgotpasswordscreen.ForgotPasswordScreen
import com.rfcreations.inventorymanager.ui.screens.homescreen.HomeScreen
import com.rfcreations.inventorymanager.ui.screens.moreinfoscreen.MoreInfoScreen
import com.rfcreations.inventorymanager.ui.screens.profilescreen.ProfileScreen
import com.rfcreations.inventorymanager.ui.screens.signinscreen.SignInScreen
import com.rfcreations.inventorymanager.ui.screens.signupscreen.SignUpScreen
import com.rfcreations.inventorymanager.ui.screens.solditemsscreen.SoldItemsScreen
import com.rfcreations.inventorymanager.utils.Constants

/**
 * Sets up the navigation within the application using Jetpack Compose.
 * @param modifier Modifier for the root layouts.
 */
@Composable
fun NavigationSetUp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val navKeys = Constants.NavArgKeys
    val firebaseAuth = FirebaseAuth.getInstance()
    val startDestination = Screens.SignInScreen.route
      //  if (firebaseAuth.currentUser == null) Screens.SignInScreen.route else Screens.HomeScreen.route
  //  firebaseAuth.currentUser?.uid?.let { Log.d("user", it) }
    // Set up bottom navigation bar
    BottomNavBarSetUp(modifier, navController) {
        NavHost(
            navController,
            startDestination = startDestination
        ) {

            // Define each screen of the app with navigation destinations
            composable(Screens.HomeScreen.route, enterTransition = {
                slideIntoContainer(
                    animationSpec = tween(300, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Up
                )
            }) {
                HomeScreen(navController)
            }

            composable(Screens.AddItemScreen.route, enterTransition = {
                slideIntoContainer(
                    animationSpec = tween(300, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            }) {
                AddItemScreen(popBackStack = { navController.popBackStack() })
            }

            composable(
                Screens.EditStockScreen.route + "/{${navKeys.ITEM_ID}}",
                arguments = listOf(navArgument(navKeys.ITEM_ID) {
                    type = NavType.StringType
                }), enterTransition = {
                    slideIntoContainer(
                        animationSpec = tween(300, easing = EaseIn),
                        towards = AnimatedContentTransitionScope.SlideDirection.Start
                    )
                }) { navBackEntry ->
                EditItemScreen(
                    navBackEntry.arguments?.getString(navKeys.ITEM_ID, "") ?: "",
                    popBackStack = {
                        navController.popBackStack()
                    }
                )
            }

            composable(Screens.CartScreen.route, enterTransition = {
                slideIntoContainer(
                    animationSpec = tween(300, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            }) {
                CartScreen(modifier) {
                    navController.navigate(Screens.HomeScreen.route)
                }
            }

            composable(Screens.MoreInfoScreen.route, enterTransition = {
                slideIntoContainer(
                    animationSpec = tween(300, easing = EaseOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.Up
                )
            }) {
                MoreInfoScreen(navController)
            }

            composable(Screens.AnalyticsScreen.route, enterTransition = {
                slideIntoContainer(
                    animationSpec = tween(300, easing = EaseOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.Up
                )
            }) {
                AnalyticsScreen(navController, modifier)
            }
            composable(Screens.SoldItemsScreen.route, enterTransition = {
                slideIntoContainer(
                    animationSpec = tween(300, easing = EaseOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.Down
                )
            }) {
                SoldItemsScreen(navController, modifier)
            }

            composable(Screens.SignUpScreen.route) {
                SignUpScreen(
                    modifier,
                    navToHomeScreen = { navController.navigate(Screens.HomeScreen.route) },
                    navToSignInScreen = { navController.navigate(Screens.SignInScreen.route) })
            }
            composable(Screens.SignInScreen.route) {
                SignInScreen(
                    modifier,
                    navToHomeScreen = { navController.navigate(Screens.HomeScreen.route) },
                    navToSignUpScreen = {
                        navController.navigate(Screens.SignUpScreen.route)
                    },
                    navToForgotPasswordScreen = {
                        navController.navigate(Screens.ForgotPasswordScreen.route)
                    }
                )
            }

            composable(Screens.ProfileScreen.route) {
                ProfileScreen(modifier, navController)
            }
            composable(Screens.ContactUsScreen.route) {
                ContactUsScreen{
                    navController.popBackStack()
                }
            }
            composable(Screens.ForgotPasswordScreen.route) {
                ForgotPasswordScreen(modifier){
                    navController.popBackStack()
                }
            }
        }

    }
}

/**
 * Sets up the bottom navigation bar for the application.
 * @param modifier Modifier for the root layout.
 * @param navController Navigation controller to handle navigation events.
 * @param content The content of the screen.
 */
@Composable
private fun BottomNavBarSetUp(
    modifier: Modifier = Modifier,
    navController: NavController,
    content: @Composable () -> Unit
) {
    // Determine which screen is currently selected in the navigation stack
    val backStackEntry = navController.currentBackStackEntryAsState()
    val isHomeScreenSelected = Screens.HomeScreen.route == backStackEntry.value?.destination?.route
    val isAnalyticsScreenSelected =
        Screens.AnalyticsScreen.route == backStackEntry.value?.destination?.route
    val isMoreInfoScreenSelected =
        Screens.MoreInfoScreen.route == backStackEntry.value?.destination?.route
    val showBottomNavBar =
        isHomeScreenSelected || isMoreInfoScreenSelected || isAnalyticsScreenSelected

    // Set up scaffold with bottom navigation bar
    Scaffold(
        modifier,
        bottomBar = {
            if (showBottomNavBar) {
                NavigationBar(containerColor = Color.Transparent) {
                    // Define each item in the bottom navigation bar
                    NavigationBarItem(
                        selected = isHomeScreenSelected,
                        onClick = {
                            if (!isHomeScreenSelected) {
                                navController.navigate(Screens.HomeScreen.route)
                            }
                        },
                        icon = {
                            Icon(
                                if (isHomeScreenSelected) Icons.Filled.Home else Icons.Outlined.Home,
                                null
                            )
                        }
                    )

                    NavigationBarItem(
                        selected = isAnalyticsScreenSelected,
                        onClick = {
                            if (!isAnalyticsScreenSelected) {
                                navController.navigate(Screens.AnalyticsScreen.route)
                            }
                        },
                        icon = {
                            Icon(
                                painterResource(
                                    if (isAnalyticsScreenSelected) R.drawable.assessment_icon_filled else R.drawable.assessment_icon_outlined
                                ), null
                            )
                        }
                    )
                    NavigationBarItem(
                        selected = isMoreInfoScreenSelected,
                        onClick = {
                            if (!isMoreInfoScreenSelected) {
                                navController.navigate(Screens.MoreInfoScreen.route)
                            }
                        },
                        icon = {
                            Icon(
                                if (isMoreInfoScreenSelected) Icons.Filled.Menu else Icons.Outlined.Menu,
                                null
                            )
                        }
                    )
                }
            }
        }
    ) {
        // Set up the content of the screen
        Box(modifier.padding(it)) {
            content()
        }
    }
}


