package com.visualinnovate.almursheed.auth.view

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.auth.viewmodel.AuthViewModel
import com.visualinnovate.almursheed.common.base.BaseFragment
import com.visualinnovate.almursheed.common.customNavigate
import com.visualinnovate.almursheed.common.isEmptySting
import com.visualinnovate.almursheed.common.toast
import com.visualinnovate.almursheed.common.value
import com.visualinnovate.almursheed.databinding.FragmentTouristRegisterBinding
import com.visualinnovate.almursheed.utils.ResponseHandler
import com.visualinnovate.almursheed.utils.Utils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TouristRegisterFragment : BaseFragment() {

    private lateinit var binding: FragmentTouristRegisterBinding

    private val vm: AuthViewModel by viewModels()

    private lateinit var username: String
    private lateinit var email: String
    private var nationalityName: String = ""
    private lateinit var password: String

    private val btnBackCallBackFunc: () -> Unit = {
        findNavController().navigateUp()
    }

    private val btnLoginCallBackListener: () -> Unit = {
        Log.d("btnLoginCallBackListener", "btnLoginCallBackListener")
        // navigate to login
        findNavController().navigate(R.id.action_touristRegister_to_login)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTouristRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initToolbar()
        setBtnListener()
        subscribeData()
    }

    private fun initView() {
        // binding.spinnerNationality.spinnerText.setText(getString(R.string.select_your_nationality))
        initNationalitySpinner()
    }

    private fun initToolbar() {
        binding.appBarTouristRegister.setTitleString(getString(R.string.tourist_register))
        binding.appBarTouristRegister.setTitleCenter(true)
        binding.appBarTouristRegister.changeBackgroundColor(R.color.gradiant)
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

    private fun initNationalitySpinner() {
        val nationalityList = Utils.nationalities.keys.toList()

        val arrayAdapter = // android.R.layout.simple_spinner_item
            ArrayAdapter(
                requireContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                nationalityList
            )

        binding.spinnerNationality.spinner.adapter = arrayAdapter
        arrayAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
        binding.spinnerNationality.spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    // Retrieve the selected national name
                    nationalityName = nationalityList[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Handle when nothing is selected
                }
            }

//        binding.spinnerNationality.spinner.setOnItemClickListener { _, _, position, _ -> // parent, view, position, long
//            // Retrieve the selected country name
//            nationalityName = nationalityList[position]
//        }
    }

    private fun setBtnListener() {
        binding.btnRegister.setOnClickListener {
            if (!validate()) {
//                val drawable = resources.getDrawable(R.drawable.img_test)
//                val bitmap = (drawable as BitmapDrawable).bitmap
//
//                val file = File(requireContext().cacheDir, "image.png")
//                val outputStream = FileOutputStream(file)
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
//                outputStream.flush()
//                outputStream.close()
//
//                vm.registerTourist(username, email, nationalityName, password, file)
                findNavController().customNavigate(R.id.verifyAccountFragment)
            }
        }
    }

    private fun subscribeData() {
        vm.registerTouristLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    hideAuthLoading()
                    // bind data to the view
                    Log.d("registerDriverLiveData", "it.data ${it.data}")
                    findNavController().customNavigate(R.id.loginFragment)
                }
                is ResponseHandler.Error -> {
                    // show error message
                    toast(it.message)
                    Log.d("ResponseHandler.Error -> Register Driver", it.message)
                }
                is ResponseHandler.Loading -> {
                    // show a progress bar
                    showAuthLoading()
                }
                is ResponseHandler.StopLoading -> {
                    // hide a progress bar
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
        username = binding.edtUserName.value
        email = binding.edtEmailAddress.value
        password = binding.edtPassword.value

        if (username.isEmptySting()) {
            binding.edtUserName.error = getString(R.string.required)
            isValid = false
        } else {
            binding.edtUserName.error = null
        }
        if (email.isEmptySting()) {
            binding.edtEmailAddress.error = getString(R.string.required)
            isValid = false
        } else if (!validEmail()) {
            binding.edtEmailAddress.error = "Please enter a valid email"
            isValid = false
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

    private fun validEmail(): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
