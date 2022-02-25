package com.example.todoapp.view.login

sealed class LoginEvent {
    data class LoginUser(val email: String, val password: String) : LoginEvent()
}
