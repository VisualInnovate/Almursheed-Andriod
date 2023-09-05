package com.visualinnovate.almursheed.home.view

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.common.value
import com.visualinnovate.almursheed.databinding.FragmentHireBinding
import com.visualinnovate.almursheed.home.MainActivity

class HireFragment : Fragment() {

    private var _binding: FragmentHireBinding? = null
    private val binding get() = _binding!!

    private var startDate: String? = null
    private var endDate: String? = null

    private val btnBackCallBackFunc: () -> Unit = {
        findNavController().navigateUp()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHireBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).changeSelectedBottomNavListener(R.id.action_hireFragment)
        initToolbar()
        setBtnListener()
    }

    private fun initToolbar() {
        binding.appBarHire.setTitleString(getString(R.string.hire))
        binding.appBarHire.setTitleCenter(true)
        binding.appBarHire.useBackButton(
            true,
            btnBackCallBackFunc,
            R.drawable.ic_back
        )
    }

    private fun setBtnListener() {
        binding.icSwitchDate.onDebouncedListener {
            // Get the text from the first EditText
            startDate = binding.edtStartDate.value

            // Get the text from the second EditText
            endDate = binding.edtEndDate.value

            // Set the text of the first EditText to the text from the second EditText
            binding.edtStartDate.setText(endDate)

            // Set the text of the second EditText to the text from the first EditText
            binding.edtEndDate.setText(startDate)
        }
        binding.driver.setOnClickListener {
            binding.driver.setBackgroundResource(R.drawable.bg_rectangle_corner_primary)
            binding.guide.setBackgroundColor(Color.TRANSPARENT)
            // gender = "1" // -> Constant
        }
        binding.guide.setOnClickListener {
            binding.guide.setBackgroundResource(R.drawable.bg_rectangle_corner_primary)
            binding.driver.setBackgroundColor(Color.TRANSPARENT)
            // gender = "2" // -> Constant
        }

        binding.btnHire.onDebouncedListener {
            // navigate to payment screen
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
