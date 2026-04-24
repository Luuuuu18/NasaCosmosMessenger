package com.example.nasacosmosmessenger.navigation

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.size
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.nasacosmosmessenger.R
import androidx.compose.ui.Modifier

@Composable
fun BottomNavBar(navController: NavController) {

    val items = listOf(
        BottomNavItem("chat", "Nova", R.drawable.ic_nova, R.drawable.ic_nova),
        BottomNavItem("favorites", "收藏", R.drawable.ic_fav_off, R.drawable.ic_fav_on)
    )

    val currentRoute = currentRoute(navController)

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        items.forEach { item ->

            NavigationBarItem(
                selected = currentRoute == item.route,

                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },

                icon = {
                    val iconRes =
                        if (currentRoute == item.route)
                            item.selectedIcon
                        else
                            item.icon

                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = item.label,
                        modifier = Modifier.size(24.dp),
                        tint = Color.Unspecified
                    )
                },

                label = {
                    Text(item.label)
                }
            )
        }
    }
}

data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: Int,
    val selectedIcon: Int
)

@Composable
fun currentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}