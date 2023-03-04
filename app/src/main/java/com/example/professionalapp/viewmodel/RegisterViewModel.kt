package com.example.professionalapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.professionalapp.data.User
import com.example.professionalapp.util.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore

) : ViewModel() {

    private val _validation = Channel<RegisterFieldState>()
    val validation = _validation.receiveAsFlow()
    private val _register = MutableStateFlow<Results<User>>(Results.Unit())
    val register: StateFlow<Results<User>> = _register

    //register users

    fun createAccount(user: User, password: String) {
        if (validate(user.email, password)) {
            _register.value = Results.Loading()
            firebaseAuth.createUserWithEmailAndPassword(user.email, password)
                .addOnSuccessListener {
                    it.user?.let {
                        signUpUser(it.uid,user)
                        _register.value = Results.Success(user)
                    }

                }.addOnFailureListener {
                    _register.value = Results.Failure(it.message.toString())

                }
        } else {
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
    private fun signUpUser(uid: String, user: User) {
        firebaseFirestore.collection(PROFESSIONAL_COLLECTION)
            .document(uid)
            .set(user)
            .addOnSuccessListener {
                _register.value = Results.Success(user)
            }
            .addOnFailureListener {
                _register.value = Results.Failure(it.message.toString())
            }
    }

    private fun validate(email: String, password: String): Boolean {
        val emailValidation = validateEmail(email)
        val passwordValidation = validatePassword(password)

        return emailValidation is AccountOptionsValidation.Success && passwordValidation is AccountOptionsValidation.Success
    }
}