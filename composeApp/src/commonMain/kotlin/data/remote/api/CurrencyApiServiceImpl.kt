package data.remote.api

import domain.CurrencyApiService
import domain.RequestState
import domain.model.Currency
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class CurrencyApiServiceImpl: CurrencyApiService {

    companion object {
        const val ENDPOINT = "https://api.currencyapi.com/v3/latest"
        const val API_KEY = "cur_live_lmhxReFKsZiZs427WZT1WHWiNRXAtUI670ViOE3v"
    }

    private val httpClient = HttpClient {
        install(ContentNegotiation){
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }

        install(HttpTimeout){
            requestTimeoutMillis = 15000
        }

        install(DefaultRequest){
            headers {
                append("apiKey", API_KEY)
            }
        }
    }

    override fun getLatestExchangeRates(): RequestState<List<Currency>> {

    }
}