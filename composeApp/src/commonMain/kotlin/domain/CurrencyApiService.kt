package domain

import domain.model.Currency

interface CurrencyApiService {

    suspend fun getLatestExchangeRates(): RequestState<List<Currency>>

}