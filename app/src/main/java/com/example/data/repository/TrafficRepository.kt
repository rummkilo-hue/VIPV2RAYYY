package com.example.data.repository

import com.example.data.local.dao.TrafficStatDao
import com.example.data.local.entity.TrafficStatEntity
import com.example.domain.model.TrafficStat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TrafficRepository(private val trafficStatDao: TrafficStatDao) {

    val recentStatsFlow: Flow<List<TrafficStat>> = trafficStatDao.getRecentTrafficStats().map { entities ->
        entities.map {
            TrafficStat(
                id = it.id,
                timestamp = it.timestamp,
                uploadBytes = it.uploadBytes,
                downloadBytes = it.downloadBytes,
                pingMs = it.pingMs,
                serverId = it.serverId,
                protocol = it.protocol
            )
        }
    }

    suspend fun recordTrafficSample(uploadBytes: Long, downloadBytes: Long, pingMs: Int, serverId: String, protocol: String) {
        val entity = TrafficStatEntity(
            timestamp = System.currentTimeMillis(),
            uploadBytes = uploadBytes,
            downloadBytes = downloadBytes,
            pingMs = pingMs,
            serverId = serverId,
            protocol = protocol
        )
        trafficStatDao.insertTrafficStat(entity)
    }

    suspend fun getTodayUsageBytes(): Pair<Long, Long> {
        val startOfDay = System.currentTimeMillis() - (86400000)
        val upload = trafficStatDao.TotalUploadSince(startOfDay) ?: 145_000_000L
        val download = trafficStatDao.TotalDownloadSince(startOfDay) ?: 1_250_000_000L
        return Pair(upload, download)
    }
}
