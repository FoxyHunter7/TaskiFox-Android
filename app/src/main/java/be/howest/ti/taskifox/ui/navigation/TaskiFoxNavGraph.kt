package be.howest.ti.taskifox.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import be.howest.ti.taskifox.TaskiFoxScreen
import be.howest.ti.taskifox.ui.screens.ModifyProjectScreen
import be.howest.ti.taskifox.ui.screens.ModifyTodoScreen
import be.howest.ti.taskifox.ui.screens.ProjectDetailsScreen
import be.howest.ti.taskifox.ui.screens.ProjectsScreen
import be.howest.ti.taskifox.ui.screens.SettingsScreen
import be.howest.ti.taskifox.ui.screens.TodoScreen

@Composable
fun TaskiFoxNavHost(
    navController: NavHostController,
    innerPadding: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = TaskiFoxScreen.Todos.name,
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        composable(
            route = TaskiFoxScreen.Settings.name) {
            SettingsScreen()
        }
        composable(route = TaskiFoxScreen.Todos.name) {
            TodoScreen(
                navigateToCreateTodo = {
                    navController.navigate(TaskiFoxScreen.CreateTodo.name)
                },
                navigateToEditTodo = {
                    navController.navigate("${TaskiFoxScreen.EditTodo.name}/${it}")
                }
            )
        }
        composable(route = TaskiFoxScreen.CreateTodo.name) {
            ModifyTodoScreen(
                navigateBack = { navController.popBackStack() },
            )
        }
        composable(
            route = "${TaskiFoxScreen.EditTodo.name}/{todoId}",
            arguments = listOf(navArgument("todoId") {
                type = NavType.LongType
            })
        ) {
            ModifyTodoScreen(
                navigateBack = { navController.popBackStack() },
            )
        }
        composable(route = TaskiFoxScreen.Projects.name) {
            ProjectsScreen(
                navigateToCreateProject = {
                    navController.navigate(TaskiFoxScreen.CreateProject.name)
                },
                navigateToProjectDetail = {
                    navController.navigate("${TaskiFoxScreen.ProjectDetail.name}/${it}")
                }
            )
        }
        composable(
            route = "${TaskiFoxScreen.ProjectDetail.name}/{projectId}",
            arguments = listOf(navArgument("projectId") {
                type = NavType.LongType
            })
        ) {
            ProjectDetailsScreen(
                navigateToEditProject = {
                    navController.navigate("${TaskiFoxScreen.EditProject.name}/${it}")
                },
                navigateToEditTodo = {
                    navController.navigate("${TaskiFoxScreen.EditTodo.name}/${it}")
                },
                navigateToProjects = {
                    navController.navigate(TaskiFoxScreen.Projects.name)
                }
            )
        }
        composable(route = TaskiFoxScreen.CreateProject.name) {
            ModifyProjectScreen(
                navigateBack = { navController.popBackStack() }
            )
        }
        composable(
            route = "${TaskiFoxScreen.EditProject.name}/{projectId}",
            arguments = listOf(navArgument("projectId") {
                type = NavType.LongType
            })
        ) {
            ModifyProjectScreen(
                navigateBack = { navController.popBackStack() }
            )
        }
    }
}