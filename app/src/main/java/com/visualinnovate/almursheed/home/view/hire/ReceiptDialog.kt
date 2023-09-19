package com.visualinnovate.almursheed.home.view.hire

import android.app.Dialog
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.databinding.LayoutReceiptBinding
import com.visualinnovate.almursheed.home.viewmodel.HireViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReceiptDialog(
) : DialogFragment() {

    private var _binding: LayoutReceiptBinding? = null
    private val binding get() = _binding!!

    private val vm: HireViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (dialog != null && dialog!!.window != null) {
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        }
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = LayoutReceiptBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog!!.setCanceledOnTouchOutside(true)
        dialog!!.window!!.setBackgroundDrawableResource(R.drawable.bg_receipt)
        setBtnListener()
        initView()
        subscribeData()
    }

    private fun initView() {}

    private fun setBtnListener() {
        binding.btnHire.onDebouncedListener {
            dialog?.dismiss()
        }
        binding.btnClose.onDebouncedListener {
            dialog?.dismiss()
        }
    }

    private fun subscribeData() {
        /*vm.createOrderLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    // save user
                    toast(it.data?.message.toString())
                    // findNavController().navigateUp()
                }

                is ResponseHandler.Error -> {
                    // show error message
                    toast(it.message)
                    Log.d("ResponseHandler.Error", it.message)
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
        }*/
    }

    override fun onStart() {
        super.onStart()
        setDialogStyle(dialog)
    }

    private fun setDialogStyle(dialog: Dialog?) {
        val window = dialog?.window
        val size = Point()
        val display = window?.windowManager?.defaultDisplay
        display?.getSize(size)
        val lp = window!!.attributes
        lp.width = (size.x - dpToPx(64))
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window.attributes = lp

        window.setGravity(Gravity.CENTER)
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
