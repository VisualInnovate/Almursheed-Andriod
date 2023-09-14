package com.visualinnovate.almursheed.home.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.visualinnovate.almursheed.databinding.FragmentHomeDriverAndGuideBinding

class HomeDriverAndGuideFragment : Fragment() {
    private var _binding: FragmentHomeDriverAndGuideBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeDriverAndGuideBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        setBtnListener()
        subscribeData()
    }

    private fun initToolbar() {

    }

    private fun setBtnListener() {

    }

    private fun subscribeData() {}

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}