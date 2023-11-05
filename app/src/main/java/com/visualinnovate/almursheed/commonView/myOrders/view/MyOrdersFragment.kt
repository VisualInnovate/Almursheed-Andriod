package com.visualinnovate.almursheed.commonView.myOrders.view

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.SharedPreference
import com.visualinnovate.almursheed.common.base.BaseFragment
import com.visualinnovate.almursheed.common.customNavigate
import com.visualinnovate.almursheed.common.isEmptySting
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.common.toast
import com.visualinnovate.almursheed.commonView.myOrders.adapters.MyOrderDriverAdapter
import com.visualinnovate.almursheed.commonView.myOrders.adapters.MyOrdersTouristAdapter
import com.visualinnovate.almursheed.commonView.myOrders.models.MyOrdersItem
import com.visualinnovate.almursheed.commonView.myOrders.viewModel.MyOrdersViewModel
import com.visualinnovate.almursheed.databinding.FragmentMyOrdersBinding
import com.visualinnovate.almursheed.utils.Constant
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyOrdersFragment : BaseFragment() {

    private var _binding: FragmentMyOrdersBinding? = null
    private val binding get() = _binding!!

    private lateinit var userRole: String
    private val vm: MyOrdersViewModel by activityViewModels()
    private lateinit var myOrdersDriverAdapter: MyOrderDriverAdapter
    private lateinit var myOrdersTouristAdapter: MyOrdersTouristAdapter

    // clicked to tourist add rate to order (trip)
    private val onAddRateClickCallback: (item: MyOrdersItem) -> Unit = {
//        if (it.rate?.trim()?.isEmptySting() == true || it.rate?.trim() == "0.0") {
            vm.orderDetails = it
            val ratDialogFragment = AddRateDialogFragment()
            ratDialogFragment.show(childFragmentManager, "RateDialog")
//        }
    }

    // clicked to (tourist, Driver, and Guide) to show order details (trip details)
    private val onAllDetailsClickCallback: (item: MyOrdersItem) -> Unit = {
        vm.orderDetails = it
        findNavController().customNavigate(R.id.orderDetailsFragment)
    }

    // clicked to driver approve order (trip)
    private val btnApproveClickCallback: (item: MyOrdersItem) -> Unit = {
        vm.changeStatus(it.id, "2", it.userId!!.toString(), it.userType!!)
    }

    // clicked to driver reject order (trip)
    private val btnRejectClickCallback: (item: MyOrdersItem) -> Unit = {
        vm.changeStatus(it.id, "3", it.userId!!.toString(), it.userType!!)
    }

    // clicked to tourist paid order (trip)
    private val onPaidClickCallback: (item: MyOrdersItem) -> Unit = {
        vm.changeStatus(it.id, "6", it.userId!!.toString(), it.userType!!)
    }

    // clicked to tourist cancel order (trip)
    private val onCancelClickCallback: (item: MyOrdersItem) -> Unit = {
        vm.changeStatus(it.id, "5", it.userId!!.toString(), it.userType!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMyOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // (requireActivity() as MainActivity).changeSelectedBottomNavListener(R.id.action_more)
        userRole = SharedPreference.getUserRole() ?: ""
        initToolbar()
        setBtnListener()
        initRecyclerView()
        subscribeData()
        // call api all here
        vm.getOrders(Constant.ALL)
    }

    private fun initToolbar() {
        binding.appBar.setTitleString(getString(R.string.my_orders))
        binding.appBar.setTitleCenter(true)
        binding.appBar.useBackButton(
            true,
            { findNavController().navigateUp() },
            R.drawable.ic_back,
        )
    }

    private fun setBtnListener() {
        binding.txtAll.onDebouncedListener {
            vm.getOrders(Constant.ALL)
            binding.txtAll.setBackgroundResource(R.drawable.bg_rectangle_corner_primary)
            binding.txtOpen.apply {
                setBackgroundColor(Color.TRANSPARENT)
                setTextColor(resources.getColor(R.color.grey))
            }
            binding.txtClose.apply {
                setBackgroundColor(Color.TRANSPARENT)
                setTextColor(resources.getColor(R.color.grey))
            }
        }

        binding.txtOpen.onDebouncedListener {
            vm.getOrders(Constant.OPEN)
            binding.txtOpen.setBackgroundResource(R.drawable.bg_rectangle_corner_primary)
            binding.txtAll.apply {
                setBackgroundColor(Color.TRANSPARENT)
                setTextColor(resources.getColor(R.color.grey))
            }
            binding.txtClose.apply {
                setBackgroundColor(Color.TRANSPARENT)
                setTextColor(resources.getColor(R.color.grey))
            }
        }

        binding.txtClose.onDebouncedListener {
            vm.getOrders(Constant.Close)
            binding.txtClose.setBackgroundResource(R.drawable.bg_rectangle_corner_primary)
            binding.txtAll.apply {
                setBackgroundColor(Color.TRANSPARENT)
                setTextColor(resources.getColor(R.color.grey))
            }
            binding.txtOpen.apply {
                setBackgroundColor(Color.TRANSPARENT)
                setTextColor(resources.getColor(R.color.grey))
            }
        }
    }

    private fun subscribeData() {
        vm.orders.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    val data = it.data?.myOrders
                    when (userRole) {
                        Constant.ROLE_DRIVER, Constant.ROLE_GUIDE -> {
                            myOrdersDriverAdapter.submitData(data)
                        }

                        else -> {
                            myOrdersTouristAdapter.submitData(data)
                        }
                    }
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

    private fun initRecyclerView() {
        when (userRole) {
            Constant.ROLE_DRIVER, Constant.ROLE_GUIDE -> initDriverGuideRecyclerView()
            else -> initTouristRecyclerView()
        }
    }

    private fun initDriverGuideRecyclerView() {
        myOrdersDriverAdapter = MyOrderDriverAdapter(
            onAllDetailsClickCallback,
            btnApproveClickCallback,
            btnRejectClickCallback,
        )
        binding.ordersRv.apply {
            itemAnimator = DefaultItemAnimator()
            myOrdersDriverAdapter.setHasStableIds(true)
            adapter = myOrdersDriverAdapter
        }
    }

    private fun initTouristRecyclerView() {
        myOrdersTouristAdapter = MyOrdersTouristAdapter(
            onAddRateClickCallback,
            onAllDetailsClickCallback,
            onPaidClickCallback,
            onCancelClickCallback,
        )
        binding.ordersRv.apply {
            itemAnimator = DefaultItemAnimator()
            myOrdersTouristAdapter.setHasStableIds(true)
            adapter = myOrdersTouristAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
