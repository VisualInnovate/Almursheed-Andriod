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
import com.visualinnovate.almursheed.common.toast
import com.visualinnovate.almursheed.common.value
import com.visualinnovate.almursheed.databinding.FragmentForgetPasswordBinding
import com.visualinnovate.almursheed.utils.Constant
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgetPasswordFragment : BaseFragment() {

    private var _binding: FragmentForgetPasswordBinding? = null
    private val binding get() = _binding!!

    private val vm: AuthViewModel by viewModels()

    private lateinit var email: String

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
        subscribeData()
    }

    private fun initToolbar() {
        binding.appBar.setTitleString(getString(R.string.forgot_password))
        binding.appBar.setTitleCenter(false)
    }

    private fun setBtnListener() {
        binding.btnSend.setOnClickListener {
            if (validate()) {
                vm.forgetPassword(email, "2")
            }
        }
    }

    private fun subscribeData() {
        vm.forgetPasswordLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    hideAuthLoading()
                    toast(it.data?.message.toString())
                    // navigate to verify and pass email
                    val bundle = Bundle()
                    bundle.putString(Constant.EMAIL, email)
                    bundle.putString(Constant.TYPE_OTP, "2")
                    findNavController().customNavigate(R.id.verifyAccountFragment, data = bundle)
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
        email = binding.edtEmailAddress.value

        if (email.isEmptySting()) {
            binding.edtEmailAddress.error = getString(R.string.required)
            isValid = false
        } else if (!validEmail(email)) {
            binding.edtEmailAddress.error = getString(R.string.please_enter_a_valid_email)
            isValid = true
        } else {
            binding.edtEmailAddress.error = null
        }
        return isValid
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
