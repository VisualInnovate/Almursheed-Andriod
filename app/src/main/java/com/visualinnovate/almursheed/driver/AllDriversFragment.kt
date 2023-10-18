package com.visualinnovate.almursheed.driver

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.base.BaseFragment
import com.visualinnovate.almursheed.common.customNavigate
import com.visualinnovate.almursheed.common.toast
import com.visualinnovate.almursheed.commonView.filter.viewModel.FilterViewModel
import com.visualinnovate.almursheed.databinding.FragmentAllDriversBinding
import com.visualinnovate.almursheed.home.adapter.AllDriverAdapter
import com.visualinnovate.almursheed.home.model.DriverAndGuideItem
import com.visualinnovate.almursheed.home.viewmodel.HomeViewModel
import com.visualinnovate.almursheed.utils.Constant
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllDriversFragment : BaseFragment() {

    private var _binding: FragmentAllDriversBinding? = null
    private val binding get() = _binding!!

    private val vm: HomeViewModel by viewModels()
    private val filterVm: FilterViewModel by activityViewModels()

    private lateinit var allDriverAdapter: AllDriverAdapter

    private val btnDriverClickCallBack: (driver: DriverAndGuideItem) -> Unit = { driver ->
        val bundle = Bundle()
        bundle.putInt(Constant.DRIVER_ID, driver.id!!)
        findNavController().customNavigate(R.id.driverDetailsFragment, false, bundle)
    }

    private val btnSortCallBackFunc: () -> Unit = {
        // findNavController().navigateUp()
    }

    private val btnFilterCallBackFunc: () -> Unit = {
        val bundle = Bundle()
        bundle.putString("from", Constant.ROLE_DRIVER)
        findNavController().customNavigate(R.id.FilterFragment, data = bundle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAllDriversBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initViews()
        subscribeData()
        getAllDriversData()
    }

    private fun initToolbar() {
        binding.appBarSeeAllDrivers.setTitleString(getString(R.string.our_drivers))
        binding.appBarSeeAllDrivers.setTitleCenter(true)
        binding.appBarSeeAllDrivers.useBackButton(
            true,
            { findNavController().navigateUp() },
            R.drawable.ic_back,
        )
        binding.appBarSeeAllDrivers.showButtonSortAndFilter(
            getString(R.string.sort),
            getString(R.string.filter),
            R.drawable.ic_sort,
            R.drawable.ic_filter,
            btnSortCallBackFunc,
            btnFilterCallBackFunc,
        )
    }

    private fun initViews() {
        initSeeAllDriverRecycler()
    }

    private fun initSeeAllDriverRecycler() {
        binding.rvSeeAllDrivers.apply {
            allDriverAdapter = AllDriverAdapter(btnDriverClickCallBack)
            adapter = allDriverAdapter
        }
    }

    private fun getAllDriversData() {
        if (filterVm.checkDestinationFromFilter()) {
            vm.fetchAllDrivers(filterVm.countryId, filterVm.cityId, filterVm.carCategory, filterVm.carModel, filterVm.searchData, filterVm.price, filterVm.rate)
            filterVm.setFromFilter(false)
        } else {
            vm.fetchAllDrivers()
        }
    }
    private fun subscribeData() {
        // observe in drivers list
        vm.driverLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    // bind data to the view
                    hideMainLoading()
                    allDriverAdapter.submitData(it.data!!.drivers)
                }

                is ResponseHandler.Error -> {
                    // show error message
                    hideMainLoading()
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

                else -> {
                    hideMainLoading()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
