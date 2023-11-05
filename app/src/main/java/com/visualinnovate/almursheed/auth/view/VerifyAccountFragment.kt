package com.visualinnovate.almursheed.auth.view

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.auth.viewmodel.AuthViewModel
import com.visualinnovate.almursheed.auth.viewmodel.VerificationViewModel
import com.visualinnovate.almursheed.common.base.BaseFragment
import com.visualinnovate.almursheed.common.customNavigate
import com.visualinnovate.almursheed.common.isEmptySting
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.common.toast
import com.visualinnovate.almursheed.databinding.FragmentVerifyAccountBinding
import com.visualinnovate.almursheed.utils.Constant
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class VerifyAccountFragment : BaseFragment() {

    private var _binding: FragmentVerifyAccountBinding? = null
    private val binding get() = _binding!!
    private val vm: VerificationViewModel by viewModels()
    private val authVm: AuthViewModel by viewModels()

    private lateinit var email: String
    private lateinit var type: String
    private var otpCode: String? = null
    private var timeLeft: Long = 0
    private var countDownTimer: CountDownTimer? = null
    private var TIMER_COUNTDOWN_INITIAL: Long = 30000
    private var enableResendCode = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentVerifyAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        email = requireArguments().getString(Constant.EMAIL)!!
        type = requireArguments().getString(Constant.TYPE_OTP)!!
        initToolbar()
        initView()
        setBtnListener()
        subscribeData()
    }

    private fun initView() {
        binding.email.text = email
        setTimer(TIMER_COUNTDOWN_INITIAL)
    }

    private fun initToolbar() {
        binding.appBarVerifyCode.setTitleString(getString(R.string.verification_code))
        binding.appBarVerifyCode.setTitleCenter(false)
    }

    private fun setBtnListener() {
        binding.edtOtpBox1.doAfterTextChanged {
            if (it != null && !it.toString().isEmptySting()) {
                binding.edtOtpBox2.requestFocus()
            }
        }
        binding.edtOtpBox2.doAfterTextChanged {
            if (it != null && !it.toString().isEmptySting()) {
                binding.edtOtpBox3.requestFocus()
            }
        }
        binding.edtOtpBox3.doAfterTextChanged {
            if (it != null && !it.toString().isEmptySting()) {
                binding.edtOtpBox4.requestFocus()
            }
        }
        binding.resendCode.onDebouncedListener {
            if (enableResendCode) {
                // call api
                authVm.forgetPassword(email, "2")
            }
        }
        //
        binding.btnVerify.onDebouncedListener {
            // call api
            if (validate()) {
                vm.validateOTP(email, otpCode!!, type)
            }
        }
    }

    private fun subscribeData() {
        vm.validateOtpLive.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    setTimer(TIMER_COUNTDOWN_INITIAL)
                    toast(it.data?.message ?: "")
                    // navigate to verify and pass email
                    val bundle = Bundle()
                    bundle.putString(Constant.EMAIL, email)
                    bundle.putString(Constant.OTP, otpCode)
                    if (type == "0") {
                        findNavController().customNavigate(R.id.loginFragment)
                    } else {
                        findNavController().customNavigate(R.id.newPasswordFragment, false, bundle)
                    }
                }

                is ResponseHandler.Error -> {
                    // show error message
                    toast(it.message)
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

        authVm.forgetPasswordLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    toast(it.data?.message.toString())
                }
                is ResponseHandler.Error -> {
                    // show error message
                    toast(it.message)
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
        otpCode = binding.edtOtpBox1.text?.trim().toString() +
                binding.edtOtpBox2.text?.trim().toString() +
                binding.edtOtpBox3.text?.trim().toString() +
                binding.edtOtpBox4.text?.trim().toString()

        if (binding.edtOtpBox1.text?.trim()?.isEmpty() == true) {
            binding.edtOtpBox1.error = getString(R.string.required)
            isValid = false
        }
        if (binding.edtOtpBox2.text?.trim()?.isEmpty() == true) {
            binding.edtOtpBox2.error = getString(R.string.required)
            isValid = false
        }
        if (binding.edtOtpBox3.text?.trim()?.isEmpty() == true) {
            binding.edtOtpBox3.error = getString(R.string.required)
            isValid = false
        }
        if (binding.edtOtpBox4.text?.trim()?.isEmpty() == true) {
            binding.edtOtpBox4.error = getString(R.string.required)
            isValid = false
        }
        return isValid
    }

    private fun setTimer(startTime: Long) {
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(startTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                enableResendCode = false
                timeLeft = millisUntilFinished
                val minutes = (millisUntilFinished / 1000).toInt() / 60
                val seconds = (millisUntilFinished / 1000).toInt() % 60
                val timeLeftString = String.format(
                    Locale.forLanguageTag("en"),
                    "%02d:%02d",
                    minutes,
                    seconds,
                )
                if (_binding != null) {
                    binding.txtResendCode.text = getString(R.string.resend_code_in, timeLeftString)
                }
            }

            override fun onFinish() {
                if (_binding != null) {
                    enableResendCode = true
                    binding.txtResendCode.text = getString(R.string.resend_code_in, "00:00")
                }
            }
        }
        countDownTimer?.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        countDownTimer?.cancel()
    }

    override fun onResume() {
        super.onResume()
        if (timeLeft.toInt() != 0) {
            setTimer(timeLeft)
        }
    }
}
