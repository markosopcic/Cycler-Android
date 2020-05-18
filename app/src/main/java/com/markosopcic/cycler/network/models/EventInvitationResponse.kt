package com.markosopcic.cycler.network.models

data class InvitedBy(
    val firstName: String,
    val fullName: String,
    val lastName: String
)

data class EventInvitationResponse(
    val accepted: Boolean,
    val canInvite: Boolean,
    val eventDescription: String,
    val eventId: String,
    val eventName: String,
    val eventStartTime: String,
    val invitationId: String,
    val invitationTime: String,
    val invitedBy: InvitedBy
)