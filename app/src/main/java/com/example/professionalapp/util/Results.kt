package com.example.professionalapp.util

sealed class Results<T>(
    val data:T? = null,
    val message:String? = null)
{
    class Unit<T>():Results<T>()
    class Success<T>(data:T?):Results<T>(data)
    class Failure<T>(message: String?):Results<T>(message=message)
    class Loading<T>():Results<T>()
}