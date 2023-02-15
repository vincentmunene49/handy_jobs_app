package com.example.handyjobs.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.handyjobs.data.User
import com.example.handyjobs.util.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore

):ViewModel() {

    private val _validation = Channel<RegisterFieldState>()
    val validation = _validation.receiveAsFlow()
    private val _register = MutableStateFlow< ResultStates<User>>(ResultStates.Unit())
    val register:StateFlow<ResultStates<User>> = _register
    //register user
    fun createAccount(user: User,password: String) {
        if (validate(user, password)) {
            viewModelScope.launch {
                _register.emit(ResultStates.Loading())
            }
            firebaseAuth.createUserWithEmailAndPassword(user.email, password)
                .addOnSuccessListener {
                    it.user?.let {
                        signUpUsers(it.uid,user)
                    }

                }
                .addOnFailureListener {
                    _register.value = ResultStates.Failure(it.message.toString())

                }

        }else{
            val registerFieldState = RegisterFieldState(
                validateEmail(user.email),
                validatePassword(password)
            )
            viewModelScope.launch {
                _validation.send(registerFieldState)
            }

        }
    }
//sign up user
    private fun signUpUsers(Uid:String,user: User){
        firebaseFirestore.collection(USER_COLLECTION)
            .document(Uid)
            .set(user)
            .addOnSuccessListener {
                _register.value = ResultStates.Success(user)
            }
            .addOnFailureListener {
                _register.value = ResultStates.Failure(it.message.toString())
            }

    }



    //validation
    private fun validate(user:User,password:String):Boolean{
        val emailValidation = validateEmail(user.email)
        val passwordValidation = validatePassword(password)

        return emailValidation is AccountOptionsValidation.Success && passwordValidation is AccountOptionsValidation.Success

    }

}