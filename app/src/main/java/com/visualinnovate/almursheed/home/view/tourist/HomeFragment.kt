package com.visualinnovate.almursheed.home.view.tourist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.SharedPreference
import com.visualinnovate.almursheed.common.base.BaseFragment
import com.visualinnovate.almursheed.common.customNavigate
import com.visualinnovate.almursheed.common.toast
import com.visualinnovate.almursheed.databinding.FragmentHomeBinding
import com.visualinnovate.almursheed.home.MainActivity
import com.visualinnovate.almursheed.home.adapter.BannerViewPagerAdapter
import com.visualinnovate.almursheed.home.adapter.DriverAdapter
import com.visualinnovate.almursheed.home.adapter.GuideAdapter
import com.visualinnovate.almursheed.home.adapter.LocationAdapter
import com.visualinnovate.almursheed.home.adapter.OfferAdapter
import com.visualinnovate.almursheed.home.model.AttractivesItem
import com.visualinnovate.almursheed.home.model.BannerModel
import com.visualinnovate.almursheed.home.model.DriverItem
import com.visualinnovate.almursheed.home.model.GuideItem
import com.visualinnovate.almursheed.home.model.OfferItem
import com.visualinnovate.almursheed.home.view.OfferDetailsFragment
import com.visualinnovate.almursheed.home.viewmodel.HomeViewModel
import com.visualinnovate.almursheed.utils.Constant
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val vm: HomeViewModel by viewModels()

    private lateinit var bannerViewPagerAdapter: BannerViewPagerAdapter
    private lateinit var driverAdapter: DriverAdapter
    private lateinit var guideAdapter: GuideAdapter
    private lateinit var offerAdapter: OfferAdapter
    private lateinit var locationAdapter: LocationAdapter

    // m.nassar@visualinnovate.com
    private val btnBannerClickCallBack: (banner: BannerModel) -> Unit = { banner ->
        toast("Clicked Item banner $banner")
        // val bundle = Bundle()
        // bundle.putString("memberName", chat.memberName)
        // findNavController().navigate(R.id.global_to_MessagesFragment, bundle)
    }

    private val btnDriverClickCallBack: (driver: DriverItem) -> Unit = { driver ->
        toast("Clicked Item driver $driver")
        // val bundle = Bundle()
        // bundle.putParcelable(Constant.DRIVER, driver)
        // findNavController().customNavigate(R.id.driverDetailsFragment, false, bundle)
    }

    private val btnGuideClickCallBack: (guide: GuideItem) -> Unit = { guide ->
        toast("Clicked Item guide $guide")
        // val bundle = Bundle()
        // bundle.putString("memberName", chat.memberName)
        // findNavController().navigate(R.id.global_to_MessagesFragment, bundle)
    }

    private val btnLocationClickCallBack: (attractives: AttractivesItem) -> Unit = { attractives ->
        toast("Clicked Item banner $attractives")
        // val bundle = Bundle()
        // bundle.putInt(Constant.ATTRACTIVE_ID, attractives.id!!)
        // findNavController().customNavigate(R.id.locationDetailsFragment, false, bundle)
    }

    private val btnBoobNowOfferCallBack: (offer: OfferItem) -> Unit = { offer ->
        toast("Clicked Item BoobNowOffer $offer")
        // val bundle = Bundle()
        // bundle.putString("memberName", chat.memberName)
        // findNavController().navigate(R.id.global_to_MessagesFragment, bundle)
    }

    private val btnDetailsOfferCallBack: (offer: OfferItem) -> Unit = { offer ->
        showOfferDetailsBottomSheet(offer)
    }

    private fun showOfferDetailsBottomSheet(offer: OfferItem) {
        val bundle = Bundle()
        bundle.putInt(Constant.OFFER_ID, offer.id!!)

        // Create an instance of the bottom sheet dialog fragment with the data
        val offerDetailsSheetFragment = OfferDetailsFragment.newInstance(bundle)

        // Show the bottom sheet dialog fragment
        offerDetailsSheetFragment.show(
            childFragmentManager,
            "OfferDetailsFragment"
        ) // offerDetailsSheetFragment.tag
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).changeSelectedBottomNavListener(R.id.action_home_tourist)
        initView()
        setBtnListener()
        subscribeData()
    }

    override fun onStart() {
        super.onStart()
        Log.d("onStart()", "${SharedPreference.getUser()}")
        vm.getLatestDriver(
            SharedPreference.getUser()?.stateId ?: SharedPreference.getUser()?.desCityId
        )
        vm.getLatestGuides(
            SharedPreference.getUser()?.stateId ?: SharedPreference.getUser()?.desCityId
        )
        // vm.fetchAllDrivers()
        // vm.fetchAllGuides()
        vm.fetchOfferResponse()
        vm.fetchAttractivesList()
    }

    private fun initView() {
        initBannerRecycler()
        initDriverRecycler()
        initGuideRecycler()
        initOfferRecycler()
        initLocationRecycler()
    }

    private fun subscribeData() {
        // observe in drivers list
        vm.driverLatestLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    // bind data to the view
                    Log.d("Success11", "${it.data!!.drivers}")
                    driverAdapter.submitData(it.data!!.drivers)
                }

                is ResponseHandler.Error -> {
                    // show error message
                    toast(it.message)
                    Log.d("Error->DriverList", it.message)
                }

                is ResponseHandler.Loading -> {
                    // show a progress bar
                    showMainLoading()
                }

                is ResponseHandler.StopLoading -> {
                    // show a progress bar
                    hideMainLoading()
                }

                else -> {
                    toast("Driver Else")
                }
            }
        }
        // observe in guides list
        vm.guideLatestLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    // bind data to the view
                    Log.d("Success22", "${it.data!!.guides}")
                    guideAdapter.submitData(it.data.guides)
                }

                is ResponseHandler.Error -> {
                    // show error message
                    toast(it.message)
                    Log.d("Error->Guides", it.message)
                }

                is ResponseHandler.Loading -> {
                    // show a progress bar
                    showMainLoading()
                }

                is ResponseHandler.StopLoading -> {
                    // show a progress bar
                    hideMainLoading()
                }

                else -> {
                    toast("Else")
                }
            }
        }
        // observe in offers list
        vm.offerLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    // bind data to the view
                    offerAdapter.submitData(it.data!!.offers)
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

        vm.attractivesLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    // bind data to the view
                    locationAdapter.submitData(it.data!!.attractives)
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

    private fun setBtnListener() {
        binding.seeAllDivers.setOnClickListener {
            findNavController().customNavigate(R.id.allDriversFragment)
        }
        binding.seeAllGuides.setOnClickListener {
            findNavController().customNavigate(R.id.allGuidesFragment)
        }
        binding.seeAllPopularLocations.setOnClickListener {
            findNavController().customNavigate(R.id.allLocationFragment)
        }
    }

    private fun initBannerRecycler() {
        bannerViewPagerAdapter = BannerViewPagerAdapter(requireContext(), btnBannerClickCallBack)
        bannerViewPagerAdapter.submitData(getBannerList())
        binding.rvBanner.offscreenPageLimit = 10
        binding.rvBanner.adapter = bannerViewPagerAdapter
        binding.dotsIndicatorBanner.attachTo(binding.rvBanner)

        startAutoSlider()
        /*binding.rvBanner.apply {
            bannerAdapter = BannerAdapter(btnBannerClickCallBack)
            bannerAdapter.submitData(getBannerList())
            adapter = bannerAdapter
        }*/
    }

    private var currentPage = 0

    // Start the auto slider
    private fun startAutoSlider() {
        lifecycleScope.launch {
            delay(2000)
            if (currentPage == getBannerList().size) {
                currentPage = 0
            }
            binding.rvBanner.setCurrentItem(currentPage++, true)
        }
    }

    private fun initDriverRecycler() {
        binding.rvDriver.apply {
            driverAdapter = DriverAdapter(btnDriverClickCallBack)
            adapter = driverAdapter
        }
    }

    private fun initGuideRecycler() {
        binding.rvGuide.apply {
            guideAdapter = GuideAdapter(btnGuideClickCallBack)
            adapter = guideAdapter
        }
    }

    private fun initOfferRecycler() {
        binding.rvOffer.apply {
            offerAdapter = OfferAdapter(btnBoobNowOfferCallBack, btnDetailsOfferCallBack)
            adapter = offerAdapter
        }
    }

    private fun initLocationRecycler() {
        binding.rvPopularLocations.apply {
            locationAdapter = LocationAdapter(btnLocationClickCallBack)
            adapter = locationAdapter
            // Use attachTo() instead of setViewPager2()
            // binding.dotsIndicatorBanner.attachTo(this)
        }

        /* viewPagerAdapter = ViewPagerAdapter(getBanners())
        binding.viewPager.adapter = viewPagerAdapter

        val snapHelper = StartSnapHelper()
        snapHelper.attachToRecyclerView(binding.viewPager)

        binding.dotsIndicatorBanner.swipePrevious()
        binding.dotsIndicatorBanner.swipeNext()

        // Use attachTo() instead of setViewPager2()
        binding.dotsIndicatorBanner.*/
    }

    private fun getBannerList(): ArrayList<BannerModel> {
        val bannerList = ArrayList<BannerModel>()

        bannerList.add(BannerModel(0, R.drawable.img_banner))
        bannerList.add(BannerModel(1, R.drawable.img_banner))
        bannerList.add(BannerModel(2, R.drawable.img_banner))
        bannerList.add(BannerModel(3, R.drawable.img_banner))

        return bannerList
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}