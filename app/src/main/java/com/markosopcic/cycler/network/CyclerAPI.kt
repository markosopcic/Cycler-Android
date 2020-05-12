package com.markosopcic.cycler.network

import com.markosopcic.cycler.network.forms.LoginForm
import com.markosopcic.cycler.network.models.UserSearchResult
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface CyclerAPI {

    @Headers("Content-Type: application/json", "Accept: */*")
    @POST("/mobile/login")
    fun login(@Body loginForm: LoginForm): Call<ResponseBody>

    @GET("/api/search-users?")
    fun searchUsers(@Query("term") term: String): Call<List<UserSearchResult>>

    @GET("/mobile/logout")
    fun logout() : Call<Void>
}