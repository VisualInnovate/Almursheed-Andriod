package com.visualinnovate.almursheed.home.view.driver

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.Constant
import com.visualinnovate.almursheed.common.customNavigate
import com.visualinnovate.almursheed.common.toast
import com.visualinnovate.almursheed.databinding.FragmentAllDriversBinding
import com.visualinnovate.almursheed.home.adapter.DriverAdapter
import com.visualinnovate.almursheed.home.model.DriverModel

class AllDriversFragment : Fragment() {

    private var _binding: FragmentAllDriversBinding? = null

    private val binding get() = _binding!!

    private lateinit var driverAdapter: DriverAdapter

    private val btnDriverClickCallBack: (driver: DriverModel) -> Unit = { driver ->
        val bundle = Bundle()
        bundle.putParcelable(Constant.DRIVER, driver)
        findNavController().customNavigate(R.id.driverDetailsFragment, false, bundle)
    }

    private val btnBackCallBackFunc: () -> Unit = {
        findNavController().navigateUp()
    }

    private val btnSortCallBackFunc: () -> Unit = {
        findNavController().navigateUp()
    }

    private val btnFilterCallBackFunc: () -> Unit = {
        findNavController().navigateUp()
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
            driverAdapter = DriverAdapter(btnDriverClickCallBack)
            driverAdapter.submitData(getDriverList())
            adapter = driverAdapter
        }
    }

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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
