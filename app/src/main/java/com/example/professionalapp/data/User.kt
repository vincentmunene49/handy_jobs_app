package com.example.professionalapp.data

import com.google.firebase.firestore.GeoPoint

data class User(
    val name:String,
    val email:String,
    val isOnline:Boolean = true,
    val location:GeoPoint? = null,
    val token:String?,
    val image:String = ""
) {
    constructor():this("","",true,null,"","")
}