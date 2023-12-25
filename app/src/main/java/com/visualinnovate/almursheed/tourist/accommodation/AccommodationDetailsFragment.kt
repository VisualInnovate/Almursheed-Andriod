package com.visualinnovate.almursheed.tourist.accommodation

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.base.BaseFragment
import com.visualinnovate.almursheed.common.gone
import com.visualinnovate.almursheed.common.toast
import com.visualinnovate.almursheed.databinding.FragmentAccommodationDetailsBinding
import com.visualinnovate.almursheed.home.model.AccommodationItem
import com.visualinnovate.almursheed.home.model.MediaItem
import com.visualinnovate.almursheed.home.viewmodel.HomeViewModel
import com.visualinnovate.almursheed.tourist.accommodation.adapter.ImagesAccommodationAdapter
import com.visualinnovate.almursheed.utils.Constant
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccommodationDetailsFragment : BaseFragment() {

    private var _binding: FragmentAccommodationDetailsBinding? = null
    private val binding get() = _binding!!

    private val vm: HomeViewModel by viewModels()
    private lateinit var imagesAccommodationAdapter: ImagesAccommodationAdapter

    private var accommodationId: Int? = null
    private var accommodationItem: AccommodationItem? = null

    private val btnBackCallBackFunc: () -> Unit = {
        findNavController().navigateUp()
    }

    private val btnImageClickCallBack: (image: MediaItem) -> Unit =
        { accommodationImage ->
            // val bundle = Bundle()
            // bundle.putInt(Constant.ACCOMMODATION_ID, accommodation.id!!)
            // findNavController().customNavigate(R.id.accommodationDetailsFragment, false, bundle)
            Glide.with(requireContext())
                .load(accommodationImage.originalUrl)
                .placeholder(R.drawable.ic_mursheed_logo)
                .error(R.drawable.ic_mursheed_logo)
                .into(binding.detailsImage)
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

        // call api to get accommodation details by id
        vm.fetchAccommodationDetails(accommodationId!!)

        initToolbar()

        initAccommodationImagesRecycler()
        setBtnListener()
        subscribeData()
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
        if (accommodationItem != null) {
            binding.detailsDescription.text = accommodationItem?.description?.localized
            binding.detailsCountry.text = accommodationItem?.country?.country
            binding.detailsCity.text = accommodationItem?.state?.state
            binding.locationLink.text = accommodationItem?.address?.localized

            if (accommodationItem?.media!!.isNotEmpty()) {
                Glide.with(requireContext())
                    .load(accommodationItem?.media?.get(0)?.originalUrl ?: R.drawable.img_banner)
                    .placeholder(R.drawable.ic_mursheed_logo)
                    .error(R.drawable.ic_mursheed_logo)
                    .into(binding.detailsImage)

                imagesAccommodationAdapter.submitData(accommodationItem?.media)
            }

            if (accommodationItem?.roomType != null) {
                binding.roomType.text = accommodationItem?.roomType?.localized
            } else {
                binding.txtRoomType.gone()
                binding.roomType.gone()
            }

            if (accommodationItem?.infoStatus == 1) {
                binding.ownerName.text = accommodationItem?.ownerInfo?.localized
                // binding.ownerPhone.text = accommodation.ownerPhone?.localized
                binding.locationLink.text = accommodationItem?.address?.localized
            } else {
                binding.txtOwnerInfo.gone()
                binding.ownerName.gone()
                binding.ownerPhone.gone()
                binding.txtLocationInfo.gone()
                binding.locationLink.gone()
                binding.imgLocationMaps.gone()
            }
        }
    }

    private fun initAccommodationImagesRecycler() {
        binding.rvImageDetails.apply {
            imagesAccommodationAdapter = ImagesAccommodationAdapter(btnImageClickCallBack)
            adapter = imagesAccommodationAdapter
        }
    }

    private fun setBtnListener() {
        binding.btnBookNow.setOnClickListener { }

        // clicked to copy link or string
        binding.locationLink.setOnClickListener {
            copyToClipboard(accommodationItem?.address?.localized ?: "")
        }

        binding.imgLocationMaps.setOnClickListener {
            openGoogleMaps(accommodationItem?.address?.localized ?: "")
        }
    }

    private fun copyToClipboard(textToCopy: String, label: String = "Copied Text") {
        val clipboard =
            requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, textToCopy)
        clipboard.setPrimaryClip(clip)

        toast(getString(R.string.text_copied_to_clipboard))
    }

    private fun openGoogleMaps(mapUrl: String) {
        val intentUri = Uri.parse(mapUrl)
        val mapIntent = Intent(Intent.ACTION_VIEW, intentUri)

        // Check if there's an app to handle the intent
        if (mapIntent.resolveActivity(requireContext().packageManager) != null) {
            requireContext().startActivity(mapIntent)
        } else {
            // Handle the case where Google Maps app is not installed
            // or there's no app to handle the intent
            toast(getString(R.string.maps_not_installed))
        }
    }

    private fun subscribeData() {
        vm.accommodationDetailsLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    // bind data to the view
                    accommodationItem = it.data?.accommodation!!
                    initViews()
                }

                is ResponseHandler.Error -> {
                    // show error message
                    toast(it.message)
                }

                is ResponseHandler.Loading -> {
                    // show a progress bar
                    showMainLoading()
                }

                is ResponseHandler.StopLoading -> {
                    // show a progress bar
                    hideMainLoading()
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
