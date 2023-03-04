package com.example.handyjobs.viewmodel

import androidx.lifecycle.ViewModel
import com.example.handyjobs.util.ResultStates
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fire_store: FirebaseFirestore,
    private val auth: FirebaseAuth

) : ViewModel() {
    private var _retrievedData =
        MutableStateFlow<MutableList<String>>(mutableListOf())
    val retrieveData = _retrievedData.asStateFlow()

    private var _retrievedState = MutableStateFlow<ResultStates<MutableList<String>>>(ResultStates.Unit())
    val retrievedState = _retrievedState.asStateFlow()

    init {
        getCategories()
    }

    private fun getCategories() {
        _retrievedState.value = ResultStates.Loading()
        fire_store.collection("Category")
            .get()
            .addOnSuccessListener {
                val category_name: MutableList<String> = mutableListOf()

                for (document in it.documents) {
                    category_name.add(document.data?.getValue("category").toString())
//                    Log.d("QUERY","${document.data?.getValue("category")}")
                }
                _retrievedData.value = category_name
                _retrievedState.value = ResultStates.Success(category_name)
            }
            .addOnFailureListener {
                _retrievedState.value = ResultStates.Failure(it.message.toString())
            }

    }
}