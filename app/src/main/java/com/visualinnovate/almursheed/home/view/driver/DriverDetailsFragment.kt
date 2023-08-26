package com.visualinnovate.almursheed.home.view.driver

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.Constant
import com.visualinnovate.almursheed.databinding.FragmentDriverDetailsBinding
import com.visualinnovate.almursheed.home.model.DriverModel

class DriverDetailsFragment : Fragment() {

    private var _binding: FragmentDriverDetailsBinding? = null
    private val binding get() = _binding!!

    private var driverArgs: DriverModel? = null

    private val btnBackCallBackFunc: () -> Unit = {
        findNavController().navigateUp()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDriverDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        driverArgs = requireArguments().getParcelable(Constant.DRIVER)
        initToolbar()
        initViews()
    }

    private fun initToolbar() {
        binding.appBarDriverDetails.setTitleString(getString(R.string.driver_details))
        binding.appBarDriverDetails.setTitleCenter(true)
        binding.appBarDriverDetails.useBackButton(
            true,
            btnBackCallBackFunc,
            R.drawable.ic_back
        )
    }

    @SuppressLint("SetTextI18n")
    private fun initViews() {
        binding.imgDriver.setImageResource(driverArgs?.driverImage ?: 0)
        binding.driverName.text = driverArgs?.driverName ?: ""
        binding.driverPrice.text = "$ ${driverArgs?.driverPrice}"
        binding.driverCity.text = driverArgs?.driverCity ?: ""
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
