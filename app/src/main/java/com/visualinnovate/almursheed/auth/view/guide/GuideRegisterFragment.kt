package com.visualinnovate.almursheed.auth.view.guide

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.customNavigate
import com.visualinnovate.almursheed.databinding.FragmentGuideRegisterBinding

class GuideRegisterFragment : Fragment() {

    private lateinit var binding: FragmentGuideRegisterBinding

    private var gender = "Male"

    private val btnBackCallBackFunc: () -> Unit = {
        findNavController().navigateUp()
    }

    private val btnLoginCallBackListener: () -> Unit = {
        Log.d("btnLoginCallBackListener", "btnLoginCallBackListener")
        // navigate to login
        findNavController().navigate(R.id.action_guideRegister_to_login)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentGuideRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initToolbar()
        initSpinners()
        setBtnListener()
    }

    private fun initView() {
        binding.spinnerNationality.spinnerText.setText(getString(R.string.select_your_nationality))
        binding.spinnerCountry.spinnerText.setText(getString(R.string.select_your_country))
        binding.spinnerCity.spinnerText.setText(getString(R.string.select_your_city))
    }

    private fun initToolbar() {
        binding.appBarTouristRegister.setTitleString(getString(R.string.guide_register))
        binding.appBarTouristRegister.setTitleCenter(true)
        binding.appBarTouristRegister.useBackButton(
            true,
            btnBackCallBackFunc,
            R.drawable.ic_back
        )
        binding.appBarTouristRegister.showButtonOneWithoutImage(
            getString(R.string.login),
            btnLoginCallBackListener
        )
    }

    private fun initSpinners() {}

    private fun setBtnListener() {
        binding.txtMale.setOnClickListener {
            binding.txtMale.setBackgroundResource(R.drawable.bg_rectangle_corner_primary)
            binding.txtMale.setTextColor(resources.getColor(R.color.white, resources.newTheme()))
            binding.txtFemale.setBackgroundColor(Color.TRANSPARENT)
            binding.txtFemale.setTextColor(resources.getColor(R.color.grey, resources.newTheme()))
            gender = "Male" // -> Constant
        }
        binding.txtFemale.setOnClickListener {
            binding.txtFemale.setBackgroundResource(R.drawable.bg_rectangle_corner_primary)
            binding.txtFemale.setTextColor(resources.getColor(R.color.white, resources.newTheme()))
            binding.txtMale.setBackgroundColor(Color.TRANSPARENT)
            binding.txtMale.setTextColor(resources.getColor(R.color.grey, resources.newTheme()))
            gender = "Female" // -> Constant
        }
        binding.btnNext.setOnClickListener {
            Log.d("btnNext", "gender $gender")
            findNavController().customNavigate(R.id.verifyAccountFragment)
        }
    }
}
