package com.visualinnovate.almursheed.commonView.bottomSheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.gone
import com.visualinnovate.almursheed.common.invisible
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.commonView.bottomSheets.model.ChooserItemModel
import com.visualinnovate.almursheed.databinding.LayoutChooseTextBottomSheetBinding

class ChooseTextBottomSheet(
    private val text: String,
    private val data: ArrayList<ChooserItemModel>,
    private val selectedItemCallback: (data: ChooserItemModel, position: Int) -> Unit,
    private val type: String = "text",
) : BottomSheetDialogFragment() {

    private var _binding: LayoutChooseTextBottomSheetBinding? = null
    private val binding get() = _binding!!
    private lateinit var chooseTextAdapter: ChooseTextAdapter
    private lateinit var chooseRatingAdapter: ChooserRatingAdapter
    private var searchQuery: String? = null

    private val selectedItemClickCallback: (data: ChooserItemModel, position: Int) -> Unit = {
            item, pos ->
        selectedItemCallback.invoke(item, pos)
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
        _binding = LayoutChooseTextBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        setStyle(STYLE_NO_FRAME, 0)
        binding.txtChoose.text = getString(R.string.choose_a, text)
        setBtnListeners()
        initRecyclerView()
    }

    private fun setBtnListeners() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchQuery = newText.toString().trim().lowercase()
                if (this@ChooseTextBottomSheet::chooseTextAdapter.isInitialized) {
                    chooseTextAdapter.filter.filter(searchQuery)
                }
                return false
            }
        })

        binding.btnClose.onDebouncedListener {
            dialog?.cancel()
        }
    }

    private fun initRecyclerView() {
        if (type == "image") {
            binding.searchView.gone()
            chooseRatingAdapter = ChooserRatingAdapter(selectedItemClickCallback)
            binding.textsRV.apply {
                chooseRatingAdapter.setHasStableIds(true)
                adapter = chooseRatingAdapter
            }
            chooseRatingAdapter.submitData(data)
        } else {
            chooseTextAdapter = ChooseTextAdapter(selectedItemClickCallback)
            binding.textsRV.apply {
                chooseTextAdapter.setHasStableIds(true)
                adapter = chooseTextAdapter
            }
            chooseTextAdapter.submitData(data)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}