package com.visualinnovate.almursheed.home.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.toast
import com.visualinnovate.almursheed.databinding.FragmentHomeBinding
import com.visualinnovate.almursheed.home.adapter.*
import com.visualinnovate.almursheed.home.model.BannerModel
import com.visualinnovate.almursheed.home.model.DriverModel
import com.visualinnovate.almursheed.home.model.GuideModel
import com.visualinnovate.almursheed.home.model.LocationModel
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var bannerAdapter: BannerAdapter
    private lateinit var bannerViewPagerAdapter: BannerViewPagerAdapter
    private lateinit var driverAdapter: DriverAdapter
    private lateinit var guideAdapter: GuideAdapter
    private lateinit var locationAdapter: LocationAdapter

    private val btnBannerClickCallBack: (banner: BannerModel) -> Unit = { banner ->
        toast("Clicked Item banner $banner")
        val bundle = Bundle()
        // bundle.putString("memberName", chat.memberName)
        // findNavController().navigate(R.id.global_to_MessagesFragment, bundle)
    }

    private val btnDriverClickCallBack: (driver: DriverModel) -> Unit = { driver ->
        toast("Clicked Item driver $driver")
        val bundle = Bundle()
        // bundle.putString("memberName", chat.memberName)
        // findNavController().navigate(R.id.global_to_MessagesFragment, bundle)
    }

    private val btnGuideClickCallBack: (guide: GuideModel) -> Unit = { guide ->
        toast("Clicked Item guide $guide")
        val bundle = Bundle()
        // bundle.putString("memberName", chat.memberName)
        // findNavController().navigate(R.id.global_to_MessagesFragment, bundle)
    }

    private val btnLocationClickCallBack: (location: LocationModel) -> Unit = { location ->
        toast("Clicked Item banner $location")
        val bundle = Bundle()
        // bundle.putString("memberName", chat.memberName)
        // findNavController().navigate(R.id.global_to_MessagesFragment, bundle)
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
        initBannerRecycler()
        initDriverRecycler()
        initGuideRecycler()
        initLocationRecycler()
        setBtnListener()
    }

    private fun setBtnListener() {
        binding.seeAllDivers.setOnClickListener { }
        binding.seeAllGuides.setOnClickListener { }
        binding.seeAllPopularLocations.setOnClickListener { }
    }

    private fun initBannerRecycler() {
        bannerViewPagerAdapter = BannerViewPagerAdapter(requireContext(), btnBannerClickCallBack)
        bannerViewPagerAdapter.submitData(getBannerList())
        binding.rvBanner.offscreenPageLimit = 10
        binding.rvBanner.adapter = bannerViewPagerAdapter
        binding.dotsIndicatorBanner.attachTo(binding.rvBanner)

        // startAutoSlider(bannerViewPagerAdapter.count)
        startAutoSlider()
        /*binding.rvBanner.apply {
            bannerAdapter = BannerAdapter(btnBannerClickCallBack)
            bannerAdapter.submitData(getBannerList())
            adapter = bannerAdapter
        }*/
    }

    private var currentPage = 0
    private var timer: Timer? = null

    // Start the auto slider
    private fun startAutoSlider() {
        val handler = Handler(Looper.getMainLooper())

        // Create a new Timer
        timer = Timer()

        // Create a TimerTask to run the ViewPager
        val timerTask: TimerTask = object : TimerTask() {
            override fun run() {
                handler.post {
                    if (currentPage == getBannerList().size) {
                        currentPage = 0
                    }
                    binding.rvBanner.setCurrentItem(currentPage++, true)
                }
            }
        }

        // Schedule the timer to run periodically
        timer?.schedule(timerTask, 2000, 2000)
    }

    private fun initDriverRecycler() {
        binding.rvDriver.apply {
            driverAdapter = DriverAdapter(btnDriverClickCallBack)
            driverAdapter.submitData(getDriverList())
            adapter = driverAdapter
        }
    }

    private fun initGuideRecycler() {
        binding.rvGuide.apply {
            guideAdapter = GuideAdapter(btnGuideClickCallBack)
            guideAdapter.submitData(getGuideList())
            adapter = guideAdapter
        }
    }

    private fun initLocationRecycler() {
        binding.rvPopularLocations.apply {
            locationAdapter = LocationAdapter(btnLocationClickCallBack)
            locationAdapter.submitData(getLocationList())
            adapter = locationAdapter
        }
    }

    private fun getBannerList(): ArrayList<BannerModel> {
        val bannerList = ArrayList<BannerModel>()

        bannerList.add(BannerModel(0, R.drawable.img_banner))
        bannerList.add(BannerModel(1, R.drawable.img_banner))
        bannerList.add(BannerModel(2, R.drawable.img_banner))
        bannerList.add(BannerModel(3, R.drawable.img_banner))

        return bannerList
    }

    private fun getDriverList(): ArrayList<DriverModel> {
        val driverList = ArrayList<DriverModel>()

        driverList.add(
            DriverModel(
                0,
                R.drawable.img_driver,
                4.5,
                "Mohamed Mohamed",
                true,
                120.0,
                "Giza",
                false
            )
        )
        driverList.add(
            DriverModel(
                1,
                R.drawable.img_driver,
                2.0,
                "Mohamed Ahmed",
                false,
                333.2,
                "Cairo",
                false
            )
        )
        driverList.add(
            DriverModel(
                2,
                R.drawable.img_driver,
                1.1,
                "Ahmed Mohamed",
                true,
                123.22,
                "New Cairo",
                true
            )
        )
        driverList.add(
            DriverModel(
                3,
                R.drawable.img_driver,
                5.1,
                "Mohamed Ahmed",
                false,
                111.0,
                "October",
                true
            )
        )
        driverList.add(
            DriverModel(
                4,
                R.drawable.img_driver,
                3.3,
                "Ahmed Mohamed",
                true,
                5555.0,
                "Giza",
                false
            )
        )

        return driverList
    }

    private fun getGuideList(): ArrayList<GuideModel> {
        val guideList = ArrayList<GuideModel>()

        guideList.add(
            GuideModel(
                0,
                R.drawable.img_driver,
                2.2,
                "Ahmed Mohamed",
                120.0,
                "October",
                false
            )
        )
        guideList.add(
            GuideModel(
                1,
                R.drawable.img_driver,
                1.1,
                "Mohamed Mohamed",
                120.0,
                "Giza",
                true
            )
        )
        guideList.add(
            GuideModel(
                2,
                R.drawable.img_driver,
                4.5,
                "Ahmed Ahmed",
                120.0,
                "Cairo",
                false
            )
        )
        guideList.add(
            GuideModel(
                3,
                R.drawable.img_driver,
                3.1,
                "Ahmed Mohamed",
                120.0,
                "Giza",
                true
            )
        )

        return guideList
    }

    private fun getLocationList(): ArrayList<LocationModel> {
        val locationList = ArrayList<LocationModel>()

        locationList.add(
            LocationModel(
                0,
                R.drawable.img_test,
                "Explore Paris",
                "Paris",
                false
            )
        )
        locationList.add(
            LocationModel(
                1,
                R.drawable.img_driver,
                "Istanbul Tour",
                "Turkey",
                true
            )
        )
        locationList.add(
            LocationModel(
                2,
                R.drawable.img_driver,
                "Explore Paris",
                "Paris",
                false
            )
        )
        locationList.add(
            LocationModel(
                3,
                R.drawable.img_driver,
                "Istanbul Tour",
                "Turkey",
                true
            )
        )

        return locationList
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
