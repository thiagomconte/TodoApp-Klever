package com.example.data.app.util

import okhttp3.ResponseBody
import org.json.JSONObject

object ErrorHandler {

    fun handle(
        response: ResponseBody?,
    ): String {
        return try {
            val jsonError = JSONObject(response!!.string())
            jsonError.getString("msg")
        } catch (e: Exception) {
            "Could not complete operation."
        }

    }
}

