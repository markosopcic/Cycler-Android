package com.markosopcic.cycler.network.interceptors

import android.content.Context
import com.markosopcic.cycler.utility.Constants.Companion.COOKIES_PREFERENCE_KEY
import com.markosopcic.cycler.utility.Constants.Companion.COOKIES_PREFERENCE_NAME
import okhttp3.Interceptor
import okhttp3.Response
import java.util.*

class ReceivedCookiesInterceptor(val context: Context) : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())
        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            var  cookie = ""
            for (header in originalResponse.headers("Set-Cookie")) {
                if(header.startsWith("Identity")){
                    cookie = header
                }
            }

            val memes =
                context.getSharedPreferences(COOKIES_PREFERENCE_NAME, Context.MODE_PRIVATE).edit()
            memes.putString(COOKIES_PREFERENCE_KEY, cookie).apply()
        }
        return originalResponse
    }
}