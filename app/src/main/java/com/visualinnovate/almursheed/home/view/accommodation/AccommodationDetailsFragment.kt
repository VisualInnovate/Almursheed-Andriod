package com.visualinnovate.almursheed.home.view.accommodation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.Constant
import com.visualinnovate.almursheed.databinding.FragmentAccommodationDetailsBinding
import com.visualinnovate.almursheed.home.model.AccommodationModel

class AccommodationDetailsFragment : Fragment() {

    private var _binding: FragmentAccommodationDetailsBinding? = null
    private val binding get() = _binding!!

    private var accommodationArgs: AccommodationModel? = null

    private val btnBackCallBackFunc: () -> Unit = {
        findNavController().navigateUp()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccommodationDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        accommodationArgs = requireArguments().getParcelable(Constant.ACCOMMODATION)
        initToolbar()
        initViews()
        setBtnListener()
    }

    private fun initToolbar() {
        binding.appBarAccommodationDetails.setTitleString(getString(R.string.accommodation_details))
        binding.appBarAccommodationDetails.setTitleCenter(true)
        binding.appBarAccommodationDetails.useBackButton(
            true,
            btnBackCallBackFunc,
            R.drawable.ic_back
        )
    }

    private fun initViews() {
        binding.detailsImage.setImageResource(accommodationArgs!!.accommodationImage)
    }

    private fun setBtnListener() {
        binding.btnBookNow.setOnClickListener { }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
