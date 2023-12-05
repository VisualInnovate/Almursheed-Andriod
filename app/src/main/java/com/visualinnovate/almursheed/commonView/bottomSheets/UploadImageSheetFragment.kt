package com.visualinnovate.almursheed.commonView.bottomSheets

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.visualinnovate.almursheed.auth.adapter.UploadImageAdapter
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.databinding.FragmentUploadImageBinding
import com.visualinnovate.almursheed.utils.Constant

class UploadImageSheetFragment(
    val onSelectImageBtnClick: () -> Unit,
    val onDismissCallBack: () -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: FragmentUploadImageBinding? = null

    private val binding get() = _binding!!

    private lateinit var uploadImageAdapter: UploadImageAdapter
    private var images: ArrayList<String>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentUploadImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        setStyle(STYLE_NO_FRAME, 0)
        // offerArgs = arguments?.getParcelable(Constant.OFFER_DETAILS)
        // Log.d("arguments?", "offerArgs $offerArgs")
        images = requireArguments().getStringArrayList(Constant.UPLOAD_IMAGE_FRAGMENT)
        initRecycler()
        setBtnListener()
    }

    private fun initRecycler() {
        binding.rvUploadImage.apply {
            uploadImageAdapter = UploadImageAdapter()
            uploadImageAdapter.submitData(getImagesList())
            adapter = uploadImageAdapter
        }
    }

    private fun setBtnListener() {
        binding.icClose.onDebouncedListener {
            dismiss()
        }
        binding.btnSelectImages.onDebouncedListener {
            onSelectImageBtnClick.invoke()
        }

        binding.btnSave.onDebouncedListener {
            dismiss()
        }
    }

    private fun getImagesList(): List<String> {
        return images!!
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissCallBack.invoke()
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
