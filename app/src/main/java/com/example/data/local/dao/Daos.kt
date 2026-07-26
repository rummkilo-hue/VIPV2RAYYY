package com.example.data.local.dao

import androidx.room.*
import com.example.data.local.entity.ConfigEntity
import com.example.data.local.entity.ServerEntity
import com.example.data.local.entity.TrafficStatEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ServerDao {
    @Query("SELECT * FROM servers ORDER BY latencyMs ASC")
    fun getAllServers(): Flow<List<ServerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertServers(servers: List<ServerEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertServer(server: ServerEntity)

    @Query("UPDATE servers SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(id: String, isFavorite: Boolean)

    @Query("SELECT * FROM servers WHERE isFavorite = 1")
    fun getFavoriteServers(): Flow<List<ServerEntity>>

    @Query("DELETE FROM servers")
    suspend fun clearServers()
}

@Dao
interface ConfigDao {
    @Query("SELECT * FROM configs ORDER BY createdAt DESC")
    fun getAllConfigs(): Flow<List<ConfigEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConfig(config: ConfigEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConfigs(configs: List<ConfigEntity>)

    @Query("UPDATE configs SET isActive = CASE WHEN id = :activeId THEN 1 ELSE 0 END")
    suspend fun setActiveConfig(activeId: String)

    @Query("UPDATE configs SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(id: String, isFavorite: Boolean)

    @Query("DELETE FROM configs WHERE id = :id")
    suspend fun deleteConfig(id: String)

    @Query("DELETE FROM configs")
    suspend fun clearConfigs()
}

@Dao
interface TrafficStatDao {
    @Query("SELECT * FROM traffic_stats ORDER BY timestamp DESC LIMIT 100")
    fun getRecentTrafficStats(): Flow<List<TrafficStatEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrafficStat(stat: TrafficStatEntity)

    @Query("SELECT SUM(uploadBytes) FROM traffic_stats WHERE timestamp >= :sinceTimestamp")
    suspend fun TotalUploadSince(sinceTimestamp: Long): Long?

    @Query("SELECT SUM(downloadBytes) FROM traffic_stats WHERE timestamp >= :sinceTimestamp")
    suspend fun TotalDownloadSince(sinceTimestamp: Long): Long?

    @Query("DELETE FROM traffic_stats")
    suspend fun clearTrafficStats()
}
