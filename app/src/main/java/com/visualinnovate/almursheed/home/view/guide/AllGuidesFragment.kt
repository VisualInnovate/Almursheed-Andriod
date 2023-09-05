package com.visualinnovate.almursheed.home.view.guide

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.customNavigate
import com.visualinnovate.almursheed.common.toast
import com.visualinnovate.almursheed.databinding.FragmentAllGuidesBinding
import com.visualinnovate.almursheed.home.adapter.AllGuideAdapter
import com.visualinnovate.almursheed.home.model.GuidesItem
import com.visualinnovate.almursheed.home.viewmodel.HomeViewModel
import com.visualinnovate.almursheed.utils.Constant
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllGuidesFragment : Fragment() {

    private var _binding: FragmentAllGuidesBinding? = null
    private val binding get() = _binding!!

    private val vm: HomeViewModel by viewModels()

    private lateinit var allGuideAdapter: AllGuideAdapter

    private val btnGuideClickCallBack: (guide: GuidesItem) -> Unit =
        { guide ->
            val bundle = Bundle()
            bundle.putInt(Constant.GUIDE_ID, guide.id!!)
            findNavController().customNavigate(R.id.guideDetailsFragment, false, bundle)
        }

    private val btnBackCallBackFunc: () -> Unit = {
        findNavController().navigateUp()
    }

    private val btnSortCallBackFunc: () -> Unit = {
        toast("Clicked btnSortCallBackFunc")
    }

    private val btnFilterCallBackFunc: () -> Unit = {
        toast("Clicked btnFilterCallBackFunc")
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
            btnBackCallBackFunc,
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
            allGuideAdapter = AllGuideAdapter(btnGuideClickCallBack)
            adapter = allGuideAdapter
        }
    }

    private fun subscribeData() {
        // observe in drivers list
        vm.guideLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    // bind data to the view
                    allGuideAdapter.submitData(it.data!!.guides)
                }
                is ResponseHandler.Error -> {
                    // show error message
                    toast(it.message)
                    Log.d("Error->DriverList", it.message)
                }
                is ResponseHandler.Loading -> {
                    // show a progress bar
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
