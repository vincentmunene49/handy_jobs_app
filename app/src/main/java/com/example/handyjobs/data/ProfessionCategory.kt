package com.example.handyjobs.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.handyjobs.util.TABLE_NAME
import com.google.firebase.firestore.GeoPoint
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
@Entity(tableName = TABLE_NAME)
data class ProfessionCategory(
    val name: String,
    @PrimaryKey val email: String,
    val experience: String,
    val description: String,
    val latitude: Double?,
    val longitude: Double?,
    val token: String?,
    val image: String? = null
) : Parcelable {
    constructor() : this("", "", "", "", null, null, "")
}




