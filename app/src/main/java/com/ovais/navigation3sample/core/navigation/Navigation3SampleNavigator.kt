import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.ovais.navigation3sample.core.navigation.Routes
import com.ovais.navigation3sample.features.details.presentation.DetailsScreen
import com.ovais.navigation3sample.features.home.presentation.HomeScreen

@Composable
fun Navigation3SampleNavigator() {
    val backStack = remember { mutableStateListOf<Routes>(Routes.Home) }
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = { key ->
            when (key) {
                is Routes.Home -> NavEntry(key) {
                    HomeScreen(
                        onNavigateToDetails = { post ->
                            backStack.add(Routes.PostDetails(post))
                        }
                    )
                }

                is Routes.PostDetails -> NavEntry(key) {
                    DetailsScreen(
                        post = key.post
                    )
                }
            }
        }
    )
}