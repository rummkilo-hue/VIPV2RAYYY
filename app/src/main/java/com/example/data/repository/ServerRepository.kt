package com.example.data.repository

import com.example.data.local.dao.ServerDao
import com.example.data.local.entity.ServerEntity
import com.example.data.remote.AdminApiService
import com.example.domain.model.Server
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ServerRepository(
    private val serverDao: ServerDao,
    private val apiService: AdminApiService
) {

    val allServersFlow: Flow<List<Server>> = serverDao.getAllServers().map { entities ->
        entities.map { it.toDomain() }
    }

    val favoriteServersFlow: Flow<List<Server>> = serverDao.getFavoriteServers().map { entities ->
        entities.map { it.toDomain() }
    }

    suspend fun toggleFavorite(serverId: String, isFavorite: Boolean) {
        serverDao.updateFavorite(serverId, isFavorite)
    }

    suspend fun refreshServersFromApi() {
        try {
            val dtos = apiService.getServers()
            val entities = dtos.map { dto ->
                ServerEntity(
                    id = dto.id,
                    countryName = dto.countryName,
                    countryCode = dto.countryCode,
                    flagEmoji = dto.flagEmoji,
                    serverName = dto.serverName,
                    ipAddress = dto.ipAddress,
                    latencyMs = dto.latencyMs,
                    loadPercentage = dto.loadPercentage,
                    isOnline = dto.isOnline,
                    isFavorite = false,
                    isRecommended = dto.isRecommended,
                    protocol = dto.protocol,
                    city = dto.city
                )
            }
            serverDao.insertServers(entities)
        } catch (e: Exception) {
            // Fallback to default pre-populated servers if network fails
            ensureDefaultServers()
        }
    }

    suspend fun ensureDefaultServers() {
        val defaultServers = listOf(
            ServerEntity(
                id = "sg-vip-01",
                countryName = "Singapore",
                countryCode = "SG",
                flagEmoji = "🇸🇬",
                serverName = "VIP Singapore 01 (Ultra Fast)",
                ipAddress = "139.99.12.45",
                latencyMs = 18,
                loadPercentage = 24,
                isOnline = true,
                isFavorite = true,
                isRecommended = true,
                protocol = "VLESS",
                city = "Marina Bay"
            ),
            ServerEntity(
                id = "sg-vip-02",
                countryName = "Singapore",
                countryCode = "SG",
                flagEmoji = "🇸🇬",
                serverName = "VIP Singapore 02 (Gaming Low Ping)",
                ipAddress = "139.99.12.88",
                latencyMs = 22,
                loadPercentage = 38,
                isOnline = true,
                isFavorite = false,
                isRecommended = true,
                protocol = "Hysteria2",
                city = "Changi"
            ),
            ServerEntity(
                id = "jp-vip-01",
                countryName = "Japan",
                countryCode = "JP",
                flagEmoji = "🇯🇵",
                serverName = "VIP Tokyo 01 (Streaming 4K)",
                ipAddress = "103.201.88.12",
                latencyMs = 45,
                loadPercentage = 32,
                isOnline = true,
                isFavorite = true,
                isRecommended = true,
                protocol = "VMess",
                city = "Tokyo"
            ),
            ServerEntity(
                id = "hk-vip-01",
                countryName = "Hong Kong",
                countryCode = "HK",
                flagEmoji = "🇭🇰",
                serverName = "VIP Hong Kong 01 (Direct Fiber)",
                ipAddress = "45.122.90.10",
                latencyMs = 28,
                loadPercentage = 42,
                isOnline = true,
                isFavorite = false,
                isRecommended = false,
                protocol = "Trojan",
                city = "Kowloon"
            ),
            ServerEntity(
                id = "th-vip-01",
                countryName = "Thailand",
                countryCode = "TH",
                flagEmoji = "🇹🇭",
                serverName = "VIP Bangkok 01 (Bypass)",
                ipAddress = "110.164.200.5",
                latencyMs = 35,
                loadPercentage = 29,
                isOnline = true,
                isFavorite = false,
                isRecommended = true,
                protocol = "Reality",
                city = "Bangkok"
            ),
            ServerEntity(
                id = "kr-vip-01",
                countryName = "South Korea",
                countryCode = "KR",
                flagEmoji = "🇰🇷",
                serverName = "VIP Seoul 01 (High Speed)",
                ipAddress = "211.233.10.15",
                latencyMs = 62,
                loadPercentage = 51,
                isOnline = true,
                isFavorite = false,
                isRecommended = false,
                protocol = "VLESS",
                city = "Seoul"
            ),
            ServerEntity(
                id = "us-vip-01",
                countryName = "United States",
                countryCode = "US",
                flagEmoji = "🇺🇸",
                serverName = "VIP Los Angeles 01 (GPort)",
                ipAddress = "192.154.220.8",
                latencyMs = 150,
                loadPercentage = 18,
                isOnline = true,
                isFavorite = false,
                isRecommended = false,
                protocol = "TUIC",
                city = "Los Angeles"
            ),
            ServerEntity(
                id = "de-vip-01",
                countryName = "Germany",
                countryCode = "DE",
                flagEmoji = "🇩🇪",
                serverName = "VIP Frankfurt 01 (EU Hub)",
                ipAddress = "185.220.101.4",
                latencyMs = 185,
                loadPercentage = 21,
                isOnline = true,
                isFavorite = false,
                isRecommended = false,
                protocol = "Shadowsocks",
                city = "Frankfurt"
            ),
            ServerEntity(
                id = "uk-vip-01",
                countryName = "United Kingdom",
                countryCode = "GB",
                flagEmoji = "🇬🇧",
                serverName = "VIP London 01 (Privacy Core)",
                ipAddress = "89.34.20.12",
                latencyMs = 195,
                loadPercentage = 30,
                isOnline = true,
                isFavorite = false,
                isRecommended = false,
                protocol = "gRPC",
                city = "London"
            ),
            ServerEntity(
                id = "fr-vip-01",
                countryName = "France",
                countryCode = "FR",
                flagEmoji = "🇫🇷",
                serverName = "VIP Paris 01 (Secure Relay)",
                ipAddress = "51.158.40.88",
                latencyMs = 210,
                loadPercentage = 25,
                isOnline = true,
                isFavorite = false,
                isRecommended = false,
                protocol = "SOCKS5",
                city = "Paris"
            ),
            ServerEntity(
                id = "ca-vip-01",
                countryName = "Canada",
                countryCode = "CA",
                flagEmoji = "🇨🇦",
                serverName = "VIP Toronto 01 (Unrestricted)",
                ipAddress = "198.50.180.20",
                latencyMs = 170,
                loadPercentage = 15,
                isOnline = true,
                isFavorite = false,
                isRecommended = false,
                protocol = "WebSocket",
                city = "Toronto"
            )
        )
        serverDao.insertServers(defaultServers)
    }

    private fun ServerEntity.toDomain() = Server(
        id = id,
        countryName = countryName,
        countryCode = countryCode,
        flagEmoji = flagEmoji,
        serverName = serverName,
        ipAddress = ipAddress,
        latencyMs = latencyMs,
        loadPercentage = loadPercentage,
        isOnline = isOnline,
        isFavorite = isFavorite,
        isRecommended = isRecommended,
        protocol = protocol,
        city = city
    )
}
