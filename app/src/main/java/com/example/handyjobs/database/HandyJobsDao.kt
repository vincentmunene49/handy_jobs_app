package com.example.handyjobs.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.handyjobs.data.ProfessionCategory
import com.example.handyjobs.util.TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface HandyJobsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIntoDatabase(professionCategory: ProfessionCategory)
    @Query("SELECT * FROM $TABLE_NAME")
    fun selectAllProfessionals():Flow<List<ProfessionCategory>>
}