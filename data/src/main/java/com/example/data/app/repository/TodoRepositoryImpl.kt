package com.example.data.app.repository

import com.example.data.app.remote.ApiService
import com.example.data.app.remote.entity.todo.ApiTodo
import com.example.data.app.remote.entity.todo.toTodo
import com.example.domain.app.boundary.TodoRepository
import com.example.domain.app.entity.Todo
import com.example.domain.app.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : TodoRepository {
    override suspend fun getTodos(): Flow<Resource<List<Todo>>> {
        return flow {
            try {
                val response = apiService.getTodos()
                emit(
                    Resource.Success(
                        response.todos.map {
                            it.toTodo(it.id, it.title, it.description, it.completed)
                        }
                    )
                )
            } catch (e: Exception) {
                emit(Resource.Error("Could not get todos."))
            }
        }
    }

    override suspend fun createTodo(title: String, description: String): Flow<Resource<String>> {
        return flow {
            try {
                val response = apiService.createTodo(title, description)
                emit(
                    Resource.Success(response.msg)
                )
            } catch (e: Exception) {
                emit(Resource.Error("Could not create todos."))
            }
        }
    }

    override suspend fun getTodoById(id: String): Flow<Resource<Todo>> {
        return flow {
            try {
                val response = apiService.getTodoById(id)
                with(response) {
                    emit(
                        Resource.Success(toTodo(id, title, description, completed))
                    )
                }
            } catch (e: Exception) {
                emit(Resource.Error("Could not get todo."))
            }
        }
    }

    override suspend fun updateTodo(todo: Todo): Flow<Resource<String>> {
        return flow {
            try {
                with(todo) {
                    val response = apiService.updateTodo(ApiTodo(id, title, description, completed))
                    emit(
                        Resource.Success(response.msg)
                    )
                }
            } catch (e: Exception) {
                emit(Resource.Error("Could not update todo."))
            }
        }
    }

    override suspend fun deleteTodo(id: String): Flow<Resource<String>> {
        return flow {
            try {
                val response = apiService.deleteTodo(id)
                emit(
                    Resource.Success(response.msg)
                )
            } catch (e: Exception) {
                emit(Resource.Error("Could not delete todo."))
            }
        }
    }
}
