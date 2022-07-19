package org.wit.houses.models

import android.net.Uri
import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class HouseModel(@PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var address: String = "",
    var listPrice: String = "",
    var bedrooms: String = "",
    var bathrooms: String = "",
    var description: String = "",
    var soldPrice: String = "",
    var listDate: String = "",
    var soldDate: String = "",
    var image: Uri = Uri.EMPTY,
    @Embedded var location : Location = Location()
) : Parcelable

@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable