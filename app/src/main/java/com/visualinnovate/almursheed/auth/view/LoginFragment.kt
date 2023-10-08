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
import com.visualinnovate.almursheed.common.SharedPreference
import com.visualinnovate.almursheed.common.base.BaseFragment
import com.visualinnovate.almursheed.common.customNavigate
import com.visualinnovate.almursheed.common.isEmptySting
import com.visualinnovate.almursheed.common.startHomeActivity
import com.visualinnovate.almursheed.common.toast
import com.visualinnovate.almursheed.common.value
import com.visualinnovate.almursheed.databinding.FragmentLoginBinding
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment() {

    private lateinit var binding: FragmentLoginBinding

    private lateinit var email: String
    private lateinit var password: String

    private var rememberMe: Boolean = false

    private val vm: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        setBtnListener()
        subscribeData()
    }

    private fun initToolbar() {
        binding.appBarLogin.setTitleString(getString(R.string.login))
        binding.appBarLogin.setTitleCenter(true)
        binding.appBarLogin.useBackButton(
            true,
            { findNavController().navigateUp() },
            R.drawable.ic_back,
        )
        binding.appBarLogin.showButtonOneWithoutImage(
            getString(R.string.register),
        ) { findNavController().customNavigate(R.id.registerTypeFragment) }
    }

    private fun setBtnListener() {
        binding.btnRememberMe.setOnClickListener {
            if (!rememberMe) {
                rememberMe = true
                binding.imgRememberMe.setImageResource(R.drawable.ic_remember_me_selected)
            } else {
                rememberMe = false
                binding.imgRememberMe.setImageResource(R.drawable.ic_remember_me_unselected)
            }
        }
        binding.txtForgetPassword.setOnClickListener {
            findNavController().customNavigate(R.id.forgetPasswordFragment)
        }

        binding.btnLogin.setOnClickListener {
            if (validate()) {
                // call api login
                vm.login(email, password)
            }
        }
    }

    private fun subscribeData() {
        vm.loginLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    // save user
                    SharedPreference.saveUser(it.data?.user)
                    SharedPreference.setUserToken(it.data?.token)
                    if (rememberMe) SharedPreference.setUserLoggedIn(true)

                    requireActivity().startHomeActivity()
                }

                is ResponseHandler.Error -> {
                    // show error message
                    toast(it.message)
                    Log.d("ResponseHandler.Error", it.message)
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
        email = binding.edtEmailAddress.value
        password = binding.edtPassword.value

        // email = "mohamed.driver77@gmail.com"
        // password = "123456789"

//         email = "mohamed.nasar8710@gmail.com"
//         password = "123456789"

        if (email.isEmptySting()) {
            binding.edtEmailAddress.error = getString(R.string.required)
            isValid = false
        } else if (!validEmail(email)) {
            binding.edtEmailAddress.error = getString(R.string.please_enter_a_valid_email)
            isValid = true
        } else {
            binding.edtEmailAddress.error = null
        }
        if (password.isEmpty()) {
            binding.edtPassword.error = getString(R.string.required)
            isValid = false
        } else if (password.length < 8) {
            binding.edtPassword.error = "password should be more than 8 characters"
            isValid = false
        } else {
            binding.edtPassword.error = null
        }
        return isValid
    }
}
