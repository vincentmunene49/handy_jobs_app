package com.example.handyjobs.viewmodel.repository

import com.example.handyjobs.data.ProfessionCategory
import com.example.handyjobs.database.HandyJobsDao
import javax.inject.Inject

class HandyJobsDbRepository @Inject constructor(private val dao: HandyJobsDao) {

    suspend fun insertProfessional(professional:ProfessionCategory) = dao.insertIntoDatabase(professional)
     fun selectAllProfessionals() = dao.selectAllProfessionals()
}