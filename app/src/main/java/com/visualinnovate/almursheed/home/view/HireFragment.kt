package com.visualinnovate.almursheed.home.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.base.BaseFragment
import com.visualinnovate.almursheed.common.formatDate
import com.visualinnovate.almursheed.common.getDatesBetweenTwoDates
import com.visualinnovate.almursheed.common.isEmptySting
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.common.toast
import com.visualinnovate.almursheed.databinding.FragmentHireBinding
import com.visualinnovate.almursheed.home.MainActivity
import com.visualinnovate.almursheed.home.adapter.DaysAdapter
import com.visualinnovate.almursheed.home.model.Order
import com.visualinnovate.almursheed.home.model.RequestCreateOrder
import com.visualinnovate.almursheed.home.viewmodel.HireViewModel
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import java.util.Date

@AndroidEntryPoint
class HireFragment : BaseFragment() {

    private var _binding: FragmentHireBinding? = null
    private val binding get() = _binding!!

    private val vm: HireViewModel by viewModels()

    private var tripType: Int? = null
    private var userChoosedType: Int? = null
    private var startDate: Date = Date()
    private var endDate: Date = Date()
    private var selectedDays = ArrayList<String>()
    private var isForStartDate: Boolean = true
    private lateinit var daysAdapter: DaysAdapter
    private var datePickerDialog: DatePickerDialog? = null

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
        initToolbar()
        setBtnListener()
        initView()
        subscribeData()
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

    private fun initView() {}

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
            binding.driver.setBackgroundResource(R.drawable.bg_rectangle_corner_green_border)
            binding.guide.setBackgroundResource(R.drawable.bg_rectangle_corner_grey_border)
            binding.txtDriver.text = getString(R.string.drivers)
            userChoosedType = 1
        }
        binding.guide.onDebouncedListener {
            binding.guide.setBackgroundResource(R.drawable.bg_rectangle_corner_green_border)
            binding.driver.setBackgroundResource(R.drawable.bg_rectangle_corner_grey_border)
            binding.txtDriver.text = getString(R.string.guides)
            userChoosedType = 2
        }

        binding.btnHire.onDebouncedListener {
            // navigate to payment screen
            val order = Order(
                trip_type = tripType ?: 0,
                start_date = startDate.formatDate(),
                end_date = endDate.formatDate(),
                country_id = 1,
                lat = "30.3030",
                longitude = "30.3030",
            )

            val requestCreateOrder = RequestCreateOrder(
                user_id = 1,
                user_type = 1, // 1 driver, 2 guide
                order = order,
                order_details = ArrayList(),
            )

            if (validate()){
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
    }

    private fun subscribeData() {
        vm.createOrderLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    // save user
                    toast(it.data?.message.toString())
                    // findNavController().navigateUp()
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
                    // show a progress bar
                    hideMainLoading()
                }

                else -> {}
            }
        }
    }

    private fun initSelectedDaysRecyclerView() {
        daysAdapter = DaysAdapter()
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
        datePickerDialog.let {
            if (it?.isShowing == false) {
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
            }
        }
    }

    private fun updateSelectedDatesTextView() {
        if (isForStartDate) {
            binding.startDate.text = startDate.formatDate()
        } else {
            binding.endDate.text = endDate.formatDate()
        }
        selectedDays = startDate.getDatesBetweenTwoDates(endDate)

        if (selectedDays.isNotEmpty()) {
            initSelectedDaysRecyclerView()
        }
    }

    private fun validate(): Boolean {
        var isValid = true

        if (tripType == null) {
            toast("Choose Destination")
            isValid = false
        }
        if (userChoosedType == null) {
            toast("Choose Destination")
            isValid = false
        }
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
        return isValid
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
