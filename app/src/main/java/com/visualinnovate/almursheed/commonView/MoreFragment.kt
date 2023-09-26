package com.visualinnovate.almursheed.commonView

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.SharedPreference
import com.visualinnovate.almursheed.common.customNavigate
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.databinding.FragmentMoreBinding
import com.visualinnovate.almursheed.home.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoreFragment : Fragment() {

    private var _binding: FragmentMoreBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).changeSelectedBottomNavListener(R.id.action_more)
        initToolbar()
        initView()
        setBtnListener()
    }

    private fun initToolbar() {
        binding.appBarMore.setTitleString(getString(R.string.more))
        binding.appBarMore.setTitleCenter(true)
        binding.appBarMore.useBackButton(
            true,
            { findNavController().navigateUp() },
            R.drawable.ic_back
        )
    }

    private fun initView() {}

    private fun setBtnListener() {
        binding.editProfile.onDebouncedListener {
            findNavController().customNavigate(R.id.editProfileFragment)
        }
        binding.logout.onDebouncedListener {
            SharedPreference.setUserToken(null)
            findNavController().customNavigate(R.id.loginFragment)
        }

        binding.myOrders.onDebouncedListener {
            findNavController().customNavigate(R.id.myOrdersFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
