package com.visualinnovate.almursheed.driver.filter.views

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
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.common.showBottomSheet
import com.visualinnovate.almursheed.commonView.bottomSheets.ChooseTextBottomSheet
import com.visualinnovate.almursheed.commonView.bottomSheets.model.ChooserItemModel
import com.visualinnovate.almursheed.databinding.FragmentFilterDriverBinding
import com.visualinnovate.almursheed.driver.filter.viewModel.FilterViewModel
import com.visualinnovate.almursheed.utils.Constant
import com.visualinnovate.almursheed.utils.Utils
import com.visualinnovate.almursheed.utils.Utils.allCarModels
import com.visualinnovate.almursheed.utils.Utils.filterCitiesByCountryId
import com.visualinnovate.almursheed.utils.Utils.selectedCountryId
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterDriverFragment : Fragment() {

    private var _binding: FragmentFilterDriverBinding? = null
    private val binding get() = _binding!!
    private val vm: FilterViewModel by activityViewModels()

    private var carCategory: String? = null
    private var carModel: String? = null
    private var rate: String? = null
    private var city: String? = null
    private var country: String? = null
    private var price: Int = 0

    private var carCategories = ArrayList<ChooserItemModel>()
    private var allCountries = ArrayList<ChooserItemModel>()
    private var citiesList = ArrayList<ChooserItemModel>()
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
        carCategory = getString(R.string.all)
        carModel = getString(R.string.all)
        rate = getString(R.string.all)
        city = getString(R.string.all)
        country = getString(R.string.all)

        val carCategoriesList = resources.getStringArray(R.array.car_categories)
        carCategories = setupCarCategoriesList(carCategoriesList)
        allCountries = setupCountriesList()
        citiesList = setupCitiesList(Utils.allCities)
        ratingList = setupRateList()
    }

    private fun setBtnListener() {
        binding.country.onDebouncedListener {
            showCountryChooser()
        }
        binding.city.onDebouncedListener {
            showCityChooser()
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

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {} })

        binding.btnSearch.onDebouncedListener {
            vm.carCategory = carCategory
            vm.carModel = carModel
            vm.price = price
            vm.rate = rate
            vm.country = country
            vm.city = city
            vm.type = "Driver"

            if (vm.from == Constant.ROLE_DRIVER) {
                findNavController().navigateUp()
            } else {
                // navigate to specific screen
            }
        }
    }

    private fun showCountryChooser() {
        chooseTextBottomSheet?.dismiss()
        chooseTextBottomSheet = ChooseTextBottomSheet(getString(R.string.countryy), allCountries, { data, position ->
            selectedCountryId = data.id ?: "-1"
            if (data.name == getString(R.string.all)) {
                citiesList = setupCitiesList(Utils.allCities)
            } else {
                citiesList = setupCitiesList(Utils.filteredCities)
            }

            city = getString(R.string.all)
            country = data.name
            binding.country.text = country
            binding.city.text = city
        })
        showBottomSheet(chooseTextBottomSheet!!, "CountryBottomSheet")
    }

    private fun showCityChooser() {
        chooseTextBottomSheet?.dismiss()
        chooseTextBottomSheet = ChooseTextBottomSheet(getString(R.string.cityy), citiesList, { data, position ->
            city = data.name
            binding.city.text = city
        })
        showBottomSheet(chooseTextBottomSheet!!, "CityBottomSheet")
    }

    private fun showCarCategoryChooser() {
        chooseTextBottomSheet?.dismiss()
        chooseTextBottomSheet = ChooseTextBottomSheet(getString(R.string.car_category), carCategories, { data, position ->
            carCategory = data.name
            binding.carCategory.text = carCategory
        })
        showBottomSheet(chooseTextBottomSheet!!, "CarCategoryBottomSheet")
    }

    private fun showCarModelChooser() {
        chooseTextBottomSheet?.dismiss()
        chooseTextBottomSheet = ChooseTextBottomSheet(getString(R.string.car_model), allCarModels, { data, position ->
            carModel = data.name
            binding.carModel.text = carModel
        })
        showBottomSheet(chooseTextBottomSheet!!, "CarModelBottomSheet")
    }

    private fun showRateChooser() {
        chooseTextBottomSheet?.dismiss()
        chooseTextBottomSheet = ChooseTextBottomSheet(getString(R.string.rating), ratingList, { data, position ->
            if (position == 0) {
                binding.rate.setImageResource(R.drawable.ic_stars_5)
                rate = getString(R.string.all)
            } else {
                binding.rate.setImageResource(data.name!!.toInt())
                rate = position.toString()
            }
        }, "image")
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

    private fun setupCountriesList(): ArrayList<ChooserItemModel> {
        val chooserItemList = ArrayList<ChooserItemModel>()
        val all = ChooserItemModel(name = getString(R.string.all))
        chooserItemList.add(all)
        Utils.allCountries.forEach {
            val item = ChooserItemModel(name = it.country, id = it.country_id)
            chooserItemList.add(item)
        }
        return chooserItemList
    }
    private fun setupCitiesList(cities: ArrayList<CityItem>): ArrayList<ChooserItemModel> {
        filterCitiesByCountryId()
        val chooserItemList = ArrayList<ChooserItemModel>()
        val all = ChooserItemModel(name = getString(R.string.all))
        chooserItemList.add(all)
        cities.forEach {
            val item = ChooserItemModel(name = it.state)
            chooserItemList.add(item)
        }
        return chooserItemList
    }
    private fun setupRateList(): ArrayList<ChooserItemModel> {
        val chooserItemList = ArrayList<ChooserItemModel>()
        chooserItemList.add(ChooserItemModel(name = getString(R.string.all)))
        chooserItemList.add(ChooserItemModel(name = "" + R.drawable.ic_star_1))
        chooserItemList.add(ChooserItemModel(name = "" + R.drawable.ic_stars_2))
        chooserItemList.add(ChooserItemModel(name = "" + R.drawable.ic_stars_3))
        chooserItemList.add(ChooserItemModel(name = "" + R.drawable.ic_stars_4))
        chooserItemList.add(ChooserItemModel(name = "" + R.drawable.ic_stars_5))
        return chooserItemList
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
