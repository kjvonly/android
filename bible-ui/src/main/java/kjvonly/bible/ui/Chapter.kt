package kjvonly.bible.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kjvonly.bible.data.models.Book
import kjvonly.bible.data.models.Chapter

import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ChapterView(viewModel: ChapterViewModel = hiltViewModel()) {
    val swipeableState = rememberSwipeableState(0)
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier.swipeable(
            state = swipeableState,
            anchors = mapOf(0f to 0, 1f to 1),
            thresholds = { _, _ -> FractionalThreshold(0.3f) },
            orientation = Orientation.Horizontal,
        )
    ) {
        if (swipeableState.isAnimationRunning) {
            DisposableEffect(Unit) {
                onDispose {
                    if (swipeableState.currentValue == 1) {
                        viewModel.turnPageBackwards()
                    } else if (swipeableState.currentValue == 0) {
                        viewModel.turnPageForward()
                    }
                }
            }
        }
        Column(modifier = Modifier.verticalScroll(scrollState)) {
            Verses(
                book = viewModel.book.value,
                chapter = viewModel.chapter.value,
                fontSize = viewModel.fontSize.value,
                verseRowRenderedHeightMap = viewModel.verseRowRenderedHeightMap,
                verseToScrollTo = viewModel.book.value.verse,
                st = scrollState
            )
        }
    }
}

@Composable
fun Verses(
    book: Book,
    chapter: Chapter,
    fontSize: Int,
    verseRowRenderedHeightMap: HashMap<Int, Float>,
    verseToScrollTo: Int,
    st: ScrollState,
) {
    if (chapter.number != -1) {
        val sorted = chapter.verseMap.keys.toList().sorted()
        for (i in sorted) {
            Row(modifier = Modifier
                .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp)
                .onGloballyPositioned { coordinates ->
                    verseRowRenderedHeightMap[i] = coordinates.size.height.toFloat()
                }
            ) {
                val v = chapter.verseMap[i]
                Text(
                    "$i $v",
                    fontSize = fontSize.sp,
                    color = MaterialTheme.colors.onSurface,
                )
            }
        }

        ScrollStateComp(book, verseToScrollTo, st, verseRowRenderedHeightMap)
    }

}

@Composable
private fun ScrollStateComp(
    book: Book,
    verseToScrollTo: Int,
    st: ScrollState,
    verseRowRenderedHeightMap: HashMap<Int, Float>
) {
    val composableScope = rememberCoroutineScope()

    LaunchedEffect(book) {
        if (verseToScrollTo == 1) {
            composableScope.launch {
                st.animateScrollTo(0)
            }
        } else {
            var totalPixelSizeOfVerses = 0f
            val versesToInclude = verseToScrollTo - 1
            (1..versesToInclude).forEach {
                var v = verseRowRenderedHeightMap[it]
                if (v == null) {
                    v = 0f
                }
                totalPixelSizeOfVerses += v
            }
            if (totalPixelSizeOfVerses.toInt() != 0) {
                composableScope.launch {
                    st.animateScrollTo(totalPixelSizeOfVerses.toInt())
                }
            }
        }
    }

}

