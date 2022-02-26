package com.example.todoapp.navgraph

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todoapp.util.Constants.Routes.LOGIN
import com.example.todoapp.util.Constants.Routes.REGISTER
import com.example.todoapp.view.login.components.LoginScreen
import com.example.todoapp.view.register.components.RegisterScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = LOGIN) {
        composable(LOGIN) {
            LoginScreen(onNavigate = {
                navController.navigate(it.route)
            })
        }
        composable(REGISTER) {
            RegisterScreen(onPopBackStack = {
                navController.popBackStack()
            })
        }
    }
}