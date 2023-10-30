package com.visualinnovate.almursheed.commonView

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.auth.model.CityItem
import com.visualinnovate.almursheed.auth.model.User
import com.visualinnovate.almursheed.common.SharedPreference
import com.visualinnovate.almursheed.common.base.BaseFragment
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.common.showBottomSheet
import com.visualinnovate.almursheed.common.toast
import com.visualinnovate.almursheed.commonView.bottomSheets.ChooseTextBottomSheet
import com.visualinnovate.almursheed.commonView.bottomSheets.model.ChooserItemModel
import com.visualinnovate.almursheed.commonView.profile.ProfileViewModel
import com.visualinnovate.almursheed.databinding.FragmentEditLocationBinding
import com.visualinnovate.almursheed.utils.ResponseHandler
import com.visualinnovate.almursheed.utils.Utils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditLocationFragment : BaseFragment() {

    private var _binding: FragmentEditLocationBinding? = null
    private val binding get() = _binding!!

    private val vm: ProfileViewModel by viewModels()

    private val currentUser: User = SharedPreference.getUser()!!

    private var cityId: String? = null
    private var cityName: String? = null
    private var countryName: String? = null
    private var countryId: String? = null

    private var allCountries = ArrayList<ChooserItemModel>()
    private var citiesList = ArrayList<ChooserItemModel>()
    private var chooseTextBottomSheet: ChooseTextBottomSheet? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentEditLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initView()
        setBtnListener()
        subscribeData()
    }

    private fun initToolbar() {
        binding.appBar.setTitleString(getString(R.string.edit_location))
        binding.appBar.setTitleCenter(true)
        binding.appBar.useBackButton(
            true,
            { findNavController().navigateUp() },
            R.drawable.ic_back,
        )
    }

    private fun initView() {
        // binding.country.text = countryName ?: ""
        // binding.city.text = cityName ?: ""

        binding.country.text = countryName ?: getString(R.string.choose_country)
        binding.city.text = cityName ?: SharedPreference.getStateId().toString()

        allCountries = setupCountriesList()
    }

    private fun setBtnListener() {
        binding.country.onDebouncedListener {
            showCountryChooser()
        }
        binding.city.onDebouncedListener {
            showCityChooser()
        }

        binding.btnSubmit.onDebouncedListener {
            currentUser.countryId = countryId?.toInt()
            currentUser.stateId = cityId?.toInt()
            currentUser.desCityId = cityId?.toInt()

            when (currentUser.type) {
                "Driver" -> {
                    // call api update tourist
                    vm.updateLocationDriver(currentUser)
                }

                "Guides" -> {
                    // call api update tourist
                    vm.updateLocationGuide(currentUser)
                }

                else -> {
                    // call api update tourist
                    vm.updateLocationTourist(currentUser)
                }
            }
        }
    }

    private fun showCountryChooser() {
        chooseTextBottomSheet?.dismiss()
        chooseTextBottomSheet =
            ChooseTextBottomSheet(getString(R.string.countryy), allCountries, { data, _ ->
                Utils.selectedCountryId = data.id ?: "-1"
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
            ChooseTextBottomSheet(getString(R.string.cityy), citiesList, { data, _ ->
                cityId = data.id
                cityName = data.name
                binding.city.text = cityName
            })
        showBottomSheet(chooseTextBottomSheet!!, "CityBottomSheet")
    }

    private fun setupCountriesList(): ArrayList<ChooserItemModel> {
        val chooserItemList = ArrayList<ChooserItemModel>()
        Utils.allCountries.forEach {
            val item = ChooserItemModel(
                name = it.country,
                id = it.country_id,
                // isSelected = vm.countryName == it.country
            )
            chooserItemList.add(item)
        }
        return chooserItemList
    }

    private fun setupCitiesList(cities: ArrayList<CityItem>): ArrayList<ChooserItemModel> {
        Utils.filterCitiesByCountryId()
        val chooserItemList = ArrayList<ChooserItemModel>()
        cities.forEach {
            val item = ChooserItemModel(
                name = it.state, id = it.stateId,
                // isSelected = vm.cityName == it.state
            )
            chooserItemList.add(item)
        }
        return chooserItemList
    }

    private fun subscribeData() {
        vm.editLocationLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    // save user
                    SharedPreference.setStateId(it.data?.user?.get(0)?.destCityId)
                    toast(it.data?.message.toString())
                }

                is ResponseHandler.Error -> {
                    // show error message
                    toast(it.message)
                }

                is ResponseHandler.Loading -> {
                    // show a progress bar
                    showMainLoading()
                }

                is ResponseHandler.StopLoading -> {
                    // show a progress bar
                    hideMainLoading()
                }

                else -> {}
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}