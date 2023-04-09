package com.karyna.core.domain

data class User(
    val id: String,
    val email: String,
    val name: String,
    val avatarUrl: String,
    val weight: String?
)
