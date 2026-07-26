package com.example.domain.model

data class Server(
    val id: String,
    val countryName: String,
    val countryCode: String,
    val flagEmoji: String,
    val serverName: String,
    val ipAddress: String,
    val latencyMs: Int,
    val loadPercentage: Int,
    val isOnline: Boolean,
    val isFavorite: Boolean = false,
    val isRecommended: Boolean = false,
    val protocol: String = "VLESS",
    val city: String = ""
)

enum class ProxyProtocol {
    VLESS, VMESS, TROJAN, SHADOWSOCKS, SOCKS5, WEBSOCKET, GRPC, TLS, REALITY, HYSTERIA2, TUIC
}

data class ProxyConfig(
    val id: String,
    val name: String,
    val protocol: ProxyProtocol,
    val serverAddress: String,
    val port: Int,
    val uuidOrPassword: String,
    val pathOrSni: String = "",
    val rawLink: String,
    val isFavorite: Boolean = false,
    val isActive: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

data class TrafficStat(
    val id: Long = 0,
    val timestamp: Long,
    val uploadBytes: Long,
    val downloadBytes: Long,
    val pingMs: Int,
    val serverId: String,
    val protocol: String
)

data class User(
    val userId: String,
    val username: String,
    val email: String,
    val avatarUrl: String? = null,
    val isVip: Boolean = true,
    val vipExpiryDate: String = "2027-12-31",
    val referralCode: String = "VIP-KH-8888",
    val token: String? = null
)

enum class AppLanguage(val code: String, val displayName: String) {
    KHMER("km", "ភាសាខ្មែរ (Khmer)"),
    ENGLISH("en", "English")
}

enum class AppTheme {
    AMOLED_DARK, GLASS_DARK, MIDNIGHT_BLUE
}

enum class ProxyMode {
    GLOBAL, PAC, BYPASS_LAN, SPLIT_TUNNEL
}

data class AppSettings(
    val language: AppLanguage = AppLanguage.KHMER,
    val theme: AppTheme = AppTheme.AMOLED_DARK,
    val proxyMode: ProxyMode = ProxyMode.GLOBAL,
    val dnsServer: String = "1.1.1.1 (Cloudflare Secure)",
    val mtuSize: Int = 1400,
    val isBiometricEnabled: Boolean = false,
    val isPinLockEnabled: Boolean = false,
    val pinCode: String = "",
    val isScreenshotProtected: Boolean = true,
    val isRootDetectionEnabled: Boolean = true,
    val isNotificationEnabled: Boolean = true
)
