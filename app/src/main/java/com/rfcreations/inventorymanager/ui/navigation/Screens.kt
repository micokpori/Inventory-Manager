package com.rfcreations.inventorymanager.ui.navigation

sealed class Screens(val route: String){
    data object HomeScreen: Screens("home_screen")
    data object AddItemScreen: Screens("create_stock_screen")
    data object EditStockScreen: Screens("edit_stock_screen")
    data object MoreInfoScreen: Screens("more_info_screen")
    data object CartScreen: Screens("cart_screen")
    data object AnalyticsScreen: Screens("analytics_screen")
    data object ContactUsScreen: Screens("contact_us_screen")
    data object SoldItemsScreen: Screens("sold_items_screen")
    data object SignUpScreen: Screens("sign_up_screen")
    data object SignInScreen: Screens("sign_in_screen")
    data object ProfileScreen: Screens("profile_screen")
    data object ForgotPasswordScreen: Screens("forgot_password_screen")
}