package com.example.todoapp.navgraph

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.todoapp.util.Constants
import com.example.todoapp.util.Constants.Routes.ADD_EDIT_TODO
import com.example.todoapp.util.Constants.Routes.AUTHENTICATED
import com.example.todoapp.util.Constants.Routes.LOGIN
import com.example.todoapp.util.Constants.Routes.TODO_LIST
import com.example.todoapp.view.addedittodo.components.AddEditTodoScreen
import com.example.todoapp.view.login.components.LoginScreen
import com.example.todoapp.view.register.components.RegisterScreen
import com.example.todoapp.view.todolist.components.TodoListScreen

@ExperimentalAnimationApi
@Composable
fun NavGraph(token: String) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = LOGIN) {
        composable(LOGIN) {
            if (token.isBlank()) {
                LoginScreen(onNavigate = {
                    navController.navigate(it.route) {
                        if (it.route == AUTHENTICATED) {
                            popUpTo(0)
                        }
                    }
                })
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate(AUTHENTICATED) {
                        popUpTo(0)
                    }
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
            composable(
                "$ADD_EDIT_TODO?id={id}",
                arguments = listOf(
                    navArgument("id") {
                        type = NavType.StringType
                        defaultValue = ""
                    }
                )
            ) {
                AddEditTodoScreen(onPopBackStack = { navController.popBackStack() })
            }
        }
    }
}
