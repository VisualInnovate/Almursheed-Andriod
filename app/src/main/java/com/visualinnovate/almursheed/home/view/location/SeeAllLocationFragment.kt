package com.visualinnovate.almursheed.home.view.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.toast
import com.visualinnovate.almursheed.databinding.FragmentSeeAllLocationBinding
import com.visualinnovate.almursheed.home.HomeActivity
import com.visualinnovate.almursheed.home.adapter.SeeAllLocationAdapter
import com.visualinnovate.almursheed.home.model.LocationModel

class SeeAllLocationFragment : Fragment() {

    private var _binding: FragmentSeeAllLocationBinding? = null

    // This property is only valid between onCreateView and
    // onDestroy.
    private val binding get() = _binding!!

    private lateinit var seeAllLocationAdapter: SeeAllLocationAdapter

    private val btnLocationClickCallBack: (location: LocationModel) -> Unit =
        { location ->
            toast("Clicked Item location $location")
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
        _binding = FragmentSeeAllLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initViews()
    }

    private fun initToolbar() {
        binding.appBarSeeAllLocation.setTitleString(getString(R.string.locations))
        binding.appBarSeeAllLocation.setTitleCenter(true)
        binding.appBarSeeAllLocation.useBackButton(
            true,
            btnBackCallBackFunc,
            R.drawable.ic_back
        )
        binding.appBarSeeAllLocation.showButtonSortAndFilter(
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
        binding.rvSeeAllLocation.apply {
            seeAllLocationAdapter = SeeAllLocationAdapter(btnLocationClickCallBack)
            seeAllLocationAdapter.submitData(getSeeAllLocationList())
            adapter = seeAllLocationAdapter
        }
    }

    private fun getSeeAllLocationList(): ArrayList<LocationModel> {
        val locationList = ArrayList<LocationModel>()

        locationList.add(
            LocationModel(
                0,
                R.drawable.img_test,
                "Explore Paris",
                "Egypt, Paris",
                false
            )
        )
        locationList.add(
            LocationModel(
                1,
                R.drawable.img_driver,
                "Istanbul Tour",
                "Egypt, Turkey",
                true
            )
        )
        locationList.add(
            LocationModel(
                2,
                R.drawable.img_driver,
                "Explore Paris",
                "Egypt, Paris",
                false
            )
        )
        locationList.add(
            LocationModel(
                3,
                R.drawable.img_driver,
                "Istanbul Tour",
                "Egypt, Turkey",
                true
            )
        )
        locationList.add(
            LocationModel(
                3,
                R.drawable.img_driver,
                "Istanbul Tour",
                "Egypt, Turkey",
                true
            )
        )
        locationList.add(
            LocationModel(
                3,
                R.drawable.img_driver,
                "Istanbul Tour",
                "Egypt, Turkey",
                true
            )
        )
        locationList.add(
            LocationModel(
                3,
                R.drawable.img_driver,
                "Istanbul Tour",
                "Egypt, Turkey",
                true
            )
        )
        locationList.add(
            LocationModel(
                3,
                R.drawable.img_driver,
                "Istanbul Tour",
                "Egypt, Turkey",
                true
            )
        )
        locationList.add(
            LocationModel(
                3,
                R.drawable.img_driver,
                "Istanbul Tour",
                "Egypt, Turkey",
                true
            )
        )
        locationList.add(
            LocationModel(
                3,
                R.drawable.img_driver,
                "Istanbul Tour",
                "Egypt, Turkey",
                true
            )
        )
        locationList.add(
            LocationModel(
                3,
                R.drawable.img_driver,
                "Istanbul Tour",
                "Egypt, Turkey",
                true
            )
        )
        locationList.add(
            LocationModel(
                3,
                R.drawable.img_driver,
                "Istanbul Tour",
                "Egypt, Turkey",
                true
            )
        )
        locationList.add(
            LocationModel(
                3,
                R.drawable.img_driver,
                "Istanbul Tour",
                "Egypt, Turkey",
                true
            )
        )

        return locationList
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
