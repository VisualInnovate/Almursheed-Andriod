package com.visualinnovate.almursheed.driver.filter.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.common.showBottomSheet
import com.visualinnovate.almursheed.commonView.bottomSheets.ChooseTextBottomSheet
import com.visualinnovate.almursheed.commonView.bottomSheets.model.ChooserItemModel
import com.visualinnovate.almursheed.databinding.FragmentFilterDriverBinding
import com.visualinnovate.almursheed.driver.filter.viewModel.FilterViewModel
import com.visualinnovate.almursheed.utils.Utils.allCarModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterDriverFragment : Fragment() {

    private var _binding: FragmentFilterDriverBinding? = null
    private val binding get() = _binding!!
    private val vm: FilterViewModel by activityViewModels()

    private var carCategory: String? = null
    private var carModel: String? = null
    private var rate: String? = null
    private var price: Int = 0

    private var carCategories = ArrayList<ChooserItemModel>()
    private var ratingList = ArrayList<ChooserItemModel>()
    private var chooseTextBottomSheet: ChooseTextBottomSheet? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFilterDriverBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        setBtnListener()
    }

    private fun initData() {
        val carCategoriesList = resources.getStringArray(R.array.car_categories)
        carCategories = setupCarCategoriesList(carCategoriesList)
    }

    private fun setBtnListener() {
        binding.btnSearch.onDebouncedListener {
            vm.carCategory = carCategory
            vm.carModel = carModel
            vm.price = price
            vm.rate = rate
            vm.type = "Driver"
        }

        binding.carCategory.onDebouncedListener {
            showCarCategoryChooser()
        }

        binding.carModel.onDebouncedListener {
            showCarModelChooser()
        }

        binding.rate.onDebouncedListener {
            showRateChooser()
        }

        binding.price.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar,
                progress: Int,
                fromUser: Boolean,
            ) {
                price = progress
                binding.txtEndPrice.text = "$price $"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })
    }

    private fun showCarCategoryChooser() {
        chooseTextBottomSheet?.dismiss()
        chooseTextBottomSheet = ChooseTextBottomSheet(getString(R.string.car_category), carCategories) { data, position ->
            carCategory = data.name
            binding.carCategory.text = carCategory
        }
        showBottomSheet(chooseTextBottomSheet!!, "CarCategoryBottomSheet")
    }

    private fun showCarModelChooser() {
        chooseTextBottomSheet?.dismiss()
        chooseTextBottomSheet = ChooseTextBottomSheet(getString(R.string.car_model), allCarModels) { data, position ->
            carModel = data.name
            binding.carModel.text = carModel
        }
        showBottomSheet(chooseTextBottomSheet!!, "CarModelBottomSheet")
    }

    private fun showRateChooser() {
        chooseTextBottomSheet?.dismiss()
        chooseTextBottomSheet = ChooseTextBottomSheet(getString(R.string.rating), arrayListOf()) { data, position ->
            rate = data.name
            binding.rate.text = rate
        }
        showBottomSheet(chooseTextBottomSheet!!, "RatingBottomSheet")
    }

    private fun setupCarCategoriesList(list: Array<String>): ArrayList<ChooserItemModel> {
        val chooserItemList = ArrayList<ChooserItemModel>()
        list.forEach {
            val item = ChooserItemModel(name = it)
            chooserItemList.add(item)
        }
        return chooserItemList
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
