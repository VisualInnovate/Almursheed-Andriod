package com.visualinnovate.almursheed.home.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.databinding.FragmentHireBinding
import com.visualinnovate.almursheed.home.HomeActivity

class HireFragment : Fragment() {

    private var _binding: FragmentHireBinding? = null

    // This property is only valid between onCreateView and
    // onDestroy.
    private val binding get() = _binding!!

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
        (requireActivity() as HomeActivity).changeSelectedBottomNavListener(R.id.action_hireFragment)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
