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

        val preferences =
            context.getSharedPreferences(COOKIES_PREFERENCE_NAME, Context.MODE_PRIVATE).getStringSet(
                COOKIES_PREFERENCE_KEY,
                HashSet<String>()
            ) as HashSet<String>

        for (cookie in preferences) {
            builder.addHeader("Cookie", cookie)
        }

        return chain.proceed(builder.build())
    }

}