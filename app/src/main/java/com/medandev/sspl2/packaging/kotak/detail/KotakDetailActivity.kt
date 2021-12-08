package com.medandev.sspl2.packaging.kotak.detail

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log.d
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.medandev.sspl2.LoginActivity
import com.medandev.sspl2.R
import com.medandev.sspl2.databinding.ActivityPackagingDetailBinding
import com.medandev.sspl2.databinding.ProgressLayoutDarkBinding
import com.medandev.sspl2.model.KotakDetailChildList
import com.medandev.sspl2.model.KotakDetailData
import com.medandev.sspl2.network.ApiResponse
import com.medandev.sspl2.packaging.kotak.KotakListViewModel
import com.medandev.sspl2.utils.listener.KotakDetailListListener

class KotakDetailActivity : AppCompatActivity(), KotakDetailListListener {

    private lateinit var binding: ActivityPackagingDetailBinding
    private lateinit var progressLayoutBinding: ProgressLayoutDarkBinding
    private lateinit var sharedPreferences: SharedPreferences

    private val kotakViewModel: KotakListViewModel by viewModels()
    private val adapter = KotakDetailListAdapter()

    private var dataId: String? = null
    private var tglKirim: String? = null
    private var idUser = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPackagingDetailBinding.inflate(layoutInflater)
        progressLayoutBinding = binding.progressLayout
        setContentView(binding.root)

        actionBar?.setHomeButtonEnabled(true);
        actionBar?.setDisplayHomeAsUpEnabled(true);

        dataId = intent.getStringExtra(ID_PACKAGING)

        kotakViewModel.getDetailDataKotak(dataId.toString())
        kotakViewModel.detailKotak.observe(this, detailObserver)

        adapter.listener = this
        binding.rvList.layoutManager = LinearLayoutManager(this)
        binding.rvList.adapter = adapter

        kotakViewModel.getDetailKotakChildList(dataId.toString())
        kotakViewModel.detailChildList.observe(this, childObserver)

        binding.srList.setOnRefreshListener {
            kotakViewModel.getDetailKotakChildList(dataId.toString())
        }

        binding.btnTambah.setOnClickListener {
            val mIntent = Intent(this, ScanBarcodeActivity::class.java)
            mIntent.putExtra(ScanBarcodeActivity.TITLE, "Scan No. Kutipan")
            resultLauncher.launch(mIntent)
        }

        kotakViewModel.dialogHandler.observe(this, {
            it.getContentIfNotHandled()?.let { handler ->
                isLoadingAction(false)
                if (handler.responseResult > 0) {
                    isLoading(true)
                    kotakViewModel.getDetailDataKotak(dataId.toString())
                    kotakViewModel.getDetailKotakChildList(dataId.toString())
                } else {
                    showAlertDialog(handler.responseResult, handler.responseMessage, this)
                }
            }
        })


        sharedPreferences = getSharedPreferences(LoginActivity.my_shared_preferences, MODE_PRIVATE)
        idUser = sharedPreferences.getString(LoginActivity.TAG_ID, null).toString()
    }

    override fun onItemDeleted(view: View, data: KotakDetailChildList, position: Int) {
        showConfirmDelete(data.id.toString(), this)
    }

    fun isEmpty(state: Boolean) {
        if (state) {
            //binding.labelDaftar.visibility = View.GONE
            binding.rvList.visibility = View.GONE
            //binding.labelDataKosong.visibility = View.VISIBLE
        } else {
            //binding.labelDaftar.visibility = View.VISIBLE
            binding.rvList.visibility = View.VISIBLE
            //binding.labelDataKosong.visibility = View.GONE
        }
    }

    private fun isLoading(state: Boolean) {
        if (state) {
            binding.srList.isRefreshing = true
            binding.rvList.visibility = View.GONE
            //binding.labelDataKosong.visibility = View.GONE
        } else {
            binding.srList.isRefreshing = false
        }
    }

    private val detailObserver = Observer<ApiResponse<KotakDetailData?>> { response ->
        when (response) {
            is ApiResponse.Success -> {
                response.data?.let {
                    with(binding) {
                        tvNoKotak.text = resources.getString(R.string.no_kotak, it.noKotak)
                        tvCustomer.text = resources.getString(R.string.customer, it.customer)
                        tvTglKirim.text = resources.getString(R.string.tgl_kirim, it.tglKirimStr)
                        tvJlhKantong.text = resources.getString(R.string.jumlah_kantong, it.jlhKantong.toString())
                        tvLm.text = resources.getString(R.string.lm_str, it.katLm.toString())
                        tvYa.text = resources.getString(R.string.ya_str, it.katYa.toString())
                        tvDpmtg.text = resources.getString(R.string.dpmtg, it.katDpmtg.toString())

                        tglKirim = it.tglKirimDate
                    }
                }
                //isLoading(false)
            }
            is ApiResponse.Error -> {
                response.message?.let {
                    Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                }
            }
            is ApiResponse.Loading -> isLoading(true)
        }
    }

    private val childObserver = Observer<ApiResponse<List<KotakDetailChildList>>> { response ->
        when (response) {
            is ApiResponse.Success -> {
                response.data?.let {
                    if (it.isNotEmpty()){
                        adapter.setData(it)
                        isEmpty(false)
                    } else {
                        isEmpty(true)
                    }
                }
                isLoading(false)
            }
            is ApiResponse.Error -> {
                response.message?.let {
                    Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                }
                isLoading(false)
                isEmpty(true)
            }
            is ApiResponse.Loading -> isLoading(true)
        }
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data

            if (data != null) {
                val noKutipScan = data.getStringExtra("strBarcode")

                if (noKutipScan != null
                    && dataId != null
                    && tglKirim != null
                ) {
                    kotakViewModel.tambahKotakDetail(noKutipScan, tglKirim.toString(), dataId.toString(), idUser)
                    isLoadingAction(true)
                }
            }
        }
    }

    private fun showAlertDialog(result: Int, msg: String, context: Context) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        val dialogTitle = "Informasi"

        alertDialogBuilder.setTitle(dialogTitle)
        alertDialogBuilder
            .setMessage(msg)
            .setCancelable(false)
            .setPositiveButton("Oke") { dialog, id ->
                dialog.cancel()
            }

        val alertDialog = alertDialogBuilder.create()

        isLoadingAction(false)
        alertDialog.show()
    }

    private fun showConfirmDelete(idDetail: String, context: Context) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        val dialogTitle = "Hapus"

        alertDialogBuilder.setTitle(dialogTitle)
        alertDialogBuilder
            .setMessage("Yakin hapus laporan?")
            .setCancelable(true)
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.cancel()
            }
            .setPositiveButton("Hapus") { dialog, id ->
                kotakViewModel.hapusKotakDetail(idDetail, idUser)
                isLoadingAction(true)
                dialog.cancel()
            }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun isLoadingAction(state: Boolean) {
        if(state) {
            progressLayoutBinding.root.visibility = View.VISIBLE
        } else {
            progressLayoutBinding.root.visibility = View.GONE
        }
    }

    companion object {
        const val ID_PACKAGING = "id_packaging"
    }

}