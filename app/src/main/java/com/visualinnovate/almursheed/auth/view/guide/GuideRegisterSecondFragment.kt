package com.visualinnovate.almursheed.auth.view.guide

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.databinding.FragmentGuideRegisterSecondBinding

class GuideRegisterSecondFragment : Fragment() {

    private lateinit var binding: FragmentGuideRegisterSecondBinding

    private val btnBackCallBackFunc: () -> Unit = {
        findNavController().navigateUp()
    }

    private val btnLoginCallBackListener: () -> Unit = {
        Log.d("btnLoginCallBackListener", "btnLoginCallBackListener")
        // navigate to login
        findNavController().navigate(R.id.action_guideRegisterSecond_to_login)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentGuideRegisterSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initToolbar()
        setBtnListener()
    }

    private fun initView() {
        binding.spinnerLanguage.spinnerText.setText(getString(R.string.select_language))
    }

    private fun initToolbar() {
        binding.appBarTouristRegisterSecond.setTitleString(getString(R.string.guide_register_second))
        binding.appBarTouristRegisterSecond.setTitleCenter(true)
        binding.appBarTouristRegisterSecond.useBackButton(
            true,
            btnBackCallBackFunc,
            R.drawable.ic_back
        )
        binding.appBarTouristRegisterSecond.showButtonOneWithoutImage(
            getString(R.string.login),
            btnLoginCallBackListener
        )
    }

    private fun setBtnListener() {
        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_guideRegisterSecond_to_verifyAccount)
        }
    }
}
