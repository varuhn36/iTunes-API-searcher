package com.example.itunes_api_searcher

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.itunes_api_searcher.ui.screens.HomePage
import com.example.itunes_api_searcher.ui.screens.ItemSearchResponseDetailsPage
import com.example.itunes_api_searcher.viewmodel.ItunesSearchViewModel

@Composable
fun AppNavHost(navController: NavHostController) {
    val vm: ItunesSearchViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

        composable("home") {
            HomePage(
                vm = vm,
                onNavigateToDetails = {
                    navController.navigate("details")
                }
            )
        }

        composable("details") {
            ItemSearchResponseDetailsPage(
                vm = vm,
                onBack = {
                    vm.clearSelectedItem()
                    navController.popBackStack()
                }
            )
        }
    }
}


