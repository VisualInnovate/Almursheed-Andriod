package com.visualinnovate.almursheed.auth.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.auth.model.CityItem
import com.visualinnovate.almursheed.auth.viewmodel.RegisterViewModel
import com.visualinnovate.almursheed.common.base.BaseFragment
import com.visualinnovate.almursheed.common.customNavigate
import com.visualinnovate.almursheed.common.gone
import com.visualinnovate.almursheed.common.isEmptySting
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.common.showBottomSheet
import com.visualinnovate.almursheed.common.toast
import com.visualinnovate.almursheed.common.value
import com.visualinnovate.almursheed.commonView.bottomSheets.ChooseTextBottomSheet
import com.visualinnovate.almursheed.commonView.bottomSheets.model.ChooserItemModel
import com.visualinnovate.almursheed.databinding.FragmentRegisterBinding
import com.visualinnovate.almursheed.utils.Constant
import com.visualinnovate.almursheed.utils.Constant.ROLE_DRIVER
import com.visualinnovate.almursheed.utils.Constant.ROLE_GUIDE
import com.visualinnovate.almursheed.utils.Constant.ROLE_TOURIST
import com.visualinnovate.almursheed.utils.ResponseHandler
import com.visualinnovate.almursheed.utils.Utils
import com.visualinnovate.almursheed.utils.Utils.filterCitiesByCountryId
import com.visualinnovate.almursheed.utils.Utils.filteredCities
import com.visualinnovate.almursheed.utils.Utils.selectedCountryId
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : BaseFragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val vm: RegisterViewModel by viewModels()

    private lateinit var role: String
    private lateinit var username: String
    private lateinit var email: String
    private var nationalityName: String = ""
    private var cityId: String? = null
    private var cityName: String? = null
    private var countryName: String? = null
    private var countryId: String? = null
    private lateinit var password: String

    private var allCountries = ArrayList<ChooserItemModel>()
    private var allNationality = ArrayList<ChooserItemModel>()
    private var citiesList = ArrayList<ChooserItemModel>()

    private var chooseTextBottomSheet: ChooseTextBottomSheet? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        role = requireArguments().getString("role")!!
        initView()
        initToolbar()
        setBtnListener()
        subscribeData()
    }

    private fun subscribeData() {
        vm.registerUserLive.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    hideAuthLoading()

                    val bundle = Bundle()
                    bundle.putString(Constant.EMAIL, email)
                    bundle.putString(Constant.TYPE_OTP, "0")
                    findNavController().customNavigate(R.id.verifyAccountFragment, data = bundle)
                }

                is ResponseHandler.Error -> {
                    // show error message
                    toast(it.message)
                }

                is ResponseHandler.Loading -> {
                    // show a progress bar
                    showAuthLoading()
                }

                is ResponseHandler.StopLoading -> {
                    // show a progress bar
                    hideAuthLoading()
                }

                else -> {
                    toast("Else")
                }
            }
        }
    }

    private fun initView() {
        if (role == ROLE_TOURIST) {
            binding.txtNationality.gone()
            binding.txtCountry.gone()
            binding.txtCity.gone()
            binding.nationality.gone()
            binding.country.gone()
            binding.city.gone()
        }
        // initNationalitySpinner()

        allNationality = setupNationalitiesList()
        allCountries = setupCountriesList()
    }

    private fun initToolbar() {
        binding.appBar.setTitleString(getString(R.string.register))
        binding.appBar.setTitleCenter(true)
        binding.appBar.useBackButton(
            true,
            { findNavController().navigateUp() },
            R.drawable.ic_back,
        )
        binding.appBar.showButtonOneWithoutImage(
            getString(R.string.login),
        ) { findNavController().customNavigate(R.id.loginFragment) }
    }

    private fun setBtnListener() {
        binding.nationality.onDebouncedListener {
            showNationalityChooser()
        }

        binding.country.onDebouncedListener {
            showCountryChooser()
        }

        binding.city.onDebouncedListener {
            showCityChooser()
        }

        binding.btnNext.setOnClickListener {
            if (validate()) {
                when (role) {
                    ROLE_GUIDE -> {
                        vm.registerGuide(
                            username,
                            email,
                            password,
                            nationalityName,
                            countryId?.toInt(),
                            cityId?.toInt(),
                            role,
                        )
                    }

                    ROLE_DRIVER -> {
                        vm.registerDriver(
                            username,
                            email,
                            password,
                            nationalityName,
                            countryId?.toInt(),
                            cityId?.toInt(),
                            role,
                        )
                    }

                    ROLE_TOURIST -> {
                        vm.registerTourist(
                            username,
                            email,
                            password,
                            nationalityName,
                            countryId?.toInt(),
                            cityId?.toInt(),
                            role,
                        )
                    }
                }
            }
        }
    }


    private fun showNationalityChooser() {
        chooseTextBottomSheet?.dismiss()
        chooseTextBottomSheet =
            ChooseTextBottomSheet(getString(R.string.nationality), allNationality, { data, _ ->
                binding.nationality.text = data.name
                nationalityName = data.name.toString()
            })
        showBottomSheet(chooseTextBottomSheet!!, "CountryBottomSheet")
    }

    private fun showCountryChooser() {
        chooseTextBottomSheet?.dismiss()
        chooseTextBottomSheet =
            ChooseTextBottomSheet(getString(R.string.countryy), allCountries, { data, _ ->
                selectedCountryId = data.id ?: "-1"
                citiesList = setupCitiesList(filteredCities)
                countryId = data.id
                countryName = data.name
                binding.country.text = countryName
                cityName = null
                binding.city.text = getString(R.string.choose_city)
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

    private fun setupNationalitiesList(): ArrayList<ChooserItemModel> {
        val chooserItemList = ArrayList<ChooserItemModel>()
        Utils.allNationalities.forEach {
            val item = ChooserItemModel(
                name = it,
                isSelected = nationalityName == it
            )
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
                isSelected = countryName == it.country
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
                isSelected = cityName == it.state
            )
            chooserItemList.add(item)
        }
        return chooserItemList
    }

    /*private fun initNationalitySpinner() {
        val nationalityList = allNationalities

        val arrayAdapter = // android.R.layout.simple_spinner_item
            ArrayAdapter(
                requireContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                nationalityList,
            )

        binding.spinnerNationality.spinner.adapter = arrayAdapter
        arrayAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
        binding.spinnerNationality.spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    // Retrieve the selected country name
                    nationalityName = nationalityList[position]
                    selectedNationalName = nationalityName
                    filterCountriesByNationality()
                    initCountrySpinner()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Handle when nothing is selected
                }
            }
//        binding.spinnerNationality.spinner.setOnItemClickListener { _, _, position, _ -> // parent, view, position, long
//            // Retrieve the selected country name
//            nationalityName = nationalityList[position]
//        }
    }

    private fun initCountrySpinner() {
        val countryList = filteredCountriesString

        val arrayAdapter = // android.R.layout.simple_spinner_item
            ArrayAdapter(
                requireContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                countryList,
            )

        binding.spinnerCountry.spinner.adapter = arrayAdapter
        arrayAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
        binding.spinnerCountry.spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    val selectedCountry = filteredCountries[position]
                    val selectedCountryName = selectedCountry.country
                    selectedCountryId = selectedCountry.country_id
                    countryId = selectedCountry.country_id.toInt()
                    initCitySpinner()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Handle when nothing is selected
                }
            }
    }

    private fun initCitySpinner() {
        filterCitiesByCountryId()
        val cities = filteredCitiesString
        val arrayAdapter = // android.R.layout.simple_spinner_item
            ArrayAdapter(
                requireContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                cities,
            )


        binding.spinnerCity.spinner.adapter = arrayAdapter
        arrayAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
        binding.spinnerCity.spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    val selectedCity = filteredCities[position]
                    val selectedCityName = selectedCity.stateId
                    val cityId = selectedCity.stateId
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Handle when nothing is selected
                }
            }
    }*/


    private fun validate(): Boolean {
        var isValid = true
        username = binding.edtUserName.value
        email = binding.edtEmailAddress.value
        password = binding.edtPassword.value

        if (username.isEmptySting()) {
            binding.edtUserName.error = getString(R.string.required)
            isValid = false
        } else {
            binding.edtUserName.error = null
        }
        if (email.isEmptySting()) {
            binding.edtEmailAddress.error = getString(R.string.required)
            isValid = false
        } else if (!validEmail(email)) {
            binding.edtEmailAddress.error = "Please enter a valid email"
            isValid = false
        } else {
            binding.edtEmailAddress.error = null
        }
        if (password.isEmptySting()) {
            binding.edtPassword.error = getString(R.string.required)
            isValid = false
        } else if (password.length < 8) {
            binding.edtPassword.error = "password should be more than 8 characters"
            isValid = false
        } else {
            binding.edtPassword.error = null
        }
        if (role == ROLE_DRIVER || role == ROLE_GUIDE) {
            if (nationalityName.isEmptySting()) {
                toast(getString(R.string.please_choose_nationality))
                isValid = false
            }
            if (countryId == null || countryId?.isEmptySting() == true) {
                toast(getString(R.string.please_choose_country))
                isValid = false
            }
            if (cityId == null || cityId?.isEmptySting() == true) {
                toast(getString(R.string.please_choose_city))
                isValid = false
            }
        }
        return isValid
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    /*private fun mapStringToChooserItemList(list: ArrayList<String>): ArrayList<ChooserItemModel> {
        val list = ArrayList<ChooserItemModel>()
        list.forEach {
            val chooserItemModel = ChooserItemModel()
            // chooserItemModel.name =
        }
    }*/
}

/*


 */