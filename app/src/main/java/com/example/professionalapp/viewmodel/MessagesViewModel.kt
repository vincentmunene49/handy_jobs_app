package com.example.professionalapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.professionalapp.data.Message
import com.example.professionalapp.data.SenderData
import com.example.professionalapp.data.User
import com.example.professionalapp.util.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class MessagesViewModel @Inject constructor(
    private val firestoredb: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ViewModel() {
    private var _messageList =
        MutableStateFlow<Results<List<Message>>>(Results.Unit())
    val messageList = _messageList.asStateFlow()

    private var _user = MutableStateFlow<Results<User>>(Results.Unit())
    val user = _user.asStateFlow()

    private var _app_user = MutableStateFlow<Results<List<SenderData>>>(Results.Unit())
    val app_users = _app_user.asStateFlow()

    init {
        getAllUsers()
    }

    fun postMessage(toId: String?, randomId: String, message: Message) {
        firestoredb.collection(MESSAGES_COLLECTION).document("$toId${auth.uid!!}")
            .collection(MESSAGES_SUB_COLLECTION).document(randomId)
            .set(message)
            .addOnSuccessListener {
                Log.d("MESSAGE", "Success")
                retrieveMessages(toId)
            }
            .addOnFailureListener {
                Log.d("MESSAGE", it.message.toString())
            }
    }


    fun getCurrentUserId(): String {
        return if (auth.currentUser?.uid != null) {
            auth.currentUser!!.uid
        } else {
            ""
        }

    }

    //function to retrieve all messages for a certain conversation
    fun retrieveMessages(toId: String?) {
        viewModelScope.launch {
            _messageList.emit(Results.Loading())
        }
        val messageRef =
            firestoredb.collection(MESSAGES_COLLECTION).document("$toId${auth.uid}").collection(
                MESSAGES_SUB_COLLECTION
            ).orderBy("timestamp", Query.Direction.ASCENDING)
        messageRef.get()
            .addOnSuccessListener {
                val messageList = it.toObjects(Message::class.java)
                Log.d("VIN", "$messageList")
                viewModelScope.launch {
                    _messageList.emit(Results.Success(messageList))
                }

            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _messageList.emit(Results.Failure(it.message.toString()))
                }

            }

    }

    fun getCurrentUser() {
        if (auth.currentUser != null) {
            val docRef =
                firestoredb.collection(PROFESSIONAL_COLLECTION).document(auth.currentUser!!.uid)
            docRef.get().addOnSuccessListener {
                val user = it.toObject(User::class.java)
                viewModelScope.launch {
                    _user.emit(Results.Success(user))
                }
            }
                .addOnFailureListener {
                    viewModelScope.launch {
                        _user.emit(Results.Failure(it.message.toString()))
                    }
                }
        }
    }

    //temporary testing method, query all users
    fun getAllUsers() {
        val docRef = firestoredb.collection(USER_COLLECTION)
        docRef.get()
            .addOnSuccessListener {
                val userLists = it.toObjects(SenderData::class.java)
                viewModelScope.launch {
                    _app_user.emit(Results.Success(userLists))
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _app_user.emit(Results.Failure(it.message.toString()))
                }

            }
    }

    //function to return document id
    fun getReceiverId(
        email: String,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        try {

            viewModelScope.launch(Dispatchers.IO) {
                val docs =
                    firestoredb.collection(USER_COLLECTION).whereEqualTo("email", email).get()
                        .await()
                val id = if (docs.size() > 0) docs.documents[0].id else ""
                onSuccess(id)
            }

        } catch (e: Exception) {
            onFailure(e)

        }

    }


}