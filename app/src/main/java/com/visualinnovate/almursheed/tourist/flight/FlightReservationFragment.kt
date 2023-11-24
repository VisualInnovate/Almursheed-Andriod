package com.visualinnovate.almursheed.tourist.flight

import android.content.Intent
import android.net.Uri
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
import com.visualinnovate.almursheed.databinding.FragmentFlightReservationBinding
import com.visualinnovate.almursheed.MainActivity
import com.visualinnovate.almursheed.tourist.flight.adapter.FlightAdapter
import com.visualinnovate.almursheed.home.model.FlightItem
import com.visualinnovate.almursheed.tourist.flight.viewmodel.FlightViewModel
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FlightReservationFragment : BaseFragment() {

    private var _binding: FragmentFlightReservationBinding? = null
    private val binding get() = _binding!!

    private val vm: FlightViewModel by viewModels()
    private lateinit var flightAdapter: FlightAdapter

    private val btnBoobNowFlightCallBack: (flight: FlightItem) -> Unit =
        { flight ->
            shareNews(flight.link)
        }


    /*private val btnSortCallBackFunc: () -> Unit = {
        toast("Clicked btnSortCallBackFunc")
    }

    private val btnFilterCallBackFunc: () -> Unit = {
        toast("Clicked btnFilterCallBackFunc")
    }*/

    override fun onStart() {
        super.onStart()
        vm.fetchFlightResponse()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFlightReservationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).changeSelectedBottomNavListener(R.id.action_flightReservation)
        initToolbar()
        initViews()
        subscribeData()
    }

    private fun initViews() {
        initFlightRecycler()
    }

    private fun initToolbar() {
        binding.appBarFlight.setTitleString(getString(R.string.flight))
        binding.appBarFlight.setTitleCenter(true)
        binding.appBarFlight.useBackButton(
            true,
            { findNavController().navigateUp() },
            R.drawable.ic_back
        )
        /*binding.appBarFlight.showButtonSortAndFilter(
            getString(R.string.sort),
            getString(R.string.filter),
            R.drawable.ic_sort,
            R.drawable.ic_filter,
            btnSortCallBackFunc,
            btnFilterCallBackFunc
        )*/
    }

    private fun initFlightRecycler() {
        binding.rvFlight.apply {
            flightAdapter = FlightAdapter(btnBoobNowFlightCallBack)
            adapter = flightAdapter
        }
    }

    private fun subscribeData() {
        vm.flightLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    // bind data to the view
                    flightAdapter.submitData(it.data!!.flights)
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

    private fun shareNews(flightLink: String?) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(flightLink))
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
