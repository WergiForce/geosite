package org.wit.geosite.models

import android.net.Uri
import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class GeositeModel(@PrimaryKey(autoGenerate = true) var id: Long = 0,
                        var fbId: String = "",
                        var title: String = "",
                        var description: String = "",
                        var landowner: String = "",
                        var phone: String = "",
                        var drilling: String = "",
                        var comment: String = "",
                        var image: String = "",
                        @Embedded var location : Location = Location()): Parcelable

@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable