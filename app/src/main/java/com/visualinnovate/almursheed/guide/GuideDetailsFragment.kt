package com.visualinnovate.almursheed.guide

import android.annotation.SuppressLint
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
import com.visualinnovate.almursheed.databinding.FragmentGuideDetailsBinding
import com.visualinnovate.almursheed.home.model.GuideItem
import com.visualinnovate.almursheed.home.viewmodel.HomeViewModel
import com.visualinnovate.almursheed.utils.Constant
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GuideDetailsFragment : BaseFragment() {

    private var _binding: FragmentGuideDetailsBinding? = null
    private val binding get() = _binding!!

    private val vm: HomeViewModel by viewModels()

    private var guideId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGuideDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        guideId = requireArguments().getInt(Constant.GUIDE_ID)
        initToolbar()
        subscribeData()
    }

    override fun onStart() {
        super.onStart()
        vm.getGuideDetailsById(guideId)
    }

    private fun initToolbar() {
        binding.appBarGuidesDetails.setTitleString(getString(R.string.guide_details))
        binding.appBarGuidesDetails.setTitleCenter(true)
        binding.appBarGuidesDetails.useBackButton(
            true,
            { findNavController().navigateUp() },
            R.drawable.ic_back
        )
    }

    private fun subscribeData() {
        // observe in drivers list
        vm.guideDetailsLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    // bind data to the view
                    initViews(it.data!!.guide)
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

    @SuppressLint("SetTextI18n")
    private fun initViews(guide: GuideItem?) {
        Glide.with(requireContext())
            .load(guide?.personalPhoto)
            .into(binding.imgGuide)

        binding.guideName.text = guide?.name ?: ""
        binding.guideCity.text = guide?.country ?: ""
        binding.guideCountry.text = guide?.state ?: ""
        binding.guideDescription.text = guide?.bio ?: ""
        // binding.guidePrice.text = "$ ${guideArgument.guidePrice}"
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}