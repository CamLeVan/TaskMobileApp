package com.example.taskapplication.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navType.navArgument
import androidx.navigation.navType.navType
import com.example.taskapplication.ui.screen.*

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object TaskDetail : Screen("task/{taskId}") {
        fun createRoute(taskId: Long) = "task/$taskId"
    }
    object TeamDetail : Screen("team/{teamId}") {
        fun createRoute(teamId: Long) = "team/$teamId"
    }
    object Chat : Screen("chat/{teamId}") {
        fun createRoute(teamId: Long) = "chat/$teamId"
    }
}

@Composable
fun AppNavigation(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onLoginClick = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onTaskClick = { taskId ->
                    navController.navigate(Screen.TaskDetail.createRoute(taskId))
                },
                onTeamClick = { teamId ->
                    navController.navigate(Screen.TeamDetail.createRoute(teamId))
                }
            )
        }

        composable(
            route = Screen.TaskDetail.route,
            arguments = listOf(
                navArgument("taskId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getLong("taskId") ?: return@composable
            TaskDetailScreen(
                taskId = taskId,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.TeamDetail.route,
            arguments = listOf(
                navArgument("teamId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val teamId = backStackEntry.arguments?.getLong("teamId") ?: return@composable
            TeamDetailScreen(
                teamId = teamId,
                onBackClick = { navController.popBackStack() },
                onTaskClick = { taskId ->
                    navController.navigate(Screen.TaskDetail.createRoute(taskId))
                },
                onChatClick = {
                    navController.navigate(Screen.Chat.createRoute(teamId))
                }
            )
        }

        composable(
            route = Screen.Chat.route,
            arguments = listOf(
                navArgument("teamId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val teamId = backStackEntry.arguments?.getLong("teamId") ?: return@composable
            ChatScreen(
                teamId = teamId,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
} 