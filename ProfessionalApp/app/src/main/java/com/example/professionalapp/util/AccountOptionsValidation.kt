package com.example.professionalapp.util

sealed class AccountOptionsValidation() {
    object Success: AccountOptionsValidation()
    data class Failed(val message:String):AccountOptionsValidation()
}
data class RegisterFieldState(
    val email:AccountOptionsValidation,
    val password:AccountOptionsValidation
)