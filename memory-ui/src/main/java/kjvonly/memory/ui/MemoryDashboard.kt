package kjvonly.memory.ui

import android.content.res.Resources
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

val screenWidth = Resources.getSystem().displayMetrics.widthPixels
val screenHeight = Resources.getSystem().displayMetrics.heightPixels
val screenDensity = Resources.getSystem().displayMetrics.density

val halfWidth = (screenWidth / 2 / screenDensity).toInt()

@Composable
fun MemoryDashboard(viewModel: MemoryPlanDashboardViewModel = hiltViewModel()) {
    viewModel.updateMemoryPlans()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState()),
    ) {
        Row {

            LeftColumn(viewModel)
            RightColumn(viewModel)
        }
    }
}

@Composable
private fun RightColumn(
    viewModel: MemoryPlanDashboardViewModel,

    ) {
    val mContext = LocalContext.current
    val onClick = {
        viewModel.navigate("memoryPlansView")
        Toast.makeText(
            mContext,
            "View All Plans",
            Toast.LENGTH_SHORT
        ).show()
    }
    Column(Modifier.width(halfWidth.dp)) {
        Row {
            MemoryPlanCard(
                "memory plans".uppercase(),
                (viewModel.leaves.size).toString(),
                MaterialTheme.typography.h1,
                "view all".uppercase(),
                onClick
            )
        }
        Row{
            MemoryPlanCard(
                "To Learn Verses".uppercase(),
                (viewModel.leaves.size).toString(),
                MaterialTheme.typography.h1,
                "view all".uppercase(),
                onClick

            )
        }
        Row {
            MemoryPlanCard(
                "To Review Verses".uppercase(),
                (viewModel.leaves.size).toString(),
                MaterialTheme.typography.h1,
                "view all".uppercase(),
                onClick
            )
        }
        Row {
            MemoryPlanCard(
                "stats".uppercase(),
                (viewModel.leaves.size).toString(),
                MaterialTheme.typography.h1,
                "view all".uppercase(),
                onClick
            )
        }
        Row {
            Spacer(Modifier.height(40.dp))
        }
    }
}
@Composable
private fun LeftColumn(
    viewModel: MemoryPlanDashboardViewModel,

    ) {
    val mContext = LocalContext.current
    val onClick = {
        Toast.makeText(
            mContext,
            "View All Plans",
            Toast.LENGTH_SHORT
        ).show()
    }
    Column(Modifier.width(halfWidth.dp)) {
        Row {
            CurrentVerseCard(viewModel, onClick)
        }
        Row{
            MemoryPlanCard(
                "To Learn Verses".uppercase(),
                (viewModel.leaves.size).toString(),
                MaterialTheme.typography.h1,
                "view all".uppercase(),
                onClick

            )
        }
        Row {
            MemoryPlanCard(
                "To Review Verses".uppercase(),
                (viewModel.leaves.size).toString(),
                MaterialTheme.typography.h1,
                "view all".uppercase(),
                onClick
            )
        }
        Row {
            MemoryPlanCard(
                "stats".uppercase(),
                (viewModel.leaves.size).toString(),
                MaterialTheme.typography.h1,
                "view all".uppercase(),
                onClick
            )
        }
        Row {
            Spacer(Modifier.height(40.dp))
        }
    }
}

@Composable
private fun CurrentVerseCard(
    viewModel: MemoryPlanDashboardViewModel,
    onClick: () -> Unit
) {
    val verseText = viewModel.currentVerse.value.text
    MemoryPlanCard(
        "current verse".uppercase(),
        verseText,
        MaterialTheme.typography.body1,
        "study".uppercase(),
        onClick
    )
}


@Composable
fun MemoryPlanCard(
    headerText: String,
    body: String,
    style: TextStyle,
    buttonText: String,
    onClick: () -> Unit

) {
    Surface() {
        Card(
            modifier = Modifier
                .padding(20.dp)
        ) {
            Column() {

                Row(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(headerText, color = MaterialTheme.colors.onBackground)
                }
                Spacer(Modifier.height(10.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(10.dp).fillMaxWidth()
                ) {
                    Text(text = "$body", style = style)
                }
                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        modifier = Modifier.padding(5.dp),
                        onClick = onClick,
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(
                            2.dp,
                            MaterialTheme.colors.primary
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colors.onSurface,
                            backgroundColor = MaterialTheme.colors.primary
                        ),

                        ) {
                        Text(buttonText)
                    }
                }

            }
        }
    }

}