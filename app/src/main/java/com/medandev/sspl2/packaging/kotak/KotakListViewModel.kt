package com.medandev.sspl2.packaging.kotak

import android.util.Log.d
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.disdukcapilmdn.lampid.utils.general.EventLiveData
import com.disdukcapilmdn.lampid.utils.general.HandlerLiveData
import com.medandev.sspl2.model.KotakDetailChildList
import com.medandev.sspl2.model.KotakDetailData
import com.medandev.sspl2.network.ApiConfig
import com.medandev.sspl2.network.ApiResponse
import com.medandev.sspl2.network.utils.JsonResponseHelper
import com.medandev.sspl2.model.KotakList
import kotlinx.coroutines.launch
import java.io.IOException

class KotakListViewModel: ViewModel() {

    private val _kotakList = MutableLiveData<ApiResponse<List<KotakList>>>()
    val kotakList: LiveData<ApiResponse<List<KotakList>>> = _kotakList

    private val apiConfig = ApiConfig.provideApiService()

    fun getKotakList() = viewModelScope.launch {
        _kotakList.postValue(ApiResponse.Loading())

        val response = apiConfig.getKotakList()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                val responsePackage = JsonResponseHelper.getResponse(body)
                val responseData = responsePackage?.data

                if (responseData != null) {
                    val data = JsonResponseHelper.kotakListMapper(responsePackage.data)
                    _kotakList.postValue(ApiResponse.Success(data))
                }
            }

        } else {
            _kotakList.postValue(ApiResponse.Error("Gagal mengambil data"))
        }
    }

    private val _detailChildList = MutableLiveData<ApiResponse<List<KotakDetailChildList>>>()
    val detailChildList: LiveData<ApiResponse<List<KotakDetailChildList>>> = _detailChildList

    fun getDetailKotakChildList(idKotak: String) = viewModelScope.launch {
        _detailChildList.postValue(ApiResponse.Loading())

        val response = apiConfig.getKotakDetailChildList(idKotak = idKotak)
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                val responsePackage = JsonResponseHelper.getResponse(body)
                val responseData = responsePackage?.data

                if (responseData != null) {
                    val data = JsonResponseHelper.kotakChildListMapper(responsePackage.data)
                    _detailChildList.postValue(ApiResponse.Success(data))
                }
            }

        } else {
            _detailChildList.postValue(ApiResponse.Error("Gagal mengambil data"))
        }
    }

    private val _detailKotak = MutableLiveData<ApiResponse<KotakDetailData?>>()
    val detailKotak: LiveData<ApiResponse<KotakDetailData?>> = _detailKotak

    fun getDetailDataKotak(idKotak: String) = viewModelScope.launch {
        _detailKotak.postValue(ApiResponse.Loading())

        val response = apiConfig.getKotakDetail(idKotak = idKotak)
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                val responsePackage = JsonResponseHelper.getResponse(body)
                val responseData = responsePackage?.data

                if (responseData != null) {
                    val data = JsonResponseHelper.kotakDetailDataMapper(responsePackage.data)
                    _detailKotak.postValue(ApiResponse.Success(data))
                }
            }

        } else {
            _detailKotak.postValue(ApiResponse.Error("Gagal mengambil data"))
        }
    }


    private val _dialogHandler = MutableLiveData<EventLiveData<HandlerLiveData>>()
    val dialogHandler: LiveData<EventLiveData<HandlerLiveData>> = _dialogHandler

    fun tambahKotakDetail(noKutip: String, tglKirim: String, idKotak: String, idPegawai: String) = viewModelScope.launch {

        try {
            val response = apiConfig.insertKotakDetail(noKutip = noKutip, tglKirim = tglKirim, idKotak = idKotak, idPegawai = idPegawai)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    val responsePackage = JsonResponseHelper.getResponse(body)

                    if (responsePackage != null) {
                        _dialogHandler.value = EventLiveData(HandlerLiveData(responsePackage.result,
                            responsePackage.message))

                    }
                }

            } else {
                _dialogHandler.value = EventLiveData(HandlerLiveData(0,
                    "Gagal mengambil update, periksa koneksi"))
            }
        } catch (e: IOException) {
            _dialogHandler.value = EventLiveData(HandlerLiveData(0, e.toString()))
        }
    }

    fun hapusKotakDetail(idDetail: String, idPegawai: String) = viewModelScope.launch {

        try {
            val response = apiConfig.deleteKotakDetail(idDetail = idDetail, idPegawai = idPegawai)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    val responsePackage = JsonResponseHelper.getResponse(body)

                    if (responsePackage != null) {
                        _dialogHandler.value = EventLiveData(HandlerLiveData(responsePackage.result,
                            responsePackage.message))

                    }
                }

            } else {
                _dialogHandler.value = EventLiveData(HandlerLiveData(0,
                    "Gagal update data, periksa koneksi"))
            }
        } catch (e: IOException) {
            _dialogHandler.value = EventLiveData(HandlerLiveData(0, e.toString()))
        }
    }

}