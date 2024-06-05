package di

import com.russhwolf.settings.Settings
import data.local.PreferencesRepositoryImpl
import data.remote.api.CurrencyApiServiceImpl
import domain.CurrencyApiService
import domain.PreferencesRepository
import org.koin.core.context.startKoin
import org.koin.dsl.module
import presentation.screen.HomeViewModel

val appModule = module {
    single { Settings() }
    single <PreferencesRepository>{ PreferencesRepositoryImpl(get()) }
    single <CurrencyApiService>{ CurrencyApiServiceImpl(get()) }
    factory {
        HomeViewModel(
            preferences = get(),
            api = get())
    }
}

fun initializeKoin(){
    startKoin {
        modules(appModule)
    }
}