package kjvonly.memory.ui

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch


import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

import kjvonly.feature.ui.layout.base.*
import kjvonly.feature.ui.layout.base.KJVonlyDrawer

@Composable
fun MemoryHome(
    widthSize: WindowWidthSizeClass,
    modifier: Modifier = Modifier,
    viewModel: MemoryViewModel
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(

        scaffoldState = scaffoldState,
        modifier = Modifier.statusBarsPadding(),
        drawerContent = {
            KJVonlyDrawer()
        },

        ) { contentPadding ->
        val scope = rememberCoroutineScope()
        MemoryHomeContent(
            modifier = modifier.background(MaterialTheme.colors.background).padding(contentPadding),
            widthSize = widthSize,
            openDrawer = {
                scope.launch {
                    scaffoldState.drawerState.open()
                }
            },
            viewModel = viewModel
        )
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
fun MemoryHomeContent(
    widthSize: WindowWidthSizeClass,
    openDrawer: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MemoryViewModel
) {

    var expanded by remember {
        mutableStateOf(false)
    }
    val ctx = LocalContext.current

    var tabSelected by remember { mutableStateOf(MemoryScreen.Dashboard) }

    Scaffold(
        modifier = modifier,
        scaffoldState = rememberScaffoldState(),
        topBar = {
            MemoryNavBar(openDrawer, tabSelected, onTabSelected = { tabSelected = it })
        },
        backgroundColor = MaterialTheme.colors.background,
        content = {
            it
            MemoryContent(tabSelected = tabSelected, viewModel = viewModel)
        },
        // a simple floating action button
        floatingActionButton = {
            if (tabSelected == MemoryScreen.Dashboard) {
                FloatingActionButton(
                    modifier = Modifier.padding(bottom = 25.dp),
                    // on below line we are adding on click for our fab
                    onClick = {
                        expanded = true
                    },
                    // on below line we are adding
                    // background color for our button
                    backgroundColor = MaterialTheme.colors.primary,
                    // on below line we are adding
                    // color for our content of fab.
                    contentColor = MaterialTheme.colors.onSurface,
                ) {
                    // on below line we are
                    // adding icon for button.
                    Icon(Icons.Filled.Add, "")
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                    ) {

                        DropdownMenuItem(onClick = { expanded = false
                        viewModel.navigationService.navigate("memory", "memoryPlanCreateView")
                        }) {
                            Text("Create Plan")
                        }
                    }
                }
            }
        }
//        frontLayerContent = {
//            AnimatedContent(
//                targetState = tabSelected,
//            ) { targetState ->
//                when (targetState) {
//                    MemoryScreen.Dashboard -> {
//
//                    }
//                    MemoryScreen.Chapter -> {
//
//                    }
//                    MemoryScreen.Verse -> {
//
//                    }
//                }
//            }
//        }
    )
}


private const val ANIMATED_CONTENT_ANIMATION_DURATION = 300

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun MemoryContent(
    tabSelected: MemoryScreen,
    viewModel: MemoryViewModel,
) {
    // Reading datesSelected State from here instead of passing the String from the ViewModel
    // to cause a recomposition when the dates change.
    val selectedDates = viewModel.calendarState.calendarUiState.value.selectedDatesFormatted
    AnimatedContent(
        targetState = tabSelected,
        transitionSpec = {
            fadeIn(
                animationSpec = tween(ANIMATED_CONTENT_ANIMATION_DURATION, easing = EaseIn)
            ).with(
                fadeOut(
                    animationSpec = tween(ANIMATED_CONTENT_ANIMATION_DURATION, easing = EaseOut)
                )
            ).using(
                SizeTransform(
                    sizeAnimationSpec = { _, _ ->
                        tween(ANIMATED_CONTENT_ANIMATION_DURATION, easing = EaseInOut)
                    }
                )
            )
        },
    ) { targetState ->
        when (targetState) {
            MemoryScreen.Dashboard -> MemoryDashboard()
            MemoryScreen.Chapter -> {}
            MemoryScreen.Verse -> {}
        }
    }
}


@Composable
private fun MemoryNavBar(
    openDrawer: () -> Unit,
    tabSelected: MemoryScreen,
    onTabSelected: (MemoryScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    MemoryTabs(
        modifier = modifier
            .wrapContentWidth()
            .fillMaxWidth(),
        onMenuClicked = openDrawer
    ) { tabBarModifier ->
        MemoryTabBarContent()
    }
}


@Composable
private fun MemoryTabBarContent() {
    Column(
        Modifier.padding(start = 10.dp, end = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

        }
    }
}
