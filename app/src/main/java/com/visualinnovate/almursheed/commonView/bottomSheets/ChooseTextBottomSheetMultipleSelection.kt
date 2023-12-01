package com.visualinnovate.almursheed.commonView.bottomSheets

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.commonView.bottomSheets.model.ChooserItemModel
import com.visualinnovate.almursheed.databinding.LayoutChooseTextBottomSheetBinding

class ChooseTextBottomSheetMultipleSelection(
    private val text: String,
    private val data: ArrayList<ChooserItemModel>,
    private val selectedItemCallback: (data: ArrayList<ChooserItemModel>) -> Unit,
) : BottomSheetDialogFragment() {

    private var _binding: LayoutChooseTextBottomSheetBinding? = null
    private val binding get() = _binding!!
    private lateinit var chooseTextAdapter: ChooseTextAdapterMultipleSelection
    private var searchQuery: String? = null

    private val selectedItemClickCallback: (data: ChooserItemModel, position: Int) -> Unit = {
            item, pos ->
        //  dialog?.dismiss()
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
                if (this@ChooseTextBottomSheetMultipleSelection::chooseTextAdapter.isInitialized) {
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
        chooseTextAdapter = ChooseTextAdapterMultipleSelection(selectedItemClickCallback)
        binding.textsRV.apply {
           // chooseTextAdapter.setHasStableIds(true)
            adapter = chooseTextAdapter
        }
        data.distinctBy { it.name }.also {
            chooseTextAdapter.submitData(ArrayList(it))
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        selectedItemCallback.invoke(chooseTextAdapter.getSelectedItems())
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}