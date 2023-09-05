package com.visualinnovate.almursheed.home.view.driver

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
import com.visualinnovate.almursheed.databinding.FragmentAllDriversBinding
import com.visualinnovate.almursheed.home.adapter.AllDriverAdapter
import com.visualinnovate.almursheed.home.model.DriversItem
import com.visualinnovate.almursheed.home.viewmodel.HomeViewModel
import com.visualinnovate.almursheed.utils.Constant
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllDriversFragment : Fragment() {

    private var _binding: FragmentAllDriversBinding? = null
    private val binding get() = _binding!!

    private val vm: HomeViewModel by viewModels()

    private lateinit var allDriverAdapter: AllDriverAdapter

    private val btnDriverClickCallBack: (driver: DriversItem) -> Unit = { driver ->
        val bundle = Bundle()
        bundle.putInt(Constant.DRIVER_ID, driver.id!!)
        findNavController().customNavigate(R.id.driverDetailsFragment, false, bundle)
    }

    private val btnBackCallBackFunc: () -> Unit = {
        findNavController().navigateUp()
    }

    private val btnSortCallBackFunc: () -> Unit = {
        // findNavController().navigateUp()
    }

    private val btnFilterCallBackFunc: () -> Unit = {
        // findNavController().navigateUp()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
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
    }

    override fun onStart() {
        super.onStart()
        vm.fetchAllDrivers()
    }

    private fun initToolbar() {
        binding.appBarSeeAllDrivers.setTitleString(getString(R.string.our_drivers))
        binding.appBarSeeAllDrivers.setTitleCenter(true)
        binding.appBarSeeAllDrivers.useBackButton(
            true,
            btnBackCallBackFunc,
            R.drawable.ic_back
        )
        binding.appBarSeeAllDrivers.showButtonSortAndFilter(
            getString(R.string.sort),
            getString(R.string.filter),
            R.drawable.ic_sort,
            R.drawable.ic_filter,
            btnSortCallBackFunc,
            btnFilterCallBackFunc
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

    private fun subscribeData() {
        // observe in drivers list
        vm.driverLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    // bind data to the view
                    allDriverAdapter.submitData(it.data!!.drivers)
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

/*
 private fun getDriverList(): ArrayList<DriverModel> {
        val driverList = ArrayList<DriverModel>()

        driverList.add(
            DriverModel(
                0,
                R.drawable.img_driver,
                4.5,
                "Mohamed Mohamed",
                true,
                120.0,
                "Giza",
                false
            )
        )
        driverList.add(
            DriverModel(
                1,
                R.drawable.img_driver,
                2.0,
                "Mohamed Ahmed",
                false,
                333.2,
                "Cairo",
                false
            )
        )
        driverList.add(
            DriverModel(
                2,
                R.drawable.img_driver,
                1.1,
                "Ahmed Mohamed",
                true,
                123.22,
                "New Cairo",
                true
            )
        )
        driverList.add(
            DriverModel(
                3,
                R.drawable.img_driver,
                5.1,
                "Mohamed Ahmed",
                false,
                111.0,
                "October",
                true
            )
        )
        driverList.add(
            DriverModel(
                4,
                R.drawable.img_driver,
                3.3,
                "Ahmed Mohamed",
                true,
                5555.0,
                "Giza",
                false
            )
        )

        return driverList
    }
 */
