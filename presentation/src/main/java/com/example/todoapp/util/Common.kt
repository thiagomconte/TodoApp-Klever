package com.example.todoapp.util

import android.util.Patterns

class Common {
    companion object {
        fun isEmailValid(email: String): Boolean {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }
}
