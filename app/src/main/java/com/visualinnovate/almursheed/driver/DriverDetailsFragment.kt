package com.visualinnovate.almursheed.driver

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.base.BaseFragment
import com.visualinnovate.almursheed.common.toast
import com.visualinnovate.almursheed.databinding.FragmentDriverDetailsBinding
import com.visualinnovate.almursheed.home.model.DriverAndGuideItem
import com.visualinnovate.almursheed.home.viewmodel.HomeViewModel
import com.visualinnovate.almursheed.utils.Constant
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DriverDetailsFragment : BaseFragment() {

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
                    showMainLoading()
                }

                is ResponseHandler.StopLoading -> {
                    // show a progress bar
                    hideMainLoading()
                }

                else -> {}
            }
        }
    }

    private fun initViews(driver: DriverAndGuideItem?) {
        Glide.with(requireContext())
            .load(driver?.personalPhoto)
            .into(binding.imgDriver)

        Glide.with(requireContext())
            .load(driver?.carPhoto!![0])
            .into(binding.imgCar)
        binding.driverName.text = driver?.name ?: ""
        binding.driverCountry.text = driver?.country ?: ""
        binding.driverCity.text = driver?.state ?: ""
        binding.driverDescription.text = driver?.bio ?: ""
        binding.carName.text = driver?.carModel ?: ""
        binding.carType.text = driver?.carType ?: ""
        // binding.driverPrice.text = "$ ${d?.driverPrice}"
        // binding.driverCity.text = driverArgs?.driverCity ?: ""
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
