package com.example.data.app.remote.entity.todo

import com.example.domain.app.entity.Todo
import com.google.gson.annotations.SerializedName

data class ApiTodo(
    @SerializedName("_id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("completed")
    val completed: Boolean
)

fun ApiTodo.toTodo() = Todo(id, title, description, completed)
