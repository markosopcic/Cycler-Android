package com.markosopcic.cycler.network.forms

data class RegisterForm(
    val FirstName: String,
    val LastName: String,
    val Password: String,
    val ConfirmPassword: String,
    val Email: String
)