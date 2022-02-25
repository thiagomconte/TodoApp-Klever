package com.example.domain.app.boundary

import com.example.domain.app.entity.User
import com.example.domain.app.util.Resource
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun register(user: User): Flow<Resource<String>>
    suspend fun login(email: String, password: String): Flow<Resource<User>>
}
