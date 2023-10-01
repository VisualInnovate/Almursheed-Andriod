package com.visualinnovate.almursheed.auth.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.SharedPreference
import com.visualinnovate.almursheed.common.base.BaseFragment
import com.visualinnovate.almursheed.common.customNavigate
import com.visualinnovate.almursheed.common.startHomeActivity
import com.visualinnovate.almursheed.databinding.FragmentSplashBinding

class SplashFragment : BaseFragment() {

    private lateinit var binding: FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // load icon next of get started
        loadGifImage(requireContext(), R.drawable.animation_logo_splash, binding.imgLogoSplash)
    }

    override fun onResume() {
        super.onResume()

        Handler(Looper.getMainLooper()).postDelayed({
            // if need to call api check version to make update to version app
            // navigate() -> if need to check to user to auto login

            if (SharedPreference.getUserLoggedIn()){
                requireActivity().startHomeActivity()
            }else findNavController().customNavigate(R.id.onBoardingFragment)

        }, 3000)
    }
}
