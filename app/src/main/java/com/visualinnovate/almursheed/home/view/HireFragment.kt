package com.visualinnovate.almursheed.home.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.common.toast
import com.visualinnovate.almursheed.databinding.FragmentHireBinding
import com.visualinnovate.almursheed.home.MainActivity
import com.visualinnovate.almursheed.home.adapter.DaysAdapter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class HireFragment : Fragment() {

    private var _binding: FragmentHireBinding? = null
    private val binding get() = _binding!!
    private var startDate: Date = Date()
    private var endDate: Date = Date()
    private var selectedDays = ArrayList<String>()
    private var isForStartDate: Boolean = true
    private lateinit var daysAdapter: DaysAdapter

    private val btnBackCallBackFunc: () -> Unit = {
        findNavController().navigateUp()
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
        initToolbar()
        setBtnListener()
        initView()
    }

    private fun initToolbar() {
        binding.appBarHire.setTitleString(getString(R.string.hire))
        binding.appBarHire.setTitleCenter(true)
        binding.appBarHire.useBackButton(
            true,
            btnBackCallBackFunc,
            R.drawable.ic_back,
        )
    }

    private fun initView() {
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
            binding.driver.setBackgroundResource(R.drawable.bg_rectangle_corner_green_border)
            binding.guide.setBackgroundResource(R.drawable.bg_rectangle_corner_grey_border)
        }
        binding.guide.onDebouncedListener {
            binding.guide.setBackgroundResource(R.drawable.bg_rectangle_corner_green_border)
            binding.driver.setBackgroundResource(R.drawable.bg_rectangle_corner_grey_border)
        }

        binding.btnHire.onDebouncedListener {
            // navigate to payment screen
        }

        binding.inCityCheckBox.setOnCheckedChangeListener { _, isChecked ->
            binding.betweenCityCheckBox.isChecked = !isChecked
        }

        binding.betweenCityCheckBox.setOnCheckedChangeListener { _, isChecked ->
            binding.inCityCheckBox.isChecked = !isChecked
        }
    }

    private fun initRecyclerView() {
        daysAdapter = DaysAdapter()
        binding.daysRecyclerView.apply {
            itemAnimator = DefaultItemAnimator()
            daysAdapter.setHasStableIds(true)
            adapter = daysAdapter
        }
        daysAdapter.submitData(selectedDays)
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
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
        ).show()
    }

    private fun updateSelectedDatesTextView() {
        if (isForStartDate) {
            binding.startDate.text = formatDate(startDate)
        } else {
            binding.endDate.text = formatDate(endDate)
        }
        getDatesBetween(startDate, endDate)

        if (selectedDays.isNotEmpty()){
            initRecyclerView()
        }
    }

    private fun formatDate(date: Date): String {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            sdf.format(date)
        } catch (e: Exception) {
            ""
        }
    }

    private fun getDatesBetween(startDate: Date, endDate: Date) {
        selectedDays.clear()
        val dates = mutableListOf<Date>()
        val calendar = Calendar.getInstance()
        calendar.time = startDate
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale("en"))
        while (!calendar.time.after(endDate)) {
            dates.add(calendar.time)
            calendar.add(Calendar.DATE, 1)
        }
        dates.forEach {
            selectedDays.add(formatDate(it))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
