package com.visualinnovate.almursheed.home.view.profile

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.common.value
import com.visualinnovate.almursheed.databinding.FragmentEditProfileGuideBinding
import com.visualinnovate.almursheed.databinding.FragmentHireBinding
import com.visualinnovate.almursheed.home.MainActivity

class EditProfileGuideFragment : Fragment() {

    private var _binding: FragmentEditProfileGuideBinding? = null
    private val binding get() = _binding!!


    private val btnBackCallBackFunc: () -> Unit = {
        findNavController().navigateUp()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileGuideBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        setBtnListener()
    }

    private fun setBtnListener() {
        binding.btnSubmit.onDebouncedListener {

        }
    }

    private fun initToolbar() {
        binding.appBarEditProfileGudie.setTitleString(getString(R.string.edit_profile))
        binding.appBarEditProfileGudie.setTitleCenter(true)
        binding.appBarEditProfileGudie.useBackButton(
            true,
            btnBackCallBackFunc,
            R.drawable.ic_back
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}