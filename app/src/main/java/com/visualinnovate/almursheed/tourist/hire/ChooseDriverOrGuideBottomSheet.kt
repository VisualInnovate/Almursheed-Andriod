package com.visualinnovate.almursheed.tourist.hire

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.databinding.BottomSheetSelectDriverGuideBinding
import com.visualinnovate.almursheed.home.adapter.SelectDriverOrGuideAdapter
import com.visualinnovate.almursheed.home.model.DriverAndGuideItem

class ChooseDriverOrGuideBottomSheet(
    private val type: String,
    private val users: ArrayList<DriverAndGuideItem>,
    private val selectedUserCallback: (user: DriverAndGuideItem) -> Unit,
) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetSelectDriverGuideBinding? = null
    private val binding get() = _binding!!
    private lateinit var selectUserAdapter: SelectDriverOrGuideAdapter

    private val selectUserClickCalBack: (user: DriverAndGuideItem) -> Unit = {
        selectedUserCallback.invoke(it)
        dialog?.dismiss()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = BottomSheetSelectDriverGuideBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        setStyle(STYLE_NO_FRAME, 0)
        binding.txtChoose.text = getString(R.string.choose_a, type)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        selectUserAdapter = SelectDriverOrGuideAdapter(selectUserClickCalBack)
        binding.driversGuidesRV.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            selectUserAdapter.setHasStableIds(true)
            adapter = selectUserAdapter
        }
        selectUserAdapter.submitData(users)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}