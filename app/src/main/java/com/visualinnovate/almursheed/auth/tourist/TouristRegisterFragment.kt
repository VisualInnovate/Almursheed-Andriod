package com.visualinnovate.almursheed.auth.tourist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.databinding.FragmentTouristRegisterBinding

class TouristRegisterFragment : Fragment() {

    private lateinit var binding: FragmentTouristRegisterBinding

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
        initSpinner()
        setBtnListener()
    }

    private fun initView() {
        binding.spinnerNationality.spinnerText.setText(getString(R.string.select_your_nationality))
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

    private fun initSpinner() {
//        val arrayAdapter = ArrayAdapter.createFromResource(
//            requireContext(),
//            R.array.options,
//            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item
//        )
//        binding.spinnerNationality.spinnerText.setAdapter(arrayAdapter)
//        binding.spinnerNationality.spinnerText.setOnItemClickListener { parent, _, position, _ -> // parent, view, position, long
//            options = parent?.getItemAtPosition(position).toString()
//            Log.d("onItemSelected", "options $options")
//        }
    }

    private fun setBtnListener() {
    }
}
