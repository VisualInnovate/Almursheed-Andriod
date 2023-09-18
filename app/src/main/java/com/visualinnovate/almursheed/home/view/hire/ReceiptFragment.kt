package com.visualinnovate.almursheed.home.view.hire

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.base.BaseFragment
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.common.toast
import com.visualinnovate.almursheed.databinding.FragmentReceiptBinding
import com.visualinnovate.almursheed.home.viewmodel.HireViewModel
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReceiptFragment : BaseFragment() {

    private var _binding: FragmentReceiptBinding? = null
    private val binding get() = _binding!!

    private val vm: HireViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentReceiptBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        setBtnListener()
        initView()
        subscribeData()
    }

    private fun initToolbar() {
        binding.appBarReceipt.setTitleString(getString(R.string.receipt))
        binding.appBarReceipt.setTitleCenter(true)
        binding.appBarReceipt.useBackButton(
            true,
            { findNavController().navigateUp() },
            R.drawable.ic_back,
        )
    }

    private fun initView() {}

    private fun setBtnListener() {
        binding.btnHire.onDebouncedListener {}
    }

    private fun subscribeData() {
        /*vm.createOrderLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    // save user
                    toast(it.data?.message.toString())
                    // findNavController().navigateUp()
                }

                is ResponseHandler.Error -> {
                    // show error message
                    toast(it.message)
                    Log.d("ResponseHandler.Error", it.message)
                }

                is ResponseHandler.Loading -> {
                    // show a progress bar
                    showMainLoading()
                }

                is ResponseHandler.StopLoading -> {
                    // show a progress bar
                    hideMainLoading()
                }

                else -> {}
            }
        }*/
    }

}