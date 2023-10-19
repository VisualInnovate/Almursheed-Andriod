package com.visualinnovate.almursheed.commonView.filter.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.navigation.fragment.findNavController
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.commonView.filter.viewModel.FilterViewModel
import com.visualinnovate.almursheed.databinding.FragmentFilterBinding
import com.visualinnovate.almursheed.utils.Constant
import com.visualinnovate.almursheed.utils.Constant.ACCOMMODATION
import com.visualinnovate.almursheed.utils.Constant.ROLE_DRIVER
import com.visualinnovate.almursheed.utils.Constant.ROLE_GUIDE
import com.visualinnovate.almursheed.utils.Constant.SEARCH
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterFragment : Fragment() {

    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!
    private val vm: FilterViewModel by activityViewModels()

    private var selectedRole = 1
    private var filterDriverFragment: FilterDriverFragment? = null
    private var filterGuideFragment: FilterGuideFragment? = null
    private var filterAccommodationFragment: FilterAccommodationFragment? = null

    private var from = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        from = requireArguments().getString("from")!!
        initToolbar()
        initData()
        subscribeSearchView()
    }

    private fun subscribeSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(text: String?): Boolean {
                vm.searchData = text.toString().trim()
                return false
            }
        })
    }

    private fun initToolbar() {
        binding.appBar.setTitleString(getString(R.string.search))
        binding.appBar.setTitleCenter(true)
        binding.appBar.useBackButton(
            true,
            { findNavController().navigateUp() },
            R.drawable.ic_back,
        )
        binding.appBar.showTextRight(getString(R.string.reset), R.drawable.ic_reset) {
            vm.clearDataOnInit()
        }
    }

    private fun initData() {
        filterDriverFragment = FilterDriverFragment()
        filterGuideFragment = FilterGuideFragment()
        filterAccommodationFragment = FilterAccommodationFragment()
        when (from) {
            Constant.ROLE_DRIVER -> {
                initDriverView()
                navigateToDriversFragment()
                vm.from = ROLE_DRIVER
            }
            Constant.ROLE_GUIDE -> {
                initGuideView()
                navigateToGuidesFragment()
                vm.from = ROLE_GUIDE
            }
            Constant.ACCOMMODATION -> {
                initAccommodationView()
                navigateToAccommodationFragment()
                vm.from = ACCOMMODATION
            }
            Constant.ALL -> {
                navigateToDriversFragment()
                setBtnListener()
                vm.from = SEARCH
            }
        }
    }

    private fun setBtnListener() {
        binding.txtDriver.onDebouncedListener {
            initDriverView()
            vm.clearDataOnInit()
            navigateToDriversFragment()
        }
        binding.txtGuide.onDebouncedListener {
            initGuideView()
            vm.clearDataOnInit()
            navigateToGuidesFragment()
        }

        binding.txtAccommodation.onDebouncedListener {
            initAccommodationView()
            vm.clearDataOnInit()
            navigateToAccommodationFragment()
        }
    }

    private fun navigateToDriversFragment() {
        requireActivity().supportFragmentManager.commit {
            replace(R.id.container, filterDriverFragment!!)
        }
    }

    private fun navigateToGuidesFragment() {
        requireActivity().supportFragmentManager.commit {
            replace(R.id.container, filterGuideFragment!!)
        }
    }

    private fun navigateToAccommodationFragment() {
        requireActivity().supportFragmentManager.commit {
            replace(R.id.container, filterAccommodationFragment!!)
        }
    }

    private fun initGuideView() {
        selectedRole = 2
        binding.txtGuide.setBackgroundResource(R.drawable.bg_rectangle_corner_primary)
        binding.txtDriver.setBackgroundResource(R.drawable.bg_rectangle_corner_white)
        binding.txtAccommodation.setBackgroundResource(R.drawable.bg_rectangle_corner_white)
        binding.txtGuide.setTextColor(resources.getColor(R.color.white))
        binding.txtDriver.setTextColor(resources.getColor(R.color.grey))
        binding.txtAccommodation.setTextColor(resources.getColor(R.color.grey))
    }

    private fun initDriverView() {
        selectedRole = 1
        binding.txtDriver.setBackgroundResource(R.drawable.bg_rectangle_corner_primary)
        binding.txtGuide.setBackgroundResource(R.drawable.bg_rectangle_corner_white)
        binding.txtAccommodation.setBackgroundResource(R.drawable.bg_rectangle_corner_white)
        binding.txtDriver.setTextColor(resources.getColor(R.color.white))
        binding.txtGuide.setTextColor(resources.getColor(R.color.grey))
        binding.txtAccommodation.setTextColor(resources.getColor(R.color.grey))
    }

    private fun initAccommodationView() {
        selectedRole = 3
        binding.txtAccommodation.setBackgroundResource(R.drawable.bg_rectangle_corner_primary)
        binding.txtDriver.setBackgroundResource(R.drawable.bg_rectangle_corner_white)
        binding.txtGuide.setBackgroundResource(R.drawable.bg_rectangle_corner_white)
        binding.txtAccommodation.setTextColor(resources.getColor(R.color.white))
        binding.txtDriver.setTextColor(resources.getColor(R.color.grey))
        binding.txtGuide.setTextColor(resources.getColor(R.color.grey))
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
