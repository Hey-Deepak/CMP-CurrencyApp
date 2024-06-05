package presentation.screen

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import data.remote.api.CurrencyApiServiceImpl
import kotlinx.coroutines.launch
import presentation.component.HomeHeader

class HomeScreen: Screen {
    @Composable
    override fun Content() {

        val viewModel = getScreenModel<HomeViewModel>()
        val rateStatus by viewModel.rateStatus
        val sourceCurrency by viewModel.sourceCurrency
        val targetCurrency by viewModel.targetCurrency


        HomeHeader(
            status = rateStatus,
            source = sourceCurrency,
            target = targetCurrency,
            onRatesRefresh = {
                viewModel.sendEvent(
                    HomeUiEvent.RefreshRates
                )
            },
            onSwitchClick = {

            }
        )
    }


}