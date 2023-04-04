package com.example.professionalapp.viewmodel


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.professionalapp.util.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    //create a channel for emitting values
    private val _validation = Channel<RegisterFieldState>()//does not require initial value
    val validation = _validation.receiveAsFlow()
    private var _login = MutableSharedFlow<Results<FirebaseUser>>()
    val login: SharedFlow<Results<FirebaseUser>> = _login
    private var _loggedIn = MutableStateFlow<Results<FirebaseUser>>(Results.Unit())
    val loggedIn = _loggedIn.asStateFlow()

    fun signInUser(email: String, password: String) {
        if (validate(email, password)) {
            viewModelScope.launch {
                _login.emit(Results.Loading())
            }
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    viewModelScope.launch {
                        it.user?.let {
                            _login.emit(Results.Success(it))
                        }
                    }
                }
                .addOnFailureListener {
                    viewModelScope.launch {
                        _login.emit(Results.Failure(it.message.toString()))
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
        _loggedIn.value = Results.Loading()
        if (firebaseAuth.currentUser != null) {
            return true
        }
        return false

    }

    fun updateLocationandStatus(geoPoint: GeoPoint?) {
        val docRef = firestore.collection(PROFESSIONAL_COLLECTION).document(firebaseAuth.uid!!)
        geoPoint.let { geoPoints->
            docRef.update("location", geoPoints,
                "online",true).addOnSuccessListener {
                Log.d("LOCUPDATE", "Success ${geoPoints!!.latitude}")


            }.addOnFailureListener {
                Log.d("LOCUPDATE", it.message.toString())


            }
        }


    }
}