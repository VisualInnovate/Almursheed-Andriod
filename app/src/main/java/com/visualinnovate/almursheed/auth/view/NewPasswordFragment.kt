package com.visualinnovate.almursheed.auth.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.auth.viewmodel.AuthViewModel
import com.visualinnovate.almursheed.common.base.BaseFragment
import com.visualinnovate.almursheed.common.customNavigate
import com.visualinnovate.almursheed.common.isEmptySting
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.common.toast
import com.visualinnovate.almursheed.common.value
import com.visualinnovate.almursheed.databinding.FragmentNewPasswordBinding
import com.visualinnovate.almursheed.utils.Constant
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewPasswordFragment : BaseFragment() {

    private var _binding: FragmentNewPasswordBinding? = null
    private val binding get() = _binding!!

    private val vm: AuthViewModel by viewModels()

    private lateinit var email: String
    private lateinit var otp: String
    private lateinit var newPassword: String
    private lateinit var confirmPassword: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentNewPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        email = requireArguments().getString(Constant.EMAIL) ?: ""
        otp = requireArguments().getString(Constant.OTP) ?: ""
        initToolbar()
        setBtnListener()
        subscribeData()
    }

    private fun initToolbar() {
        binding.appBarNewPassword.setTitleString(getString(R.string.create_new_password))
        binding.appBarNewPassword.setTitleCenter(false)
    }

    private fun setBtnListener() {
        binding.btnCreate.onDebouncedListener {
            // call api
            if (validate()) {
                vm.resetPassword(email, otp, newPassword, confirmPassword)
            }
        }
    }

    private fun subscribeData() {
        vm.resetPasswordLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    toast(it.data?.message.toString())
                    // navigate to login to enter new password
                    findNavController().customNavigate(R.id.loginFragment)
                }

                is ResponseHandler.Error -> {
                    // show error message
                    toast(it.message)
                    Log.d("ResponseHandler.Error ", "forgetPassword ${it.message}")
                }

                is ResponseHandler.Loading -> {
                    // show a progress bar
                    showAuthLoading()
                }

                is ResponseHandler.StopLoading -> {
                    // show a progress bar
                    hideAuthLoading()
                }

                else -> {}
            }
        }
    }

    private fun validate(): Boolean {
        var isValid = true
        newPassword = binding.edtNewPassword.value
        confirmPassword = binding.edtConfirmPassword.value

        if (newPassword.isEmptySting()) {
            binding.edtNewPassword.error = getString(R.string.required)
            isValid = false
        } else {
            binding.edtNewPassword.error = null
        }
        if (confirmPassword.isEmptySting()) {
            binding.edtConfirmPassword.error = getString(R.string.required)
            binding.edtConfirmPassword.error =
                getString(R.string.password_should_be_more_than_8_characters)
            isValid = false
        } else {
            binding.edtConfirmPassword.error = null
        }
        if (newPassword.length < 8 && confirmPassword.length < 8) {
            binding.edtNewPassword.error =
                getString(R.string.password_should_be_more_than_8_characters)
            binding.edtConfirmPassword.error =
                getString(R.string.confirm_password_should_be_more_than_8_characters)
            isValid = false
        }

        return isValid
    }
}
