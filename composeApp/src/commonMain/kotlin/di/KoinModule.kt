package di

import com.russhwolf.settings.Settings
import data.local.MongoRepositoryImpl
import data.local.PreferencesRepositoryImpl
import data.remote.api.CurrencyApiServiceImpl
import domain.CurrencyApiService
import domain.MongoRepository
import domain.PreferencesRepository
import org.koin.core.context.startKoin
import org.koin.dsl.module
import presentation.screen.HomeViewModel

val appModule = module {
    single { Settings() }
    single <PreferencesRepository>{ PreferencesRepositoryImpl(get()) }
    single <CurrencyApiService>{ CurrencyApiServiceImpl(get()) }
    single <MongoRepository>{ MongoRepositoryImpl() }
    factory {
        HomeViewModel(
            preferences = get(),
            api = get(),
            mongoDB = get())
    }
}

fun initializeKoin(){
    startKoin {
        modules(appModule)
    }
}