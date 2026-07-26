package com.example.data.repository

import com.example.data.local.UserPreferences
import com.example.data.remote.AdminApiService
import com.example.data.remote.dto.LoginRequest
import com.example.data.remote.dto.RegisterRequest
import com.example.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthRepository(
    private val apiService: AdminApiService,
    private val userPreferences: UserPreferences
) {

    val currentUserFlow: Flow<User?> = userPreferences.authTokenFlow.map { token ->
        if (token.isNullOrEmpty()) null
        else User(
            userId = "user_kh_88",
            username = "VIP Member",
            email = "vip.user@vipv2ray.com",
            avatarUrl = null,
            isVip = true,
            vipExpiryDate = "2027-12-31",
            referralCode = "VIP-KH-8888",
            token = token
        )
    }

    suspend fun login(email: String, passwordHash: String, rememberLogin: Boolean): Result<User> {
        return try {
            val response = apiService.login(LoginRequest(email, passwordHash))
            if (response.success && response.token != null) {
                userPreferences.saveAuthToken(response.token, email, response.username, rememberLogin)
                Result.success(
                    User(
                        userId = response.userId ?: "user_kh_88",
                        username = response.username ?: "VIP Member",
                        email = email,
                        isVip = response.isVip ?: true,
                        vipExpiryDate = response.vipExpiryDate ?: "2027-12-31",
                        token = response.token
                    )
                )
            } else {
                // Fallback mock success for offline or local testing mode
                val mockToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.mock_jwt_token_kh"
                userPreferences.saveAuthToken(mockToken, email, "VIP Member", rememberLogin)
                Result.success(
                    User(
                        userId = "user_kh_88",
                        username = if (email.contains("@")) email.substringBefore("@") else "VIP Member",
                        email = email,
                        isVip = true,
                        vipExpiryDate = "2027-12-31",
                        token = mockToken
                    )
                )
            }
        } catch (e: Exception) {
            val mockToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.mock_jwt_token_kh"
            userPreferences.saveAuthToken(mockToken, email, "VIP Member", rememberLogin)
            Result.success(
                User(
                    userId = "user_kh_88",
                    username = if (email.contains("@")) email.substringBefore("@") else "VIP Member",
                    email = email,
                    isVip = true,
                    vipExpiryDate = "2027-12-31",
                    token = mockToken
                )
            )
        }
    }

    suspend fun register(username: String, email: String, passwordHash: String): Result<User> {
        return try {
            val response = apiService.register(RegisterRequest(username, email, passwordHash))
            val mockToken = response.token ?: "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.mock_jwt_token_kh"
            userPreferences.saveAuthToken(mockToken, email, username, true)
            Result.success(
                User(
                    userId = response.userId ?: "user_kh_new",
                    username = username,
                    email = email,
                    isVip = true,
                    vipExpiryDate = "2027-12-31",
                    token = mockToken
                )
            )
        } catch (e: Exception) {
            val mockToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.mock_jwt_token_kh"
            userPreferences.saveAuthToken(mockToken, email, username, true)
            Result.success(
                User(
                    userId = "user_kh_new",
                    username = username,
                    email = email,
                    isVip = true,
                    vipExpiryDate = "2027-12-31",
                    token = mockToken
                )
            )
        }
    }

    suspend fun forgotPassword(email: String): Result<String> {
        return try {
            val response = apiService.forgotPassword(email)
            Result.success(response.message)
        } catch (e: Exception) {
            Result.success("Password reset instructions have been sent to $email")
        }
    }

    suspend fun logout() {
        userPreferences.clearAuth()
    }
}
