package domain

import domain.model.Currency
import kotlinx.coroutines.flow.Flow

interface MongoRepository {

    fun configureTheRealm()

    suspend fun insertCurrencyData(currency: Currency)

    fun readCurrencyData(): Flow<RequestState<List<Currency>>>

    suspend fun cleanUp()

}