import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import kjvonly.core.services.NavigationService
import kjvonly.memory.ui.*
import kjvonly.memory.ui.Display
import kjvonly.memory.ui.MainScreen
import kjvonly.memory.ui.MemoryGroupCreateViewWithName
import kjvonly.memory.ui.MemoryPlanCreateView
import kjvonly.memory.ui.MemoryPlansView
import kjvonly.memory.ui.MemoryViewModel
import kjvonly.memory.ui.NodeMode


@Composable
fun MemoryNavigation(
    windowWidth: WindowWidthSizeClass,
    navigationService: NavigationService
) {
    val navController = rememberNavController()
    navigationService.add("memory", navController)

    NavHost(navController = navController, startDestination = Routes.Dashboard.route) {
        composable(Routes.Dashboard.route) {
            val parentEntry = remember(it) {
                navController.getBackStackEntry(Routes.Dashboard.route)
            }

            val memoryViewModel = hiltViewModel<MemoryViewModel>()

            MainScreen(
                memoryViewModel = memoryViewModel,
                widthSize = windowWidth,
            )
        }
        composable(Routes.MemoryPlanCreateView.route) {
            Display()
        }

        composable(Routes.MemoryPlanCreateView.route + "/{name}") {
            MemoryGroupCreateViewWithName("plan name")
        }

        composable(Routes.MemoryPlanEditView.route + "/{id}") { navBackStack ->
            var planId = navBackStack.arguments?.getString("id")
            if (planId == null) {
                val context = LocalContext.current
                Toast.makeText(
                    context,
                    "No Id Passed to MemoryPlanEdit view. Must Pass ID.",
                    Toast.LENGTH_SHORT
                ).show()
                navigationService.navigate("memory", "memoryPlansEdit")
                planId = ""
            }
            MemoryPlanCreateView(NodeMode.Edit, planId)
        }
        composable(Routes.MemoryPlansView.route) {
            MemoryPlansView()
        }
        composable(Routes.MemoryPlanView.route + "/{id}") { navBackStack ->
            var planId = navBackStack.arguments?.getString("id")
            if (planId == null) {
                val context = LocalContext.current

                Toast.makeText(
                    context,
                    "No Id Passed to MemoryPlan view. Must Pass ID.",
                    Toast.LENGTH_SHORT
                ).show()
                planId = ""
                navigationService.navigate("memory", "memoryPlansView")
            }
            //  MemoryPlanView(planId = planId.toString())
        }

    }
}

sealed class Routes(val route: String) {
    object Dashboard : Routes("dashboard")
    object MemoryPlanCreateView : Routes("memoryPlanCreateView")
    object MemoryPlanEditView : Routes("memoryPlanEditView")
    object MemoryPlansView : Routes("memoryPlansView")
    object MemoryPlanView : Routes("memoryPlanView")
}
