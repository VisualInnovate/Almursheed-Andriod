package com.visualinnovate.almursheed.tourist.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.toast
import com.visualinnovate.almursheed.databinding.FragmentLocationDetailsBinding
import com.visualinnovate.almursheed.home.model.Attracive
import com.visualinnovate.almursheed.home.viewmodel.HomeViewModel
import com.visualinnovate.almursheed.utils.Constant
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationDetailsFragment : Fragment() {

    private var _binding: FragmentLocationDetailsBinding? = null

    // This property is only valid between onCreateView and // onDestroy.
    private val binding get() = _binding!!

    private val vm: HomeViewModel by viewModels()

    private var locationId: Int? = null

    private val btnBackCallBackFunc: () -> Unit = {
        findNavController().navigateUp()
    }

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

    override fun onStart() {
        super.onStart()
        vm.getAttractivesDetailsById(locationId)
    }

    private fun subscribeData() {
        vm.attractivesDetailsLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    // bind data to the view
                    initView(it.data!!.attracive)
                }
                is ResponseHandler.Error -> {
                    // show error message
                    toast(it.message)
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

    private fun initToolbar() {
        binding.appBarLocationDetails.setTitleString(getString(R.string.location_details))
        binding.appBarLocationDetails.setTitleCenter(true)
        binding.appBarLocationDetails.useBackButton(
            true,
            btnBackCallBackFunc,
            R.drawable.ic_back
        )
    }

    private fun initView(attracive: Attracive?) {
        Glide.with(requireContext())
            .load(attracive?.media?.get(0)?.originalUrl)
            .into(binding.detailsImage)
        binding.detailsDescription.text = attracive?.name?.gb
        binding.detailsDescription.text = attracive?.description?.gb
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
