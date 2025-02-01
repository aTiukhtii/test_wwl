package com.wwl.test.navigation

sealed class Routes(val route: String) {
    data object MainScreen : Routes("main_screen")
    data object GifDetailsScreen : Routes("gif_details_screen/{id}") {
        fun createRoute(id: String) = "gif_details_screen/$id"
    }
}