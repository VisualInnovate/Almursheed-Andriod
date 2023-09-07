package com.visualinnovate.almursheed.home.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.SharedPreference
import com.visualinnovate.almursheed.common.customNavigate
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.databinding.FragmentEditProfileBinding

class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("getUser", "${SharedPreference.getUser()}")
        initView()
        setBtnListener()
    }

    private fun initView() {
        val user = SharedPreference.getUser()
        if (user?.type == "Driver") {
            findNavController().customNavigate(R.id.editProfileDriverFragment)
        }
        Glide.with(requireContext())
            .load(user?.personalPhoto)
            .error(R.drawable.ic_launcher_foreground)
            .centerCrop()
            .circleCrop()
            .into(binding.imgUser)
        binding.edtUserName.setText(user?.name)
        binding.edtEmailAddress.setText(user?.email)
//        binding.spinnerNationality.spinner.selectedView
    }

    private fun setBtnListener() {
        binding.btnNext.onDebouncedListener {
            findNavController().customNavigate(R.id.editProfileDriverFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
