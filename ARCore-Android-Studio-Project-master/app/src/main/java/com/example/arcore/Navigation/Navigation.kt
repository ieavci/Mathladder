package com.example.arcore.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.arcore.ui.*

sealed class Screen(val route: String) {
    object Start : Screen("start_screen")
    object Payment : Screen("payment_screen")
    object Game : Screen("game_screen")
    object Login : Screen("login_screen")
    object Register : Screen("register_screen")
    object PaymentDetails : Screen("payment_details_screen")
    object PaymentSuccess : Screen("payment_success_screen")

}

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Start.route
    ) {
        composable(Screen.Start.route) {
            GameStartScreen(onStartGame = {
                navController.navigate(Screen.Game.route)
            })
        }

        composable(Screen.Game.route) {
            GameScreen(
                onPaymentRequired = {
                    navController.navigate(Screen.Payment.route)
                },
                onPlayAgain = {
                    navController.navigate(Screen.Game.route)
                }
            )
        }

        composable(Screen.Payment.route) {
            PaymentForm(onPaymentSuccess = {
                navController.navigate(Screen.PaymentSuccess.route) // Payment success screen
            })
        }

        composable(Screen.PaymentSuccess.route) {
            PaymentSuccessScreen(onBackToGame = {
                navController.navigate(Screen.Game.route) // Return to the game
            })
        }
    }
}

