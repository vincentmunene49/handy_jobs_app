package com.example.handyjobs.data

data class User (
    val name:String,
    val email:String,
    var image:String = ""
        ){
    constructor():this("","", "")
}