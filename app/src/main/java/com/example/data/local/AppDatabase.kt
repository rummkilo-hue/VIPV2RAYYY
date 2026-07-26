package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.ConfigDao
import com.example.data.local.dao.ServerDao
import com.example.data.local.dao.TrafficStatDao
import com.example.data.local.entity.ConfigEntity
import com.example.data.local.entity.ServerEntity
import com.example.data.local.entity.TrafficStatEntity

@Database(
    entities = [ServerEntity::class, ConfigEntity::class, TrafficStatEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun serverDao(): ServerDao
    abstract fun configDao(): ConfigDao
    abstract fun trafficStatDao(): TrafficStatDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "vipv2ray_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
