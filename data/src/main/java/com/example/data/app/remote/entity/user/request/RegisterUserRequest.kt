package com.example.data.app.remote.entity.user.request

import com.google.gson.annotations.SerializedName

class RegisterUserRequest(
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
)
