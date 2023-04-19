package com.karyna.framework.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "user")
data class LocalUser(
    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    val id: String,
    @ColumnInfo(name = "email")
    @SerializedName("email")
    val email: String,
    @ColumnInfo(name = "name")
    @SerializedName("name")
    val name: String,
    @ColumnInfo(name = "avatarUrl")
    @SerializedName("avatarUrl")
    val avatarUrl: String,
    @ColumnInfo(name = "weight")
    @SerializedName("weight")
    val weight: Float?
)