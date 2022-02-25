package com.example.todoapp.util

sealed class UiEvent {
    data class Navigate(val route: String) : UiEvent()
    object PopBackStack : UiEvent()
    data class ShowAlertDialog(val msg: String) : UiEvent()
}
