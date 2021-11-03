package com.zk.timetracker

import TimeTrackerScreen
import androidx.annotation.StringRes
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.FoodBank
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

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
    NavHost(navController, startDestination = BottomNavigationScreens.TimeTracker.route) {
        composable(BottomNavigationScreens.TimeTracker.route) {
            TimeTrackerScreen()
        }
        composable(BottomNavigationScreens.Another.route) {
            AnotherScreen()
        }
    }
}

@Composable
fun AnotherScreen() {
//    val context = LocalContext.current
//    val customView = remember { LottieAnimationView(context) }
//    // Adds view to Compose
//    AndroidView(
//        { customView },
//        modifier = Modifier.background(Color.Black)
//    ) { view ->
//        // View's been inflated - add logic here if necessary
//        with(view) {
//            setAnimation(scaryAnimation.animId)
//            playAnimation()
//            repeatMode = LottieDrawable.REVERSE
//        }
//    }
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
                icon = { Icon(screen.icon, "") },
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