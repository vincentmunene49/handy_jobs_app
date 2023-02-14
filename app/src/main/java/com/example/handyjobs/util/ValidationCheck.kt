package com.example.handyjobs.util

import android.util.Patterns

fun validateEmail(email:String):RegisterValidation{
    if(email.isEmpty())
        return RegisterValidation.Failed("Email cannot be empty")

    if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        return RegisterValidation.Failed("Wrong email Format")

    return RegisterValidation.Success
}
fun validatePassword(password:String):RegisterValidation{
    if(password.isEmpty())
        return RegisterValidation.Failed("Password field cannot be empty")

    if(password.length < 6)
        return RegisterValidation.Failed("Password should be more than 6 characters")

    return RegisterValidation.Success
}