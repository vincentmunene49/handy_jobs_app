package com.example.professionalapp.util

sealed class Validation{
    object Success:Validation()
    data class Failure(val message:String?):Validation()

}
data class UploadSKillsFieldStates(
    val category:Validation,
    val experience:Validation,
    val description:Validation,
    val skill_name:Validation
)


