package com.markosopcic.cycler.dependencyinjection

import android.content.Context
import com.markosopcic.cycler.data.CyclerDatabase
import com.markosopcic.cycler.network.CustomTrust
import com.markosopcic.cycler.network.CyclerAPI
import com.markosopcic.cycler.network.interceptors.AddCookiesInterceptor
import com.markosopcic.cycler.network.interceptors.ReceivedCookiesInterceptor
import com.markosopcic.cycler.network.models.RegisterViewModel
import com.markosopcic.cycler.network.models.UserEventViewResponse
import com.markosopcic.cycler.utility.Constants
import com.markosopcic.cycler.viewmodel.*
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


val databaseModule = module {

    single {
        CyclerDatabase.getDatabase(get())
    }
}

val viewModelModule = module {
    single {
        FriendRequestsViewModel(get(), get())
    }

    single {
        EventInvitationsViewModel(get(), get())
    }

    single {
        TrackingViewModel(get(), get(), get())
    }
    single {
        FriendsViewModel(get(), get())
    }

    single {
        HomeViewModel(get(), get())
    }

    single {
        LoginViewModel(get(), get())
    }

    single{
        ProfileViewModel(get(),get())
    }

    single{
        RegisterViewModel(get(),get())
    }

    single{
        EventViewViewModel(get(),get())
    }

    single{
        NewEventViewModel(get(),get())
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