package be.howest.ti.taskifox.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import be.howest.ti.taskifox.R
import be.howest.ti.taskifox.TaskiFoxScreen

@Composable
fun BottomNavigation(
    currentScreen: TaskiFoxScreen,
    navController: NavHostController
) {
    NavigationBar {
        NavigationBarItem(
            selected = currentScreen == TaskiFoxScreen.Settings,
            onClick = { navController.navigateIfNotAlreadyThere(route = TaskiFoxScreen.Settings.name) },
            icon = { Icon(
                Icons.Filled.Settings,
                stringResource(TaskiFoxScreen.Settings.title)
            )},
            label = { stringResource(R.string.screen_settings) }
        )
        NavigationBarItem(
            selected = currentScreen == TaskiFoxScreen.Todos,
            onClick = { navController.navigateIfNotAlreadyThere(route = TaskiFoxScreen.Todos.name) },
            icon = { Icon(
                Icons.AutoMirrored.Filled.List,
                stringResource(TaskiFoxScreen.Todos.title)
            )},
            label = { stringResource(R.string.screen_todos) }
        )
        NavigationBarItem(
            selected = currentScreen == TaskiFoxScreen.Projects,
            onClick = { navController.navigateIfNotAlreadyThere(route = TaskiFoxScreen.Projects.name) },
            icon = { Icon(
                Icons.Filled.Folder,
                stringResource(TaskiFoxScreen.Projects.title)
            )},
            label = { stringResource(R.string.screen_todos) }
        )
    }
}

private fun NavHostController.navigateIfNotAlreadyThere(route: String) {
    val currentRoute = this.currentBackStackEntry?.destination?.route

    if (currentRoute != route) {
        this.navigate(route)
    }
}
