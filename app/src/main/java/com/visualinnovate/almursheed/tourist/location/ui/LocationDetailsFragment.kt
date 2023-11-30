package com.visualinnovate.almursheed.tourist.location.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.base.BaseFragment
import com.visualinnovate.almursheed.common.toast
import com.visualinnovate.almursheed.databinding.FragmentLocationDetailsBinding
import com.visualinnovate.almursheed.home.model.AttractiveLocation
import com.visualinnovate.almursheed.home.viewmodel.HomeViewModel
import com.visualinnovate.almursheed.tourist.location.viewmodel.LocationViewModel
import com.visualinnovate.almursheed.utils.Constant
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationDetailsFragment : BaseFragment() {

    private var _binding: FragmentLocationDetailsBinding? = null

    // This property is only valid between onCreateView and // onDestroy.
    private val binding get() = _binding!!

    private val vm: LocationViewModel by viewModels()

    private var locationId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        locationId = requireArguments().getInt(Constant.LOCATION_DETAILS)
        initToolbar()
        setBtnListener()
        subscribeData()
    }

    private fun initToolbar() {
        binding.appBarLocationDetails.setTitleString(getString(R.string.location_details))
        binding.appBarLocationDetails.setTitleCenter(true)
        binding.appBarLocationDetails.useBackButton(
            true,
            { findNavController().navigateUp() },
            R.drawable.ic_back
        )
    }

    override fun onStart() {
        super.onStart()
        vm.getAttractiveDetailsById(locationId)
    }

    private fun subscribeData() {
        vm.attractiveDetailsLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    // bind data to the view
                    initView(it.data!!.attractiveLocation)
                }

                is ResponseHandler.Error -> {
                    // show error message
                    toast(it.message)
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

    private fun initView(attractive: AttractiveLocation?) {
        Glide.with(requireContext())
            .load(attractive?.photos?.get(0))
            .error(R.drawable.ic_mursheed_logo)
            .into(binding.detailsImage)

        binding.pinDetails.text = attractive?.name?.localized
        binding.detailsCountry.text = attractive?.country
        binding.detailsCity.text = attractive?.state
        binding.detailsDescription.text = attractive?.description?.localized
    }

    private fun setBtnListener() {
        binding.locationLink.setOnClickListener { }
        binding.imgLocationMaps.setOnClickListener { }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
