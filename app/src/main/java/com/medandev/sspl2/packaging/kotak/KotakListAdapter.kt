package com.medandev.sspl2.packaging.kotak

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.medandev.sspl2.R
import com.medandev.sspl2.databinding.ItemListKotakBinding
import com.medandev.sspl2.model.KotakList
import com.medandev.sspl2.utils.listener.KotakListListener

class KotakListAdapter : RecyclerView.Adapter<KotakListAdapter.ViewHolder>() {

    private val listPackaging = ArrayList<KotakList>()
    var listener: KotakListListener? = null

    inner class ViewHolder(private val binding: ItemListKotakBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(singleData: KotakList) {

            with(binding) {

                noKotak.text = singleData.noKotak
                jumlahKantong.text = itemView.resources.getString(R.string.jumlah_kantong, singleData.jlhKantong)
                jumlahButir.text = itemView.resources.getString(R.string.jumlah_butir, singleData.jlhButir)

                itemView.setOnClickListener {
                    listener?.onItemClick(itemView, singleData)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListKotakBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listPackaging[position])
    }

    override fun getItemCount(): Int = listPackaging.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(listData: List<KotakList>){
        listPackaging.clear()
        listPackaging.addAll(listData)
        notifyDataSetChanged()
    }

}