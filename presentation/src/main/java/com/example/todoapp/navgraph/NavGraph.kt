package com.example.todoapp.navgraph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.todoapp.util.Constants
import com.example.todoapp.util.Constants.Routes.AUTHENTICATED
import com.example.todoapp.util.Constants.Routes.LOGIN
import com.example.todoapp.util.Constants.Routes.TODO_LIST
import com.example.todoapp.view.login.components.LoginScreen
import com.example.todoapp.view.register.components.RegisterScreen
import com.example.todoapp.view.todolist.components.TodoListScreen

@Composable
fun NavGraph(token: String, name: String) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = LOGIN) {
        composable(LOGIN) {
            if (token.isBlank()) {
                LoginScreen(onNavigate = {
                    navController.navigate(it.route)
                })
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate(AUTHENTICATED)
                }
            }
        }
        composable(Constants.Routes.REGISTER) {
            RegisterScreen(onPopBackStack = {
                navController.popBackStack()
            })
        }
        navigation(startDestination = TODO_LIST, AUTHENTICATED) {
            composable(TODO_LIST) {
                TodoListScreen(onNavigate = {
                    navController.navigate(it.route)
                })
            }
        }
    }
}
