package kjvonly.feature.ui.layout.base

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kjvonly.feature.ui.layout.R

@Composable
fun BookSettings(
    viewModel: BookSettingsViewModel = hiltViewModel(),
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.End,
    ) {


        Row {


            Image(
                painter = painterResource(R.drawable.ic_settings),
                colorFilter = ColorFilter.tint(MaterialTheme.colors.onSurface),
                contentDescription = null,
                modifier = Modifier
                    .size(35.dp)
                    .clickable(onClick = viewModel.onSettingsClicked)
            )
        }
    }
    Row {
        DropdownMenu(
            expanded = viewModel.expandMenu.value,
            onDismissRequest = viewModel.onDismiss,
        ) {
            Column {
                FontSize()
                Spacer(Modifier.height(10.dp))
                Spacer(
                    modifier = Modifier
                        .height(2.dp)
                        .background(MaterialTheme.colors.onSurface)
                        .fillMaxWidth()
                        .padding(bottom = 35.dp, top = 35.dp)
                )
            }

            Column {
                Spacer(Modifier.height(10.dp))
                ThemeOption()
                Spacer(Modifier.height(10.dp))
                Spacer(
                    modifier = Modifier
                        .height(2.dp)
                        .background(MaterialTheme.colors.onSurface)
                        .fillMaxWidth()
                )
            }
        }

    }

}