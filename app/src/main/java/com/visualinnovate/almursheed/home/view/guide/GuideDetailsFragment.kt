package com.visualinnovate.almursheed.home.view.guide

import android.annotation.SuppressLint
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
import com.visualinnovate.almursheed.databinding.FragmentGuideDetailsBinding
import com.visualinnovate.almursheed.home.model.Guide
import com.visualinnovate.almursheed.home.viewmodel.HomeViewModel
import com.visualinnovate.almursheed.utils.Constant
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GuideDetailsFragment : Fragment() {

    private var _binding: FragmentGuideDetailsBinding? = null
    private val binding get() = _binding!!

    private val vm: HomeViewModel by viewModels()

    private var guideId: Int? = null

    private val btnBackCallBackFunc: () -> Unit = {
        findNavController().navigateUp()
    }

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
            btnBackCallBackFunc,
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
                }
                else -> {
                    toast("Else")
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initViews(guide: Guide?) {
        binding.guideName.text = guide?.name?.localized
        // binding.guidePrice.text = "$ ${guideArgument.guidePrice}"
        // binding.guideCity.text = guideArgument.guideCity
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
