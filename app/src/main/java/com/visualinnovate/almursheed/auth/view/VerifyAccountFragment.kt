package com.visualinnovate.almursheed.auth.view

import android.annotation.SuppressLint
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
import com.visualinnovate.almursheed.databinding.FragmentVerifyAccountBinding
import com.visualinnovate.almursheed.utils.Constant
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VerifyAccountFragment : BaseFragment() {

    private lateinit var binding: FragmentVerifyAccountBinding

    private val vm: AuthViewModel by viewModels()

    private var otpCode: String? = null
    private lateinit var email: String
    private var counter: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentVerifyAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        email = requireArguments().getString(Constant.EMAIL) ?: "empty"
        initToolbar()
        initView()
        setBtnListener()
        subscribeData()
    }

    @SuppressLint("SetTextI18n", "StringFormatInvalid")
    private fun initView() {
        binding.txt1.text =
            "${
            getString(R.string.a_verification_code_has_been_sent_to, R.color.primary, email)
            } ${getString(R.string.enter_the_code_below)}"

        binding.resendCode.text =
            getString(R.string.resend_code_in, counter)
    }

    private fun initToolbar() {
        binding.appBarVerifyCode.setTitleString(getString(R.string.verification_code))
        binding.appBarVerifyCode.setTitleCenter(false)
    }

    private fun setBtnListener() {
        binding.resendCode.onDebouncedListener {
            // counter
        }
        //
        binding.btnVerify.onDebouncedListener {
            // call api
            if (validate()) {
                vm.validateOTP(email, otpCode!!)
            }
        }
    }

    private fun subscribeData() {
        vm.validateOtpLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    hideAuthLoading()
                    Log.d("Success", "${it.data!!.message}")
                    toast(it.data.message.toString())
                    // navigate to verify and pass email
                    val bundle = Bundle()
                    bundle.putString(Constant.EMAIL, email)
                    bundle.putString(Constant.OTP, otpCode)
                    findNavController().customNavigate(R.id.newPasswordFragment, false, bundle)
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
                else -> {
                    toast("Else")
                }
            }
        }
    }

    private fun validate(): Boolean {
        var isValid = true
        otpCode = binding.otpView.getStringFromFields()

        if (otpCode!!.isEmptySting()) {
            toast("Please enter code to verification")
            isValid = false
        }

        return isValid
    }
}
