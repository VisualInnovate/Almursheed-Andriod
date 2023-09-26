package com.visualinnovate.almursheed.tourist.hire

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.bumptech.glide.Glide
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.base.BaseFragment
import com.visualinnovate.almursheed.common.formatDate
import com.visualinnovate.almursheed.common.getDatesBetweenTwoDates
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.common.permission.Permission
import com.visualinnovate.almursheed.common.permission.PermissionHelper
import com.visualinnovate.almursheed.common.showAlertDialog
import com.visualinnovate.almursheed.common.showBottomSheet
import com.visualinnovate.almursheed.common.showDialog
import com.visualinnovate.almursheed.common.toast
import com.visualinnovate.almursheed.commonView.myOrders.models.DayModel
import com.visualinnovate.almursheed.databinding.FragmentHireBinding
import com.visualinnovate.almursheed.home.MainActivity
import com.visualinnovate.almursheed.home.adapter.DaysAdapter
import com.visualinnovate.almursheed.home.model.DriverItem
import com.visualinnovate.almursheed.home.model.Order
import com.visualinnovate.almursheed.home.model.OrderDetail
import com.visualinnovate.almursheed.home.model.RequestCreateOrder
import com.visualinnovate.almursheed.home.viewmodel.HireViewModel
import com.visualinnovate.almursheed.utils.LocationHelper
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import java.util.Date

@AndroidEntryPoint
class HireFragment : BaseFragment() {

    private var _binding: FragmentHireBinding? = null
    private val binding get() = _binding!!

    private val vm: HireViewModel by activityViewModels()

    private var tripType: Int = 1
    private var userChoosedType: Int = 1
    private var selectedDriverGuideId: Int = -1
    private var startDate: Date = Date()
    private var endDate: Date = Date()
    private var currentLocation: Location? = null
    private var selectedDays = ArrayList<DayModel>()
    private var orderDetailsList = ArrayList<OrderDetail>()
    private var isForStartDate: Boolean = true
    private lateinit var daysAdapter: DaysAdapter
    private var datePickerDialog: DatePickerDialog? = null
    private lateinit var permissionHelper: PermissionHelper

    private val selectDaysAndCityCallback: (day: String, cityId: Int) -> Unit = { day, cityId ->
        // Check if the date already exists in orderDetailsList
        val existingOrderDetail = orderDetailsList.find { it.date == day }

        if (existingOrderDetail != null) {
            // If the date already exists, update the city_id
            existingOrderDetail.city_id += cityId
        } else {
            // If the date doesn't exist, add it to the list
            orderDetailsList.add(OrderDetail(date = day, cityId))
        }
    }

    private val selectDriverGuideClickCalBack: (user: DriverItem) -> Unit = {
        selectedDriverGuideId = it.id!!
        binding.chooseDriver.text = it.name
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHireBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).changeSelectedBottomNavListener(R.id.action_hireFragment)
        permissionHelper = PermissionHelper.init(this)
        askForLocationPermission()
        initToolbar()
        setBtnListener()
        initView()
        subscribeData()
    }

    private fun askForLocationPermission() {
        permissionHelper
            .addPermissionsToAsk(Permission.Location)
            .isRequired(true)
            .requestPermissions { grantedList ->
                for (permission in grantedList) {
                    when (permission) {
                        Permission.Location -> {
                            checkGpsIsEnabled()
                            break
                        }

                        else -> {}
                    }
                }
            }
    }



    private fun initToolbar() {
        binding.appBarHire.setTitleString(getString(R.string.hire))
        binding.appBarHire.setTitleCenter(true)
        binding.appBarHire.useBackButton(
            true,
            { findNavController().navigateUp() },
            R.drawable.ic_back,
        )
    }

    private fun initView() {
        binding.inCityCheckBox.isChecked = true
    }

    private fun setBtnListener() {
        binding.startDate.onDebouncedListener {
            isForStartDate = true
            showDatePicker()
        }
        binding.endDate.onDebouncedListener {
            isForStartDate = false
            showDatePicker()
        }
        binding.icSwitchDate.onDebouncedListener {
        }
        binding.driver.onDebouncedListener {
            selectedDriverGuideId = -1
            binding.driver.setBackgroundResource(R.drawable.bg_rectangle_corner_green_border)
            binding.guide.setBackgroundResource(R.drawable.bg_rectangle_corner_grey_border)
            binding.txtDriver.text = getString(R.string.drivers)
            binding.chooseDriver.hint = getString(R.string.choose_a,getString(R.string.driver))
            userChoosedType = 1
        }
        binding.guide.onDebouncedListener {
            selectedDriverGuideId = -1
            binding.guide.setBackgroundResource(R.drawable.bg_rectangle_corner_green_border)
            binding.driver.setBackgroundResource(R.drawable.bg_rectangle_corner_grey_border)
            binding.txtDriver.text = getString(R.string.guides)
            binding.chooseDriver.hint = getString(R.string.choose_a,getString(R.string.guide))
            userChoosedType = 2
        }

        binding.btnHire.onDebouncedListener {
            // navigate to payment screen
            val order = Order(
                trip_type = tripType,
                start_date = startDate.formatDate(),
                end_date = endDate.formatDate(),
                country_id = 1,
                lat = currentLocation?.latitude.toString(),
                longitude = currentLocation?.longitude.toString(),
            )

            val requestCreateOrder = RequestCreateOrder(
                user_id = selectedDriverGuideId,
                user_type = userChoosedType, // 1 driver, 2 guide
                order = order,
                order_details = orderDetailsList,
            )

            if (validate()) {
                // call create order api
                vm.createOrder(requestCreateOrder)
            }
        }

        binding.inCityCheckBox.setOnCheckedChangeListener { _, isChecked ->
            binding.betweenCityCheckBox.isChecked = !isChecked
            tripType = 1
        }

        binding.betweenCityCheckBox.setOnCheckedChangeListener { _, isChecked ->
            binding.inCityCheckBox.isChecked = !isChecked
            tripType = 2
        }

        binding.chooseDriver.onDebouncedListener {
            if (userChoosedType == 1) {
                userChoosedType = 1
                showDriversGuidesBottomSheet("Driver", vm.allDrivers)
            } else {
                userChoosedType = 1
                showDriversGuidesBottomSheet("Guide", vm.allGuides)
            }
        }
    }

    private fun subscribeData() {
        vm.createOrderLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    Log.d("ResponseHandler.Error", it.data.toString())
                    vm.order = it.data
                    showReceiptDialog()
                }

                is ResponseHandler.Error -> {
                    // show error message
                    toast(it.message)
                    // showReceiptDialog()
                    Log.d("ResponseHandler.Error", it.message)
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

    private fun showReceiptDialog() {
        val dialog = ReceiptDialog()
        val bundle = Bundle()
        bundle.putString("START_DATE", startDate.formatDate())
        bundle.putString("END_DATE", endDate.formatDate())
        bundle.putInt("NUMBER_OF_DATE", orderDetailsList.size)

        // Set the Bundle as arguments for the DialogFragment
        dialog.arguments = bundle
        this.showDialog(dialog, "ReceiptDialog")
    }

    private fun showDriversGuidesBottomSheet(type: String, data: ArrayList<DriverItem>) {
        val bottomSheet = ChooseDriverOrGuideBottomSheet(type, data, selectDriverGuideClickCalBack)
        this.showBottomSheet(bottomSheet, "ChooseDriversGuidesBottomSheet")
    }

    private fun initSelectedDaysRecyclerView() {
        daysAdapter = DaysAdapter(selectDaysAndCityCallback, true)
        binding.daysRecyclerView.apply {
            itemAnimator = DefaultItemAnimator()
            daysAdapter.setHasStableIds(true)
            adapter = daysAdapter
        }
        daysAdapter.submitData(selectedDays)
    }

    private fun initDriversOrGuidesRecyclerView() {
//        daysAdapter = DaysAdapter()
//        binding.daysRecyclerView.apply {
//            itemAnimator = DefaultItemAnimator()
//            daysAdapter.setHasStableIds(true)
//            adapter = daysAdapter
//        }
//        daysAdapter.submitData(selectedDays)
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        // datePickerDialog.let {
        // if (datePickerDialog?.isShowing == false) {
        datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                val selectedDate = calendar.time

                if (isForStartDate) {
                    startDate = selectedDate
                } else {
                    endDate = selectedDate
                }

                updateSelectedDatesTextView()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH),
        )
        datePickerDialog?.show()
        // }
        // }
    }

    private fun updateSelectedDatesTextView() {
        if (isForStartDate) {
            binding.startDate.text = startDate.formatDate()
        } else {
            binding.endDate.text = endDate.formatDate()
        }

        val days = startDate.getDatesBetweenTwoDates(endDate)
        days.forEach {
            var day = DayModel("",it)
            selectedDays.add(day)
        }

        if (selectedDays.isNotEmpty()) {
            initSelectedDaysRecyclerView()
        }
    }

    private fun validate(): Boolean {
        var isValid = true

        if (binding.startDate.text.isEmpty()) {
            binding.startDate.error = getString(R.string.required)
            isValid = false
        } else {
            binding.startDate.error = null
        }
        if (binding.endDate.text.isEmpty()) {
            binding.endDate.error = getString(R.string.required)
            isValid = false
        } else {
            binding.endDate.error = null
        }

        if (orderDetailsList.isEmpty()) {
            toast("Please choose city")
            isValid = false
        }

        if (selectedDriverGuideId == -1) {
            toast("Please choose Driver or Guide")
            isValid = false
        }
        return isValid
    }

    private fun getCurrentLocation() {
        LocationHelper.getInstance(requireContext()).singleListenLocationUpdate { location ->
            location?.let {
                currentLocation = it
            }
        }
    }

    private fun checkGpsIsEnabled() {
        val locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        // Checking GPS is enabled
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (isGpsEnabled) {
            getCurrentLocation()
        } else {
            showAlertDialog("GPS is not enabled", "Please enable GPS to take action") {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
