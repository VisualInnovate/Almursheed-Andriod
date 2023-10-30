package com.visualinnovate.almursheed.tourist.hire.ui

import android.app.Dialog
import android.content.res.Resources
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.gone
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.common.toast
import com.visualinnovate.almursheed.common.visible
import com.visualinnovate.almursheed.databinding.LayoutReceiptBinding
import com.visualinnovate.almursheed.tourist.hire.viewmodel.HireViewModel
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReceiptDialog : DialogFragment() {

    private var _binding: LayoutReceiptBinding? = null
    private val binding get() = _binding!!

    private val vm: HireViewModel by activityViewModels()

    private var startDate: String = ""
    private var endDate: String = ""
    private var numOfDays: Int = -1

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (dialog != null && dialog!!.window != null) {
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

    private fun initView() {
        // Access data from the received Bundle (if needed)
        val bundle = arguments
        startDate = bundle!!.getString("START_DATE")!!
        endDate = bundle.getString("END_DATE")!!
        numOfDays = bundle.getInt("NUMBER_OF_DATE")

        // set data
        binding.from.text = startDate
        binding.to.text = endDate
        binding.fees.text = getString(R.string.dollar, vm.order?.countryPrice?.fees)
        binding.taxes.text = getString(R.string.dollar, vm.order?.countryPrice?.tax)
        binding.subTotal.text = getString(R.string.dollar, vm.order?.subTotal.toString())
        binding.numOfDay.text =
            "${getString(R.string.total_)} $numOfDays ${getString(R.string.days)}"
        binding.totalPrice.text = "${getString(R.string.total_equal)} ${vm.order?.cost}"
    }

    private fun setBtnListener() {
        Log.d("vm.order?.order_id!!", "vm.order?.order_id!! ${vm.order?.order_id!!}")
        binding.btnHire.onDebouncedListener {
            vm.submitOrder(vm.order?.order_id!!)
        }
        binding.btnClose.onDebouncedListener {
            dialog?.dismiss()
        }
    }

    private fun subscribeData() {
        vm.submitOrderLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    // save user
                    toast(it.data?.message.toString())
                    dialog?.dismiss()
                }

                is ResponseHandler.Error -> {
                    // show error message
                    toast(it.message)
                }

                is ResponseHandler.Loading -> {
                    binding.loading.visible()
                    // show a progress bar
                    // showMainLoading()
                }

                is ResponseHandler.StopLoading -> {
                    binding.loading.gone()
                    // show a progress bar
                    // hideMainLoading()
                }

                else -> {}
            }
        }
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
