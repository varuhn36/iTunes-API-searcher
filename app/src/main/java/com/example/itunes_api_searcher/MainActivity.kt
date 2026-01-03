package com.example.itunes_api_searcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.itunes_api_searcher.ui.theme.VA_Skydio_Take_HomeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            VA_Skydio_Take_HomeTheme {
                val navController = rememberNavController()
                AppNavHost(navController = navController)
            }
        }
    }
}
