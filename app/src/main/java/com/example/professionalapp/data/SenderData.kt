package com.example.professionalapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SenderData(
    val name:String,
    val email:String,
    val token:String?,
    var image:String = ""
):Parcelable{
    constructor():this("","", "","")
}