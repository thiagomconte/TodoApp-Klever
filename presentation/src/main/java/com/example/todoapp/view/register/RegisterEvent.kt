package com.example.todoapp.view.register

sealed class RegisterEvent {
    data class RegisterUser(
        val name: String,
        val email: String,
        val password: String
    ) : RegisterEvent()
}
