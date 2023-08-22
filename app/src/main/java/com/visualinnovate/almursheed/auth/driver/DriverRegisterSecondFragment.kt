package com.visualinnovate.almursheed.auth.driver

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.databinding.FragmentDriverRegisterSecondBinding

class DriverRegisterSecondFragment : Fragment() {

    private lateinit var binding: FragmentDriverRegisterSecondBinding

    private val btnBackCallBackFunc: () -> Unit = {
        findNavController().navigateUp()
    }

    private val btnLoginCallBackListener: () -> Unit = {
        Log.d("btnLoginCallBackListener", "btnLoginCallBackListener")
        // navigate to login
        findNavController().navigate(R.id.action_driverRegisterSecond_to_login)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDriverRegisterSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initToolbar()
    }

    private fun initViews() {
        binding.spinnerCarType.spinnerText.setText(getString(R.string.select_car_type))
        binding.spinnerCarBrandName.spinnerText.setText(getString(R.string.select_car_brand_name))
        binding.spinnerCarManufacturingDate.spinnerText.setText(getString(R.string.select_car_manufacturing_date))
        binding.spinnerLanguage.spinnerText.setText(getString(R.string.select_language))
    }

    private fun initToolbar() {
        binding.appBarDriverRegisterSecond.setTitleString(getString(R.string.driver_register_second))
        binding.appBarDriverRegisterSecond.setTitleCenter(true)
        binding.appBarDriverRegisterSecond.useBackButton(
            true,
            btnBackCallBackFunc,
            R.drawable.ic_back
        )
        binding.appBarDriverRegisterSecond.showButtonOneWithoutImage(
            getString(R.string.login),
            btnLoginCallBackListener
        )
    }
}
