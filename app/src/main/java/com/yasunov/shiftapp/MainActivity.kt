package com.yasunov.shiftapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSavedStateRegistryOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yasunov.shiftapp.data.UserData
import com.yasunov.shiftapp.mainScreen.MainScreen
import com.yasunov.shiftapp.registration.RegistrationScreen
import com.yasunov.shiftapp.ui.theme.SHIFTAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SHIFTAppTheme {
                val navController = rememberNavController()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val appViewModel: AppViewModel = viewModel(
                        factory = AppViewModel.provideFactory(
                            (LocalContext.current.applicationContext as MyApplication).appRepository,
                            owner = LocalSavedStateRegistryOwner.current
                        )
                    )
                    val userData: UserData = appViewModel.userData.collectAsState().value
                    Scaffold(
                    ) { paddingValues ->
                        NavHost(
                            navController = navController,
                            modifier = Modifier.padding(paddingValues),
                            startDestination = if (userData.firstName != "")
                                Destinations.MainScreen.name
                            else Destinations.StartScreen.name
                        ) {
                            composable(Destinations.StartScreen.name) {
                                RegistrationScreen(
                                    modifier = Modifier.padding(paddingValues),
                                    onRegistrationButtonClicked = {
                                        navController.navigate(Destinations.MainScreen.name)
                                    }
                                )
                            }
                            composable(Destinations.MainScreen.name) {
                                MainScreen(
                                    modifier = Modifier.padding(paddingValues),
                                    userData
                                )
                            }

                        }
                    }
                }
            }
        }
    }
}

enum class Destinations {
    StartScreen, MainScreen
}
