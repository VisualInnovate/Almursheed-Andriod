package com.visualinnovate.almursheed.commonView.notification

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.base.BaseFragment
import com.visualinnovate.almursheed.common.toast
import com.visualinnovate.almursheed.commonView.notification.adapter.NotificationAdapter
import com.visualinnovate.almursheed.commonView.notification.model.NotificationItem
import com.visualinnovate.almursheed.commonView.notification.viewmodel.NotificationViewModel
import com.visualinnovate.almursheed.databinding.FragmentNotificationBinding
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationFragment : BaseFragment() {

    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    private val vm: NotificationViewModel by viewModels()

    private lateinit var notificationAdapter: NotificationAdapter

    private val btnNotificationItemClickCallBack: (item: NotificationItem) -> Unit = {
        Log.d("MyDebugData", "MyOrdersFragment :  :  $it")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // call api get all notification mark as reed
        vm.getMarkAsReadNotificationResponse()

        initToolbar()
        initRecycler()
        subscribeData()
    }

    private fun initToolbar() {
        binding.appBar.setTitleString(getString(R.string.notifictions))
        binding.appBar.setTitleCenter(true)
        binding.appBar.useBackButton(
            true,
            { findNavController().navigateUp() },
            R.drawable.ic_back,
        )
    }

    private fun initRecycler() {
        binding.rvNotification.apply {
            notificationAdapter = NotificationAdapter(btnNotificationItemClickCallBack)
            adapter = notificationAdapter
        }
    }

    private fun subscribeData() {
        vm.getMarkAsReadNotificationLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    notificationAdapter.submitData(it.data?.data)
                    toast(it.data?.message.toString())
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