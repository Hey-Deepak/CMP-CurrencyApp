package domain

import domain.model.Currency

interface CurrencyApiService {

    fun getLatestExchangeRates(): RequestState<List<Currency>>

}