package com.visualinnovate.almursheed.home.view.guide

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.Constant
import com.visualinnovate.almursheed.databinding.FragmentGuideDetailsBinding
import com.visualinnovate.almursheed.home.model.GuideModel

class GuideDetailsFragment : Fragment() {

    private var _binding: FragmentGuideDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var guideArgument: GuideModel

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
        guideArgument = requireArguments().getParcelable(Constant.GUIDE)!!
        initToolbar()
        initViews()
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

    @SuppressLint("SetTextI18n")
    private fun initViews() {
        binding.guideName.text = guideArgument.guideName
        binding.guidePrice.text = "$ ${guideArgument.guidePrice}"
        binding.guideCity.text = guideArgument.guideCity
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
