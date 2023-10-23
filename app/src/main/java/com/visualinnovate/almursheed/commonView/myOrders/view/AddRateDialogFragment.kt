package com.visualinnovate.almursheed.commonView.myOrders.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.common.toast
import com.visualinnovate.almursheed.common.value
import com.visualinnovate.almursheed.commonView.myOrders.viewModel.MyOrdersViewModel
import com.visualinnovate.almursheed.databinding.FragmentAddRateDialogBinding
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddRateDialogFragment : DialogFragment() {

    private var _binding: FragmentAddRateDialogBinding? = null
    private val binding get() = _binding!!

    private val vm: MyOrdersViewModel by activityViewModels()

    private var rate: Int = 0
    private var rateComment: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddRateDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        setStyle(STYLE_NO_FRAME, 0)
        setBtnListeners()
        subscribeData()
    }

    private fun setBtnListeners() {
        binding.ratingBar.setOnRatingBarChangeListener { _, rating, fromUser ->
            // This callback is called when the user changes the rating
            if (fromUser) {
                rate = rating.toInt()
            }
        }

        binding.btnSubmit.onDebouncedListener {
            if (validate()) {
                // call api add rate
                vm.addRate(
                    rate,
                    rateComment ?: "",
                    reviewableId = vm.orderDetails!!.userId!!,
                    type = 0
                )
                dialog?.dismiss()
            }
        }
    }

    private fun subscribeData() {
        vm.addRate.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    toast(it.data?.message ?: "")
                    dialog?.dismiss()
                    toast(it.data?.message.toString())
                    findNavController().navigateUp()
                }

                is ResponseHandler.Error -> {
                    // show error message
                    toast(it.message)
                }

                /*is ResponseHandler.Loading -> {
                    // show a progress bar
                    showAuthLoading()
                }

                is ResponseHandler.StopLoading -> {
                    // show a progress bar
                    hideAuthLoading()
                }*/

                else -> {}
            }
        }
    }

    private fun validate(): Boolean {
        var isValid = true

        // rate = binding.ratingBar.rating
        rateComment = binding.edtRateComment.value

        if (rate == 0) {
            toast(getString(R.string.must_give_rate))
            isValid = false
        }
        return isValid
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}