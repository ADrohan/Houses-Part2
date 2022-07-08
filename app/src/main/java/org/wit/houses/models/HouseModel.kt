package org.wit.houses.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HouseModel(
    var id: Long = 0,
    var address: String = "",
    var listPrice: Int? = null,
    var bedrooms: Int? = null,
    var bathrooms: Int? = null,
    var description: String = "",
    var soldPrice: Int? = null,
    var listDate: String = "",
    var soldDate: String = "",
    var image: Uri = Uri.EMPTY,
    var lat : Double = 0.0,
    var lng: Double = 0.0,
    var zoom: Float = 0f
) : Parcelable

@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable