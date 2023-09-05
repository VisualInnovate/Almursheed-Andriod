package com.visualinnovate.almursheed.home.view.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.databinding.FragmentCompleteEditProfileDriverBinding
import com.visualinnovate.almursheed.home.MainActivity

class CompleteEditProfileDriverFragment : Fragment() {

    private var _binding: FragmentCompleteEditProfileDriverBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompleteEditProfileDriverBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setBtnListener()
    }

    private fun initView() {
        TODO("Not yet implemented")
    }

    private fun setBtnListener() {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
