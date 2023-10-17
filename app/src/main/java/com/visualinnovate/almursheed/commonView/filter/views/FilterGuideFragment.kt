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
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.common.showBottomSheet
import com.visualinnovate.almursheed.commonView.bottomSheets.ChooseTextBottomSheet
import com.visualinnovate.almursheed.commonView.bottomSheets.model.ChooserItemModel
import com.visualinnovate.almursheed.databinding.FragmentFilterGuideBinding
import com.visualinnovate.almursheed.commonView.filter.viewModel.FilterViewModel
import com.visualinnovate.almursheed.utils.Constant
import com.visualinnovate.almursheed.utils.Utils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterGuideFragment : Fragment() {

    private var _binding: FragmentFilterGuideBinding? = null
    private val binding get() = _binding!!

    private val vm: FilterViewModel by activityViewModels()

    private var price: Int = 0
    private var language: String? =null
    private var rate: String? = null
    private var city: String? = null
    private var country: String? = null

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
        language = getString(R.string.all)
        rate = getString(R.string.all)
        city = getString(R.string.all)
        country = getString(R.string.all)

        val languages = resources.getStringArray(R.array.languages)
        allLanguages = setupLanguagesList(languages)
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
                price = progress
                binding.txtEndPrice.text = "$price $"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        binding.btnSearch.onDebouncedListener {
            vm.language = language
            vm.price = price.toString()
            vm.rate = rate
            vm.countryId = country
            vm.cityId = city
            vm.type = "Guide"
            if (vm.from == Constant.ROLE_GUIDE)
            findNavController().navigateUp()
            else {
                // navigate to specific screen
            }
        }
    }

    private fun showCountryChooser() {
        chooseTextBottomSheet?.dismiss()
        chooseTextBottomSheet = ChooseTextBottomSheet(getString(R.string.countryy), allCountries, { data, position ->
            Utils.selectedCountryId = data.id ?: "-1"
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

    private fun showLanguagesChooser() {
        chooseTextBottomSheet?.dismiss()
        chooseTextBottomSheet = ChooseTextBottomSheet(getString(R.string.language_text), allLanguages, { data, position ->
            language = data.name
            binding.language.text = rate
        })
        showBottomSheet(chooseTextBottomSheet!!, "LanguageBottomSheet")
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
        val all = ChooserItemModel(name = getString(R.string.all))
        chooserItemList.add(all)
        Utils.allCountries.forEach {
            val item = ChooserItemModel(name = it.country, id = it.country_id)
            chooserItemList.add(item)
        }
        return chooserItemList
    }
    private fun setupCitiesList(cities: ArrayList<CityItem>): ArrayList<ChooserItemModel> {
        Utils.filterCitiesByCountryId()
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
