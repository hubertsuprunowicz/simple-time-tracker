package com.zk.timetracker

import EventsScreen
import com.zk.timetracker.screens.TimeTrackerScreen
import androidx.annotation.StringRes
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zk.timetracker.models.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.newCoroutineContext

sealed class BottomNavigationScreens(
    val route: String,
    @StringRes val resourceId: Int,
    val icon: ImageVector
) {
    // user { today {} }
    object TimeTracker :
        BottomNavigationScreens("TimeTracker", R.string.timetracker_route, Icons.Filled.LockClock)

    //

    object Another :
        BottomNavigationScreens("Another", R.string.another_route, Icons.Filled.EditCalendar)

    // Teams
    //
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    val bottomNavigationItems = listOf(
        BottomNavigationScreens.TimeTracker,
        BottomNavigationScreens.Another,
    )

    val context = LocalContext.current
    val jsonFileString = getJsonDataFromAsset(context, "app.json")
    val gson = Gson()
    val responseType = object : TypeToken<Response>() {}.type
    val response: Response = gson.fromJson(jsonFileString, responseType)

    usersList.addAll(response.users)
    projectsList.addAll(response.projects)
    eventsList.addAll(response.events)

    Scaffold(
        bottomBar = {
            BottomNavigation(navController, bottomNavigationItems)
        },
    ) {
        MainScreenNavigationConfigurations(navController)
    }
}

@Composable
private fun MainScreenNavigationConfigurations(
    navController: NavHostController
) {
    val passedSeconds = remember { mutableStateOf(0) }
    val isPlaying = remember { mutableStateOf(false) }
    val timer = Timer(passedSeconds, isPlaying)

    NavHost(navController, startDestination = BottomNavigationScreens.TimeTracker.route) {
        composable(BottomNavigationScreens.TimeTracker.route) {
            TimeTrackerScreen(passedSeconds, isPlaying, timer)
        }
        composable(BottomNavigationScreens.Another.route) {
            EventsScreen()
        }
//        composable("events/{eventId}") { backStackEntry ->
//            run {
//                val eventId = backStackEntry.arguments?.getString("eventId") ?: throw Exception()
//                return@composable EventScreen(eventId)
//            }
//        }
    }
}

@Composable
private fun BottomNavigation(
    navController: NavHostController,
    items: List<BottomNavigationScreens>
) {
    BottomNavigation {
        val currentRoute = currentRoute(navController)
        items.forEach { screen ->
            BottomNavigationItem(
                icon = { Icon(screen.icon, "Navigation") },
                label = { Text(stringResource(id = screen.resourceId)) },
                selected = currentRoute == screen.route,
//                alwaysShowLabel = false,
                onClick = {
                    // This if check gives us a "singleTop" behavior where we do not create a
                    // second instance of the composable if we are already on that destination
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route)
                    }
                }
            )
        }
    }
}

@Composable
private fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navController.currentBackStackEntry?.destination?.route
    return navBackStackEntry?.arguments?.getString(currentRoute)
}