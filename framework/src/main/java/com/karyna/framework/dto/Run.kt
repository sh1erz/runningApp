package com.karyna.framework.dto

import androidx.room.*
import com.google.gson.annotations.SerializedName
import com.karyna.core.domain.LatLng
import com.karyna.core.domain.LocationShort

@Entity(
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Run(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @SerializedName("id")
    val id: Long,
    @ColumnInfo(name = "date")
    @SerializedName("date")
    val date: String,
    @Embedded
    @SerializedName("location")
    val location: LocationShort,
    @ColumnInfo(name = "userId", index = true)
    @SerializedName("userId")
    val userId: String,
    @ColumnInfo(name = "coordinates")
    @SerializedName("coordinates")
    val coordinates: ArrayList<LatLng>,
    @ColumnInfo(name = "durationMs")
    @SerializedName("durationMs")
    val durationS: Long,
    @ColumnInfo(name = "distance")
    @SerializedName("distance")
    val distanceMeters: Int,
    @ColumnInfo(name = "pace")
    @SerializedName("pace")
    val paceMetersInS: Int,
    @ColumnInfo(name = "calories")
    @SerializedName("calories")
    val calories: Int?
)