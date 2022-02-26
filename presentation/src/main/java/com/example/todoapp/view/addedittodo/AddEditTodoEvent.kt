package com.example.todoapp.view.addedittodo

import com.example.domain.app.entity.Todo

sealed class AddEditTodoEvent {
    data class AddTodoClick(val title: String, val description: String) : AddEditTodoEvent()
    data class UpdateTodoClick(val todo: Todo) : AddEditTodoEvent()
}
