package com.visualinnovate.almursheed.driver

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.bumptech.glide.Glide
import com.visualinnovate.almursheed.MainActivity
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.base.BaseFragment
import com.visualinnovate.almursheed.common.customNavigate
import com.visualinnovate.almursheed.common.gone
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.common.toast
import com.visualinnovate.almursheed.commonView.price.adapters.MyPricesAdapter
import com.visualinnovate.almursheed.databinding.FragmentDriverDetailsBinding
import com.visualinnovate.almursheed.driver.adapter.CarsImagesViewPagerAdapter
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
    private var driver: DriverAndGuideItem? = null
    private lateinit var pricesAdapter: MyPricesAdapter

    private lateinit var carsImagesViewPagerAdapter: CarsImagesViewPagerAdapter

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
        (requireActivity() as MainActivity).hideBottomNav()

        driverId = requireArguments().getInt(Constant.DRIVER_ID)

        // call api to get driver details by id
        vm.getDriverDetailsById(driverId)

        initToolbar()
        initBannerRecycler()
        initPricesRecyclerView()
        setBtnListeners()
        subscribeData()
    }

    private fun setBtnListeners() {
        binding.btnHire.onDebouncedListener {
            driver?.let {
                val bundle = Bundle()
                bundle.putParcelable("selectedDriverOrGuide", driver)
                bundle.putString("type", Constant.ROLE_DRIVER)
                findNavController().customNavigate(R.id.hireFragment, data = bundle)
            }
        }
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
                    driver = it.data!!.driver
                    initViews(driver)
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

    private fun initBannerRecycler() {
        carsImagesViewPagerAdapter = CarsImagesViewPagerAdapter(requireContext())

        binding.rvCars.offscreenPageLimit = 10
        binding.rvCars.adapter = carsImagesViewPagerAdapter
        binding.dotsIndicator.attachTo(binding.rvCars)
    }

    @SuppressLint("SetTextI18n")
    private fun initViews(driver: DriverAndGuideItem?) {
        Glide.with(requireContext())
            .load(driver?.personalPhoto)
            .error(R.drawable.ic_person)
            .into(binding.imgDriver)

        if (driver?.carPhoto?.isNotEmpty() == true) {
            binding.imgCar.gone()
            carsImagesViewPagerAdapter.submitData(driver.carPhoto)

        } else {
            binding.rvCars.gone()
            binding.dotsIndicator.gone()
            Glide.with(requireContext())
                .load(R.drawable.ic_mursheed_logo)
                .error(R.drawable.ic_mursheed_logo)
                .into(binding.imgCar)
        }

        binding.driverName.text = driver?.name ?: ""
        binding.driverCountry.text = driver?.country ?: ""
        binding.driverCity.text = driver?.state ?: ""
        binding.driverDescription.text = driver?.bio?.trim() ?: ""
        binding.carName.text = driver?.carModel ?: ""
        binding.carType.text = driver?.carType ?: ""

        val rateImage = when (driver?.totalRating?.toDouble()?.roundToInt() ?: 0) {
            1 -> R.drawable.ic_star_1
            2 -> R.drawable.ic_stars_2
            3 -> R.drawable.ic_stars_3
            4 -> R.drawable.ic_stars_4
            5 -> R.drawable.ic_stars_5
            0 -> R.drawable.ic_group_rate
            else -> R.drawable.ic_group_rate
        }

        binding.driverReview.setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(requireContext(), rateImage), null, null, null
        )
        // binding.driverReview.text = "(${driver?.count_rate} ${getString(R.string.review)})"

        driver?.languages.let {
            if (it?.isNotEmpty() == true) {
                binding.languageEnglish.text = driver?.getLanguage()?.joinToString(" , ")
            } else {
                binding.languageEnglish.text = getString(R.string.no_languages)
            }
        }

        driver?.priceServices?.let {
            if (it.isNotEmpty()) {
                binding.driverPrice.text = "$ ${it[0]?.price}"
            } else {
                binding.driverPrice.text = "$ 0.0"
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
