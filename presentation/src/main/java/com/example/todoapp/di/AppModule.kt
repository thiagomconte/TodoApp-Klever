package com.example.todoapp.di

import android.app.Application
import android.content.Context
import com.example.data.app.remote.ApiService
import com.example.data.app.remote.TokenInterceptor
import com.example.data.app.repository.TodoRepositoryImpl
import com.example.data.app.repository.UserRepositoryImpl
import com.example.data.app.util.Constants.BASE_URL
import com.example.domain.app.boundary.TodoRepository
import com.example.domain.app.boundary.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkhttp(tokenInterceptor: TokenInterceptor): OkHttpClient {
        val client = OkHttpClient.Builder()
        client.addInterceptor(tokenInterceptor)
        return client.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL).client(okHttpClient).build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserRepository(apiService: ApiService): UserRepository {
        return UserRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideTodoRepository(apiService: ApiService): TodoRepository {
        return TodoRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideContextApplication(application: Application): Context {
        return application.applicationContext
    }
}
