package com.visualinnovate.almursheed.commonView

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.pusher.pushnotifications.PushNotifications
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.SharedPreference
import com.visualinnovate.almursheed.common.customNavigate
import com.visualinnovate.almursheed.common.gone
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.common.startAuthActivity
import com.visualinnovate.almursheed.common.visible
import com.visualinnovate.almursheed.databinding.FragmentMoreBinding
import com.visualinnovate.almursheed.MainActivity
import com.visualinnovate.almursheed.utils.Constant
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoreFragment : Fragment() {

    private var _binding: FragmentMoreBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).changeSelectedBottomNavListener(R.id.action_more)
        (requireActivity() as MainActivity).showBottomNav()
        initToolbar()
        initView()
        setBtnListener()
    }

    private fun initToolbar() {
        binding.appBar.setTitleString(getString(R.string.more))
        binding.appBar.setTitleCenter(true)
    }

    private fun initView() {
        if (SharedPreference.getUserRole() == Constant.ROLE_GUIDE || SharedPreference.getUserRole() == Constant.ROLE_DRIVER || SharedPreference.getUserRole() == Constant.ROLE_GUIDES) {
            binding.constraintTotalEarning.visible()
            binding.myPrices.visible()
            binding.editLocation.gone()
        } else {
            binding.constraintTotalEarning.gone()
            binding.myPrices.gone()
            binding.editLocation.visible()
        }
    }

    private fun setBtnListener() {
        binding.editProfile.onDebouncedListener {
            findNavController().customNavigate(R.id.editProfileFragment)
        }

        binding.contactUs.onDebouncedListener {
            // findNavController().customNavigate(R.id.editProfileFragment)
        }

        binding.logout.onDebouncedListener {
            SharedPreference.saveUser(null)
            SharedPreference.setUserToken(null)
            SharedPreference.setUserLoggedIn(false)
            SharedPreference.setCityId(null)
            SharedPreference.setCountryId(null)
            PushNotifications.clearAllState()
            requireActivity().startAuthActivity()
        }

        binding.myOrders.onDebouncedListener {
            findNavController().customNavigate(R.id.myOrdersFragment)
        }

        binding.myPrices.onDebouncedListener {
            findNavController().customNavigate(R.id.priceFragment)
        }

        binding.aboutUs.onDebouncedListener {
            // findNavController().customNavigate(R.id.priceFragment)
        }

        binding.editLocation.onDebouncedListener {
            findNavController().customNavigate(R.id.editLocationFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
