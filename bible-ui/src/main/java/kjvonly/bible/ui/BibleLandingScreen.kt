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

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val SplashWaitTime: Long = 1000

@Composable
fun BibleLandingScreen(modifier: Modifier = Modifier, onTimeout: () -> Unit, viewModel: BibleLandingScreenViewModel = hiltViewModel()) {
    var showText by remember { mutableStateOf(false) }
    Box(
        modifier = modifier
            .fillMaxSize()
            .onGloballyPositioned { coordinates ->
                val h = coordinates.size.height
                val w = coordinates.size.width
                showText = h > w
            }, contentAlignment = Alignment.Center
    ) {
        // Adds composition consistency. Use the value when LaunchedEffect is first called
        val currentOnTimeout by rememberUpdatedState(onTimeout)
        val composableScope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            var resolving = true
            composableScope.launch(Dispatchers.IO) {
                viewModel.init()
                resolving = false
            }

            while (resolving) {
                delay(100)
            }
            currentOnTimeout()
        }

        Column(Modifier) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painterResource(id = kjvonly.feature.ui.layout.R.drawable.ic_complimentary_github_cross_primary_secondary_colors),
                    contentDescription = null,
                    modifier = Modifier.size(300.dp)
                )
            }
            if (showText) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text(
                        fontSize = 30.sp,
                        text = "The Holy Bible",
                        color = MaterialTheme.colors.onSurface,
                        textAlign = TextAlign.Center,
                    )

                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {

                    Text(
                        fontSize = 30.sp,
                        text = "King James Version",
                        color = MaterialTheme.colors.onSurface,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}
