package com.visualinnovate.almursheed.commonView.myOrders.view

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.SharedPreference
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.databinding.FragmentMyOrdersBinding
import com.visualinnovate.almursheed.home.MainActivity

class MyOrdersFragment : Fragment() {

    private var _binding: FragmentMyOrdersBinding? = null
    private val binding get() = _binding!!
    private lateinit var userRole :String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMyOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).changeSelectedBottomNavListener(R.id.action_more)
        userRole = SharedPreference.getUserRole()?:""
        initToolbar()
        initView()
        setBtnListener()
        initRecyclerView()
        // call api all here
        handleCallApi()
    }

    private fun handleCallApi() {
    }

    private fun initRecyclerView() {

    }

    private fun initToolbar() {
        binding.appBar.setTitleString(getString(R.string.my_orders))
        binding.appBar.setTitleCenter(false)
        binding.appBar.useBackButton(
            true,
            { findNavController().navigateUp() },
            R.drawable.ic_back,
        )
    }

    private fun initView() {

    }

    private fun setBtnListener() {
        binding.txtAll.onDebouncedListener {
            binding.txtAll.setBackgroundResource(R.drawable.bg_rectangle_corner_primary)
            binding.txtOpen.apply {
                setBackgroundColor(Color.TRANSPARENT)
                setTextColor(resources.getColor(R.color.grey))
            }
            binding.txtClose.apply {
                setBackgroundColor(Color.TRANSPARENT)
                setTextColor(resources.getColor(R.color.grey))
            }
        }
        binding.txtOpen.onDebouncedListener {
            binding.txtOpen.setBackgroundResource(R.drawable.bg_rectangle_corner_primary)
            binding.txtAll.apply {
                setBackgroundColor(Color.TRANSPARENT)
                setTextColor(resources.getColor(R.color.grey))
            }
            binding.txtClose.apply {
                setBackgroundColor(Color.TRANSPARENT)
                setTextColor(resources.getColor(R.color.grey))
            }
        }
        binding.txtClose.onDebouncedListener {
            binding.txtClose.setBackgroundResource(R.drawable.bg_rectangle_corner_primary)
            binding.txtAll.apply {
                setBackgroundColor(Color.TRANSPARENT)
                setTextColor(resources.getColor(R.color.grey))
            }
            binding.txtOpen.apply {
                setBackgroundColor(Color.TRANSPARENT)
                setTextColor(resources.getColor(R.color.grey))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
