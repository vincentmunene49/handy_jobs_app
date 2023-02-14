package com.example.handyjobs.util

sealed class ResultStates<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T?):ResultStates<T>(data)
    class Failure<T>(message: String?):ResultStates<T>(message=message)
    class Loading<T>():ResultStates<T>()
    class Unit<T>():ResultStates<T>()
}