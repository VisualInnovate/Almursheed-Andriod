package com.visualinnovate.almursheed.commonView.contactUs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.visualinnovate.almursheed.MainActivity
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.base.BaseFragment
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.common.showBottomSheet
import com.visualinnovate.almursheed.common.toast
import com.visualinnovate.almursheed.common.value
import com.visualinnovate.almursheed.commonView.bottomSheets.ChooseTextBottomSheet
import com.visualinnovate.almursheed.commonView.bottomSheets.model.ChooserItemModel
import com.visualinnovate.almursheed.commonView.contactUs.viewmodel.ContactUsViewModel
import com.visualinnovate.almursheed.databinding.FragmentContactUsBinding
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactUsFragment : BaseFragment() {

    private var _binding: FragmentContactUsBinding? = null
    private val binding get() = _binding!!

    private val vm: ContactUsViewModel by viewModels()

    private var chooseTextBottomSheet: ChooseTextBottomSheet? = null

    private var subject: String? = null
    private var type: String? = null
    private var priority: String? = null
    private var message: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentContactUsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).hideBottomNav()

        initToolbar()
        setBtnListener()
        subscribeData()
    }

    private fun initToolbar() {
        binding.appBar.setTitleString(getString(R.string.contact_us))
        binding.appBar.setTitleCenter(true)
        binding.appBar.useBackButton(
            true,
            { findNavController().navigateUp() },
            R.drawable.ic_back,
        )
    }

    private fun setBtnListener() {
        binding.type.onDebouncedListener {
            showTypesChooser()
        }

        binding.priority.onDebouncedListener {
            showPrioritiesChooser()
        }

        binding.btnSend.onDebouncedListener {
            subject = binding.edtSubject.value
            message = binding.edtMessage.value
            vm.sendToContactUs(subject, type?.lowercase(), priority?.lowercase(), message)
        }
    }

    private fun showTypesChooser() {
        chooseTextBottomSheet?.dismiss()
        chooseTextBottomSheet = ChooseTextBottomSheet(
            getString(R.string.type),
            setupTypesList(),
            { data, _ ->
                type = data.name
                binding.type.text = type
            })
        showBottomSheet(chooseTextBottomSheet!!, "TypesBottomSheet")
    }

    private fun setupTypesList(): ArrayList<ChooserItemModel> {
        val allTypes = arrayOf("Sales", "Issue", "inquire") // Inquires
        val chooserItemList = ArrayList<ChooserItemModel>()
        allTypes.forEach {
            val item = ChooserItemModel(name = it, isSelected = it == type)
            chooserItemList.add(item)
        }
        return chooserItemList
    }

    private fun showPrioritiesChooser() {
        chooseTextBottomSheet?.dismiss()
        chooseTextBottomSheet = ChooseTextBottomSheet(
            getString(R.string.priority),
            setupPrioritiesList(),
            { data, _ ->
                if (data.name == "Medium") {
                    priority = "mid"
                } else {
                    priority = data.name
                }

                binding.priority.text = priority
            })
        showBottomSheet(chooseTextBottomSheet!!, "PrioritiesBottomSheet")
    }

    private fun setupPrioritiesList(): ArrayList<ChooserItemModel> {
        val allPriorities = arrayOf("High", "Medium", "Low")
        val chooserItemList = ArrayList<ChooserItemModel>()
        allPriorities.forEach {
            val item = ChooserItemModel(name = it, isSelected = it == priority)
            chooserItemList.add(item)
        }
        return chooserItemList
    }

    private fun subscribeData() {
        vm.contactUsLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    toast(it.data?.status.toString())
                    findNavController().navigateUp()
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