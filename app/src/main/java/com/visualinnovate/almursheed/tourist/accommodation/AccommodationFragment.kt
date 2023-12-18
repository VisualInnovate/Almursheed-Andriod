package com.visualinnovate.almursheed.tourist.accommodation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.visualinnovate.almursheed.MainActivity
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.base.BaseFragment
import com.visualinnovate.almursheed.common.customNavigate
import com.visualinnovate.almursheed.common.toast
import com.visualinnovate.almursheed.commonView.filter.viewModel.FilterViewModel
import com.visualinnovate.almursheed.databinding.FragmentAccommodationBinding
import com.visualinnovate.almursheed.home.model.AccommodationItem
import com.visualinnovate.almursheed.home.viewmodel.HomeViewModel
import com.visualinnovate.almursheed.tourist.accommodation.adapter.AccommodationAdapter
import com.visualinnovate.almursheed.utils.Constant
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccommodationFragment : BaseFragment() {

    private var _binding: FragmentAccommodationBinding? = null
    private val binding get() = _binding!! // This property is only valid between onCreateView and // onDestroy.

    // initialize home view model to call api and make observe
    private val vm: HomeViewModel by viewModels()
    private val filterVm: FilterViewModel by activityViewModels()

    private lateinit var accommodationAdapter: AccommodationAdapter

    private val btnAccommodationClickCallBack: (accommodation: AccommodationItem) -> Unit =
        { accommodation ->
            val bundle = Bundle()
            bundle.putInt(Constant.ACCOMMODATION_ID, accommodation.id!!)
            findNavController().customNavigate(R.id.accommodationDetailsFragment, false, bundle)
        }

    private val btnSortCallBackFunc: () -> Unit = {
        toast("Clicked btnSortCallBackFunc")
    }

    private val btnFilterCallBackFunc: () -> Unit = {
        val bundle = Bundle()
        bundle.putString("from", Constant.ACCOMMODATION)
        findNavController().customNavigate(R.id.FilterFragment, true, data = bundle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAccommodationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).changeSelectedBottomNavListener(R.id.action_accommodationFragment)
        initToolbar()
        initViews()
        subscribeData()
        getAccommodationData()
    }

    private fun getAccommodationData() {
        if (filterVm.checkDestinationFromFilter()) {
            vm.fetchAccommodationsList(
                filterVm.countryId, filterVm.cityId,
                filterVm.accommodationCategoryId, filterVm.roomsCountId,
                filterVm.searchData, filterVm.price
            )
            filterVm.setFromFilter(false)
        } else {
            vm.fetchAccommodationsList()
        }
    }

    private fun initToolbar() {
        binding.appBarAccommodation.setTitleString(getString(R.string.accommodation))
        binding.appBarAccommodation.setTitleCenter(true)
        binding.appBarAccommodation.useBackButton(
            true,
            { findNavController().navigateUp() },
            R.drawable.ic_back,
        )
        binding.appBarAccommodation.showButtonSortAndFilter(
            getString(R.string.sort),
            getString(R.string.filter),
            R.drawable.ic_sort,
            R.drawable.ic_filter,
            btnSortCallBackFunc,
            btnFilterCallBackFunc,
        )
    }

    private fun initViews() {
        binding.rvAccommodation.apply {
            accommodationAdapter = AccommodationAdapter(btnAccommodationClickCallBack)
            adapter = accommodationAdapter
        }
    }

    private fun subscribeData() {
        vm.accommodationLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    // bind data to the view
                    accommodationAdapter.submitData(it.data!!.accommodationsList)
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
                    // hide a progress bar
                    hideMainLoading()
                }

                else ->{}
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}