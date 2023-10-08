package com.visualinnovate.almursheed.driver.filter.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.commonView.bottomSheets.ChooseTextBottomSheet
import com.visualinnovate.almursheed.commonView.bottomSheets.model.ChooserItemModel
import com.visualinnovate.almursheed.databinding.FragmentFilterGuideBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterGuideFragment : Fragment() {

    private var _binding: FragmentFilterGuideBinding? = null
    private val binding get() = _binding!!

    private var carCategory: String? = null
    private var carModel: String? = null
    private var rate: String? = null
    private var price: Int = 0

    private var carCategories = ArrayList<ChooserItemModel>()
    private var ratingList = ArrayList<ChooserItemModel>()
    private var chooseTextBottomSheet: ChooseTextBottomSheet? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFilterGuideBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        setBtnListener()
    }



    private fun initData() {

    }

    private fun setBtnListener() {

        binding.rate.onDebouncedListener {
        }

        binding.price.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar,
                progress: Int,
                fromUser: Boolean,
            ) {
                price = progress
                binding.txtEndPrice.text = "$price $"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
