package com.visualinnovate.almursheed.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.databinding.FragmentForgetPasswordBinding

class ForgetPasswordFragment : Fragment() {

    private var _binding: FragmentForgetPasswordBinding? = null

    // This property is only valid between onCreateView and
    // onDestroy.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForgetPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        setBtnListener()
    }

    private fun initToolbar() {
        binding.appBarForgetPassword.setTitleString(getString(R.string.forgot_password))
        binding.appBarForgetPassword.setTitleCenter(false)
    }

    private fun setBtnListener() {
        binding.btnSend.setOnClickListener {
            // navigate
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
