package com.visualinnovate.almursheed.commonView.chat.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.base.BaseFragment
import com.visualinnovate.almursheed.common.hideKeyboard
import com.visualinnovate.almursheed.common.isEmptySting
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.common.value
import com.visualinnovate.almursheed.commonView.chat.adapters.ChatAdapter
import com.visualinnovate.almursheed.commonView.chat.viewModel.ChatViewModel
import com.visualinnovate.almursheed.databinding.FragmentChatBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragment : BaseFragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private val vm: ChatViewModel by viewModels()

    private var message: String? = null

    private lateinit var chatAdapter: ChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        subscribeData()
        setBtnListener()
        initAppbar()
    }

    private fun initAppbar() {
        binding.appBar.setTitleString(getString(R.string.live_chat))
        binding.appBar.useBackButton(
            true,
            { findNavController().navigateUp() },
            R.drawable.ic_back,
        )
    }
    private fun subscribeData() {
        vm.messages.observe(viewLifecycleOwner) {
            chatAdapter.submitList(it!!)
            binding.edtSendMessage.setText("")
        }
        vm.loading.observe(viewLifecycleOwner) {
            if (it) {
                showMainLoading()
            } else {
                hideMainLoading()
            }
        }
    }

    private fun initRecyclerView() {
        chatAdapter = ChatAdapter()
        binding.rvChat.apply {
            chatAdapter.setHasStableIds(true)
            adapter = chatAdapter
        }
    }

    private fun setBtnListener() {
        binding.btnSend.onDebouncedListener {
            message = binding.edtSendMessage.value
            if (message?.isEmptySting() == false) {
                vm.sendMessage(message!!)
                hideKeyboard()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
