package com.example.data.app.repository

import com.example.data.app.remote.ApiService
import com.example.data.app.remote.entity.todo.ApiTodo
import com.example.data.app.remote.entity.todo.toTodo
import com.example.domain.app.boundary.TodoRepository
import com.example.domain.app.entity.Todo
import com.example.domain.app.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
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
                            it.toTodo(it)
                        }
                    )
                )
            } catch (e: Exception) {
                if (e is IOException) emit(Resource.Error("Could not reach server."))
                else emit(Resource.Error("Could not get tasks."))
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
                if (e is IOException) emit(Resource.Error("Could not reach server."))
                else emit(Resource.Error("Could not create task."))
            }
        }
    }

    override suspend fun getTodoById(id: String): Flow<Resource<Todo>> {
        return flow {
            try {
                val response = apiService.getTodoById(id)
                emit(
                    Resource.Success(response.toTodo(response))
                )
            } catch (e: Exception) {
                if (e is IOException) emit(Resource.Error("Could not reach server."))
                else emit(Resource.Error("Could not get task."))
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
                if (e is IOException) emit(Resource.Error("Could not reach server."))
                else emit(Resource.Error("Could not update task."))
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
                if (e is IOException) emit(Resource.Error("Could not reach server."))
                else emit(Resource.Error("Could not delete task."))
            }
        }
    }
}
