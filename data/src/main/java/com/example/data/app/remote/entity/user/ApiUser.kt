package com.example.data.app.remote.entity.user

import com.google.gson.annotations.SerializedName

data class ApiUser(
    @SerializedName("_id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("token")
    val token: String,
)
