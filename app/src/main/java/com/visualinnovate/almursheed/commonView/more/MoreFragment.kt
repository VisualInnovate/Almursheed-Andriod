package com.visualinnovate.almursheed.commonView.more

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.pusher.pushnotifications.PushNotifications
import com.visualinnovate.almursheed.MainActivity
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.SharedPreference
import com.visualinnovate.almursheed.common.base.BaseFragment
import com.visualinnovate.almursheed.common.customNavigate
import com.visualinnovate.almursheed.common.gone
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.common.showBottomSheet
import com.visualinnovate.almursheed.common.startAuthActivity
import com.visualinnovate.almursheed.common.toast
import com.visualinnovate.almursheed.common.visible
import com.visualinnovate.almursheed.commonView.bottomSheets.ChooseTextBottomSheet
import com.visualinnovate.almursheed.commonView.bottomSheets.model.ChooserItemModel
import com.visualinnovate.almursheed.databinding.FragmentMoreBinding
import com.visualinnovate.almursheed.utils.Constant
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoreFragment : BaseFragment() {

    private var _binding: FragmentMoreBinding? = null
    private val binding get() = _binding!!

    private val vm: MoreViewModel by viewModels()

    private var chooseTextBottomSheet: ChooseTextBottomSheet? = null

    private var language: String = "en"

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
        subscribeData()
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
            vm.getTotalEarningToDriverAndGuide()
        } else {
            binding.constraintTotalEarning.gone()
            binding.myPrices.gone()
            binding.editLocation.visible()
        }
    }

    private fun setBtnListener() {
        binding.liveChat.onDebouncedListener {
            findNavController().customNavigate(R.id.chatFragment)
        }

        binding.editProfile.onDebouncedListener {
            findNavController().customNavigate(R.id.editProfileFragment)
        }

        binding.contactUs.onDebouncedListener {
            findNavController().customNavigate(R.id.myTicketsFragment)
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
            findNavController().customNavigate(R.id.aboutUsFragment)
        }

        binding.editLocation.onDebouncedListener {
            findNavController().customNavigate(R.id.editLocationFragment)
        }

        binding.termsAndConditions.onDebouncedListener {
            findNavController().customNavigate(R.id.termsAndConditionFragment)
        }

        binding.language.onDebouncedListener {
            showLanguageChooser()
        }
    }

    private fun showLanguageChooser() {
        chooseTextBottomSheet?.dismiss()
        chooseTextBottomSheet = ChooseTextBottomSheet(
            getString(R.string.priority),
            setupLanguageList(),
            { data, _ ->
                language = data.name.toString()
                binding.language.text = data.name.toString()
                /*language = when (data.name) {
                    getString(R.string.english) -> "en"
                    getString(R.string.arabic) -> "ar"
                    getString(R.string.azerbaijan) -> "az"
                    getString(R.string.georgia) -> "ge"
                    getString(R.string.russia) -> "ru"
                    getString(R.string.turkey) -> "tu"
                    else -> "en"
                }*/
            })
        showBottomSheet(chooseTextBottomSheet!!, "languagesBottomSheet")
    }

    private fun setupLanguageList(): ArrayList<ChooserItemModel> {
        val allTypes = arrayOf(
            getString(R.string.english),
            getString(R.string.arabic),
            getString(R.string.azerbaijan),
            getString(R.string.georgia),
            getString(R.string.russia),
            getString(R.string.turkey)
        )
        val chooserItemList = ArrayList<ChooserItemModel>()
        allTypes.forEach {
            val item = ChooserItemModel(name = it, isSelected = it == language)
            chooserItemList.add(item)
        }
        return chooserItemList
    }

    @SuppressLint("SetTextI18n")
    private fun subscribeData() {
        vm.getTotalEarningLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    // bind data to the view
                    if (SharedPreference.getUserRole() == Constant.ROLE_GUIDE || SharedPreference.getUserRole() == Constant.ROLE_GUIDES
                    ) {
                        binding.totalEarning.text = "${it.data?.profits?.guidesProfits} $"
                        binding.total.text = "${it.data?.profits?.guidesProfits} $"
                    } else if (SharedPreference.getUserRole() == Constant.ROLE_DRIVER) {
                        binding.totalEarning.text = "${it.data?.profits?.driversProfits} $"
                        binding.total.text = "${it.data?.profits?.driversProfits} $"
                    }
                }

                is ResponseHandler.Error -> {
                    // show error message
                    toast(it.message)
                }

                is ResponseHandler.Loading -> {
                    // show a progress bar
                    showMainLoading()
                }

                is ResponseHandler.StopLoading -> {
                    // show a progress bar
                    hideMainLoading()
                }

                else -> {}
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
