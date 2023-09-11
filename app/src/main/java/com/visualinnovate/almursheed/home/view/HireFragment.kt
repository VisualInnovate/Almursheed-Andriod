package com.visualinnovate.almursheed.home.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.databinding.FragmentHireBinding
import com.visualinnovate.almursheed.home.MainActivity
import com.visualinnovate.almursheed.home.adapter.DaysAdapter

class HireFragment : Fragment() {

    private var _binding: FragmentHireBinding? = null
    private val binding get() = _binding!!

    private lateinit var daysAdapter: DaysAdapter

    private val btnBackCallBackFunc: () -> Unit = {
        findNavController().navigateUp()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHireBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).changeSelectedBottomNavListener(R.id.action_hireFragment)
        initToolbar()
        setBtnListener()
        initView()
        initRecyclerView()
    }

    private fun initToolbar() {
        binding.appBarHire.setTitleString(getString(R.string.hire))
        binding.appBarHire.setTitleCenter(true)
        binding.appBarHire.useBackButton(
            true,
            btnBackCallBackFunc,
            R.drawable.ic_back,
        )
    }

    private fun initView() {
    }
    private fun setBtnListener() {
        binding.driver.setOnClickListener {
            binding.driver.setBackgroundResource(R.drawable.bg_rectangle_corner_green_border)
            binding.guide.setBackgroundResource(R.drawable.bg_rectangle_corner_grey_border)
        }
        binding.guide.setOnClickListener {
            binding.guide.setBackgroundResource(R.drawable.bg_rectangle_corner_green_border)
            binding.driver.setBackgroundResource(R.drawable.bg_rectangle_corner_grey_border)
        }

        binding.btnHire.onDebouncedListener {
            // navigate to payment screen
        }

        binding.inCityCheckBox.setOnCheckedChangeListener { _, isChecked ->
             binding.betweenCityCheckBox.isChecked = !isChecked
        }

        binding.betweenCityCheckBox.setOnCheckedChangeListener { _, isChecked ->
            binding.inCityCheckBox.isChecked = !isChecked
        }
    }

    private fun initRecyclerView() {
        val x = arrayListOf<String>("Day One", "Day Two", "Day Three")
        daysAdapter = DaysAdapter()
        binding.daysRecyclerView.apply {
            itemAnimator = DefaultItemAnimator()
            daysAdapter.setHasStableIds(true)
            adapter = daysAdapter
        }
        daysAdapter.submitData(x)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
