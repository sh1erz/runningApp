package com.karyna.framework.dto

data class RemoteUser(
    val id: String = "",
    val email: String = "",
    val name: String = "",
    val avatarUrl: String = "",
    val weight: Float? = 0f
)