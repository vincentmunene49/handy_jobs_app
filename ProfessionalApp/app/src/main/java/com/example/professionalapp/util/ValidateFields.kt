package com.example.professionalapp.util

import android.widget.Spinner


fun validateCategory(spinner: String): Validation {
    if (spinner.isEmpty())
        return Validation.Failure("Please Select a Category Item")
    return Validation.Success
}

fun validateSKill(skills: String): Validation {
    if (skills.isEmpty())
        return Validation.Failure("Please fill in the skills Field")
    return Validation.Success
}

fun validateExperience(yearsExperience: String): Validation {
    if (yearsExperience.isEmpty())
        return Validation.Failure("Please fill in years of experience")
    return Validation.Success
}

fun validateDescription(description: String): Validation {
    if (description.isEmpty())
        return Validation.Failure("Fill in the description field")
    return Validation.Success

}