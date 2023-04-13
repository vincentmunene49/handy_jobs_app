package com.example.handyjobs.data

import android.os.Parcelable
import com.google.firebase.firestore.GeoPoint
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class ProfessionCategory(
    val name:String,
    val email:String,
    val experience:String,
    val description:String,
    val latitude: Double?,
    val longitude: Double?,
    val token:String?,
    val image:String? = null
        ):Parcelable{
    constructor():this("","","","",null,null,"")
}




