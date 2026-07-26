package com.example.data.repository

import com.example.data.local.dao.ConfigDao
import com.example.data.local.entity.ConfigEntity
import com.example.domain.model.ProxyConfig
import com.example.domain.model.ProxyProtocol
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class ConfigRepository(private val configDao: ConfigDao) {

    val allConfigsFlow: Flow<List<ProxyConfig>> = configDao.getAllConfigs().map { entities ->
        entities.map { it.toDomain() }
    }

    suspend fun addConfig(config: ProxyConfig) {
        configDao.insertConfig(config.toEntity())
    }

    suspend fun setActiveConfig(configId: String) {
        configDao.setActiveConfig(configId)
    }

    suspend fun toggleFavorite(configId: String, isFavorite: Boolean) {
        configDao.updateFavorite(configId, isFavorite)
    }

    suspend fun deleteConfig(configId: String) {
        configDao.deleteConfig(configId)
    }

    suspend fun ensureDefaultConfigs() {
        val sampleConfigs = listOf(
            ConfigEntity(
                id = "cfg-01",
                name = "SG-VLESS-Reality-VIP",
                protocol = ProxyProtocol.REALITY.name,
                serverAddress = "sg.vipv2ray.net",
                port = 443,
                uuidOrPassword = "8a2f4c10-9b8e-4a3d-8f2e-1c0b9a8d7e6f",
                pathOrSni = "sg.vipv2ray.net",
                rawLink = "vless://8a2f4c10-9b8e-4a3d-8f2e-1c0b9a8d7e6f@sg.vipv2ray.net:443?encryption=none&security=reality&type=grpc&sni=sg.vipv2ray.net#SG-VLESS-Reality-VIP",
                isFavorite = true,
                isActive = true,
                createdAt = System.currentTimeMillis()
            ),
            ConfigEntity(
                id = "cfg-02",
                name = "JP-Hysteria2-Gaming",
                protocol = ProxyProtocol.HYSTERIA2.name,
                serverAddress = "jp.vipv2ray.net",
                port = 8443,
                uuidOrPassword = "pass_hysteria2_vip_jp",
                pathOrSni = "jp.vipv2ray.net",
                rawLink = "hy2://pass_hysteria2_vip_jp@jp.vipv2ray.net:8443?insecure=1&sni=jp.vipv2ray.net#JP-Hysteria2-Gaming",
                isFavorite = true,
                isActive = false,
                createdAt = System.currentTimeMillis() - 86400000
            ),
            ConfigEntity(
                id = "cfg-03",
                name = "HK-VMess-WebSocket-TLS",
                protocol = ProxyProtocol.VMESS.name,
                serverAddress = "hk.vipv2ray.net",
                port = 443,
                uuidOrPassword = "7b1c3d2e-4f5a-6b7c-8d9e-0f1a2b3c4d5e",
                pathOrSni = "/vipv2ray-ws",
                rawLink = "vmess://eyJhZGQiOiJoay52aXB2MnJheS5uZXQiLCJwb3J0Ijo0NDMsImlkIjoiN2IxYzNkMmUtNGY1YS02YjdjLThkOWUtMGYxYTJiM2M0ZDVlIiwibmV0Ijoid3MiLCJ0eXBlIjoibm9uZSIsImhvc3QiOiJoay52aXB2MnJheS5uZXQiLCJwYXRoIjoiL3ZpcHYycmF5LXdzIiwidGxzIjoidGxzIiwicHMiOiJISy1WTWVzcy1XZWJTb2NrZXQtVExTIn0=",
                isFavorite = false,
                isActive = false,
                createdAt = System.currentTimeMillis() - 172800000
            ),
            ConfigEntity(
                id = "cfg-04",
                name = "TH-Trojan-gRPC",
                protocol = ProxyProtocol.TROJAN.name,
                serverAddress = "th.vipv2ray.net",
                port = 443,
                uuidOrPassword = "trojan_secret_token_kh",
                pathOrSni = "th.vipv2ray.net",
                rawLink = "trojan://trojan_secret_token_kh@th.vipv2ray.net:443?type=grpc&serviceName=vipv2ray-grpc&sni=th.vipv2ray.net#TH-Trojan-gRPC",
                isFavorite = false,
                isActive = false,
                createdAt = System.currentTimeMillis() - 259200000
            ),
            ConfigEntity(
                id = "cfg-05",
                name = "US-TUIC-UDP-Turbo",
                protocol = ProxyProtocol.TUIC.name,
                serverAddress = "us.vipv2ray.net",
                port = 8443,
                uuidOrPassword = "tuic_token_8888",
                pathOrSni = "us.vipv2ray.net",
                rawLink = "tuic://uuid_tuic_8888:tuic_token_8888@us.vipv2ray.net:8443?congestion_control=bbr&alpn=h3&sni=us.vipv2ray.net#US-TUIC-UDP-Turbo",
                isFavorite = false,
                isActive = false,
                createdAt = System.currentTimeMillis() - 345600000
            )
        )
        configDao.insertConfigs(sampleConfigs)
    }

    fun parseConfigLink(link: String): ProxyConfig? {
        val trimmed = link.trim()
        if (trimmed.isEmpty()) return null
        val id = "cfg-" + UUID.randomUUID().toString().take(8)

        return when {
            trimmed.startsWith("vless://", ignoreCase = true) -> {
                val name = extractTagFromLink(trimmed) ?: "VLESS Config"
                ProxyConfig(id, name, ProxyProtocol.VLESS, "vless.vipv2ray.net", 443, "uuid-vless", "", trimmed)
            }
            trimmed.startsWith("vmess://", ignoreCase = true) -> {
                val name = extractTagFromLink(trimmed) ?: "VMess Config"
                ProxyConfig(id, name, ProxyProtocol.VMESS, "vmess.vipv2ray.net", 443, "uuid-vmess", "", trimmed)
            }
            trimmed.startsWith("trojan://", ignoreCase = true) -> {
                val name = extractTagFromLink(trimmed) ?: "Trojan Config"
                ProxyConfig(id, name, ProxyProtocol.TROJAN, "trojan.vipv2ray.net", 443, "pass-trojan", "", trimmed)
            }
            trimmed.startsWith("hy2://", ignoreCase = true) || trimmed.startsWith("hysteria2://", ignoreCase = true) -> {
                val name = extractTagFromLink(trimmed) ?: "Hysteria2 Config"
                ProxyConfig(id, name, ProxyProtocol.HYSTERIA2, "hy2.vipv2ray.net", 8443, "pass-hy2", "", trimmed)
            }
            trimmed.startsWith("tuic://", ignoreCase = true) -> {
                val name = extractTagFromLink(trimmed) ?: "TUIC Config"
                ProxyConfig(id, name, ProxyProtocol.TUIC, "tuic.vipv2ray.net", 8443, "pass-tuic", "", trimmed)
            }
            trimmed.startsWith("ss://", ignoreCase = true) -> {
                val name = extractTagFromLink(trimmed) ?: "Shadowsocks Config"
                ProxyConfig(id, name, ProxyProtocol.SHADOWSOCKS, "ss.vipv2ray.net", 8388, "pass-ss", "", trimmed)
            }
            else -> {
                ProxyConfig(id, "Imported Proxy Config", ProxyProtocol.VLESS, "custom.vipv2ray.net", 443, "uuid-custom", "", trimmed)
            }
        }
    }

    private fun extractTagFromLink(link: String): String? {
        val hashIdx = link.indexOf('#')
        return if (hashIdx != -1 && hashIdx < link.length - 1) {
            runCatching { java.net.URLDecoder.decode(link.substring(hashIdx + 1), "UTF-8") }.getOrNull()
        } else null
    }

    private fun ConfigEntity.toDomain() = ProxyConfig(
        id = id,
        name = name,
        protocol = runCatching { ProxyProtocol.valueOf(protocol) }.getOrDefault(ProxyProtocol.VLESS),
        serverAddress = serverAddress,
        port = port,
        uuidOrPassword = uuidOrPassword,
        pathOrSni = pathOrSni,
        rawLink = rawLink,
        isFavorite = isFavorite,
        isActive = isActive,
        createdAt = createdAt
    )

    private fun ProxyConfig.toEntity() = ConfigEntity(
        id = id,
        name = name,
        protocol = protocol.name,
        serverAddress = serverAddress,
        port = port,
        uuidOrPassword = uuidOrPassword,
        pathOrSni = pathOrSni,
        rawLink = rawLink,
        isFavorite = isFavorite,
        isActive = isActive,
        createdAt = createdAt
    )
}
