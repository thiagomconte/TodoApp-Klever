package com.example.todoapp.view.todolist

sealed class TodoListEvent {
    object AddTodoClick : TodoListEvent()
    data class EditTodoClick(val id: String) : TodoListEvent()
    data class DeleteTodoClick(val id: String) : TodoListEvent()
}
