package kjvonly.feature.ui.layout.base

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kjvonly.core.ui.theme.BibleTheme
import kjvonly.feature.ui.layout.R

@Composable
fun ThemeOption(viewModel: ThemeOptionViewModel = hiltViewModel()) {

    Row {
        OutlinedButton(
            onClick = {
                viewModel.darkMode()
            },
            modifier = Modifier.padding(5.dp),
            shape = CircleShape,
            border = BorderStroke(2.dp, MaterialTheme.colors.onSurface),
            contentPadding = PaddingValues(5.dp),


            ) {

            Icon(
                painter = painterResource(R.drawable.ic_dark_mode_fill),
                contentDescription = stringResource(id = R.string.dark_mode),
                tint = MaterialTheme.colors.onSurface
            )
        }
        OutlinedButton(
            onClick = {
                viewModel.lightMode()
            },
            modifier = Modifier.padding(5.dp),
            shape = CircleShape,
            contentPadding = PaddingValues(5.dp),
            border = BorderStroke(2.dp, MaterialTheme.colors.onSurface),
        ) {

            Icon(
                painter = painterResource(R.drawable.ic_light_mode_fill),
                contentDescription = stringResource(id = R.string.light_mode),
                tint = MaterialTheme.colors.onSurface
            )
        }

    }
}

@Preview
@Composable
fun NightPreview() {
    BibleTheme {
        ThemeOption()
    }

}