package edu.bu.androiddev.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import edu.bu.androiddev.page.MainPage
import edu.bu.androiddev.page.Page

@Composable
fun navMesh(dataStore: DataStore<Preferences>) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = NavData.mainPage.navigate) {
        composable(route = NavData.mainPage.navigate) {
            BackHandler(true) {
                // Or do nothing
            }
            MainPage(navController = navController).render(dataStore)
        }
        composable(
            route = NavData.page.navigate + "/{name}",
            arguments = listOf(
                navArgument("name"){
                    type = NavType.StringType
                    defaultValue = "Sai"
                    nullable = true
                }
            )
        ) {entry->
            BackHandler(true) {
                // Or do nothing
            }
            Page(name = entry.arguments?.getString("name"), navController = navController).render(dataStore)
        }
    }
}