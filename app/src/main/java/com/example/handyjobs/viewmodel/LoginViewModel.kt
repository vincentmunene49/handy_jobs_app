package com.example.handyjobs.viewmodel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.handyjobs.data.User
import com.example.handyjobs.services.FirebaseMessagingService
import com.example.handyjobs.util.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {
    //create a channel for emitting values
    private val _validation = Channel<RegisterFieldState>()//does not require initial value
    val validation = _validation.receiveAsFlow()
    private var _login = MutableSharedFlow<ResultStates<FirebaseUser>>()
    val login: SharedFlow<ResultStates<FirebaseUser>> = _login
    private var _loggedIn = MutableStateFlow<ResultStates<FirebaseUser>>(ResultStates.Unit())
    val loggedIn = _loggedIn.asStateFlow()

    fun signInUser(email: String, password: String) {
        if (validate(email, password)) {
            viewModelScope.launch {
                _login.emit(ResultStates.Loading())
            }
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    viewModelScope.launch {
                        it.user?.let {
                            _login.emit(ResultStates.Success(it))
                        }
                    }
                }
                .addOnFailureListener {
                    viewModelScope.launch {
                        _login.emit(ResultStates.Failure(it.message.toString()))
                    }
                }

        }//end of if
        else {
            val loginFieldState = RegisterFieldState(
                validateEmail(email),
                validatePassword(password)
            )
            viewModelScope.launch {
                _validation.send(loginFieldState)
            }
        }

    }

    //validation
    private fun validate(email: String, password: String): Boolean {
        val emailValidation = validateEmail(email)
        val passwordValidation = validatePassword(password)

        return emailValidation is AccountOptionsValidation.Success && passwordValidation is AccountOptionsValidation.Success

    }

    fun checkIfUserIsLoggedIn(): Boolean {
        _loggedIn.value = ResultStates.Loading()
        if (firebaseAuth.currentUser != null) {
            return true
        }
        return false

    }

    //update token
    fun updateToken(token: String?) {
        if (firebaseAuth.currentUser != null) {
            val dbRef =
                firestore.collection(USER_COLLECTION).document(firebaseAuth.currentUser!!.uid)
            dbRef.update("token", token).addOnSuccessListener {
                Log.d("Token","Success")
            }.addOnFailureListener {
                Log.d("Token",it.message.toString())

            }

        }
    }
}