package com.example.data.app.repository

import com.example.domain.app.boundary.TodoRepository
import com.example.domain.app.entity.Todo
import com.example.domain.app.util.Resource
import kotlinx.coroutines.flow.Flow

class TodoRepositoryImpl : TodoRepository {
    override suspend fun getTodos(): Flow<Resource<List<Todo>>> {
        TODO("Not yet implemented")
    }

    override suspend fun createTodo(title: String, description: String): Flow<Resource<String>> {
        TODO("Not yet implemented")
    }

    override suspend fun getTodoById(id: String): Flow<Resource<Todo>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateTodo(todo: Todo): Flow<Resource<String>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTodo(id: String): Flow<Resource<String>> {
        TODO("Not yet implemented")
    }
}
