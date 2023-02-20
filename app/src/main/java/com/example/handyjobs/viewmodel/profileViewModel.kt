package com.example.handyjobs.viewmodel

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Media
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.handyjobs.HandyJobsApplication
import com.example.handyjobs.data.User
import com.example.handyjobs.util.ResultStates
import com.example.handyjobs.util.USER_COLLECTION
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class profileViewModel @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    app: Application
) : AndroidViewModel(app) {
    private val storage  = Firebase.storage.reference
    private var _userInfo = MutableStateFlow<ResultStates<User>>(ResultStates.Unit())
    val userInfo = _userInfo.asStateFlow()



    init {
        getUserInfo()
    }

    private fun getUserInfo() {
        viewModelScope.launch {
            _userInfo.emit(ResultStates.Loading())
        }
        val docRef = firebaseFirestore.collection(USER_COLLECTION).document(auth.uid!!)
        docRef.get().addOnSuccessListener {
            val current_user = it.toObject(User::class.java)
            viewModelScope.launch {
                _userInfo.emit(ResultStates.Success(current_user))
            }

        }.addOnFailureListener {
            viewModelScope.launch {
                _userInfo.emit(ResultStates.Failure(it.message.toString()))
            }
        }
    }

    //upload an image
    fun uploadProfilePic(imageUri: Uri) {
            val userRef = firebaseFirestore.collection(USER_COLLECTION).document(auth.uid!!)
            userRef.get().addOnSuccessListener {
                val user = it.toObject(User::class.java)
                try {
                    if (user?.image.isNullOrEmpty()) {
                        val bitmap = MediaStore.Images.Media.getBitmap(
                            getApplication<HandyJobsApplication>().contentResolver, imageUri
                        )
                        val byteArrayOutpUtStream = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutpUtStream)
                        val imageByteArray = byteArrayOutpUtStream.toByteArray()
                        val imageRef = storage.child("profileImage/${auth.uid}/${UUID.randomUUID()}")
                        viewModelScope.launch {
                            val result = imageRef.putBytes(imageByteArray).await()
                            val imageUrl = result.storage.downloadUrl.await().toString()
                            userRef.update("image",imageUrl).addOnSuccessListener {
                                getUserInfo()

                            }.addOnFailureListener{

                            }//for update success

                        }
                    }
                }catch (_:java.lang.Exception){

                }


            }
                .addOnFailureListener {

                }//for retrieving user success


    }

   
}