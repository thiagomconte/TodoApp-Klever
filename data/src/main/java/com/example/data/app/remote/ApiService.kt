package com.example.data.app.remote

import com.example.data.app.remote.entity.SuccessResponse
import com.example.data.app.remote.entity.todo.ApiTodo
import com.example.data.app.remote.entity.todo.response.GetTodosResponse
import com.example.data.app.remote.entity.user.ApiUser
import com.example.data.app.remote.entity.user.request.RegisterUserRequest
import com.example.data.app.remote.entity.user.response.LoginResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("users/register")
    suspend fun register(@Body user: RegisterUserRequest): SuccessResponse

    @FormUrlEncoded
    @POST("users/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("todos")
    suspend fun createTodo(
        @Field("title") title: String,
        @Field("description") description: String
    ): SuccessResponse

    @GET("todos")
    suspend fun getTodos(): GetTodosResponse

    @GET("todos/{id}")
    suspend fun getTodoById(@Path("id") id: String): ApiTodo

    @PUT("todos")
    suspend fun updateTodo(@Body todo: ApiTodo): SuccessResponse

    @DELETE("todos/{id}")
    suspend fun deleteTodo(@Path("id") id: String): SuccessResponse
}
