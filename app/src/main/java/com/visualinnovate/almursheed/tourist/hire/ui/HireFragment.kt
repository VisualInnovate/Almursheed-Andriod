package com.visualinnovate.almursheed.tourist.hire.ui

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.visualinnovate.almursheed.MainActivity
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.SharedPreference
import com.visualinnovate.almursheed.common.base.BaseFragment
import com.visualinnovate.almursheed.common.customNavigate
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
import com.visualinnovate.almursheed.home.adapter.PricesCitesAdapter
import com.visualinnovate.almursheed.home.model.DriverAndGuideItem
import com.visualinnovate.almursheed.tourist.hire.viewmodel.HireViewModel
import com.visualinnovate.almursheed.utils.Constant
import com.visualinnovate.almursheed.utils.LocationHelper
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import java.util.Calendar
import java.util.Date

@AndroidEntryPoint
class HireFragment : BaseFragment() {

    private var _binding: FragmentHireBinding? = null
    private val binding get() = _binding!!

    private val vm: HireViewModel by activityViewModels()

    private var tripType: Int = 1 // 1 inCity , 2 BetweenCity
    private var userType: Int = 1 // 1 driver , 2 guide

    private lateinit var daysAdapter: PricesCitesAdapter
    private lateinit var permissionHelper: PermissionHelper
    var daysForAdapter = ArrayList<DayModel>()
    private var startDate: Date = Date()
    private var endDate: Date = Date()
    private var currentLocation: Location? = null

    private val selectDaysAndCityCallback: (day: String, cityId: Int, cityName: String?) -> Unit = { day, cityId, cityName ->
        vm.setSelectedCity(cityName.toString(), cityId.toString())
    }

    private val selectDriverGuideClickCalBack: (user: DriverAndGuideItem) -> Unit = {
        vm.setSelectedDriverOrGuide(it)
        binding.nameDriverGuide.text = vm.getSelectedDriverAndGuideName()
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
        callGetDriverAndGuide()
        initToolbar()
        setBtnListener()
        initView()
        subscribeData()
        handleLoading()
        handleOpenFromBookNow() }

    private fun handleOpenFromBookNow() {
        if (arguments != null) {
            val selectDriverGuide: DriverAndGuideItem? = arguments?.getParcelable("selectedDriverOrGuide")
            val type: String? = arguments?.getString("type")
            if (selectDriverGuide != null) {
                if (type == Constant.ROLE_DRIVER) {
                    handleChooseDriverUi()
                } else {
                    handleChooseGuideUi()
                }

                vm.setSelectedDriverOrGuide(selectDriverGuide)
                binding.nameDriverGuide.text = vm.getSelectedDriverAndGuideName()
            }
        }
    }

    private fun handleLoading() {
        // show loading to get drivers and guides data
        lifecycleScope.launchWhenResumed {
            showMainLoading()
            delay(2000)
            hideMainLoading()
        }
    }

    private fun initToolbar() {
        binding.appBar.setTitleString(getString(R.string.hire))
        binding.appBar.setTitleCenter(true)
        binding.appBar.useBackButton(
            true,
            { findNavController().navigateUp() },
            R.drawable.ic_back,
        )
    }

    private fun initView() {
        initSelectedDaysRecyclerView()
        handleInCityClick()
    }

    private fun setBtnListener() {
        binding.inCity.onDebouncedListener {
            handleInCityClick()
        }

        binding.betweenCity.onDebouncedListener {
            handleBetweenCityClick()
        }
        binding.startDate.onDebouncedListener {
            showStartDatePicker()
        }

        binding.endDate.onDebouncedListener {
            showEndDatePicker()
        }

        binding.driver.onDebouncedListener {
            handleChooseDriverUi()
        }

        binding.guide.onDebouncedListener {
            handleChooseGuideUi()
        }

        binding.nameDriverGuide.onDebouncedListener {
            if (vm.allDrivers.isNotEmpty() || vm.allGuides.isNotEmpty()) {
                if (userType == 1) {
                    userType = 1
                    showDriversGuidesBottomSheet("Driver", vm.allDrivers)
                } else {
                    userType = 2
                    showDriversGuidesBottomSheet("Guide", vm.allGuides)
                }
            }
        }

        binding.btnHire.onDebouncedListener {
            if (validate()) {
                // call create order api
                vm.createOrder(tripType, currentLocation, startDate.formatDate(), endDate.formatDate(), userType, setupDataForApiCall())
            }
        }
    }

    private fun handleInCityClick() {
        binding.inCity.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.drawable.ic_remember_me_selected, null), null, null, null)
        binding.betweenCity.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.drawable.ic_remember_me_unselected), null, null, null)
        tripType = 1
        updateStartDateAndEndDateTextView()
    }
    private fun handleBetweenCityClick() {
        binding.betweenCity.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.drawable.ic_remember_me_selected, null), null, null, null)
        binding.inCity.setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.drawable.ic_remember_me_unselected), null, null, null)
        tripType = 2
        updateStartDateAndEndDateTextView()
    }

    private fun handleChooseDriverUi() {
        binding.driver.setBackgroundResource(R.drawable.bg_rectangle_corner_green_border)
        binding.guide.setBackgroundResource(R.drawable.bg_rectangle_corner_grey_border)
        binding.txtDriver.text = getString(R.string.drivers)
        binding.nameDriverGuide.text = getString(R.string.choose_a, getString(R.string.driver))
        userType = 1
        vm.setSelectedDriverOrGuide(null)
    }
    private fun handleChooseGuideUi() {
        binding.guide.setBackgroundResource(R.drawable.bg_rectangle_corner_green_border)
        binding.driver.setBackgroundResource(R.drawable.bg_rectangle_corner_grey_border)
        binding.txtDriver.text = getString(R.string.guides)
        binding.nameDriverGuide.text = getString(R.string.choose_a, getString(R.string.guide))
        userType = 2
        vm.setSelectedDriverOrGuide(null)
    }

    private fun subscribeData() {
        vm.cites.observe(viewLifecycleOwner) {
            it?.let {
                daysAdapter.submitData(daysForAdapter, it)
            }
        }

        vm.createOrderLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    vm.order = it.data
                    showReceiptDialog()
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

    private fun showReceiptDialog() {
        val dialog = ReceiptDialog(onDismissCalBack = {
            findNavController().customNavigate(R.id.myOrdersFragment)
        })
        val bundle = Bundle()
        bundle.putString("START_DATE", startDate.formatDate())
        bundle.putString("END_DATE", endDate.formatDate())
        bundle.putInt("NUMBER_OF_DATE", setupDataForApiCall().size)

        // Set the Bundle as arguments for the DialogFragment
        dialog.arguments = bundle
        this.showDialog(dialog, "ReceiptDialog")
    }

    private fun showDriversGuidesBottomSheet(type: String, data: ArrayList<DriverAndGuideItem>) {
        val bottomSheet = ChooseDriverOrGuideBottomSheet(type, data, selectDriverGuideClickCalBack)
        this.showBottomSheet(bottomSheet, "ChooseDriversGuidesBottomSheet")
    }

    private fun initSelectedDaysRecyclerView() {
        daysAdapter = PricesCitesAdapter(selectDaysAndCityCallback)
        binding.daysRecyclerView.apply {
            itemAnimator = DefaultItemAnimator()
            daysAdapter.setHasStableIds(true)
            adapter = daysAdapter
        }
    }

    private fun showStartDatePicker() {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                val selectedDate = calendar.time
                startDate = selectedDate
                updateStartDateAndEndDateTextView()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH),
        )

        datePicker.datePicker.minDate = System.currentTimeMillis()
        datePicker.show()
    }

    private fun showEndDatePicker() {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                val selectedDate = calendar.time
                endDate = selectedDate
                updateStartDateAndEndDateTextView()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH),
        )
        datePicker.datePicker.minDate = System.currentTimeMillis()
        datePicker.show()
    }

    private fun updateStartDateAndEndDateTextView() {
        binding.startDate.text = startDate.formatDate()
        binding.endDate.text = endDate.formatDate()
        daysForAdapter = if (tripType == 1) {
            setupDaysModelForInCityCase()
        } else {
            setupDaysModelForBetweenCityCase()
        }
        vm.getSelectedDriverAndGuideCities()?.let { daysAdapter.submitData(daysForAdapter, it) }
    }

    private fun getDatesBetweenStartDateAndEndDate(): ArrayList<String> {
        return startDate.getDatesBetweenTwoDates(endDate)
    }

    private fun setupDataForApiCall(): ArrayList<DayModel> {
        var days = ArrayList<DayModel>()
        if (tripType == 1) {
            setupDaysModelForBetweenCityCase().forEach {
                it.cityId = vm.getSelectedCityId()
                days.add(it)
            }
        } else {
            days = daysForAdapter
        }
        return days
    }

    private fun setupDaysModelForBetweenCityCase(): ArrayList<DayModel> {
        val list = ArrayList<DayModel>()
        getDatesBetweenStartDateAndEndDate().forEach {
            val day = DayModel("", it)
            list.add(day)
        }
        return list
    }
    private fun setupDaysModelForInCityCase(): ArrayList<DayModel> {
        val list = ArrayList<DayModel>()
        if (getDatesBetweenStartDateAndEndDate().isNotEmpty()) {
            list.add(DayModel("", getDatesBetweenStartDateAndEndDate().first() + " TO " + getDatesBetweenStartDateAndEndDate().last()))
        }
        return list
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

        if (vm.getSelectedDriverAndGuideName().isNullOrEmpty()) {
            toast(getString(R.string.please_choose_driver_or_guide))
            isValid = false
        }

        if (startDate.after(endDate)) {
            toast(getString(R.string.start_date_must_be_before_end_date))
            isValid = false
        }

        if (currentLocation == null) {
            askForLocationPermission()
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

    private fun callGetDriverAndGuide() {
        if (checkIfUserHasCityId()) {
            vm.getAllDriversByDistCityId()
            vm.getAllGuidesByDistCityId()
        } else {
            showAlertDialog(
                getString(R.string.destination_city_is_empty),
                getString(R.string.please_go_to_add_city_from_update_profile),
            ) {
                findNavController().customNavigate(R.id.editLocationFragment)
            }
        }
    }

    private fun checkIfUserHasCityId(): Boolean {
        // check if user has location or not
        return SharedPreference.getCityId() != null
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
    private fun checkGpsIsEnabled() {
        val locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
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
        vm.clearData()
        _binding = null
    }
}
