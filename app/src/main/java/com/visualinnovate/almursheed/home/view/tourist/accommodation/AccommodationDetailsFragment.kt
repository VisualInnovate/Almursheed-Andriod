package com.visualinnovate.almursheed.home.view.tourist.accommodation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.base.BaseFragment
import com.visualinnovate.almursheed.common.toast
import com.visualinnovate.almursheed.databinding.FragmentAccommodationDetailsBinding
import com.visualinnovate.almursheed.home.adapter.AccommodationImagesAdapter
import com.visualinnovate.almursheed.home.model.AccommodationItem
import com.visualinnovate.almursheed.home.viewmodel.HomeViewModel
import com.visualinnovate.almursheed.utils.Constant
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccommodationDetailsFragment : BaseFragment() {

    private var _binding: FragmentAccommodationDetailsBinding? = null
    private val binding get() = _binding!!

    private val vm: HomeViewModel by viewModels()
    private lateinit var accommodationImagesAdapter: AccommodationImagesAdapter

    private var accommodationId: Int? = null
    private val imagesList = ArrayList<String>()

    private val btnBackCallBackFunc: () -> Unit = {
        findNavController().navigateUp()
    }

    private val btnImageClickCallBack: (image: String) -> Unit =
        { accommodation ->
            Log.d("btnAccommodationClickCallBack", "Clicked Item accommodation $accommodation")
            // val bundle = Bundle()
            // bundle.putInt(Constant.ACCOMMODATION_ID, accommodation.id!!)
            // findNavController().customNavigate(R.id.accommodationDetailsFragment, false, bundle)
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
        accommodationId = requireArguments().getInt(Constant.ACCOMMODATION_ID)
        Log.d("onViewCreated", "accommodationId $accommodationId")
        initToolbar()
        initAccommodationImagesRecycler()
        setBtnListener()
        subscribeData()
    }

    override fun onStart() {
        super.onStart()
        vm.fetchAccommodationDetails(accommodationId!!)
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

    private fun initViews(accommodation: AccommodationItem) {
        Log.d("initViews", "media ${accommodation.media}")
        Glide.with(requireContext())
            .load(accommodation.media?.get(0)?.originalUrl ?: R.drawable.img_banner)
            .into(binding.detailsImage)
        accommodation.media?.forEach { it ->
            imagesList.add(it?.originalUrl!!)
            Log.d("forEach", "media@@ $imagesList")
        }
        // submit data
        accommodationImagesAdapter.submitData(imagesList)
        Log.d("forEach", "media&& $imagesList")

        binding.detailsDescription.text = accommodation.description?.localized
        binding.ownerName.text = accommodation.ownerInfo?.localized
        binding.detailsCountry.text = accommodation.country?.country
        binding.detailsCity.text = accommodation.state?.state
    }

    private fun initAccommodationImagesRecycler() {
        binding.rvImageDetails.apply {
            accommodationImagesAdapter = AccommodationImagesAdapter(btnImageClickCallBack)
            adapter = accommodationImagesAdapter
        }
    }

    private fun setBtnListener() {
        binding.btnBookNow.setOnClickListener { }
        // clicked to copy link or string
        binding.locationLink.setOnClickListener { }
        binding.imgLocationMaps.setOnClickListener { }
    }

    private fun subscribeData() {
        vm.accommodationDetailsLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    // bind data to the view
                    Log.d("ResponseHandler.Success", it.data!!.accommodation!!.toString())
                    initViews(it.data!!.accommodation!!)
                }
                is ResponseHandler.Error -> {
                    // show error message
                    toast(it.message)
                    Log.d("ResponseHandler.Error", it.message)
                }
                is ResponseHandler.Loading -> {
                    // show a progress bar
                }
                else -> {
                    toast("Else")
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
