package com.karyna.framework.dto

data class RemoteLocation(
    val results: List<Result>
)

data class Result(
    val address_components: List<AddressComponent>,
    val types: List<String>
)

data class AddressComponent(
    val long_name: String,
    val short_name: String,
    val types: List<String>
)