package com.visualinnovate.almursheed.home.view.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.common.value
import com.visualinnovate.almursheed.databinding.FragmentEditProfileGuideBinding
import com.visualinnovate.almursheed.home.view.ProfileData

class EditProfileGuideFragment : Fragment() {

    private var _binding: FragmentEditProfileGuideBinding? = null
    private val binding get() = _binding!!

    private val vm: ProfileViewModel by viewModels()

    private var profileData: ProfileData = ProfileData()

    private var govId: Int? = null
    private var languagesList: List<Int>? = null
    private var bio: String? = null

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
        profileData = requireArguments().getParcelable("ProfileData")!!
        Log.d("ProfileData", "ProfileData $profileData")
        initToolbar()
        setBtnListener()
    }

    private fun setBtnListener() {
        binding.btnSubmit.onDebouncedListener {
            govId = binding.edtGovernmentID.value.toInt()
            bio = binding.edtBiography.value
            // call api update guide
        }
    }

    private fun initToolbar() {
//        binding.appBarEditProfileGuide.setTitleString(getString(R.string.edit_profile))
//        binding.appBarEditProfileGuide.setTitleCenter(true)
//        binding.appBarEditProfileGuide.useBackButton(
//            true,
//            { findNavController().navigateUp() },
//            R.drawable.ic_back
//        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}