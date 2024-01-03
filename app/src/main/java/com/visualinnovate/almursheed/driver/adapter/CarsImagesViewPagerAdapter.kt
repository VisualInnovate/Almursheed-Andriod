package com.visualinnovate.almursheed.driver.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.visualinnovate.almursheed.databinding.ItemCarImageBinding

class CarsImagesViewPagerAdapter(
    private var context: Context,
) : PagerAdapter() {

    private lateinit var binding: ItemCarImageBinding

    private var bannerImages: List<String?>? = ArrayList()

    override fun getCount(): Int {
        return bannerImages?.size ?: 0
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    fun submitData(data: List<String?>?) {
        bannerImages = data
        notifyDataSetChanged()
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        binding = ItemCarImageBinding.inflate(LayoutInflater.from(context), container, false)

        // Initialize and set data to your views
        Glide.with(context)
            .load(bannerImages!![position]!!)
            .into(binding.imgBanner)

        container.addView(binding.root, 0)
        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val viewPager = container as ViewPager
        val view = `object` as View
        viewPager.removeView(view)
    }
}
