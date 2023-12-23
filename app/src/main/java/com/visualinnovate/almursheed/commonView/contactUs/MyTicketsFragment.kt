package com.visualinnovate.almursheed.commonView.contactUs

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
import com.visualinnovate.almursheed.commonView.contactUs.model.TicketItem
import com.visualinnovate.almursheed.commonView.contactUs.viewmodel.ContactUsViewModel
import com.visualinnovate.almursheed.commonView.contactUs.adapter.MyTicketsAdapter
import com.visualinnovate.almursheed.databinding.FragmentMyTicketsBinding
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyTicketsFragment : BaseFragment() {
    private var _binding: FragmentMyTicketsBinding? = null
    private val binding get() = _binding!!

    private val vm: ContactUsViewModel by viewModels()
    private lateinit var myTicketsAdapter: MyTicketsAdapter

    private val onAllDetailsClickCallback: (ticket: TicketItem) -> Unit = { ticket ->
        val bundle = Bundle()
        bundle.putInt("TICKET_ID", ticket.id ?: 0)
        findNavController().customNavigate(R.id.ticketDetailsFragment, data = bundle)
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

        // call api to get all tickets of user
        vm.getMyTickets()

        initToolbar()
        initRecyclerView()
        subscribeData()
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
                    myTicketsAdapter.submitData(it.data?.tickets)
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