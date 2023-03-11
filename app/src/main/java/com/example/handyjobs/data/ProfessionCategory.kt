package com.example.handyjobs.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProfessionCategory (
     val name:String,
     val email:String,
     val experience:String,
     val description:String,
     val image:String? = null
        ):Parcelable{
    constructor():this("","","","","")
}