package com.markosopcic.cycler

import android.app.Application
import com.markosopcic.cycler.dependencyinjection.databaseModule
import com.markosopcic.cycler.dependencyinjection.networkModule
import com.markosopcic.cycler.dependencyinjection.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class CyclerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@CyclerApplication)
            modules(listOf(networkModule, viewModelModule, databaseModule))
        }
    }
}