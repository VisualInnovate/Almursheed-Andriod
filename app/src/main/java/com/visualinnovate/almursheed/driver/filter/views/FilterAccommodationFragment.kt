package com.visualinnovate.almursheed.driver.filter.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.visualinnovate.almursheed.databinding.FragmentFilterAccomodationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterAccommodationFragment : Fragment() {

    private var _binding: FragmentFilterAccomodationBinding? = null
    private val binding get() = _binding!!

    private var price: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFilterAccomodationBinding.inflate(inflater, container, false)
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
