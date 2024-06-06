package presentation.screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import domain.CurrencyApiService
import domain.MongoRepository
import domain.PreferencesRepository
import domain.RequestState
import domain.model.Currency
import domain.model.RateStatus
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock


sealed class HomeUiEvent {
    data object RefreshRates : HomeUiEvent()
}

class HomeViewModel(
    private val preferences: PreferencesRepository,
    private val api: CurrencyApiService,
    private val mongoDB: MongoRepository
) : ScreenModel {

    private var _rateStatus: MutableState<RateStatus> = mutableStateOf(RateStatus.Idle)
    val rateStatus: State<RateStatus> = _rateStatus

    private var _sourceCurrency: MutableState<RequestState<Currency>> = mutableStateOf(RequestState.Idle)
    val sourceCurrency: State<RequestState<Currency>> = _sourceCurrency

    private var _targetCurrency = mutableStateOf(RequestState.Idle)
    val targetCurrency: State<RequestState<Currency>> = _targetCurrency

    private var _allCurrency = mutableStateListOf<Currency>()
    val allCurrency: List<Currency> = _allCurrency

    init {
        screenModelScope.launch {
            fetchNewRates()
        }
    }

    private suspend fun fetchNewRates() {
        try {
            val localCache = mongoDB.readCurrencyData().first()
            if (localCache.isSuccess()){
                mongoDB.cleanUp()
                if (localCache.getSuccessData().isNotEmpty()){
                    _allCurrency.addAll(localCache.getSuccessData())
                    if (!preferences.isDataFresh(Clock.System.now().toEpochMilliseconds())){
                        cacheTheData()
                    } else {
                        println("Database is Fresh")
                    }
                } else {
                        println("Database need data")
                        cacheTheData()
                }
            } else if (localCache.isError()) {
                error("Error Reading local database :- ${localCache.getErrorMessage()}")
            }
            getRateStatus()
        } catch (e: Exception) {
            println(e.message.toString())
        }
    }

    private suspend fun cacheTheData(){
        val fetchedData = api.getLatestExchangeRates()
        if (fetchedData.isSuccess()){
            fetchedData.getSuccessData().forEach {
                mongoDB.insertCurrencyData(it)
            }
            _allCurrency.addAll(fetchedData.getSuccessData())
        } else if (fetchedData.isError()) {
            error("Fetching Failed")
        }
    }

    fun sendEvent(event: HomeUiEvent){
        when(event) {
            HomeUiEvent.RefreshRates -> {
                screenModelScope.launch {
                    fetchNewRates()
                }
            }
        }
    }
    private suspend fun getRateStatus() {
        _rateStatus.value = if (preferences.isDataFresh(
                currentTimestamp = Clock.System.now().toEpochMilliseconds()
            )
        ) RateStatus.Fresh
        else RateStatus.Stale
    }
}