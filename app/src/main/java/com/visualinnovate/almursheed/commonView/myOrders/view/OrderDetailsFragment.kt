package com.visualinnovate.almursheed.commonView.myOrders.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.commonView.myOrders.models.DayModel
import com.visualinnovate.almursheed.commonView.myOrders.models.MyOrdersItem
import com.visualinnovate.almursheed.commonView.myOrders.viewModel.MyOrdersViewModel
import com.visualinnovate.almursheed.databinding.FragmentOrderDetailsBinding
import com.visualinnovate.almursheed.home.adapter.DaysAdapter

class OrderDetailsFragment : Fragment() {

    private var _binding: FragmentOrderDetailsBinding? = null
    private val binding get() = _binding!!

    private val vm: MyOrdersViewModel by activityViewModels()

    private lateinit var daysAdapter: DaysAdapter

    private var days: ArrayList<DayModel> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentOrderDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initView()
        setBtnListener()
    }

    private fun initView() {
        if (days.isEmpty()) {
            getDaysList(vm.orderDetails)
        }
        initRecyclerView(days)
        binding.country.text = vm.orderDetails?.countryId.toString()
        binding.entryDate.text = vm.orderDetails?.startDate
        binding.exitDate.text = vm.orderDetails?.endDate
    }

    private fun initToolbar() {
        binding.appBar.setTitleString(getString(R.string.order_details))
        binding.appBar.setTitleCenter(true)
        binding.appBar.useBackButton(
            true,
            { findNavController().navigateUp() },
            R.drawable.ic_back,
        )
    }

    private fun setBtnListener() {
        binding.btnApprove.onDebouncedListener {

        }

        binding.btnReject.onDebouncedListener {

        }
    }

    private fun initRecyclerView(
        selectedDays: ArrayList<DayModel>
    ) {
        daysAdapter = DaysAdapter(null, false)
        binding.daysRecyclerView.apply {
            itemAnimator = DefaultItemAnimator()
            daysAdapter.setHasStableIds(true)
            adapter = daysAdapter
        }
        daysAdapter.submitData(selectedDays, vm.orderDetails?.orderDetails)
    }

    private fun getDaysList(order: MyOrdersItem?) {
        order?.orderDetails?.forEach { orderDetails ->
            orderDetails.let {
                val day = DayModel(it!!.state.toString(), it.date.toString())
                days.add(day)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}