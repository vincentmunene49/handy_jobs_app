package com.example.handyjobs.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.handyjobs.data.Message
import com.example.handyjobs.util.MESSAGES_COLLECTION
import com.example.handyjobs.util.MESSAGES_SUB_COLLECTION
import com.example.handyjobs.util.ResultStates
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
        MutableStateFlow<ResultStates<List<Message>>>(ResultStates.Unit())
    val messageList = _messageList.asStateFlow()

    fun postMessage(fromId: String?, randomId: String, message: Message) {
        firestoredb.collection(MESSAGES_COLLECTION).document("${auth.uid!!}$fromId")
            .collection(MESSAGES_SUB_COLLECTION).document(randomId)
            .set(message)
            .addOnSuccessListener {
                Log.d("MESSAGE", "Success")
            }
            .addOnFailureListener {
                Log.d("MESSAGE", it.message.toString())
            }
    }

    fun retrieveProfessionalId(
        email: String,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val docs =
                    firestoredb.collection("Professionals").whereEqualTo("email", email).get()
                        .await()
                val id = if (docs.size() > 0) docs.documents[0].id else ""
                onSuccess(id)
            } catch (e: Exception) {
                onFailure(e)
            }
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
    fun retrieveMessages(fromId: String?) {
        viewModelScope.launch {
            _messageList.emit(ResultStates.Loading())
        }
        val messageRef =
            firestoredb.collection(MESSAGES_COLLECTION).document("${auth.uid}$fromId").collection(
                MESSAGES_SUB_COLLECTION
            ).whereEqualTo("fromId", fromId).whereEqualTo("toId", auth.uid).orderBy("timestamp",Query.Direction.ASCENDING)
        messageRef.get()
            .addOnSuccessListener {
                val messageList = it.toObjects(Message::class.java)
                viewModelScope.launch {
                    _messageList.emit(ResultStates.Success(messageList))
                }

        }
            .addOnFailureListener {
                viewModelScope.launch {
                    _messageList.emit(ResultStates.Failure(it.message.toString()))
                }

            }

    }


}