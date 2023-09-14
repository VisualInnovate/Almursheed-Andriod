package com.visualinnovate.almursheed.home.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.common.toast
import com.visualinnovate.almursheed.databinding.FragmentHomeDriverAndGuideBinding
import com.visualinnovate.almursheed.home.MainActivity
import com.visualinnovate.almursheed.home.adapter.BannerViewPagerAdapter
import com.visualinnovate.almursheed.home.adapter.DaysAdapter
import com.visualinnovate.almursheed.home.adapter.MyOrdersDriveAdapter
import com.visualinnovate.almursheed.home.model.BannerModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PriceFragment : Fragment() {
    private var _binding: FragmentHomeDriverAndGuideBinding? = null
    private val binding get() = _binding!!

    private lateinit var bannerViewPagerAdapter: BannerViewPagerAdapter
    private lateinit var myOrdersAdapter: MyOrdersDriveAdapter

    // m.nassar@visualinnovate.com
    //
    private val btnBannerClickCallBack: (banner: BannerModel) -> Unit = { banner ->
        toast("Clicked Item banner $banner")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeDriverAndGuideBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).changeSelectedBottomNavListener(R.id.action_home_driver_guide)
        initViews()
        setBtnListener()
        subscribeData()
    }

    private fun initViews() {
        initBannerRecycler()
        setDataToEarningView()
        initOrdersRecyclerView()
    }

    private fun setBtnListener() {
        binding.seeAllOrders.onDebouncedListener {
        }
    }

    private fun setDataToEarningView() {
        binding.earningTime.text = getString(R.string.number_hours, "25")
        binding.totalEarning.text = "25.300"
        binding.clientNumber.text = getString(R.string.number_clients, "100")
    }
    private fun subscribeData() {}


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

    // Start the auto slider
    private fun startAutoSlider() {
        var currentPage = 0
        lifecycleScope.launch {
            delay(2000)
            if (currentPage == getBannerList().size) {
                currentPage = 0
            }
            binding.rvBanner.setCurrentItem(currentPage++, true)
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

    private fun initOrdersRecyclerView() {
        myOrdersAdapter = MyOrdersDriveAdapter()
        binding.myOrdersRecyclerView.apply {
            itemAnimator = DefaultItemAnimator()
            myOrdersAdapter.setHasStableIds(true)
            adapter = myOrdersAdapter
        }
        myOrdersAdapter.submitData(arrayListOf("c", "d", "e", "f", "g", "h"))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
