package com.karyna.feature.core.utils.base

data class SnackBarInfo(
    val message: String,
    val isPositive: Boolean = false,
    val lengthLong: Boolean = false,
    val actionTitle: String = "Action",
    val action: (() -> Unit)? = null
)