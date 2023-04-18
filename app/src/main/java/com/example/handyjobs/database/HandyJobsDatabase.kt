package com.example.handyjobs.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.handyjobs.data.ProfessionCategory

@Database(entities = [ProfessionCategory::class], version = 1, exportSchema = false)
abstract class HandyJobsDatabase:RoomDatabase() {
    abstract fun HandyJobsDao():HandyJobsDao
}