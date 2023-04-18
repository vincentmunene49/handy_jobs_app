package com.example.handyjobs.viewmodel

import androidx.lifecycle.ViewModel
import com.example.handyjobs.data.ProfessionCategory
import com.example.handyjobs.viewmodel.repository.HandyJobsDbRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class DbViewModel @Inject constructor(private val repository: HandyJobsDbRepository):ViewModel() {

    val professionalsList = repository.selectAllProfessionals()

    suspend fun insertProfessional(professionCategory: ProfessionCategory){
        repository.insertProfessional(professionCategory)
    }


}