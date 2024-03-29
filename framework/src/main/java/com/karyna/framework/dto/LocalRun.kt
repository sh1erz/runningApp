package com.karyna.framework.dto

import androidx.room.*
import com.google.gson.annotations.SerializedName
import com.karyna.core.domain.LatLng
import com.karyna.core.domain.LocationShort

@Entity(
    tableName = "run",
    foreignKeys = [ForeignKey(
        entity = LocalUser::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class LocalRun(
    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    val id: String,
    @ColumnInfo(name = "date")
    @SerializedName("date")
    val date: String,
    @Embedded
    @SerializedName("location")
    val location: LocationShort,
    @ColumnInfo(name = "userId", index = true)
    @SerializedName("userId")
    val userId: String,
    @ColumnInfo(name = "userName")
    @SerializedName("userName")
    val userName: String,
    @ColumnInfo(name = "userAvatarUrl")
    @SerializedName("userAvatarUrl")
    val userAvatarUrl: String,
    @ColumnInfo(name = "coordinates")
    @SerializedName("coordinates")
    val coordinates: ArrayList<LatLng>,
    @ColumnInfo(name = "durationS")
    @SerializedName("durationS")
    val durationS: Long,
    @ColumnInfo(name = "distance")
    @SerializedName("distance")
    val distanceMeters: Int,
    @ColumnInfo(name = "pace")
    @SerializedName("pace")
    val paceMetersInS: Float,
    @ColumnInfo(name = "calories")
    @SerializedName("calories")
    val calories: Int?
)