package com.example.professionalapp.viewmodel

import android.widget.Spinner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.professionalapp.data.ProfessionUpload
import com.example.professionalapp.util.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddProfessionViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) : ViewModel() {
    private val _validation = Channel<UploadSKillsFieldStates>()
    val validation = _validation.receiveAsFlow()
    private var _upload = MutableStateFlow<Results<ProfessionUpload>>(Results.Unit())
    val upload = _upload.asStateFlow()

    //emit userId
//    private var _emitId = Channel<String>()
//    val emitId = _emitId.receiveAsFlow()


//    init {
//        viewModelScope.launch {
//            getUserId()?.let { _emitId.send(it) }
//        }
//    }

    //fun to upload profession
    fun uploadProfession(professionUpload: ProfessionUpload) {
        if (validationCheck(
                professionUpload.category,
                professionUpload.skillName,
                professionUpload.experience,
                professionUpload.experience
            )
        ) {
            _upload.value = Results.Loading()

            firebaseFirestore.collection(CATEGORY_COLLECTION).document(UUID.randomUUID().toString())
                .set(professionUpload)
                .addOnSuccessListener {
                    _upload.value = Results.Success(professionUpload)

                }.addOnFailureListener {
                    _upload.value = Results.Failure(it.message.toString())

                }
        } else {
            val uploadSKillsFieldStates = UploadSKillsFieldStates(
                validateCategory(professionUpload.category),
                validateExperience(professionUpload.experience),
                validateDescription(professionUpload.description),
                validateSKill(professionUpload.skillName)
            )
            viewModelScope.launch {
                _validation.send(uploadSKillsFieldStates)
            }
        }

    }


    //fun to validate fields
    private fun validationCheck(
        spinner: String,
        skill: String,
        experience: String,
        description: String
    ): Boolean {
        val categories = validateCategory(spinner)
        val skillDesc = validateSKill(skill)
        val yearsExperience = validateExperience(experience)
        val workDesc = validateDescription(description)

        return categories is Validation.Success && skillDesc is Validation.Success && yearsExperience is Validation.Success && workDesc is Validation.Success
    }

    //emit Id
     fun getUserId(): String {
        return firebaseAuth.currentUser!!.uid
    }

}