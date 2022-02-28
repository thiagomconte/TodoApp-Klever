package com.example.data.app.repository

import com.example.data.app.remote.ApiService
import com.example.data.app.remote.entity.todo.ApiTodo
import com.example.data.app.remote.entity.todo.toTodo
import com.example.domain.app.boundary.TodoRepository
import com.example.domain.app.entity.Todo
import com.example.domain.app.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONObject
import java.io.IOException
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : TodoRepository {
    override suspend fun getTodos(): Flow<Resource<List<Todo>>> {
        return flow {
            try {
                val response = apiService.getTodos()
                if (response.isSuccessful) {
                    emit(
                        Resource.Success(
                            response.body()!!.todos.map {
                                it.toTodo()
                            }
                        )
                    )
                } else {
                    try {
                        val jsonError = JSONObject(response.errorBody()!!.string())
                        emit(Resource.Error(jsonError.getString("msg")))
                    } catch (e: Exception) {
                        emit(Resource.Error("Could not complete operation."))
                    }
                }
            } catch (e: Exception) {
                if (e is IOException) emit(Resource.Error("Could not reach server."))
                else emit(Resource.Error("Could not complete operation."))
            }
        }
    }

    override suspend fun createTodo(title: String, description: String): Flow<Resource<String>> {
        return flow {
            try {
                val response = apiService.createTodo(title, description)
                if (response.isSuccessful) {
                    emit(
                        Resource.Success(
                            response.body()?.msg ?: "Task successfully created."
                        )
                    )
                } else {
                    try {
                        val jsonError = JSONObject(response.errorBody()!!.string())
                        emit(Resource.Error(jsonError.getString("msg")))
                    } catch (e: Exception) {
                        emit(Resource.Error("Could not complete operation."))
                    }
                }
            } catch (e: Exception) {
                if (e is IOException) emit(Resource.Error("Could not reach server."))
                else emit(Resource.Error("Could not complete operation."))
            }
        }
    }

    override suspend fun getTodoById(id: String): Flow<Resource<Todo>> {
        return flow {
            try {
                val response = apiService.getTodoById(id)
                if (response.isSuccessful) {
                    emit(
                        Resource.Success(response.body()!!.toTodo())
                    )
                } else {
                    try {
                        val jsonError = JSONObject(response.errorBody()!!.string())
                        emit(Resource.Error(jsonError.getString("msg")))
                    } catch (e: Exception) {
                        emit(Resource.Error("Could not complete operation."))
                    }
                }
            } catch (e: Exception) {
                if (e is IOException) emit(Resource.Error("Could not reach server."))
                else emit(Resource.Error("Could not complete operation."))
            }
        }
    }

    override suspend fun updateTodo(todo: Todo): Flow<Resource<String>> {
        return flow {
            try {
                with(todo) {
                    val response = apiService.updateTodo(ApiTodo(id, title, description, completed))
                    if (response.isSuccessful) {
                        emit(
                            Resource.Success(response.body()?.msg ?: "Task successfully updated.")
                        )
                    } else {
                        try {
                            val jsonError = JSONObject(response.errorBody()!!.string())
                            emit(Resource.Error(jsonError.getString("msg")))
                        } catch (e: Exception) {
                            emit(Resource.Error("Could not complete operation."))
                        }
                    }
                }
            } catch (e: Exception) {
                if (e is IOException) emit(Resource.Error("Could not reach server."))
                else emit(Resource.Error("Could not complete operation."))
            }
        }
    }

    override suspend fun deleteTodo(id: String): Flow<Resource<String>> {
        return flow {
            try {
                val response = apiService.deleteTodo(id)
                if (response.isSuccessful) {
                    emit(
                        Resource.Success(response.body()?.msg ?: "Task successfully deleted.")
                    )
                } else {
                    try {
                        val jsonError = JSONObject(response.errorBody()!!.string())
                        emit(Resource.Error(jsonError.getString("msg")))
                    } catch (e: Exception) {
                        emit(Resource.Error("Could not complete operation."))
                    }
                }
            } catch (e: Exception) {
                if (e is IOException) emit(Resource.Error("Could not reach server."))
                else emit(Resource.Error("Could not complete operation."))
            }
        }
    }
}
