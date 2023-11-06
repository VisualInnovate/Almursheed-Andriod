package com.visualinnovate.almursheed.commonView.myOrders.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.SharedPreference
import com.visualinnovate.almursheed.common.base.BaseFragment
import com.visualinnovate.almursheed.common.gone
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.common.toast
import com.visualinnovate.almursheed.common.visible
import com.visualinnovate.almursheed.commonView.myOrders.models.DayModel
import com.visualinnovate.almursheed.commonView.myOrders.models.MyOrdersItem
import com.visualinnovate.almursheed.commonView.myOrders.viewModel.MyOrdersViewModel
import com.visualinnovate.almursheed.databinding.FragmentOrderDetailsBinding
import com.visualinnovate.almursheed.home.adapter.DaysAdapter
import com.visualinnovate.almursheed.utils.Constant
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderDetailsFragment : BaseFragment() {

    private var _binding: FragmentOrderDetailsBinding? = null
    private val binding get() = _binding!!

    private val vm: MyOrdersViewModel by activityViewModels()

    private lateinit var daysAdapter: DaysAdapter

    private var days: ArrayList<DayModel> = ArrayList()

    private lateinit var userRole: String

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
        userRole = SharedPreference.getUserRole() ?: ""

        initToolbar()
        initView()
        setBtnListener()
        subscribeData()
    }

    private fun initView() {
        if (days.isEmpty()) {
            getDaysList(vm.orderDetails)
        }

        when (userRole) {
            Constant.ROLE_DRIVER, Constant.ROLE_GUIDE -> {
                binding.btnReject.visible()
                binding.btnApprove.visible()
                binding.btnPaid.gone()
                binding.btnCancel.gone()
            }

            else -> {
                binding.btnReject.gone()
                binding.btnApprove.gone()

                if (vm.orderDetails?.status == "2") {
                    binding.btnPaid.visible()
                } else {
                    binding.btnPaid.gone()
                }

                binding.btnCancel.visible()
            }
        }
        initRecyclerView(days)
        binding.country.text = vm.orderDetails?.countryId.toString()
        binding.entryDate.text = vm.orderDetails?.startDate
        binding.exitDate.text = vm.orderDetails?.endDate
        binding.price.text = vm.orderDetails?.cost +" $"
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
            vm.changeStatus(
                orderId = vm.orderDetails?.id,
                "2",
                vm.orderDetails?.userId!!.toString(),
                vm.orderDetails?.userType!!
            )
        }

        binding.btnReject.onDebouncedListener {
            vm.changeStatus(
                orderId = vm.orderDetails?.id,
                "3",
                vm.orderDetails?.userId!!.toString(),
                vm.orderDetails?.userType!!
            )
        }

        binding.btnPaid.onDebouncedListener {
            vm.changeStatus(
                orderId = vm.orderDetails?.id,
                "6",
                vm.orderDetails?.userId!!.toString(),
                vm.orderDetails?.userType!!
            )
        }

        binding.btnCancel.onDebouncedListener {
            vm.changeStatus(
                orderId = vm.orderDetails?.id,
                "5",
                vm.orderDetails?.userId!!.toString(),
                vm.orderDetails?.userType!!
            )
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

    private fun subscribeData() {
        vm.changeStatus.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    toast(it.data?.message!!)
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}