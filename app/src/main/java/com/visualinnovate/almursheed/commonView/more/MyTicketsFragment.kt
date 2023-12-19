package com.visualinnovate.almursheed.commonView.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.base.BaseFragment
import com.visualinnovate.almursheed.common.customNavigate
import com.visualinnovate.almursheed.common.toast
import com.visualinnovate.almursheed.commonView.more.adapter.MyTicketsAdapter
import com.visualinnovate.almursheed.commonView.myOrders.models.MyOrdersItem
import com.visualinnovate.almursheed.databinding.FragmentMyTicketsBinding
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyTicketsFragment : BaseFragment() {
    private var _binding: FragmentMyTicketsBinding? = null
    private val binding get() = _binding!!

    private val vm: MyTicketsViewModel by viewModels()
    private lateinit var myTicketsAdapter: MyTicketsAdapter

    private val onAllDetailsClickCallback: (item: MyOrdersItem) -> Unit = {
        // findNavController().customNavigate(R.id.orderDetailsFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMyTicketsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initRecyclerView()
        subscribeData()
        vm.getMyTickets()
    }

    private fun initToolbar() {
        binding.appBar.setTitleString(getString(R.string.my_tickets))
        binding.appBar.setTitleCenter(true)
        binding.appBar.useBackButton(
            true,
            { findNavController().navigateUp() },
            R.drawable.ic_back,
        )
        binding.appBar.showButtonOneWithoutImage(getString(R.string.add_ticket)) {
            findNavController().customNavigate(R.id.contactUsFragment)
        }
    }

    private fun subscribeData() {
        vm.tickets.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    // val data = it.data?.myOrders
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
        myTicketsAdapter = MyTicketsAdapter(onAllDetailsClickCallback)
        binding.ordersRv.apply {
            itemAnimator = DefaultItemAnimator()
            myTicketsAdapter.setHasStableIds(true)
            adapter = myTicketsAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}