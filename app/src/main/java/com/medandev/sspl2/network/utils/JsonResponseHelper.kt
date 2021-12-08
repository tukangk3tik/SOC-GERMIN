package com.medandev.sspl2.network.utils

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import com.medandev.sspl2.model.KotakDetailChildList
import com.medandev.sspl2.model.KotakDetailData
import com.medandev.sspl2.network.response.ResponsePackage
import com.medandev.sspl2.model.KotakList

object JsonResponseHelper {

    fun getResponse(responseObject: JsonObject): ResponsePackage? {
        var response: ResponsePackage? = null

        try {
            val result = responseObject.get("result").asInt
            val message = responseObject.get("message").asString

            var data = JsonArray()
            if (responseObject.has("data")) {
                val checkAsArray = responseObject.get("data").isJsonArray

                data = if (checkAsArray) {
                    responseObject.get("data").asJsonArray
                } else {
                    val jsonObject = responseObject.get("data").asJsonObject
                    val jsonArray = JsonArray()
                    jsonArray.add(jsonObject)

                    jsonArray
                }
            }

            response = ResponsePackage(result, message, data)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return response
    }


    fun kotakListMapper(response: JsonArray): ArrayList<KotakList> {
        val result = ArrayList<KotakList>()

        try {
            if (response.size() > 0) {
                for (i in 0 until response.size()) {
                    val data = response[i].asJsonObject

                    val id = data.get("id").asInt
                    val noKotak = data.get("noKotak").asString
                    val jlhKantong = data.get("jumlah_kantong").asString
                    val jlhButir = data.get("jumlah_butir").asString

                    val packaging = KotakList(id, noKotak, jlhKantong, jlhButir)
                    result.add(packaging)
                }
            }
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
        }

        return result
    }

    fun kotakChildListMapper(response: JsonArray): ArrayList<KotakDetailChildList> {
        val result = ArrayList<KotakDetailChildList>()

        try {
            if (response.size() > 0) {
                for (i in 0 until response.size()) {
                    val data = response[i].asJsonObject

                    val id = data.get("id").asInt
                    val noKutip = data.get("noKutip").asString
                    val noSeal = data.get("noSeal").asString
                    val kategori = data.get("kategori").asString
                    val jlhButir = data.get("jumlahButir").asInt

                    val detail = KotakDetailChildList(id, noKutip, noSeal, kategori, jlhButir)
                    result.add(detail)
                }
            }
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
        }

        return result
    }


    fun kotakDetailDataMapper(response: JsonArray): KotakDetailData? {
        var result: KotakDetailData? = null

        try {
            if (response.size() > 0) {
                val data = response[0].asJsonObject

                val id = data.get("id").asInt
                val noKotak = data.get("noKotak").asString
                val customer = data.get("customer").asString
                val tglKirimStr = data.get("tgl_kirim_str").asString
                val tglKirimDate = data.get("tgl_kirim_date").asString
                val jlhButir = data.get("jumlah_butir").asInt
                val jlhKantong = data.get("jumlah_kantong").asInt

                val katLm = data.get("kat_lm").asInt
                val katYa = data.get("kat_ya").asInt
                val katDpmtg = data.get("kat_dpmtg").asInt

                result = KotakDetailData(
                    id,
                    noKotak,
                    customer,
                    tglKirimStr,
                    tglKirimDate,
                    jlhKantong,
                    jlhButir,
                    katLm,
                    katYa,
                    katDpmtg
                )
            }
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
        }

        return result
    }


}