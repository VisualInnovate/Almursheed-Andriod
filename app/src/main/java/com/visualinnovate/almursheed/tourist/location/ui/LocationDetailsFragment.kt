package com.visualinnovate.almursheed.tourist.location.ui

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
import com.visualinnovate.almursheed.common.toast
import com.visualinnovate.almursheed.databinding.FragmentLocationDetailsBinding
import com.visualinnovate.almursheed.home.model.AttractiveLocation
import com.visualinnovate.almursheed.tourist.location.adapter.ImagesAttractiveAdapter
import com.visualinnovate.almursheed.tourist.location.viewmodel.LocationViewModel
import com.visualinnovate.almursheed.utils.Constant
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationDetailsFragment : BaseFragment() {

    private var _binding: FragmentLocationDetailsBinding? = null
    private val binding get() = _binding!!

    private val vm: LocationViewModel by viewModels()

    private var attractiveLocationItem: AttractiveLocation? = null

    private var locationId: Int? = null

    private lateinit var imagesAttractiveAdapter: ImagesAttractiveAdapter

    private val btnImageClickCallBack: (image: String) -> Unit =
        { attractiveImage ->
            // val bundle = Bundle()
            // bundle.putInt(Constant.ACCOMMODATION_ID, accommodation.id!!)
            // findNavController().customNavigate(R.id.accommodationDetailsFragment, false, bundle)
            Glide.with(requireContext())
                .load(attractiveImage)
                .error(R.drawable.ic_mursheed_logo)
                .into(binding.detailsImage)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        locationId = requireArguments().getInt(Constant.LOCATION_DETAILS)

        // call api to get attractive details by id
        vm.getAttractiveDetailsById(locationId)

        initToolbar()
        setBtnListener()
        subscribeData()
    }

    private fun initToolbar() {
        binding.appBarLocationDetails.setTitleString(getString(R.string.location_details))
        binding.appBarLocationDetails.setTitleCenter(true)
        binding.appBarLocationDetails.useBackButton(
            true,
            { findNavController().navigateUp() },
            R.drawable.ic_back
        )
    }

    private fun subscribeData() {
        vm.attractiveDetailsLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    // bind data to the view
                    attractiveLocationItem = it.data?.attractiveLocation
                    initView(it.data?.attractiveLocation)
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

    private fun initView(attractive: AttractiveLocation?) {
        binding.rvImageDetails.apply {
            imagesAttractiveAdapter = ImagesAttractiveAdapter(btnImageClickCallBack)
            adapter = imagesAttractiveAdapter
        }

        Glide.with(requireContext())
            .load(attractive?.photos?.get(0))
            .error(R.drawable.ic_mursheed_logo)
            .into(binding.detailsImage)

        binding.pinDetails.text = attractive?.name?.localized
        binding.detailsCountry.text = attractive?.country
        binding.detailsCity.text = attractive?.state
        binding.detailsDescription.text = attractive?.description?.localized
        binding.locationLink.text = attractive?.location

        imagesAttractiveAdapter.submitData(attractive?.photos)
    }

    private fun setBtnListener() {
        // clicked to copy link or string
        binding.locationLink.setOnClickListener {
            copyToClipboard(attractiveLocationItem?.location ?: "")
        }

        binding.imgLocationMaps.setOnClickListener {
            openGoogleMaps(attractiveLocationItem?.location ?: "")
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
