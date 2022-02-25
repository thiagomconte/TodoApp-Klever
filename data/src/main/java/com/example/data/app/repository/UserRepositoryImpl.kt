package com.example.data.app.repository

import com.example.data.app.remote.ApiService
import com.example.domain.app.boundary.UserRepository
import com.example.domain.app.entity.User
import com.example.domain.app.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    apiService: ApiService
) : UserRepository {
    override suspend fun register(user: User): Flow<Resource<String>> {
        TODO("Not yet implemented")
    }

    override suspend fun login(
        email: String,
        password: String
    ): Flow<Resource<User>> {
        TODO("Not yet implemented")
    }
}
