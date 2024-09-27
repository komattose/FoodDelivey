package com.example.myapplication6

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun SetupNavGraph(navController: NavHostController, context: Context) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(onLoginSuccess = { _ ->  // Renombrado a '_'

                navController.navigate("menu") // Descomentar para navegar a Menu
            }, context = context) // Pasar el contexto a LoginScreen
        }

        composable("menu") {
            MenuScreen() // Define y crea tu pantalla de menú
        }
    }
}
