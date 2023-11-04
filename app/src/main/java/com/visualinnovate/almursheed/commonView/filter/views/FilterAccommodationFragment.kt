package com.visualinnovate.almursheed.commonView.filter.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.auth.model.CityItem
import com.visualinnovate.almursheed.common.customNavigate
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.common.showBottomSheet
import com.visualinnovate.almursheed.commonView.bottomSheets.ChooseTextBottomSheet
import com.visualinnovate.almursheed.commonView.bottomSheets.model.ChooserItemModel
import com.visualinnovate.almursheed.commonView.filter.adapters.TextAdapter
import com.visualinnovate.almursheed.commonView.filter.viewModel.FilterViewModel
import com.visualinnovate.almursheed.databinding.FragmentFilterAccomodationBinding
import com.visualinnovate.almursheed.utils.Constant
import com.visualinnovate.almursheed.utils.Utils
import com.visualinnovate.almursheed.utils.Utils.selectedCountryId
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterAccommodationFragment : Fragment() {

    private var _binding: FragmentFilterAccomodationBinding? = null
    private val binding get() = _binding!!

    private val vm: FilterViewModel by activityViewModels()

    private var cityId: String? = null
    private var cityName: String? = null
    private var countryName: String? = null
    private var countryId: String? = null
    private var price: String? = null
    private var roomsCountId: String? = null
    private var categoryId: String? = null

    private var roomName: String? = null
    private var categoryName: String? = null

    private var allCountries = ArrayList<ChooserItemModel>()
    private var citiesList = ArrayList<ChooserItemModel>()
    private var chooseTextBottomSheet: ChooseTextBottomSheet? = null

    private lateinit var accommodationCategoryAdapter: TextAdapter
    private lateinit var roomsCountAdapter: TextAdapter
    private lateinit var accommodationCategoriesList: ArrayList<ChooserItemModel>
    private lateinit var roomCountList: ArrayList<ChooserItemModel>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFilterAccomodationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeData()
        initData()
        initView()
        setBtnListener()
    }

    private fun subscribeData() {
        vm.resetData.observe(viewLifecycleOwner) {
            if (it){
                initData()
                initView()
            }
        }
    }

    private fun initData() {
        allCountries = setupCountriesList()
        selectedCountryId = allCountries[0].id ?: "-1"
        accommodationCategoriesList = setupCategoriesList(resources.getStringArray(R.array.accommodation_categories))
        roomCountList = setupRoomsList(resources.getStringArray(R.array.numbers_of_rooms))

        countryId = vm.countryId
        countryName = vm.countryName
        cityId = vm.cityId
        cityName = vm.cityName
        price = vm.price
        categoryId = vm.accommodationCategoryId
        roomsCountId = vm.roomsCountId
    }

    private fun initView() {
        binding.country.text = countryName ?: getString(R.string.all)
        binding.city.text = cityName ?: getString(R.string.all)
        binding.txtStartPrice.text = (price ?: 0).toString()
        binding.price.progress = price?.toInt() ?: 0
        initCategoryRecyclerView()
        initRoomsCountRecyclerView()

        // accommodationCategoryAdapter.submitData(data)
    }

    private fun setBtnListener() {
        binding.country.onDebouncedListener {
            showCountryChooser()
        }
        binding.city.onDebouncedListener {
            showCityChooser()
        }

        binding.price.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar,
                progress: Int,
                fromUser: Boolean,
            ) {
                price = progress.toString()
                binding.txtStartPrice.text = "$price $"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {} })

        binding.btnSearch.onDebouncedListener {
            vm.price = price
            vm.countryId = countryId
            vm.countryName = countryName
            vm.cityId = cityId
            vm.cityName = cityName
            vm.accommodationCategoryId = categoryId
            vm.roomsCountId = roomsCountId
            vm.roomsName = roomName
            vm.accommodationCategoryName = categoryName
            vm.type = "Accommodation"
            vm.setFromFilter(true)
            if (vm.from == Constant.ACCOMMODATION) {
                findNavController().navigateUp()
            } else {
                findNavController().customNavigate(R.id.accommodationFragment)
            }
        }
    }

    private fun initCategoryRecyclerView() {
        accommodationCategoryAdapter = TextAdapter { data, _ ->
            categoryName = data.name
            categoryId = (accommodationCategoriesList.indexOf(data) + 1).toString()
        }
        binding.categoryRV.apply {
            accommodationCategoryAdapter.setHasStableIds(true)
            adapter = accommodationCategoryAdapter
        }
        accommodationCategoryAdapter.submitData(accommodationCategoriesList)
    }

    private fun initRoomsCountRecyclerView() {
        roomsCountAdapter = TextAdapter { data, _ ->
            roomName = data.name
            roomsCountId = (roomCountList.indexOf(data) + 1).toString()
        }

        binding.roomsRV.apply {
            roomsCountAdapter.setHasStableIds(true)
            adapter = roomsCountAdapter
        }
        roomsCountAdapter.submitData(roomCountList)
    }
    private fun showCountryChooser() {
        chooseTextBottomSheet?.dismiss()
        chooseTextBottomSheet = ChooseTextBottomSheet(getString(R.string.countryy), allCountries, { data, _ ->
            selectedCountryId = data.id ?: "-1"
            citiesList = setupCitiesList(Utils.filteredCities)
            countryId = data.id
            countryName = data.name
            binding.country.text = countryName
            cityName = null
            binding.city.text = getString(R.string.all)
        })
        showBottomSheet(chooseTextBottomSheet!!, "CountryBottomSheet")
    }

    private fun showCityChooser() {
        chooseTextBottomSheet?.dismiss()
        chooseTextBottomSheet = ChooseTextBottomSheet(getString(R.string.cityy), citiesList, { data, _ ->
            cityId = data.id
            cityName = data.name
            binding.city.text = cityName
        })
        showBottomSheet(chooseTextBottomSheet!!, "CityBottomSheet")
    }

    private fun setupCountriesList(): ArrayList<ChooserItemModel> {
        val chooserItemList = ArrayList<ChooserItemModel>()
        Utils.allCountries.forEach {
            val item = ChooserItemModel(name = it.country, id = it.country_id , isSelected = vm.countryName==it.country)
            chooserItemList.add(item)
        }
        return chooserItemList
    }
    private fun setupCitiesList(cities: ArrayList<CityItem>): ArrayList<ChooserItemModel> {
        Utils.filterCitiesByCountryId()
        val chooserItemList = ArrayList<ChooserItemModel>()
        cities.forEach {
            val item = ChooserItemModel(name = it.state, id = it.stateId , isSelected = vm.cityName==it.state)
            chooserItemList.add(item)
        }
        return chooserItemList
    }

    private fun setupCategoriesList(list: Array<String>): ArrayList<ChooserItemModel> {
        val chooserItemList = ArrayList<ChooserItemModel>()
        list.forEach {
            val item = ChooserItemModel(name = it ,  isSelected = vm.accommodationCategoryName==it)
            chooserItemList.add(item)
        }
        return chooserItemList
    }
    private fun setupRoomsList(list: Array<String>): ArrayList<ChooserItemModel> {
        val chooserItemList = ArrayList<ChooserItemModel>()
        list.forEach {
            val item = ChooserItemModel(name = it , isSelected = vm.roomsName==it)
            chooserItemList.add(item)
        }
        return chooserItemList
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
