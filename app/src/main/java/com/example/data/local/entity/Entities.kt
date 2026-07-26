package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "servers")
data class ServerEntity(
    @PrimaryKey val id: String,
    val countryName: String,
    val countryCode: String,
    val flagEmoji: String,
    val serverName: String,
    val ipAddress: String,
    val latencyMs: Int,
    val loadPercentage: Int,
    val isOnline: Boolean,
    val isFavorite: Boolean,
    val isRecommended: Boolean,
    val protocol: String,
    val city: String
)

@Entity(tableName = "configs")
data class ConfigEntity(
    @PrimaryKey val id: String,
    val name: String,
    val protocol: String,
    val serverAddress: String,
    val port: Int,
    val uuidOrPassword: String,
    val pathOrSni: String,
    val rawLink: String,
    val isFavorite: Boolean,
    val isActive: Boolean,
    val createdAt: Long
)

@Entity(tableName = "traffic_stats")
data class TrafficStatEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long,
    val uploadBytes: Long,
    val downloadBytes: Long,
    val pingMs: Int,
    val serverId: String,
    val protocol: String
)
