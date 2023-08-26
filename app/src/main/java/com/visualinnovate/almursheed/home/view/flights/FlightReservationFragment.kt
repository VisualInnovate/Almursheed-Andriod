package com.visualinnovate.almursheed.home.view.flights

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.toast
import com.visualinnovate.almursheed.databinding.FragmentFlightReservationBinding
import com.visualinnovate.almursheed.home.HomeActivity
import com.visualinnovate.almursheed.home.adapter.FlightAdapter
import com.visualinnovate.almursheed.home.model.FlightModel

class FlightReservationFragment : Fragment() {

    private var _binding: FragmentFlightReservationBinding? = null

    // This property is only valid between onCreateView and
    // onDestroy.
    private val binding get() = _binding!!

    private lateinit var flightAdapter: FlightAdapter

    private val btnBoobNowFlightCallBack: (flight: FlightModel) -> Unit =
        { flight ->
            toast("Clicked Item flight $flight")
            // val bundle = Bundle()
            // bundle.putString("memberName", chat.memberName)
            // findNavController().navigate(R.id.global_to_MessagesFragment, bundle)
        }

    private val btnBackCallBackFunc: () -> Unit = {
        findNavController().navigateUp()
    }

    private val btnSortCallBackFunc: () -> Unit = {
        toast("Clicked btnSortCallBackFunc")
    }

    private val btnFilterCallBackFunc: () -> Unit = {
        toast("Clicked btnFilterCallBackFunc")
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
        (requireActivity() as HomeActivity).changeSelectedBottomNavListener(R.id.action_flightReservation)
        initToolbar()
        initViews()
    }

    private fun initToolbar() {
        binding.appBarFlight.setTitleString(getString(R.string.flight))
        binding.appBarFlight.setTitleCenter(true)
        binding.appBarFlight.useBackButton(
            true,
            btnBackCallBackFunc,
            R.drawable.ic_back
        )
        binding.appBarFlight.showButtonSortAndFilter(
            getString(R.string.sort),
            getString(R.string.filter),
            R.drawable.ic_sort,
            R.drawable.ic_filter,
            btnSortCallBackFunc,
            btnFilterCallBackFunc
        )
    }

    private fun initViews() {
        initSeeAllDriverRecycler()
    }

    private fun initSeeAllDriverRecycler() {
        binding.rvFlight.apply {
            flightAdapter = FlightAdapter(btnBoobNowFlightCallBack)
            flightAdapter.submitData(getFlightList())
            adapter = flightAdapter
        }
    }

    private fun getFlightList(): ArrayList<FlightModel> {
        val flightList = ArrayList<FlightModel>()

        flightList.add(
            FlightModel(
                1,
                "Egypt Air Line",
                R.drawable.img_test,
                "12%"
            )
        )
        flightList.add(
            FlightModel(
                2,
                "Fly Dubai",
                R.drawable.ic_egypt_air,
                "30%"
            )
        )
        flightList.add(
            FlightModel(
                3,
                "Qatar Air Line",
                R.drawable.img_driver,
                "50%"
            )
        )
        flightList.add(
            FlightModel(
                3,
                "Oman Air Line",
                R.drawable.ic_egypt_air,
                "50%"
            )
        )
        flightList.add(
            FlightModel(
                3,
                "Saudi Air Line",
                R.drawable.ic_egypt_air,
                "50%"
            )
        )
        flightList.add(
            FlightModel(
                3,
                "Royal Air Line",
                R.drawable.ic_egypt_air,
                "50%"
            )
        )
        flightList.add(
            FlightModel(
                3,
                "Middle East Air Line",
                R.drawable.ic_egypt_air,
                "50%"
            )
        )

        return flightList
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
