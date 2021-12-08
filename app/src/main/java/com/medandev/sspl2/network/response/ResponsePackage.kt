package com.medandev.sspl2.network.response

import com.google.gson.JsonArray

data class ResponsePackage(
    val result: Int,
    val message: String,
    val data: JsonArray
)
