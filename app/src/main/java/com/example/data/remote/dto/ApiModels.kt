package com.example.data.remote.dto

data class LoginRequest(
    val email: String,
    val passwordHash: String
)

data class RegisterRequest(
    val username: String,
    val email: String,
    val passwordHash: String
)

data class AuthResponse(
    val success: Boolean,
    val message: String,
    val token: String?,
    val userId: String?,
    val username: String?,
    val isVip: Boolean? = true,
    val vipExpiryDate: String? = "2027-12-31"
)

data class ServerResponseDto(
    val id: String,
    val countryName: String,
    val countryCode: String,
    val flagEmoji: String,
    val serverName: String,
    val ipAddress: String,
    val latencyMs: Int,
    val loadPercentage: Int,
    val isOnline: Boolean,
    val isRecommended: Boolean,
    val protocol: String,
    val city: String
)

data class ConfigResponseDto(
    val id: String,
    val name: String,
    val protocol: String,
    val serverAddress: String,
    val port: Int,
    val uuidOrPassword: String,
    val pathOrSni: String,
    val rawLink: String
)

data class TrafficSyncRequest(
    val userId: String,
    val uploadBytes: Long,
    val downloadBytes: Long,
    val timestamp: Long
)

data class GenericApiResponse(
    val success: Boolean,
    val message: String
)
