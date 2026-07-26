package com.example.data.remote

import com.example.data.remote.dto.*
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.http.*

class AuthInterceptor(private val tokenProvider: () -> String?) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = tokenProvider()
        
        val newRequest = if (!token.isNullOrEmpty()) {
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .header("User-Agent", "VIPV2RAY-Android-Client/2.4.0")
                .header("Accept", "application/json")
                .build()
        } else {
            originalRequest.newBuilder()
                .header("User-Agent", "VIPV2RAY-Android-Client/2.4.0")
                .header("Accept", "application/json")
                .build()
        }
        
        return chain.proceed(newRequest)
    }
}

interface AdminApiService {
    @POST("api/v1/auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("api/v1/auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @POST("api/v1/auth/forgot-password")
    suspend fun forgotPassword(@Query("email") email: String): GenericApiResponse

    @GET("api/v1/servers")
    suspend fun getServers(): List<ServerResponseDto>

    @GET("api/v1/configs")
    suspend fun getConfigs(): List<ConfigResponseDto>

    @POST("api/v1/traffic/sync")
    suspend fun syncTrafficStats(@Body request: TrafficSyncRequest): GenericApiResponse
}
