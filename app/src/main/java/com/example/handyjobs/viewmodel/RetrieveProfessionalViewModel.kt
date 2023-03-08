package com.example.handyjobs.viewmodel

import androidx.lifecycle.ViewModel
import com.example.handyjobs.data.ProfessionCategory
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class RetrieveProfessionalViewModel @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
) : ViewModel() {
    private var _professionals =
        MutableStateFlow<MutableList<ProfessionCategory>>(mutableListOf())
    val professionals = _professionals.asStateFlow()


    fun getDocumentIds(category: String) {
        firebaseFirestore.collection("Category")
            .whereEqualTo("category", category)
            .get()
            .addOnSuccessListener { category_response ->
                val professionalList = mutableListOf<ProfessionCategory>()
                category_response.documents.forEach { category_document ->
                    firebaseFirestore.collection("Professionals")
                        .document(category_document.id)
                        .get()
                        .addOnSuccessListener {
                            val professionalData = it.toObject(ProfessionCategory::class.java)
                            val experience = category_document.data?.getValue("experience").toString()
                            val description = category_document.data?.getValue("description").toString()

                            val professional = ProfessionCategory(
                                professionalData!!.name,
                                professionalData.email,
                                experience,
                                description,
                                professionalData.image ?: ""

                            )
                            professionalList.add(professional)

                        }
                        .addOnFailureListener {

                        }
                }


                _professionals.value = professionalList

            }
            .addOnFailureListener {

            }
    }


}