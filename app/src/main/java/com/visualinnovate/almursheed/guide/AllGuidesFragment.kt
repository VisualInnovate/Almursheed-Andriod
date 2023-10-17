package com.visualinnovate.almursheed.guide

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
import com.visualinnovate.almursheed.databinding.FragmentAllGuidesBinding
import com.visualinnovate.almursheed.driver.filter.viewModel.FilterViewModel
import com.visualinnovate.almursheed.home.adapter.AllGuideAdapter
import com.visualinnovate.almursheed.home.model.DriverAndGuideItem
import com.visualinnovate.almursheed.home.viewmodel.HomeViewModel
import com.visualinnovate.almursheed.utils.Constant
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllGuidesFragment : BaseFragment() {

    private var _binding: FragmentAllGuidesBinding? = null
    private val binding get() = _binding!!

    private val vm: HomeViewModel by viewModels()
    private val filterVm: FilterViewModel by activityViewModels()

    private lateinit var allGuideAdapter: AllGuideAdapter

    private val btnGuideClickCallBack: (guide: DriverAndGuideItem) -> Unit =
        { guide ->
            val bundle = Bundle()
            bundle.putInt(Constant.GUIDE_ID, guide.id!!)
            findNavController().customNavigate(R.id.guideDetailsFragment, false, bundle)
        }

    private val onFavoriteClickCallBack: (guide: DriverAndGuideItem) -> Unit = { guide ->
        vm.selectedUserPosition = guide.id!!
        vm.addAndRemoveFavorite(guide.id.toString(), "1")
    }

    private val btnSortCallBackFunc: () -> Unit = {
        toast("Clicked btnSortCallBackFunc")
    }

    private val btnFilterCallBackFunc: () -> Unit = {
        val bundle = Bundle()
        bundle.putString("from", Constant.ROLE_GUIDE)
        findNavController().customNavigate(R.id.FilterFragment, data = bundle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllGuidesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initSeeAllDriverRecycler()
        subscribeData()
    }

    override fun onStart() {
        super.onStart()
        vm.fetchAllGuides()
    }

    private fun initToolbar() {
        binding.appBarSeeAllGuides.setTitleString(getString(R.string.guides))
        binding.appBarSeeAllGuides.setTitleCenter(true)
        binding.appBarSeeAllGuides.useBackButton(
            true,
            { findNavController().navigateUp() },
            R.drawable.ic_back
        )
        binding.appBarSeeAllGuides.showButtonSortAndFilter(
            getString(R.string.sort),
            getString(R.string.filter),
            R.drawable.ic_sort,
            R.drawable.ic_filter,
            btnSortCallBackFunc,
            btnFilterCallBackFunc
        )
    }

    private fun initSeeAllDriverRecycler() {
        binding.rvSeeAllGuides.apply {
            allGuideAdapter = AllGuideAdapter(btnGuideClickCallBack, onFavoriteClickCallBack)
            adapter = allGuideAdapter
        }
    }

    private fun subscribeData() {
        // observe in drivers list
        vm.guideLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    // bind data to the view
                    allGuideAdapter.submitData(it.data!!.drivers)
                    vm.guidesList = it.data.drivers
                }

                is ResponseHandler.Error -> {
                    // show error message
                    toast(it.message)
                    Log.d("Error->DriverList", it.message)
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

        vm.isFavoriteResponse.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    // bind data to the view
                    vm.handleIsFavouriteGuides(it.data?.isFavourite)
                    toast(it.data?.message.toString())
                }

                is ResponseHandler.Error -> {
                    // show error message
                    toast(it.message)
                    Log.d("Error->DriverList", it.message)
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
