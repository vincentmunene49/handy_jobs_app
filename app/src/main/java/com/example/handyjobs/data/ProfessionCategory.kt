package com.example.handyjobs.data

data class ProfessionCategory (
     val name:String,
     val email:String,
     val experience:String,
     val description:String,
     val image:String? = null
        ){
    constructor():this("","","","","")
}