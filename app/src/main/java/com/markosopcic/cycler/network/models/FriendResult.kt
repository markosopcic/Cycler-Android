package com.markosopcic.cycler.network.models

 class FriendResult(
    val fullName: String,
    val id: String,
    val resultType: String
){
     override fun toString(): String {
         return fullName
     }
 }

