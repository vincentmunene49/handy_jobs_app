package com.example.professionalapp.util

import android.util.Patterns

fun validateEmail(email:String):AccountOptionsValidation{
    if(email.isEmpty())
        return AccountOptionsValidation.Failed("Email cannot be empty")

    if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        return AccountOptionsValidation.Failed("Wrong email Format")

    return AccountOptionsValidation.Success
}
fun validatePassword(password:String):AccountOptionsValidation{
    if(password.isEmpty())
        return AccountOptionsValidation.Failed("Password field cannot be empty")

    if(password.length < 6)
        return AccountOptionsValidation.Failed("Password should be more than 6 characters")

    return AccountOptionsValidation.Success
}