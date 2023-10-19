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

    private var languageName: String? = null
    private var languageId: String? = null

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
        allLanguages = setupLanguagesList()
        allCountries = setupCountriesList()
        selectedCountryId = allCountries[0].id ?: "-1"
        ratingList = setupRateList()

        countryId = vm.countryId
        countryName = vm.countryName
        cityId = vm.cityId
        cityName = vm.cityName
        price = vm.price
        rate = vm.rate
        languageName = vm.languageName

        binding.country.text = countryName ?: getString(R.string.all)
        binding.city.text = cityName ?: getString(R.string.all)
        binding.language.text = languageName ?: getString(R.string.all)
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
            vm.languageName = languageName
            languageId?.let {
                vm.languageId = ArrayList()
                vm.languageId!!.add(0, languageId?.toInt())
            }
            vm.type = "Guide"
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

    private fun showLanguagesChooser() {
        chooseTextBottomSheet?.dismiss()
        chooseTextBottomSheet = ChooseTextBottomSheet(getString(R.string.language_text), allLanguages, { data, _ ->
            languageName = data.name
            languageId = data.id
            binding.language.text = languageName
        })
        showBottomSheet(chooseTextBottomSheet!!, "LanguageBottomSheet")
    }

    private fun showRateChooser() {
        chooseTextBottomSheet?.dismiss()
        chooseTextBottomSheet = ChooseTextBottomSheet(getString(R.string.rating), ratingList, { data, _ ->
            binding.rate.setImageResource(data.name!!.toInt())
            rate = (ratingList.indexOf(data)+1).toString()
        }, "image")
        showBottomSheet(chooseTextBottomSheet!!, "RatingBottomSheet")
    }

    private fun setupLanguagesList(): ArrayList<ChooserItemModel> {
        val chooserItemList = ArrayList<ChooserItemModel>()
        Utils.allLanguages.forEach {
            val item = ChooserItemModel(name = it.lang, id = it.id.toString() , isSelected = vm.languageName == it.lang)
            chooserItemList.add(item)
        }
        return chooserItemList
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
