package presentation.screen

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import data.remote.api.CurrencyApiServiceImpl
import kotlinx.coroutines.launch

class HomeScreen: Screen {
    @Composable
    override fun Content() {
        val scope = rememberCoroutineScope()

        Button(
            onClick = {
                scope.launch {
                    CurrencyApiServiceImpl().getLatestExchangeRates()
                }
            }
        ){
            Text("Click me")
        }
    }


}