package com.visualinnovate.almursheed.tourist.accommodation

import android.os.Bundle
import android.util.Log
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
import com.visualinnovate.almursheed.databinding.FragmentAccommodationBinding
import com.visualinnovate.almursheed.home.MainActivity
import com.visualinnovate.almursheed.tourist.accommodation.adapter.AccommodationAdapter
import com.visualinnovate.almursheed.home.model.AccommodationItem
import com.visualinnovate.almursheed.home.viewmodel.HomeViewModel
import com.visualinnovate.almursheed.utils.Constant
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccommodationFragment : BaseFragment() {

    private var _binding: FragmentAccommodationBinding? = null

    // This property is only valid between onCreateView and // onDestroy.
    private val binding get() = _binding!!

    // initialize home view model to call api and make observe
    private val vm: HomeViewModel by viewModels()
    private val filterVm: FilterViewModel by activityViewModels()

    private lateinit var accommodationAdapter: AccommodationAdapter

    private val btnAccommodationClickCallBack: (accommodation: AccommodationItem) -> Unit =
        { accommodation ->
            Log.d("btnAccommodationClickCallBack", "Clicked Item accommodation $accommodation")
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
        findNavController().customNavigate(R.id.FilterFragment, data = bundle)
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
            vm.fetchAccommodationsList(filterVm.countryId, filterVm.cityId, filterVm.accommodationCategoryId, filterVm.roomsCountId, filterVm.searchData, filterVm.price)
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
        initSeeAllDriverRecycler()
    }

    private fun initSeeAllDriverRecycler() {
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
                    accommodationAdapter.submitData(it.data!!.accommodations)
                }

                is ResponseHandler.Error -> {
                    // show error message
                    toast(it.message)
                    Log.d("ResponseHandler.Error", it.message)
                }

                is ResponseHandler.Loading -> {
                    // show a progress bar
                    showMainLoading()
                }

                is ResponseHandler.StopLoading -> {
                    // hide a progress bar
                    hideMainLoading()
                }

                else -> {
                    toast("Else")
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

/*
private fun getAccommodationList(): ArrayList<AccommodationModel> {
        val accommodationList = ArrayList<AccommodationModel>()

        accommodationList.add(
            AccommodationModel(
                0,
                R.drawable.img_test,
                "Mohamed Mohamed",
                "Egypt, Cairo",
                true,
                "120.0"
            )
        )
        accommodationList.add(
            AccommodationModel(
                1,
                R.drawable.img_driver,
                "Mohamed Ahmed",
                "Egypt, Cairo",
                false,
                "333.2"
            )
        )
        accommodationList.add(
            AccommodationModel(
                2,
                R.drawable.img_test,
                "Ahmed Mohamed",
                "Egypt, New Cairo",
                true,
                "123.22"
            )
        )
        accommodationList.add(
            AccommodationModel(
                3,
                R.drawable.img_driver,
                "Mohamed Ahmed",
                "Egypt, October",
                false,
                "111.0"
            )
        )
        accommodationList.add(
            AccommodationModel(
                4,
                R.drawable.img_test,
                "Ahmed Mohamed",
                "Egypt, Giza",
                true,
                "5555.0"
            )
        )

        accommodationList.add(
            AccommodationModel(
                4,
                R.drawable.img_test,
                "Ahmed Mohamed",
                "Egypt, Giza",
                true,
                "5555.0"
            )
        )
        accommodationList.add(
            AccommodationModel(
                4,
                R.drawable.img_test,
                "Ahmed Mohamed",
                "Egypt, Giza",
                true,
                "5555.0"
            )
        )
        accommodationList.add(
            AccommodationModel(
                4,
                R.drawable.img_test,
                "Ahmed Mohamed",
                "Egypt, Giza",
                true,
                "5555.0"
            )
        )
        accommodationList.add(
            AccommodationModel(
                4,
                R.drawable.img_test,
                "Ahmed Mohamed",
                "Egypt, Giza",
                true,
                "5555.0"
            )
        )
        accommodationList.add(
            AccommodationModel(
                4,
                R.drawable.img_test,
                "Ahmed Mohamed",
                "Egypt, Giza",
                true,
                "5555.0"
            )
        )
        accommodationList.add(
            AccommodationModel(
                4,
                R.drawable.img_test,
                "Ahmed Mohamed",
                "Egypt, Giza",
                true,
                "5555.0"
            )
        )
        accommodationList.add(
            AccommodationModel(
                4,
                R.drawable.img_test,
                "Ahmed Mohamed",
                "Egypt, Giza",
                true,
                "5555.0"
            )
        )
        accommodationList.add(
            AccommodationModel(
                4,
                R.drawable.img_test,
                "Ahmed Mohamed",
                "Egypt, Giza",
                true,
                "5555.0"
            )
        )

        accommodationList.add(
            AccommodationModel(
                4,
                R.drawable.img_test,
                "Ahmed Mohamed",
                "Egypt, Giza",
                true,
                "5555.0"
            )
        )
        accommodationList.add(
            AccommodationModel(
                4,
                R.drawable.img_test,
                "Ahmed Mohamed",
                "Egypt, Giza",
                true,
                "5555.0"
            )
        )
        accommodationList.add(
            AccommodationModel(
                4,
                R.drawable.img_test,
                "Ahmed Mohamed",
                "Egypt, Giza",
                true,
                "5555.0"
            )
        )
        accommodationList.add(
            AccommodationModel(
                4,
                R.drawable.img_test,
                "Ahmed Mohamed",
                "Egypt, Giza",
                true,
                "5555.0"
            )
        )
        accommodationList.add(
            AccommodationModel(
                4,
                R.drawable.img_test,
                "Ahmed Mohamed",
                "Egypt, Giza",
                true,
                "5555.0"
            )
        )
        accommodationList.add(
            AccommodationModel(
                4,
                R.drawable.img_test,
                "Ahmed Mohamed",
                "Egypt, Giza",
                true,
                "5555.0"
            )
        )

        return accommodationList
    }
 */
