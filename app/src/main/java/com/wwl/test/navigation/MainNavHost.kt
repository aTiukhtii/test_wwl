package com.wwl.test.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.wwl.test.ui.details.GifDetailsScreen
import com.wwl.test.ui.list.GifListScreen

@Composable
fun MainNavHost(
    navHostController: NavHostController,
    innerPadding: PaddingValues
) {
    NavHost(
        navHostController,
        Routes.MainScreen.route,
        Modifier.padding(innerPadding)
    ) {
        composable(Routes.MainScreen.route) {
            GifListScreen(hiltViewModel(it)) { id ->
                navHostController.navigate(Routes.GifDetailsScreen.createRoute(id))
            }
        }
        composable(Routes.GifDetailsScreen.route) {
            GifDetailsScreen(
                hiltViewModel(),
                it.arguments?.getString("id") ?: "",
            ) {
                navHostController.popBackStack()
            }
        }
    }
}