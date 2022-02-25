package com.example.data.app.remote.entity

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("msg")
    val error: String
)
