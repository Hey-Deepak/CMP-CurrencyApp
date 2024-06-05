package data.remote.api

import domain.CurrencyApiService
import domain.PreferencesRepository
import domain.RequestState
import domain.model.ApiResponse
import domain.model.Currency
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class CurrencyApiServiceImpl(
    private val preferencesRepository: PreferencesRepository
): CurrencyApiService {

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
                append("apikey", API_KEY)
            }
        }
    }

    override suspend fun getLatestExchangeRates(): RequestState<List<Currency>> {
        return try {
            val response = httpClient.get(ENDPOINT)
            if (response.status.value == 200){
                println("Api Response :- ${response.body<String>()}")
                val apiResponse = Json.decodeFromString<ApiResponse>(response.body())

                val lastUpdated = apiResponse.meta.latestUpdateAt
                preferencesRepository.saveLastUpdated(lastUpdated)

                RequestState.Success(data = apiResponse.data.values.toList())
            } else {
                RequestState.Error(message = "Https Error Code :- ${response.status}")
            }
        } catch (e: Exception){
            RequestState.Error(message = e.message.toString())
        }
    }
}