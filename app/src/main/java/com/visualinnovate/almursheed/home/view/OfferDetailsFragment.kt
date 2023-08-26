package com.visualinnovate.almursheed.home.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.visualinnovate.almursheed.common.Constant
import com.visualinnovate.almursheed.databinding.FragmentOfferDetailsBinding
import com.visualinnovate.almursheed.home.model.OfferModel

class OfferDetailsFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentOfferDetailsBinding? = null

    private val binding get() = _binding!!

    private var offerArgs: OfferModel? = null

    companion object {
        fun newInstance(bundle: Bundle): OfferDetailsFragment {
            val fragment = OfferDetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOfferDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        offerArgs = arguments?.getParcelable(Constant.OFFER_DETAILS)
        Log.d("arguments?", "offerArgs $offerArgs")
        initView()
        setBtnListener()
    }

    private fun initView() {
        binding.imgOffer.setImageResource(offerArgs?.offerImage!!)
    }

    private fun setBtnListener() {
        binding.icClose.setOnClickListener {
            dismiss()
        }
        binding.btnBookNow.setOnClickListener { }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
