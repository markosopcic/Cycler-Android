package com.markosopcic.cycler.network

import com.markosopcic.cycler.network.forms.LocationModel
import com.markosopcic.cycler.network.forms.LoginForm
import com.markosopcic.cycler.network.models.EventInvitationResponse
import com.markosopcic.cycler.network.models.FriendRequestResponse
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

    @GET("/mobile/friend-requests")
    fun getFriendRequests() : Call<List<FriendRequestResponse>>

    @GET("mobile/accept-friend-request")
    fun acceptFriendRequest(@Query("fromUser") fromUserId:String,@Query("accept") accept:Boolean) : Call<Void>

    @GET("/mobile/invitations")
    fun getEventInvitations() : Call<List<EventInvitationResponse>>

    @GET("/mobile/accept-invitation")
    fun acceptInvitation(@Query("InvitationId") invitationId : String, @Query("accept") accept: Boolean) : Call<Void>

    @POST("/mobile/send-location")
    fun sendLocation(@Body model : LocationModel) : Call<Void>
}