package com.example.data.app.repository

import com.example.data.app.remote.ApiService
import com.example.data.app.remote.entity.user.request.RegisterUserRequest
import com.example.data.app.util.ErrorHandler
import com.example.domain.app.boundary.UserRepository
import com.example.domain.app.entity.User
import com.example.domain.app.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : UserRepository {

    override suspend fun register(
        name: String,
        email: String,
        password: String
    ): Flow<Resource<String>> {
        return flow {
            try {
                val response = apiService.register(RegisterUserRequest(name, email, password))
                if (response.isSuccessful) {
                    emit(
                        Resource.Success(
                            response.body()?.msg ?: ""
                        )
                    )
                } else {
                    emit(Resource.Error(ErrorHandler.handle(response.errorBody())))
                }
            } catch (e: Exception) {
                if (e is IOException) emit(Resource.Error("Could not reach server."))
                else emit(Resource.Error("Could not complete operation."))
            }
        }
    }

    override suspend fun login(
        email: String,
        password: String
    ): Flow<Resource<User>> {
        return flow {

                val response = apiService.login(email, password)
                if (response.isSuccessful) {
                    emit(
                        Resource.Success(
                            User(
                                response.body()!!.name,
                                response.body()!!.email,
                                response.body()!!.token
                            )
                        )
                    )
                } else {
                    emit(Resource.Error(ErrorHandler.handle(response.errorBody())))
                }

        }
    }
}
