package be.howest.ti.taskifox

import androidx.annotation.StringRes
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import be.howest.ti.taskifox.ui.components.BottomNavigation
import be.howest.ti.taskifox.ui.components.TaskiFoxTopAppBar
import be.howest.ti.taskifox.ui.navigation.TaskiFoxNavHost

enum class TaskiFoxScreen(@StringRes val title: Int) {
    Settings(title = R.string.screen_settings),
    Todos(title = R.string.screen_todos),
    CreateTodo(title = R.string.screen_todo_create),
    EditTodo(title = R.string.screen_todo_edit),
    Projects(title = R.string.screen_projects),
    ProjectDetail(title = R.string.screen_project_detail),
    CreateProject(title = R.string.screen_project_create),
    EditProject(title = R.string.screen_project_edit)
}

@Composable
fun TaskiFoxApp(
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentScreen = TaskiFoxScreen.valueOf(backStackEntry?.destination?.route
        ?.split("/")?.get(0)
        ?: TaskiFoxScreen.Todos.name
    )

    Scaffold(
        topBar = {
            TaskiFoxTopAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.popBackStack() }
            )
        },
        bottomBar = {
            BottomNavigation(
                currentScreen = currentScreen,
                navController = navController
            )
        }
    ) { innerPadding ->
        TaskiFoxNavHost(navController, innerPadding)
    }
}