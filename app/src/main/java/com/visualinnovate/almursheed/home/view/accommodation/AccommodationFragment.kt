package com.visualinnovate.almursheed.home.view.accommodation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.Constant
import com.visualinnovate.almursheed.common.toast
import com.visualinnovate.almursheed.databinding.FragmentAccommodationBinding
import com.visualinnovate.almursheed.home.HomeActivity
import com.visualinnovate.almursheed.home.adapter.AccommodationAdapter
import com.visualinnovate.almursheed.home.model.AccommodationModel

class AccommodationFragment : Fragment() {

    private var _binding: FragmentAccommodationBinding? = null

    // This property is only valid between onCreateView and
    // onDestroy.
    private val binding get() = _binding!!

    private lateinit var accommodationAdapter: AccommodationAdapter

    private val btnAccommodationClickCallBack: (accommodation: AccommodationModel) -> Unit =
        { accommodation ->
            toast("Clicked Item accommodation $accommodation")
            val bundle = Bundle()
            bundle.putParcelable(Constant.ACCOMMODATION, accommodation)
            findNavController().navigate(R.id.accommodationDetailsFragment, bundle)
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
        _binding = FragmentAccommodationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as HomeActivity).changeSelectedBottomNavListener(R.id.action_accommodationFragment)
        initToolbar()
        initViews()
    }

    private fun initToolbar() {
        binding.appBarAccommodation.setTitleString(getString(R.string.accommodation))
        binding.appBarAccommodation.setTitleCenter(true)
        binding.appBarAccommodation.useBackButton(
            true,
            btnBackCallBackFunc,
            R.drawable.ic_back
        )
        binding.appBarAccommodation.showButtonSortAndFilter(
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
        binding.rvAccommodation.apply {
            accommodationAdapter = AccommodationAdapter(btnAccommodationClickCallBack)
            accommodationAdapter.submitData(getAccommodationList())
            adapter = accommodationAdapter
        }
    }

    private fun getAccommodationList(): ArrayList<AccommodationModel> {
        val driverList = ArrayList<AccommodationModel>()

        driverList.add(
            AccommodationModel(
                0,
                R.drawable.img_test,
                "Mohamed Mohamed",
                "Egypt, Cairo",
                true,
                "120.0"
            )
        )
        driverList.add(
            AccommodationModel(
                1,
                R.drawable.img_driver,
                "Mohamed Ahmed",
                "Egypt, Cairo",
                false,
                "333.2"
            )
        )
        driverList.add(
            AccommodationModel(
                2,
                R.drawable.img_test,
                "Ahmed Mohamed",
                "Egypt, New Cairo",
                true,
                "123.22"
            )
        )
        driverList.add(
            AccommodationModel(
                3,
                R.drawable.img_driver,
                "Mohamed Ahmed",
                "Egypt, October",
                false,
                "111.0"
            )
        )
        driverList.add(
            AccommodationModel(
                4,
                R.drawable.img_test,
                "Ahmed Mohamed",
                "Egypt, Giza",
                true,
                "5555.0"
            )
        )

        driverList.add(
            AccommodationModel(
                4,
                R.drawable.img_test,
                "Ahmed Mohamed",
                "Egypt, Giza",
                true,
                "5555.0"
            )
        )
        driverList.add(
            AccommodationModel(
                4,
                R.drawable.img_test,
                "Ahmed Mohamed",
                "Egypt, Giza",
                true,
                "5555.0"
            )
        )
        driverList.add(
            AccommodationModel(
                4,
                R.drawable.img_test,
                "Ahmed Mohamed",
                "Egypt, Giza",
                true,
                "5555.0"
            )
        )
        driverList.add(
            AccommodationModel(
                4,
                R.drawable.img_test,
                "Ahmed Mohamed",
                "Egypt, Giza",
                true,
                "5555.0"
            )
        )
        driverList.add(
            AccommodationModel(
                4,
                R.drawable.img_test,
                "Ahmed Mohamed",
                "Egypt, Giza",
                true,
                "5555.0"
            )
        )
        driverList.add(
            AccommodationModel(
                4,
                R.drawable.img_test,
                "Ahmed Mohamed",
                "Egypt, Giza",
                true,
                "5555.0"
            )
        )
        driverList.add(
            AccommodationModel(
                4,
                R.drawable.img_test,
                "Ahmed Mohamed",
                "Egypt, Giza",
                true,
                "5555.0"
            )
        )
        driverList.add(
            AccommodationModel(
                4,
                R.drawable.img_test,
                "Ahmed Mohamed",
                "Egypt, Giza",
                true,
                "5555.0"
            )
        )

        driverList.add(
            AccommodationModel(
                4,
                R.drawable.img_test,
                "Ahmed Mohamed",
                "Egypt, Giza",
                true,
                "5555.0"
            )
        )
        driverList.add(
            AccommodationModel(
                4,
                R.drawable.img_test,
                "Ahmed Mohamed",
                "Egypt, Giza",
                true,
                "5555.0"
            )
        )
        driverList.add(
            AccommodationModel(
                4,
                R.drawable.img_test,
                "Ahmed Mohamed",
                "Egypt, Giza",
                true,
                "5555.0"
            )
        )
        driverList.add(
            AccommodationModel(
                4,
                R.drawable.img_test,
                "Ahmed Mohamed",
                "Egypt, Giza",
                true,
                "5555.0"
            )
        )
        driverList.add(
            AccommodationModel(
                4,
                R.drawable.img_test,
                "Ahmed Mohamed",
                "Egypt, Giza",
                true,
                "5555.0"
            )
        )
        driverList.add(
            AccommodationModel(
                4,
                R.drawable.img_test,
                "Ahmed Mohamed",
                "Egypt, Giza",
                true,
                "5555.0"
            )
        )

        return driverList
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
