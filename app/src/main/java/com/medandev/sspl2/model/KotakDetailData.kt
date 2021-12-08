package com.medandev.sspl2.model

data class KotakDetailData (
    val id : Int,
    val noKotak : String,
    val customer: String,
    val tglKirimStr: String,
    val tglKirimDate: String,
    val jlhKantong: Int,
    val jlhButir: Int,
    val katLm: Int,
    val katYa: Int,
    val katDpmtg: Int
)