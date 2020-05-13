package com.markosopcic.cycler.dependencyinjection

import android.content.Context
import com.markosopcic.cycler.network.CustomTrust
import com.markosopcic.cycler.network.CyclerAPI
import com.markosopcic.cycler.network.interceptors.AddCookiesInterceptor
import com.markosopcic.cycler.network.interceptors.ReceivedCookiesInterceptor
import com.markosopcic.cycler.utility.Constants
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {

    single {
        AddCookiesInterceptor(androidContext())
    }

    single {
        ReceivedCookiesInterceptor(androidContext())
    }

    single {
        provideRetrofitInstance(get())
    }
    single {
        provideRetrofitInstance(get()).create(CyclerAPI::class.java)
    }

}


fun provideRetrofitInstance(context: Context): Retrofit {
    return Retrofit.Builder().client(
        CustomTrust.GetCustomTrustClient().addInterceptor(AddCookiesInterceptor(context))
            .addInterceptor(ReceivedCookiesInterceptor(context)).build()
    ).baseUrl(
        Constants.ROOT_URL
    ).addConverterFactory(
        GsonConverterFactory.create()
    ).build()
}