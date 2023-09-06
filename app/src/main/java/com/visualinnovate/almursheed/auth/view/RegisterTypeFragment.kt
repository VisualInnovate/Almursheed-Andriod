package com.visualinnovate.almursheed.auth.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.utils.Constant.ROLE_DRIVER
import com.visualinnovate.almursheed.utils.Constant.ROLE_GUIDE
import com.visualinnovate.almursheed.utils.Constant.ROLE_TOURIST
import com.visualinnovate.almursheed.common.customNavigate
import com.visualinnovate.almursheed.databinding.FragmentRegisterTypeBinding

class RegisterTypeFragment : Fragment() {

    private lateinit var binding: FragmentRegisterTypeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRegisterTypeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        setBtnListener()
    }

    private fun initToolbar() {
        binding.appBarRegisterType.setTitleString(getString(R.string.let_s_get_started))
        binding.appBarRegisterType.setTitleCenter(false)
        binding.appBarRegisterType.changeBackgroundColor(R.color.gradiant)
    }

    private fun setBtnListener() {
        binding.imgRegisterAsTourist.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("role", ROLE_TOURIST)
            findNavController().customNavigate(R.id.RegisterFragment, data = bundle)
        }
        binding.imgRegisterAsGuide.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("role", ROLE_GUIDE)
            findNavController().customNavigate(R.id.RegisterFragment, data = bundle)
        }
        binding.imgRegisterAsDriver.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("role", ROLE_DRIVER)
            findNavController().customNavigate(R.id.RegisterFragment, data = bundle)
        }
        binding.txtLogin.setOnClickListener {
            findNavController().customNavigate(R.id.action_registerType_to_login)
        }
    }
}
