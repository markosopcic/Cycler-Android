package com.markosopcic.cycler.network.interceptors

import android.content.Context
import com.markosopcic.cycler.utility.Constants.Companion.PREFERENCE_KEY
import com.markosopcic.cycler.utility.Constants.Companion.PREFERENCE_NAME
import okhttp3.Interceptor
import okhttp3.Response
import java.util.*

class ReceivedCookiesInterceptor(val context: Context) : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())
        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            val cookies =
                context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).getStringSet(
                    PREFERENCE_KEY,
                    HashSet()
                ) as HashSet<String>?

            for (header in originalResponse.headers("Set-Cookie")) {
                cookies!!.add(header)
            }

            val memes =
                context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit()
            memes.putStringSet("PREF_COOKIES", cookies).apply()
            memes.commit()
        }
        return originalResponse
    }
}