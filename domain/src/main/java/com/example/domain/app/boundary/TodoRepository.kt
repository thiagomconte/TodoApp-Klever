package com.example.domain.app.boundary

import com.example.domain.app.entity.Todo
import com.example.domain.app.util.Resource
import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    suspend fun getTodos(): Flow<Resource<List<Todo>>>
    suspend fun createTodo(todo: Todo): Flow<Resource<String>>
    suspend fun getTodoById(id: String): Flow<Resource<Todo>>
    suspend fun updateTodo(todo: Todo): Flow<Resource<String>>
    suspend fun deleteTodo(id: String): Flow<Resource<String>>
}
