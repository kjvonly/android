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

package kjvonly.feature.ui.layout.base

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import kjvonly.feature.ui.layout.R
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

private val screens = listOf(
    R.string.drawer_title_bible,
    // R.string.drawer_title_reading_plans,
    R.string.drawer_title_memory,
    // R.string.screen_title_my_account

)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun KJVonlyDrawer(
    viewModel: KJVonlyDrawerViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .fillMaxSize()

    ) {
        Image(
            painter = painterResource(R.drawable.ic_cross_3d),
            //   colorFilter = ColorFilter.tint(MaterialTheme.colors.primary),
            contentDescription = stringResource(id = R.string.app_icon),
            modifier = Modifier
                .padding(start = 24.dp, top = 48.dp, end = 24.dp)
                .height(150.dp)
        )
        Spacer(Modifier.height(24.dp))

        for (screenTitleResource in screens) {
            val clickedItem = stringResource(id = screenTitleResource).lowercase()
            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable {
                        viewModel.navigate("MainActivity", clickedItem)

                    },
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.height(50.dp)
                ) {

                    Text(
                        text = stringResource(id = screenTitleResource),
                        style = MaterialTheme.typography.h4,
                        modifier = Modifier
                            .padding(start = 24.dp),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colors.onSurface,
                    )

                }
            }
        }
    }
}