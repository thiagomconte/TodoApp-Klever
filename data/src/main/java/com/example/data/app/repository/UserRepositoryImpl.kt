package com.example.data.app.repository

import com.example.data.app.remote.ApiService
import com.example.data.app.remote.entity.user.request.RegisterUserRequest
import com.example.domain.app.boundary.UserRepository
import com.example.domain.app.entity.User
import com.example.domain.app.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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
//            try {
                val response = apiService.register(RegisterUserRequest(name, email, password))
                emit(Resource.Success(response.msg))
//            } catch (e: Exception) {
//                emit(Resource.Error("Could not complete operation."))
//            }
        }
    }

    override suspend fun login(
        email: String,
        password: String
    ): Flow<Resource<User>> {
        return flow {
            try {
                val response = apiService.login(email, password)
                with(response) {
                    emit(Resource.Success(User(name, this.email, token)))
                }
            } catch (e: Exception) {
                emit(Resource.Error("Incorrect credentials."))
            }
        }
    }
}
