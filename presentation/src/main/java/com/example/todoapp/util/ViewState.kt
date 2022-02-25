package com.example.todoapp.util

sealed class ViewState<out T> {
    data class Success<out T>(val data: T) : ViewState<T>()
    object Loading : ViewState<Nothing>()
    object Empty : ViewState<Nothing>()
    object Initial : ViewState<Nothing>()
    object Error : ViewState<Nothing>()
}
