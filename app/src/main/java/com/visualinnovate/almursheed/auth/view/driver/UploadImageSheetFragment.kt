package com.visualinnovate.almursheed.auth.view.driver

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.auth.adapter.UploadImageAdapter
import com.visualinnovate.almursheed.databinding.FragmentUploadImageBinding
import com.visualinnovate.almursheed.utils.Constant

class UploadImageSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentUploadImageBinding? = null

    private val binding get() = _binding!!

    private lateinit var uploadImageAdapter: UploadImageAdapter
    private var images: List<String>? = null
    private var image: String = ""

    companion object {
        fun newInstance(bundle: Bundle): UploadImageSheetFragment {
            val fragment = UploadImageSheetFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUploadImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // offerArgs = arguments?.getParcelable(Constant.OFFER_DETAILS)
        // Log.d("arguments?", "offerArgs $offerArgs")
        image = requireArguments().getString(Constant.UPLOAD_IMAGE_FRAGMENT) ?: ""
        loadImage(image)
        // initRecycler()
        setBtnListener()
    }

    private fun loadImage(path: String) {
        val bitmap = BitmapFactory.decodeFile(path)
        Glide.with(requireContext())
            .load(bitmap)
            // .circleCrop()
            .placeholder(R.drawable.img_test)
            .error(R.drawable.img_test)
            .into(binding.image)
    }

    private fun initRecycler() {
        binding.rvUploadImage.apply {
            uploadImageAdapter = UploadImageAdapter()
            uploadImageAdapter.submitData(getImagesList())
            adapter = uploadImageAdapter
        }
    }

    private fun setBtnListener() {
        binding.icClose.setOnClickListener {
            dismiss()
        }
    }

    private fun getImagesList(): ArrayList<Int> {
        val imagesList = ArrayList<Int>()

        imagesList.add(R.drawable.img_test)
        imagesList.add(R.drawable.img_test)

        return imagesList
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
