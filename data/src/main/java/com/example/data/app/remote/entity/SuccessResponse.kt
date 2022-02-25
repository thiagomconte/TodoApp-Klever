package com.example.data.app.remote.entity

import com.google.gson.annotations.SerializedName

data class SuccessResponse(
    @SerializedName("msg")
    val msg: String
)
