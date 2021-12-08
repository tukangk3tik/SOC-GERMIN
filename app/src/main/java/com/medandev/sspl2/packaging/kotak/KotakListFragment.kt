package com.medandev.sspl2.packaging.kotak

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.medandev.sspl2.MainActivity
import com.medandev.sspl2.databinding.FragmentKotakListBinding
import com.medandev.sspl2.network.ApiResponse
import com.medandev.sspl2.model.KotakList
import com.medandev.sspl2.packaging.kotak.detail.KotakDetailActivity
import com.medandev.sspl2.utils.listener.KotakListListener

class KotakListFragment : Fragment(), KotakListListener {

    private val kotakViewModel: KotakListViewModel by activityViewModels()
    private var _binding: FragmentKotakListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentKotakListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).supportActionBar?.title = "Packaging - Kotak"

        val adapter = KotakListAdapter()

        adapter.listener = this
        binding.rvList.layoutManager = LinearLayoutManager(view.context)
        binding.rvList.adapter = adapter

        isLoading(true)
        kotakViewModel.getKotakList()
        kotakViewModel.kotakList.observe(viewLifecycleOwner, { response ->
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
                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    }
                    isLoading(false)
                    isEmpty(true)
                }
                is ApiResponse.Loading -> isLoading(true)
            }
        })

        binding.srList.setOnRefreshListener {
            kotakViewModel.getKotakList()
        }

    }

    override fun onItemClick(view: View, kotakData: KotakList) {
        val mIntent = Intent(activity, KotakDetailActivity::class.java)
        mIntent.putExtra(KotakDetailActivity.ID_PACKAGING, kotakData.id.toString())
        startActivity(mIntent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun isEmpty(state: Boolean) {
        if (state) {
            binding.labelDaftar.visibility = View.GONE
            binding.rvList.visibility = View.GONE
            binding.labelDataKosong.visibility = View.VISIBLE
        } else {
            binding.labelDaftar.visibility = View.VISIBLE
            binding.rvList.visibility = View.VISIBLE
            binding.labelDataKosong.visibility = View.GONE
        }
    }

    private fun isLoading(state: Boolean) {
        if (state) {
            binding.srList.isRefreshing = true
            binding.rvList.visibility = View.GONE
            binding.labelDataKosong.visibility = View.GONE
        } else {
            binding.srList.isRefreshing = false
        }
    }
}