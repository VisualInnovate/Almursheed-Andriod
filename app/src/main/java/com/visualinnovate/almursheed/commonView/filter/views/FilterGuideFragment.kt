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
import com.visualinnovate.almursheed.databinding.FragmentFilterGuideBinding
import com.visualinnovate.almursheed.utils.Constant
import com.visualinnovate.almursheed.utils.Utils
import com.visualinnovate.almursheed.utils.Utils.selectedCountryId
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterGuideFragment : Fragment() {

    private var _binding: FragmentFilterGuideBinding? = null
    private val binding get() = _binding!!

    private val vm: FilterViewModel by activityViewModels()

    private var rate: String? = null
    private var cityId: String? = null
    private var cityName: String? = null
    private var countryName: String? = null
    private var countryId: String? = null
    private var price: String? = null

    private var language: String? = null

    private var allLanguages = ArrayList<ChooserItemModel>()
    private var ratingList = ArrayList<ChooserItemModel>()
    private var allCountries = ArrayList<ChooserItemModel>()
    private var citiesList = ArrayList<ChooserItemModel>()

    private var chooseTextBottomSheet: ChooseTextBottomSheet? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFilterGuideBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        setBtnListener()
    }

    private fun initData() {
        val languages = resources.getStringArray(R.array.languages)
        allLanguages = setupLanguagesList(languages)
        allCountries = setupCountriesList()
        selectedCountryId = allCountries[0].id ?: "-1"
        citiesList = setupCitiesList(Utils.filteredCities)
        ratingList = setupRateList()

        countryId = vm.countryId
        countryName = vm.countryName
        cityId = vm.cityId
        cityName = vm.cityName
        price = vm.price
        rate = vm.rate
        language = vm.language

        binding.country.text = countryName ?: getString(R.string.all)
        binding.city.text = cityName ?: getString(R.string.all)
        binding.language.text = language ?: getString(R.string.all)
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

        binding.language.onDebouncedListener {
            showLanguagesChooser()
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
            vm.price = price
            vm.rate = rate
            vm.countryId = countryId
            vm.countryName = countryName
            vm.cityId = cityId
            vm.cityName = cityName
            vm.type = "Guide"
            vm.language = language

            vm.setFromFilter(true)
            if (vm.from == Constant.ROLE_GUIDE) {
                findNavController().navigateUp()
            } else {
                findNavController().customNavigate(R.id.allGuidesFragment)
            }
        }
    }

    private fun showCountryChooser() {
        chooseTextBottomSheet?.dismiss()
        chooseTextBottomSheet = ChooseTextBottomSheet(getString(R.string.countryy), allCountries, { data, position ->
            selectedCountryId = data.id ?: "-1"
            citiesList = setupCitiesList(Utils.filteredCities)
            countryId = data.id
            countryName = data.name
            binding.country.text = countryName
        })
        showBottomSheet(chooseTextBottomSheet!!, "CountryBottomSheet")
    }

    private fun showCityChooser() {
        chooseTextBottomSheet?.dismiss()
        chooseTextBottomSheet = ChooseTextBottomSheet(getString(R.string.cityy), citiesList, { data, position ->
            cityId = data.id
            cityName = data.name
            binding.city.text = cityName
        })
        showBottomSheet(chooseTextBottomSheet!!, "CityBottomSheet")
    }

    private fun showLanguagesChooser() {
        chooseTextBottomSheet?.dismiss()
        chooseTextBottomSheet = ChooseTextBottomSheet(getString(R.string.language_text), allLanguages, { data, position ->
            language = data.name
            binding.language.text = language
        })
        showBottomSheet(chooseTextBottomSheet!!, "LanguageBottomSheet")
    }

    private fun showRateChooser() {
        chooseTextBottomSheet?.dismiss()
        chooseTextBottomSheet = ChooseTextBottomSheet(getString(R.string.rating), ratingList, { data, position ->
            if (position == 0) {
                rate = "1"
            } else {
                binding.rate.setImageResource(data.name!!.toInt())
                rate = (position + 1).toString()
            }
        }, "image")
        showBottomSheet(chooseTextBottomSheet!!, "RatingBottomSheet")
    }

    private fun setupLanguagesList(list: Array<String>): ArrayList<ChooserItemModel> {
        val chooserItemList = ArrayList<ChooserItemModel>()
        list.forEach {
            val item = ChooserItemModel(name = it)
            chooserItemList.add(item)
        }
        return chooserItemList
    }

    private fun setupCountriesList(): ArrayList<ChooserItemModel> {
        val chooserItemList = ArrayList<ChooserItemModel>()
        Utils.allCountries.forEach {
            val item = ChooserItemModel(name = it.country, id = it.country_id)
            chooserItemList.add(item)
        }
        return chooserItemList
    }
    private fun setupCitiesList(cities: ArrayList<CityItem>): ArrayList<ChooserItemModel> {
        Utils.filterCitiesByCountryId()
        val chooserItemList = ArrayList<ChooserItemModel>()
        cities.forEach {
            val item = ChooserItemModel(name = it.state, id = it.stateId)
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
}
