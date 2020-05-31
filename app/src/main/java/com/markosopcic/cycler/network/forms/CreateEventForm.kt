package com.markosopcic.cycler.network.forms

data class CreateEventForm(val name : String, val description : String, val startTimeMillis : Long, val friendIdsToInvite : List<String>)