package com.eyepetizer.app.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.eyepetizer.app.R
import com.eyepetizer.app.presentation.ui.screens.home.HomeScreen
import com.eyepetizer.app.presentation.ui.screens.discover.DiscoverScreen
import com.eyepetizer.app.presentation.ui.screens.profile.ProfileScreen

/**
 * 应用导航配置
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    
    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                
                bottomNavItems.forEach { item ->
                    NavigationBarItem(
                        icon = { 
                            Icon(
                                imageVector = item.icon,
                                contentDescription = stringResource(item.titleRes)
                            )
                        },
                        label = { 
                            Text(stringResource(item.titleRes))
                        },
                        selected = currentDestination?.hierarchy?.any { 
                            it.route == item.route 
                        } == true,
                        onClick = {
                            navController.navigate(item.route) {
                                // 避免重复导航到同一个目的地
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // 避免多个相同目的地的副本
                                launchSingleTop = true
                                // 重新选择之前选择的项目时恢复状态
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(navController = navController)
            }
            composable(Screen.Discover.route) {
                DiscoverScreen(navController = navController)
            }
            composable(Screen.Profile.route) {
                ProfileScreen(navController = navController)
            }
        }
    }
}

/**
 * 导航屏幕定义
 */
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Discover : Screen("discover")
    object Profile : Screen("profile")
    object VideoDetail : Screen("video_detail/{videoId}") {
        fun createRoute(videoId: String) = "video_detail/$videoId"
    }
    object Search : Screen("search")
}

/**
 * 底部导航项数据类
 */
data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val titleRes: Int
)

/**
 * 底部导航项列表
 */
val bottomNavItems = listOf(
    BottomNavItem(
        route = Screen.Home.route,
        icon = Icons.Default.Home,
        titleRes = R.string.nav_home
    ),
    BottomNavItem(
        route = Screen.Discover.route,
        icon = Icons.Default.Search,
        titleRes = R.string.nav_discover
    ),
    BottomNavItem(
        route = Screen.Profile.route,
        icon = Icons.Default.Person,
        titleRes = R.string.nav_profile
    )
)