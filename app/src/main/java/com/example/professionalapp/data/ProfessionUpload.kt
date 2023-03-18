package com.example.professionalapp.data

data class ProfessionUpload(
    val category: String,
    val skillName:String,
    val experience:String,
    val image:List<String> = emptyList(),
    val description:String
) {
    constructor():this("","", "",emptyList(),"")
}