package com.example.professionalapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.professionalapp.util.PROFESSIONAL_COLLECTION
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
):ViewModel() {

    fun signOut(){
        updatedStatus()
        firebaseAuth.signOut()
    }

    fun updatedStatus(){
        val docRef = firestore.collection(PROFESSIONAL_COLLECTION).document(firebaseAuth.uid!!)
        docRef.update("online",false).addOnSuccessListener {
            Log.d("STATUS", "success")
        }.addOnFailureListener {
            Log.d("STATUS",it.message.toString())
        }

    }
}