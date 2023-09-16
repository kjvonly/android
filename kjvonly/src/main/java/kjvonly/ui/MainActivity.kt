package kjvonly.ui

import MemoryNavigation
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.remember
import kjvonly.feature.ui.calendar.CalendarScreen
import kjvonly.core.ui.theme.BibleTheme
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kjvonly.bible.ui.*
import kjvonly.core.services.NavigationService
import kjvonly.feature.ui.calendar.CalendarViewModel


import dagger.hilt.android.AndroidEntryPoint
import kjvonly.bible.ui.BibleViewModel
import kjvonly.bible.ui.MainScreen
import javax.inject.Inject

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@AndroidEntryPoint
class MainActivity(
) : ComponentActivity() {
    @Inject
    lateinit var navigationService: NavigationService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            BibleTheme {
                val widthSizeClass = calculateWindowSizeClass(this).widthSizeClass

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.background)
                ) {

                    val navController = rememberNavController()
                    navigationService.add("MainActivity", navController)

                    NavHost(navController = navController, startDestination = Routes.Bible.route) {
                        composable(Routes.Bible.route) {
                            val bibleViewModel = hiltViewModel<BibleViewModel>()
                            MainScreen(
                                widthSize = widthSizeClass,
                                onDateSelectionClicked = {
                                    navController.navigate(Routes.Calendar.route)
                                },
                                bibleViewModel = bibleViewModel
                            )
                        }
                        composable(Routes.Calendar.route) {
                            val calendarViewModel = hiltViewModel<CalendarViewModel>()
                            val parentEntry = remember(it) {
                                navController.getBackStackEntry(Routes.Bible.route)
                            }
                            CalendarScreen(onBackPressed = {
                                navController.popBackStack()
                            }, calendarViewModel = calendarViewModel)
                        }

                        composable(Routes.Memory.route) {

                            MemoryNavigation(
                                windowWidth = widthSizeClass,
                                navigationService = navigationService
                            )

                        }
                    }
                }
            }
        }
    }
}

sealed class Routes(val route: String) {
    object Bible : Routes("bible")
    object Calendar : Routes("calendar")
    object Memory : Routes("memory")
}

