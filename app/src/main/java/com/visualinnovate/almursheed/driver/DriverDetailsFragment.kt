package com.visualinnovate.almursheed.driver

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.bumptech.glide.Glide
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.base.BaseFragment
import com.visualinnovate.almursheed.common.toast
import com.visualinnovate.almursheed.commonView.price.adapters.MyPricesAdapter
import com.visualinnovate.almursheed.databinding.FragmentDriverDetailsBinding
import com.visualinnovate.almursheed.home.model.DriverAndGuideItem
import com.visualinnovate.almursheed.home.viewmodel.HomeViewModel
import com.visualinnovate.almursheed.utils.Constant
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt

@AndroidEntryPoint
class DriverDetailsFragment : BaseFragment() {

    private var _binding: FragmentDriverDetailsBinding? = null
    private val binding get() = _binding!!

    private val vm: HomeViewModel by viewModels()

    private var driverId: Int? = null
    private lateinit var pricesAdapter: MyPricesAdapter

    private val btnBackCallBackFunc: () -> Unit = {
        findNavController().navigateUp()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDriverDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        driverId = requireArguments().getInt(Constant.DRIVER_ID)
        initToolbar()
        subscribeData()
        initPricesRecyclerView()
    }

    override fun onStart() {
        super.onStart()
        vm.getDriverDetailsById(driverId)
    }

    private fun initToolbar() {
        binding.appBarDriverDetails.setTitleString(getString(R.string.driver_details))
        binding.appBarDriverDetails.setTitleCenter(true)
        binding.appBarDriverDetails.useBackButton(
            true,
            btnBackCallBackFunc,
            R.drawable.ic_back,
        )
    }

    private fun subscribeData() {
        // observe in drivers list
        vm.driverDetailsLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    // bind data to the view
                    initViews(it.data!!.driver)
                    pricesAdapter.submitData(it.data.driver!!.priceServices)
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

    @SuppressLint("SetTextI18n")
    private fun initViews(driver: DriverAndGuideItem?) {
        Glide.with(requireContext())
            .load(driver?.personalPhoto)
            .into(binding.imgDriver)

        if (driver?.carPhoto?.isNotEmpty() == true) {
            Glide.with(requireContext())
                .load(driver?.carPhoto?.get(0) ?: "")
                .into(binding.imgCar)
        }

        binding.driverName.text = driver?.name ?: ""
        binding.driverCountry.text = driver?.country ?: ""
        binding.driverCity.text = driver?.state ?: ""
        binding.driverDescription.text = driver?.bio ?: ""
        binding.carName.text = driver?.carModel ?: ""
        binding.carType.text = driver?.carType ?: ""

        binding.driverReview.text = "(${driver?.count_rate ?: 0} review)"

        val rateImage = when (driver?.totalRating?.toDouble()?.roundToInt() ?: 0) {
            1 -> R.drawable.ic_star_1
            2 -> R.drawable.ic_stars_2
            3 -> R.drawable.ic_stars_3
            4 -> R.drawable.ic_stars_4
            5 -> R.drawable.ic_stars_5
            else -> R.drawable.ic_group_rate
        }

        binding.driverReview.setCompoundDrawables(null, null, resources.getDrawable(rateImage), null)
        binding.driverReview.text = "(${driver?.count_rate} review)"

        driver?.priceServices?.let {
            if (it.isNotEmpty()) {
                binding.driverPrice.text = "$ ${it[0]?.price}"
            } else {
                binding.driverPrice.text = "$0.0"
            }
        }
    }

    private fun initPricesRecyclerView() {
        pricesAdapter = MyPricesAdapter()
        binding.pricesRV.apply {
            itemAnimator = DefaultItemAnimator()
            pricesAdapter.setHasStableIds(true)
            adapter = pricesAdapter
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
