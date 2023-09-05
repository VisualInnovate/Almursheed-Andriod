package com.visualinnovate.almursheed.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.visualinnovate.almursheed.utils.Utils
import com.visualinnovate.almursheed.databinding.ItemBannerBinding
import com.visualinnovate.almursheed.home.model.BannerModel

class BannerViewPagerAdapter(
    private var context: Context,
    private val btnBannerClickCallBack: (bannerModel: BannerModel) -> Unit
) : PagerAdapter() {

    private lateinit var binding: ItemBannerBinding

    private var bannerImages: List<BannerModel> = ArrayList()

    override fun getCount(): Int {
        return bannerImages.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    fun submitData(data: List<BannerModel>) {
        bannerImages = data
        notifyDataSetChanged()
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        binding = ItemBannerBinding.inflate(LayoutInflater.from(context), container, false)

        // Initialize and set data to your views
        Utils.loadImage(context, bannerImages[position].imageBanner, binding.imgBanner)

        binding.root.setOnClickListener {
            btnBannerClickCallBack.invoke(bannerImages[position])
        }

        container.addView(binding.root, 0)
        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val viewPager = container as ViewPager
        val view = `object` as View
        viewPager.removeView(view)
    }

    init {
        context = context
    }
}
