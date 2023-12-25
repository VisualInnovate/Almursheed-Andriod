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
import com.visualinnovate.almursheed.commonView.filter.viewModel.FilterViewModel
import com.visualinnovate.almursheed.databinding.FragmentFilterDriverBinding
import com.visualinnovate.almursheed.utils.Utils
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
    private var cityId: String? = null
    private var cityName: String? = null
    private var countryName: String? = null
    private var countryId: String? = null
    private var price: String? = null

    private var carCategories = ArrayList<ChooserItemModel>()
    private var allCountries = ArrayList<ChooserItemModel>()
    private var allCarYears = ArrayList<ChooserItemModel>()
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
        subscribeData()
    }

    private fun subscribeData() {
        vm.resetData.observe(viewLifecycleOwner) {
            if (it) {
                initData()
                binding.rate.setImageResource(R.drawable.ic_stars_5)
            }
        }
    }

    private fun initData() {
        val carCategoriesList = resources.getStringArray(R.array.car_categories)
        val carYearsList = resources.getStringArray(R.array.car_years)
        allCarYears = setupCarModelsList(carYearsList)
        carCategories = setupCarCategoriesList(carCategoriesList)
        allCountries = setupCountriesList()
        selectedCountryId = allCountries[0].id ?: "-1"
        ratingList = setupRateList()

        countryId = vm.countryId
        countryName = vm.countryName
        cityId = vm.cityId
        cityName = vm.cityName
        carCategory = vm.carCategory
        carModel = vm.carModel
        price = vm.price
        rate = vm.rate

        binding.country.text = countryName ?: getString(R.string.all)
        binding.city.text = cityName ?: getString(R.string.all)
        binding.carCategory.text = carCategory ?: getString(R.string.all)
        binding.carModel.text = carModel ?: getString(R.string.all)
        binding.txtStartPrice.text = (price ?: 0).toString()
        binding.price.progress = price?.toInt() ?: 0
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
                price = progress.toString()
                binding.txtStartPrice.text = "$price $"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        binding.btnSearch.onDebouncedListener {
            vm.carCategory = carCategory
            vm.carModel = carModel
            vm.price = price
            vm.rate = rate
            vm.countryId = countryId
            vm.countryName = countryName
            vm.cityId = cityId
            vm.cityName = cityName
            vm.type = "Driver"
            vm.setFromFilter(true)
            findNavController().customNavigate(R.id.allDriversFragment)
        }
    }

    private fun showCountryChooser() {
        chooseTextBottomSheet?.dismiss()
        chooseTextBottomSheet =
            ChooseTextBottomSheet(getString(R.string.countryy), allCountries, { data, _ ->
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
        chooseTextBottomSheet =
            ChooseTextBottomSheet(getString(R.string.cityy), citiesList, { data, _ -> // position
                cityId = data.id
                cityName = data.name
                binding.city.text = cityName
            })
        showBottomSheet(chooseTextBottomSheet!!, "CityBottomSheet")
    }

    private fun showCarCategoryChooser() {
        chooseTextBottomSheet?.dismiss()
        chooseTextBottomSheet = ChooseTextBottomSheet(
            getString(R.string.car_category),
            carCategories,
            { data, _ -> // position
                carCategory = data.name
                binding.carCategory.text = carCategory
            })
        showBottomSheet(chooseTextBottomSheet!!, "CarCategoryBottomSheet")
    }

    private fun showCarModelChooser() {
        chooseTextBottomSheet?.dismiss()
        chooseTextBottomSheet =
            ChooseTextBottomSheet(getString(R.string.car_model), allCarYears, { data, _ ->
                carModel = data.name
                binding.carModel.text = carModel
            })
        showBottomSheet(chooseTextBottomSheet!!, "CarModelBottomSheet")
    }

    private fun showRateChooser() {
        chooseTextBottomSheet?.dismiss()
        chooseTextBottomSheet =
            ChooseTextBottomSheet(getString(R.string.rating), ratingList, { data, _ ->
                binding.rate.setImageResource(data.name!!.toInt())
                rate = (ratingList.indexOf(data) + 1).toString()
            }, "image")
        showBottomSheet(chooseTextBottomSheet!!, "RatingBottomSheet")
    }

    private fun setupCarCategoriesList(list: Array<String>): ArrayList<ChooserItemModel> {
        val chooserItemList = ArrayList<ChooserItemModel>()
        list.forEach {
            val item = ChooserItemModel(name = it, isSelected = vm.carCategory == it)
            chooserItemList.add(item)
        }
        return chooserItemList
    }

    private fun setupCarModelsList(list: Array<String>): ArrayList<ChooserItemModel> {
        val chooserItemList = ArrayList<ChooserItemModel>()
        list.forEach {
            val item = ChooserItemModel(name = it, isSelected = it == vm.carModel)
            chooserItemList.add(item)
        }
        return chooserItemList
    }

    private fun setupCountriesList(): ArrayList<ChooserItemModel> {
        val chooserItemList = ArrayList<ChooserItemModel>()
        Utils.allCountries.forEach {
            val item = ChooserItemModel(
                name = it.country,
                id = it.country_id,
                isSelected = vm.countryName == it.country
            )
            chooserItemList.add(item)
        }
        return chooserItemList
    }

    private fun setupCitiesList(cities: ArrayList<CityItem>): ArrayList<ChooserItemModel> {
        filterCitiesByCountryId()
        val chooserItemList = ArrayList<ChooserItemModel>()
        cities.forEach {
            val item = ChooserItemModel(
                name = it.state,
                id = it.stateId,
                isSelected = vm.cityName == it.state
            )
            chooserItemList.add(item)
        }
        return chooserItemList
    }

    private fun setupRateList(): ArrayList<ChooserItemModel> {
        val chooserItemList = ArrayList<ChooserItemModel>()
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

    fun isFragmentInBackStack(fragmentId: Int): Boolean {
        val backStackCount = requireActivity().supportFragmentManager.backStackEntryCount
        for (i in 0 until backStackCount) {
            val entry = requireActivity().supportFragmentManager.getBackStackEntryAt(i)
            if (entry.id == fragmentId) {
                return true
            }
        }
        return false
    }
}
