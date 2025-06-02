package com.example.core.database

import androidx.room.Database
import com.example.core.database.dao.RunDao
import com.example.core.database.entity.RunEntity

@Database(
    entities = [RunEntity::class],
    version = 1
)
abstract class RunDatabase {
    abstract val runDao: RunDao
}