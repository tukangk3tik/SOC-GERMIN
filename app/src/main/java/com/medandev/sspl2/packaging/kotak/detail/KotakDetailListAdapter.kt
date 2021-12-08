package com.medandev.sspl2.packaging.kotak.detail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.medandev.sspl2.R
import com.medandev.sspl2.databinding.ItemListKotakDetailBinding
import com.medandev.sspl2.model.KotakDetailChildList
import com.medandev.sspl2.utils.listener.KotakDetailListListener

class KotakDetailListAdapter : RecyclerView.Adapter<KotakDetailListAdapter.ViewHolder>() {

    private val listPackaging = ArrayList<KotakDetailChildList>()
    var listener: KotakDetailListListener? = null

    inner class ViewHolder(private val binding: ItemListKotakDetailBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(singleData: KotakDetailChildList, position: Int) {

            with(binding) {

                noKutip.text = itemView.resources.getString(R.string.no_kutip, singleData.noKutip)
                noSeal.text = itemView.resources.getString(R.string.no_seal, singleData.noSeal)
                kategori.text = itemView.resources.getString(R.string.kategori, singleData.kategori)
                jumlahButir.text = itemView.resources.getString(R.string.jumlah_butir, singleData.jlhButir.toString())

                btnHapus.setOnClickListener {
                    listener?.onItemDeleted(itemView, singleData, position)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListKotakDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listPackaging[position], position)
    }

    override fun getItemCount(): Int = listPackaging.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(childListData: List<KotakDetailChildList>){
        listPackaging.clear()
        listPackaging.addAll(childListData)
        notifyDataSetChanged()
    }

}