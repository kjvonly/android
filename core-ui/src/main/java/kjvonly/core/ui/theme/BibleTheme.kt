package kjvonly.core.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kjvonly.bible.services.Mode


private val primary = Color(0xff6200ee)
private val primaryVariant = Color(0xff3700b3)
private val secondary = Color(0xff03dac6)
private val secondaryVariant = Color(0xff018786)
private val background = Color.White
private val surface = Color.White
private val error = Color(0xffb00020)
private val onPrimary = Color.White
private val onSecondary = Color.Black
private val onBackground = Color.Black
private val onSurface = Color.Black
private val onError = Color.White



private val _primary = Color(0xffbb86fc)
private val _primaryVariant = primaryVariant
private val _secondary = secondary
private val _secondaryVariant = secondary
private val _background = Color(0xff000000)
private val _surface = Color(0xff000000)
private val _error = Color(0xffcf6679)
private val _onPrimary = Color.Black
private val _onSecondary = onSecondary
private val _onBackground = Color.White
private val _onSurface = Color.DarkGray
private val _onError = Color.Black



val lightColors = lightColors(
    primary = primary,
    primaryVariant = primaryVariant,
    secondary = secondary,
    secondaryVariant = secondaryVariant,
    background = background,
    surface = surface,
    error = error,
    onPrimary = onPrimary,
    onSecondary = onSecondary,
    onBackground = onBackground,
    onSurface = onSurface,
    onError = onError
)


val darkColors = darkColors(
    primary = _primary,
    primaryVariant = _primaryVariant,
    secondary = _secondary,
    secondaryVariant = _secondaryVariant,
    background = _background,
    surface = _surface,
    error = _error,
    onPrimary = _onPrimary,
    onSecondary = _onSecondary,
    onBackground = _onBackground,
    onSurface = _onSurface,
    onError = _onError
)

val BottomSheetShape = RoundedCornerShape(
    topStart = 20.dp,
    topEnd = 20.dp,
    bottomStart = 0.dp,
    bottomEnd = 0.dp
)

@Composable
fun BibleTheme(viewModel: BibleThemeViewModel = hiltViewModel(), content: @Composable () -> Unit) {
    
    if (viewModel.mode.value == Mode.DARK){
        MaterialTheme(colors = darkColors, typography = KJVonlyTypography) {
            content()
        }
    } else if (viewModel.mode.value == Mode.LIGHT){
        MaterialTheme(colors = lightColors, typography = KJVonlyTypography) {
            content()
        }

    }

}

@Composable
fun DarkBibleTheme(content: @Composable () -> Unit){
    MaterialTheme(colors = darkColors, typography = KJVonlyTypography) {
        content()
    }
}
@Composable
fun LightBibleTheme(content: @Composable () -> Unit){
    MaterialTheme(colors = lightColors, typography = KJVonlyTypography) {
        content()
    }
}


