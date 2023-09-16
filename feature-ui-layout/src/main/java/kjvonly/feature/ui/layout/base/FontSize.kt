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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kjvonly.feature.ui.layout.R

@Composable
fun FontSize(viewModel: FontSizeViewModel = hiltViewModel()) {
    Row {
        OutlinedButton(
            onClick = {
                viewModel.decreaseFont()
            },
            modifier = Modifier.padding(5.dp),
            shape = CircleShape,
            contentPadding = PaddingValues(5.dp),
            border = BorderStroke(2.dp, MaterialTheme.colors.onSurface),
        ) {

            Icon(
                painter = painterResource(R.drawable.ic_text_decrease),
                contentDescription = stringResource(id = R.string.text_decrease),
                tint = MaterialTheme.colors.onSurface
            )
        }
        OutlinedButton(
            onClick = {
                viewModel.increaseFont()
            },
            modifier = Modifier.padding(5.dp),
            shape = CircleShape,
            border = BorderStroke(2.dp, MaterialTheme.colors.onSurface),
            contentPadding = PaddingValues(5.dp),


            ) {

            Icon(
                painter = painterResource(R.drawable.ic_text_increase),
                contentDescription = stringResource(id = R.string.text_increase),
                tint = MaterialTheme.colors.onSurface
            )
        }
    }
}