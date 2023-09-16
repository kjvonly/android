/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kjvonly.bible.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kjvonly.core.ui.theme.BottomSheetShape
import kjvonly.feature.ui.layout.base.*
import kotlinx.coroutines.launch
import kjvonly.feature.ui.layout.base.BookChapterVerseNavBar
import kjvonly.feature.ui.layout.base.BookSettings
import kjvonly.feature.ui.layout.base.KJVonlyDrawer


@Composable
fun BibleHome(
    widthSize: WindowWidthSizeClass,
    modifier: Modifier = Modifier,
    viewModel: BibleViewModel
) {
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.statusBarsPadding(),
        drawerContent = {
            KJVonlyDrawer()
        }
    ) { contentPadding ->
        val scope = rememberCoroutineScope()
        BibleHomeContent(
            modifier = modifier.padding(contentPadding),
            widthSize = widthSize,
            openDrawer = {
                scope.launch {
                    scaffoldState.drawerState.open()
                }
            },
            viewModel = viewModel,

            )
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
fun BibleHomeContent(
    widthSize: WindowWidthSizeClass,
    openDrawer: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BibleViewModel
) {

    var tabSelected by remember { mutableStateOf(BibleScreen.Bible) }

    BackdropScaffold(
        modifier = modifier,
        scaffoldState = rememberBackdropScaffoldState(BackdropValue.Revealed),
        frontLayerShape = BottomSheetShape,
        backLayerBackgroundColor = MaterialTheme.colors.background,
        backLayerContentColor = MaterialTheme.colors.onBackground,
        appBar = {
            HomeTabBar(openDrawer, tabSelected, onTabSelected = { tabSelected = it })
        },
        backLayerContent = {
            BibleContent(
                widthSize,
                tabSelected,
                viewModel,
            )
        },
        frontLayerContent = {
            AnimatedContent(
                targetState = tabSelected,
                transitionSpec = {
                    val direction = if (initialState.ordinal < targetState.ordinal)
                        AnimatedContentScope.SlideDirection.Left else AnimatedContentScope
                        .SlideDirection.Right

                    slideIntoContainer(
                        towards = direction,
                        animationSpec = tween(ANIMATED_CONTENT_ANIMATION_DURATION)
                    ) with
                            slideOutOfContainer(
                                towards = direction,
                                animationSpec = tween(ANIMATED_CONTENT_ANIMATION_DURATION)
                            ) using SizeTransform(
                        clip = false,
                        sizeAnimationSpec = { _, _ ->
                            tween(ANIMATED_CONTENT_ANIMATION_DURATION, easing = EaseInOut)
                        }
                    )
                }
            ) { targetState ->
                when (targetState) {
                    BibleScreen.Bible -> {}
                    BibleScreen.Chapter -> {}
                    BibleScreen.Verse -> {}
                }
            }
        }
    )
}

@Composable
private fun HomeTabBar(
    openDrawer: () -> Unit,
    tabSelected: BibleScreen,
    onTabSelected: (BibleScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    BibleTabBar(
        modifier = modifier
            .wrapContentWidth()
            .fillMaxWidth(),
        onMenuClicked = openDrawer
    ) {
        BibleTabBarContent()
    }
}

@Composable
private fun BibleTabBarContent() {
    Column(
        Modifier.padding(start = 10.dp, end = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BookChapterVerseNavBar()
            BookSettings()
        }
    }
}

private const val ANIMATED_CONTENT_ANIMATION_DURATION = 300

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun BibleContent(
    widthSize: WindowWidthSizeClass,
    tabSelected: BibleScreen,
    viewModel: BibleViewModel,
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
            BibleScreen.Bible -> ChapterView()
            BibleScreen.Chapter -> {}
            BibleScreen.Verse -> {}
        }
    }
}
