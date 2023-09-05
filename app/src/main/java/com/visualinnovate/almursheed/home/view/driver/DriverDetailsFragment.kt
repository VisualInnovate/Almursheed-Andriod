package com.visualinnovate.almursheed.home.view.driver

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.toast
import com.visualinnovate.almursheed.databinding.FragmentDriverDetailsBinding
import com.visualinnovate.almursheed.home.model.Driver
import com.visualinnovate.almursheed.home.viewmodel.HomeViewModel
import com.visualinnovate.almursheed.utils.Constant
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DriverDetailsFragment : Fragment() {

    private var _binding: FragmentDriverDetailsBinding? = null
    private val binding get() = _binding!!

    private val vm: HomeViewModel by viewModels()

    private var driverId: Int? = null

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
        driverId = requireArguments().getInt(Constant.DRIVER_ID)
        initToolbar()
        subscribeData()
    }

    override fun onStart() {
        super.onStart()
        vm.getDriverDetailsById(driverId)
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

    private fun subscribeData() {
        // observe in drivers list
        vm.driverDetailsLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    // bind data to the view
                    initViews(it.data!!.driver)
                }
                is ResponseHandler.Error -> {
                    // show error message
                    toast(it.message)
                    Log.d("Error->DriverList", it.message)
                }
                is ResponseHandler.Loading -> {
                    // show a progress bar
                }
                else -> {
                    toast("Else")
                }
            }
        }
    }

    private fun initViews(driver: Driver?) {
//        Glide.with(requireContext())
//            .load(driver.media.get(0)personalPictures?.get(position)?.originalUrl)
//            .into(binding.imgDriver)
        binding.imgDriver.setImageResource(R.drawable.img_driver)
        binding.driverName.text = driver?.name ?: ""
        // binding.driverPrice.text = "$ ${d?.driverPrice}"
        // binding.driverCity.text = driverArgs?.driverCity ?: ""
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
