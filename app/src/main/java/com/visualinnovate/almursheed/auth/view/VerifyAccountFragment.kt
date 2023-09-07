package com.visualinnovate.almursheed.auth.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
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
import java.lang.StringBuilder
import java.util.Locale

@AndroidEntryPoint
class VerifyAccountFragment : BaseFragment() {

    private var _binding: FragmentVerifyAccountBinding? = null
    private val binding get() = _binding!!
    private val vm: AuthViewModel by viewModels()

    private var otpCode: String? = null
    private lateinit var email: String
    private var timeLeft: Long = 0
    private var countDownTimer: CountDownTimer? = null
    private var TIMER_COUNTDOWN_INITIAL: Long = 120000

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
        initToolbar()
        initView()
        setBtnListener()
        subscribeData()
    }

    private fun initView() {
        binding.txt1.text =
            "${
                getString(R.string.a_verification_code_has_been_sent_to)
            } ${getString(R.string.enter_the_code_below)}"

        setTimer(TIMER_COUNTDOWN_INITIAL)
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

    private fun setTimer(startTime: Long) {
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(startTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
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
