package com.example.data.app.remote.entity.user.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("token")
    val token: String
)
