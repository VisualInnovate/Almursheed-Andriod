package com.visualinnovate.almursheed.tourist.offer

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.invisible
import com.visualinnovate.almursheed.common.toast
import com.visualinnovate.almursheed.databinding.FragmentOfferDetailsBinding
import com.visualinnovate.almursheed.home.model.Offer
import com.visualinnovate.almursheed.home.viewmodel.HomeViewModel
import com.visualinnovate.almursheed.utils.Constant
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OfferDetailsFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentOfferDetailsBinding? = null
    private val binding get() = _binding!!

    private val vm: HomeViewModel by viewModels()

    private var offerId: Int? = null

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

        // get argument
        offerId = requireArguments().getInt(Constant.OFFER_ID)

        // call api to get offer details by offer id
        vm.getOfferDetailsById(offerId ?: 0)

        setBtnListener()
        subscribeData()
    }

    @SuppressLint("SetTextI18n")
    private fun initView(offer: Offer?) {
        binding.txtBoatTour.text = offer?.title ?: ""

        binding.realPrice.invisible()
        Glide.with(requireContext())
            .load(offer?.media?.get(0)?.originalUrl)
            .error(R.drawable.ic_mursheed_logo)
            .into(binding.imgOffer)

        binding.OfferDescription.text = offer?.title ?: ""
        binding.price.text = "${offer?.price?.toString()} $"
    }

    private fun setBtnListener() {
        binding.icClose.setOnClickListener {
            dismiss()
        }
        binding.btnBookNow.setOnClickListener { }
    }

    private fun subscribeData() {
        // observe in drivers list
        vm.offerDetailsLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    // bind data to the view
                    initView(it.data?.offer)
                }

                is ResponseHandler.Error -> {
                    // show error message
                    toast(it.message)
                }

                is ResponseHandler.Loading -> {
                    // show a progress bar
                }

                is ResponseHandler.StopLoading -> {
                    // show a progress bar
                }

                else -> {}
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
