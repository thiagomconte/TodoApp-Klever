package com.example.data.app.remote.entity.todo.response

import com.example.data.app.remote.entity.todo.ApiTodo
import com.google.gson.annotations.SerializedName

data class GetTodosResponse(
    @SerializedName("todos")
    val todos: List<ApiTodo>
)
