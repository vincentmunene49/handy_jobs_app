package com.example.handyjobs.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.handyjobs.data.ProfessionCategory
import com.example.handyjobs.util.ResultStates
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class RetrieveProfessionalViewModel @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
) : ViewModel() {
    private var _professionals =
        MutableStateFlow<MutableList<ProfessionCategory>>(mutableListOf())
    val professionals = _professionals.asStateFlow()
    private var _screenState = MutableStateFlow<ResultStates<String>>(ResultStates.Unit())
    val screenState = _screenState.asStateFlow()

    fun getDocumentIds(category: String) {
        viewModelScope.launch {
            _screenState.emit(ResultStates.Loading())
            try {
                val categoryResponse = firebaseFirestore.collection("Category")
                    .whereEqualTo("category", category)
                    .get().await()

                val deferredProfessionals = categoryResponse.documents.map { categoryDocument ->
                    async {
                        val professionalDocument = firebaseFirestore.collection("Professionals")
                            .document(categoryDocument.id)
                            .get().await()

                        val professionalData = professionalDocument.toObject(ProfessionCategory::class.java)
                        val experience = categoryDocument.data?.getValue("experience").toString()
                        val description = categoryDocument.data?.getValue("description").toString()

                        ProfessionCategory(
                            professionalData!!.name,
                            professionalData.email,
                            experience,
                            description,
                            professionalData.image ?: ""
                        )
                    }
                }

                val professionalList = deferredProfessionals.awaitAll()
                _professionals.value = professionalList as MutableList<ProfessionCategory>
                _screenState.value = ResultStates.Success("success")
               // Log.d("Professional", _professionals.value.toString())

            } catch (e: Exception) {
                Log.e("Error", e.message, e)
            }
        }
    }



}