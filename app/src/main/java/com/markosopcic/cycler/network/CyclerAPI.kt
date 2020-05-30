package com.markosopcic.cycler.network

import com.markosopcic.cycler.network.forms.EventModel
import com.markosopcic.cycler.network.forms.LocationModel
import com.markosopcic.cycler.network.forms.LoginForm
import com.markosopcic.cycler.network.forms.RegisterForm
import com.markosopcic.cycler.network.models.*
import retrofit2.Call
import retrofit2.http.*

interface CyclerAPI {

    @Headers("Content-Type: application/json", "Accept: */*")
    @POST("/mobile/login")
    fun login(@Body loginForm: LoginForm): Call<LoginResponse>

    @GET("/api/search-users?")
    fun searchUsers(@Query("term") term: String): Call<List<UserSearchResult>>

    @GET("/mobile/logout")
    fun logout(): Call<Void>

    @GET("/mobile/friend-requests")
    fun getFriendRequests(): Call<List<FriendRequestResponse>>

    @GET("mobile/accept-friend-request")
    fun acceptFriendRequest(
        @Query("fromUser") fromUserId: String,
        @Query("accept") accept: Boolean
    ): Call<Void>

    @GET("/mobile/invitations")
    fun getEventInvitations(): Call<List<EventInvitationResponse>>

    @GET("/mobile/accept-invitation")
    fun acceptInvitation(
        @Query("InvitationId") invitationId: String,
        @Query("accept") accept: Boolean
    ): Call<Void>

    @POST("/mobile/send-location")
    fun sendLocation(@Body model: LocationModel): Call<Void>

    @POST("/mobile/register")
    fun register(@Body model: RegisterForm): Call<Void>

    @GET("/mobile/get-active-events")
    fun getActiveEvents(): Call<List<EventResponse>>

    @GET("/mobile/profile/{userId}")
    fun getUserProfile(@Path("userId") userId: String): Call<UserDetails>

    @GET("/mobile/SendFriendRequest/{friendId}")
    fun sendFriendRequest(@Path("friendId") id: String): Call<Void>

    @POST("/mobile/upload-event")
    fun uploadEventLocations(@Body model: EventModel): Call<Void>

    @GET("/mobile/get-event-feed")
    fun getEventFeed(
        @Query("skip") skip: Int,
        @Query("take") take: Int
    ): Call<List<EventFeedResponse>>

    @GET("/mobile/user-profile")
    fun getUserProfile() : Call<UserProfileResponse>

}