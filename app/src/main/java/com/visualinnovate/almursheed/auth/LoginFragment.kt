package com.visualinnovate.almursheed.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.gone
import com.visualinnovate.almursheed.common.visible
import com.visualinnovate.almursheed.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    private var options = ""
    private var rememberMeType: Boolean = false

    private val btnBackCallBackFunc: () -> Unit = {
        findNavController().navigateUp()
    }

    private val btnRegisterCallBackListener: () -> Unit = {
        Log.d("btnFilterCallBack", "btnFilterCallBack")
        // navigate to register
        findNavController().navigate(R.id.action_login_to_touristRegister)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initToolbar()
        initSpinner()
        setBtnListener()
    }

    private fun initView() {
        binding.spinnerLoginAs.spinnerText.setText(getString(R.string.select_your_option))
        binding.spinnerChooseDestination.spinnerText.setText(getString(R.string.select_your_destination))
        binding.spinnerChooseCity.spinnerText.setText(getString(R.string.select_your_city))
    }

    private fun initToolbar() {
        binding.appBarLogin.setTitleString(getString(R.string.login))
        binding.appBarLogin.setTitleCenter(true)
        binding.appBarLogin.useBackButton(
            true,
            btnBackCallBackFunc,
            R.drawable.ic_back
        )
        binding.appBarLogin.showButtonOneWithoutImage(
            getString(R.string.register),
            btnRegisterCallBackListener
        )
    }

    private fun initSpinner() {
        val arrayAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.options,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item
        )
        binding.spinnerLoginAs.spinnerText.setAdapter(arrayAdapter)
        binding.spinnerLoginAs.spinnerText.setOnItemClickListener { parent, _, position, _ -> // parent, view, position, long
            options = parent?.getItemAtPosition(position).toString()

            Log.d("onItemSelected", "options $options")
            controlViews()
        }
    }

    private fun controlViews() {
        Log.d("controlViews", "options $options")
        if (options == "Tourist") {
            binding.txtChooseDestination.visible()
            binding.spinnerChooseDestination.root.visible()
            binding.txtChooseCity.visible()
            binding.spinnerChooseCity.root.visible()
        } else {
            binding.txtChooseDestination.gone()
            binding.spinnerChooseDestination.root.gone()
            binding.txtChooseCity.gone()
            binding.spinnerChooseCity.root.gone()
        }
    }

    private fun setBtnListener() {
        binding.btnRememberMe.setOnClickListener {
            if (!rememberMeType) {
                rememberMeType = true
                binding.imgRememberMe.setImageResource(R.drawable.ic_remember_me_selected)
            } else {
                rememberMeType = false
                binding.imgRememberMe.setImageResource(R.drawable.ic_remember_me_unselected)
            }
        }

        binding.spinnerLoginAs.root.setOnClickListener {
            openSpinner()
        }

        binding.txtForgetPassword.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_forgetPassword)
        }

        binding.btnLogin.setOnClickListener {
            // navigate to home
        }
    }

    private fun openSpinner() {}
}
