package com.medandev.sspl2.network

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {

    //LIST KOTAK
    @Headers("Accept: application/json")
    @POST("packaging/model.php")
    @FormUrlEncoded
    suspend fun getKotakList(
        @Field("request") request: String = "list_kotak"
    ) : Response<JsonObject>

    //LIST KOTAK DETAIL
    @Headers("Accept: application/json")
    @POST("packaging/model.php")
    @FormUrlEncoded
    suspend fun getKotakDetailChildList(
        @Field("request") request: String = "list_detail_child_kotak",
        @Field("id_kotak") idKotak: String
    ) : Response<JsonObject>

    //KOTAK DETAIL
    @Headers("Accept: application/json")
    @POST("packaging/model.php")
    @FormUrlEncoded
    suspend fun getKotakDetail(
        @Field("request") request: String = "list_detail_data_kotak",
        @Field("id_kotak") idKotak: String
    ) : Response<JsonObject>


    //INSERT KUTIPAN
    @Headers("Accept: application/json")
    @POST("packaging/model.php")
    @FormUrlEncoded
    suspend fun insertKotakDetail(
        @Field("request") request: String = "insert_kotak_detail",
        @Field("no_kutip") noKutip: String,
        @Field("tgl_kirim") tglKirim: String,
        @Field("id_kotak") idKotak: String,
        @Field("id_pegawai") idPegawai: String,
    ) : Response<JsonObject>


    //DELETED KUTIPAN
    @Headers("Accept: application/json")
    @POST("packaging/model.php")
    @FormUrlEncoded
    suspend fun deleteKotakDetail(
        @Field("request") request: String = "delete_kotak_detail",
        @Field("id_detail") idDetail: String,
        @Field("id_pegawai") idPegawai: String,
    ) : Response<JsonObject>

}