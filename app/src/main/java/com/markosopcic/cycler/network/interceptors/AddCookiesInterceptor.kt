package com.markosopcic.cycler.network.interceptors

import android.content.Context
import com.markosopcic.cycler.utility.Constants.Companion.COOKIES_PREFERENCE_KEY
import com.markosopcic.cycler.utility.Constants.Companion.COOKIES_PREFERENCE_NAME
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AddCookiesInterceptor(val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()

        val cookie =
            context.getSharedPreferences(COOKIES_PREFERENCE_NAME, Context.MODE_PRIVATE)
                .getString(
                    COOKIES_PREFERENCE_KEY,
                    null
                )

        if(cookie != null){
            builder.addHeader("Cookie", cookie)
        }

        return chain.proceed(builder.build())
    }

}