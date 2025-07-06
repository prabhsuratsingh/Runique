package com.example.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.core.database.dao.RunDao
import com.example.core.database.dao.RunPendingSyncDao
import com.example.core.database.entity.DeletedRunSyncEntity
import com.example.core.database.entity.RunEntity

@Database(
    entities = [
        RunEntity::class,
        RunPendingSyncDao::class,
        DeletedRunSyncEntity::class
               ],
    version = 1
)
abstract class RunDatabase : RoomDatabase() {
    abstract val runDao: RunDao
    abstract val runPendingSyncDao: RunPendingSyncDao
}