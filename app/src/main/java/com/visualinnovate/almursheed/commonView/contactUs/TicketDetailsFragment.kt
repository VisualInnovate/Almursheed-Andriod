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
import com.visualinnovate.almursheed.common.isEmptySting
import com.visualinnovate.almursheed.common.toast
import com.visualinnovate.almursheed.common.value
import com.visualinnovate.almursheed.commonView.contactUs.adapter.TicketConversationAdapter
import com.visualinnovate.almursheed.commonView.contactUs.model.TicketItem
import com.visualinnovate.almursheed.commonView.contactUs.viewmodel.ContactUsViewModel
import com.visualinnovate.almursheed.databinding.FragmentTicketDetailsBinding
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TicketDetailsFragment : BaseFragment() {

    private var _binding: FragmentTicketDetailsBinding? = null
    private val binding get() = _binding!!

    private val vm: ContactUsViewModel by viewModels()

    private var ticketId: Int? = null
    private lateinit var replay: String

    private lateinit var ticketConversationAdapter: TicketConversationAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTicketDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ticketId = requireArguments().getInt("TICKET_ID")

        // call get ticket details by ticket id
        vm.getTicketDetailsById(ticketId)

        initToolbar()
        initRecyclerView()
        setBtnListener()
        subscribeData()
    }

    private fun initToolbar() {
        binding.appBar.setTitleString(getString(R.string.ticket_details))
        binding.appBar.setTitleCenter(true)
        binding.appBar.useBackButton(
            true,
            { findNavController().navigateUp() },
            R.drawable.ic_back,
        )
    }

    private fun initView(ticket: TicketItem?) {
        binding.ticketId.text = ticket?.id.toString()
        binding.type.text = ticket?.type
        binding.priority.text = ticket?.priority
        binding.subject.text = getString(R.string.technical_support_issues)

        // submit data conversation in recycler
        ticketConversationAdapter.submitData(ticket?.conversation)
    }

    private fun initRecyclerView() {
        ticketConversationAdapter = TicketConversationAdapter()
        binding.rvTicketConversation.apply {
            itemAnimator = DefaultItemAnimator()
            ticketConversationAdapter.setHasStableIds(true)
            adapter = ticketConversationAdapter
        }
    }

    private fun setBtnListener() {
        binding.btnSend.setOnClickListener {
            replay = binding.edtReplay.value
            if (replay.isEmptySting()) {
                binding.edtReplay.error = getString(R.string.required)
            } else {
                vm.addReplay(ticketId!!, replay)
            }
        }
    }


    private fun subscribeData() {
        vm.getTicketDetails.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    initView(it.data?.ticket)
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

        vm.addReplay.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    vm.getTicketDetailsById(ticketId)
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