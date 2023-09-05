package com.visualinnovate.almursheed.auth.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.base.BaseFragment
import com.visualinnovate.almursheed.databinding.FragmentOnBoardingBinding

class OnBoardingFragment : BaseFragment() {

    private lateinit var binding: FragmentOnBoardingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentOnBoardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // load icon next of get started
        loadGifImage(requireContext(), R.drawable.ic_next_animation, binding.icNextOnBoarding)
        setBtnListener()
    }

    private fun setBtnListener() {
        binding.btnGetStarted.setOnClickListener {
            findNavController().navigate(R.id.action_onBoarding_to_registerType)
        }
    }
}
