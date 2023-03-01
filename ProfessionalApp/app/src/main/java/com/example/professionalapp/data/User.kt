package com.example.professionalapp.data

data class User(
    val name:String,
    val email:String,
    val image:String = ""
) {
    constructor():this("","","")
}