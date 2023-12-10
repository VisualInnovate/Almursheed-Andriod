package com.visualinnovate.almursheed.commonView.profile

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.visualinnovate.almursheed.MainActivity
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.auth.model.CityItem
import com.visualinnovate.almursheed.auth.model.User
import com.visualinnovate.almursheed.common.ImageCompressorHelper
import com.visualinnovate.almursheed.common.SharedPreference
import com.visualinnovate.almursheed.common.base.BaseFragment
import com.visualinnovate.almursheed.common.customNavigate
import com.visualinnovate.almursheed.common.gone
import com.visualinnovate.almursheed.common.isEmptySting
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.common.permission.FileUtils
import com.visualinnovate.almursheed.common.permission.Permission
import com.visualinnovate.almursheed.common.permission.PermissionHelper
import com.visualinnovate.almursheed.common.showBottomSheet
import com.visualinnovate.almursheed.common.toast
import com.visualinnovate.almursheed.common.value
import com.visualinnovate.almursheed.common.visible
import com.visualinnovate.almursheed.commonView.bottomSheets.ChooseTextBottomSheet
import com.visualinnovate.almursheed.commonView.bottomSheets.model.ChooserItemModel
import com.visualinnovate.almursheed.databinding.FragmentEditProfileBinding
import com.visualinnovate.almursheed.utils.Constant
import com.visualinnovate.almursheed.utils.Constant.ROLE_DRIVER
import com.visualinnovate.almursheed.utils.Constant.ROLE_GUIDE
import com.visualinnovate.almursheed.utils.Constant.ROLE_GUIDES
import com.visualinnovate.almursheed.utils.ResponseHandler
import com.visualinnovate.almursheed.utils.Utils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileFragment : BaseFragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    private val vm: ProfileViewModel by viewModels()

    private lateinit var currentUser: User

    private var nationalityName: String = ""
    private var cityId: String? = null
    private var cityName: String? = null
    private var countryName: String? = null
    private var countryId: String? = null
    private var gender: String? = "1"

    private var allCountries = ArrayList<ChooserItemModel>()
    private var allNationality = ArrayList<ChooserItemModel>()
    private var citiesList = ArrayList<ChooserItemModel>()

    private var chooseTextBottomSheet: ChooseTextBottomSheet? = null

    // for image
    private var imagePath: String? = null
    private val fileUtils by lazy { FileUtils(requireContext()) }
    private val imageCompressor by lazy { ImageCompressorHelper.with(requireContext()) }
    private lateinit var permissionHelper: PermissionHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        permissionHelper = PermissionHelper.init(this)
        currentUser = SharedPreference.getUser()
        (requireActivity() as MainActivity).hideBottomNav()
        initToolbar()
        initView()
        setBtnListener()
        subscribeActivityResult()
        subscribeData()
    }

    private fun initToolbar() {
        binding.appBar.setTitleString(getString(R.string.edit_profile))
        binding.appBar.setTitleCenter(true)
        binding.appBar.useBackButton(
            true,
            { findNavController().navigateUp() },
            R.drawable.ic_back,
        )
    }

    private fun initView() {
        if (currentUser.nationality?.isEmptySting() == false) {
            binding.nationality.text = currentUser.nationality
        } else {
            binding.nationality.text = getString(R.string.choose_nationality)
        }

        nationalityName = currentUser.nationality.toString()
        countryId = currentUser.countryId.toString()

        if (currentUser.destCityId != null) {
            cityId = currentUser.destCityId.toString()
            binding.city.text =
                (
                        currentUser.destCityId?.let { vm.getCityName(it) }
                            ?: getString(R.string.choose_city)
                        )
        } else if (currentUser.desCityId != null) {
            cityId = currentUser.desCityId.toString()
            binding.city.text =
                (
                        currentUser.desCityId?.let { vm.getCityName(it) }
                            ?: getString(R.string.choose_city)
                        )
        } else {
            cityId = currentUser.stateId.toString()
            binding.city.text =
                (currentUser.stateId?.let { vm.getCityName(it) } ?: getString(R.string.choose_city))
        }

        if (currentUser.destCountryId != null) {
            countryId = currentUser.destCountryId.toString()
            binding.country.text =
                (
                        currentUser.destCountryId?.let { vm.getCountryName(it.toInt()) }
                            ?: getString(R.string.choose_country)
                        )
        } else {
            countryId = currentUser.countryId.toString()
            binding.country.text = currentUser.countryId?.let { vm.getCountryName(it) }
                ?: getString(R.string.choose_country)
        }

        // binding.city.text = (currentUser.destCityId?.let { vm.getCityName(it) } ?: getString(R.string.choose_city))

        if (currentUser.type == ROLE_DRIVER || currentUser.type == ROLE_GUIDE || currentUser.type == ROLE_GUIDES) {
            binding.btnCarInfo.visible()
        } else {
            binding.btnCarInfo.gone()
        }

        Glide.with(requireContext())
            .load(currentUser.personalPhoto)
            .placeholder(R.drawable.ic_mursheed_logo)
            .error(R.drawable.ic_mursheed_logo)
            .centerCrop()
            .circleCrop()
            .into(binding.imgUser)

        binding.edtUserName.setText(currentUser.name)
        binding.edtEmailAddress.setText(currentUser.email)
        binding.edtEmailAddress.isEnabled = false
        binding.edtPhone.setText(currentUser.phone)

        allNationality = setupNationalitiesList()
        allCountries = setupCountriesList()
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

        binding.txtMale.onDebouncedListener {
            binding.txtMale.setBackgroundResource(R.drawable.bg_rectangle_corner_primary)
            binding.txtMale.setTextColor(resources.getColor(R.color.white, resources.newTheme()))
            binding.txtFemale.setBackgroundColor(Color.TRANSPARENT)
            binding.txtFemale.setTextColor(resources.getColor(R.color.grey, resources.newTheme()))
            gender = "1" // -> Constant
        }

        binding.txtFemale.onDebouncedListener {
            binding.txtFemale.setBackgroundResource(R.drawable.bg_rectangle_corner_primary)
            binding.txtFemale.setTextColor(resources.getColor(R.color.white, resources.newTheme()))
            binding.txtMale.setBackgroundColor(Color.TRANSPARENT)
            binding.txtMale.setTextColor(resources.getColor(R.color.grey, resources.newTheme()))
            gender = "2" // -> Constant
        }

        binding.icEditUserImage.setOnClickListener {
            handleProfilePictureChange()
        }

        binding.btnSubmit.onDebouncedListener {
            saveData()
            if (validate()) {
                vm.updatePersonalInformation(currentUser, imagePath)
            }
        }

        binding.btnCarInfo.onDebouncedListener {
            saveData()
            val bundle = Bundle()
            bundle.putParcelable("userData", currentUser)
            when (currentUser.type) {
                ROLE_DRIVER -> {
                    findNavController().customNavigate(
                        R.id.editProfileDriverFragment,
                        data = bundle,
                    )
                }

                ROLE_GUIDES -> {
                    findNavController().customNavigate(R.id.editProfileGuideFragment, data = bundle)
                }
            }
        }
    }

    private fun saveData() {
        currentUser = SharedPreference.getUser()

        if (binding.edtUserName.value.isNotEmpty()) {
            currentUser.name = binding.edtUserName.value
        }
        currentUser.phone = binding.edtPhone.value
        currentUser.personalPhoto = imagePath
        currentUser.gender = gender.toString()
        currentUser.countryId = countryId?.toInt() ?: 0
        currentUser.destCountryId = countryId?.toInt()?.toString() ?: ""
        currentUser.nationality = nationalityName

        currentUser.stateId = cityId?.toInt()
        currentUser.desCityId = cityId?.toInt()
        currentUser.destCityId = cityId?.toInt()
    }

    private fun validate(): Boolean {
        var isValid = true
        if (binding.nationality.text.toString().trim().isEmptySting()) {
            isValid = false
            toast(getString(R.string.choose_nationality))
        }
        if (binding.city.text.toString().trim().isEmptySting()) {
            isValid = false
            toast(getString(R.string.choose_city))
        }
        if (binding.country.text.toString().trim().isEmptySting()) {
            isValid = false
            toast(getString(R.string.choose_country))
        }

        if (currentUser.phone?.isEmptySting() == true) {
            isValid = false
            toast(getString(R.string.enter_your_phone_number))
        }

        if (currentUser.name?.isEmptySting() == true) {
            isValid = false
            toast(getString(R.string.enter_your_name))
        }
        return isValid
    }

    private fun showNationalityChooser() {
        chooseTextBottomSheet?.dismiss()
        chooseTextBottomSheet =
            ChooseTextBottomSheet(getString(R.string.nationality), allNationality, { data, _ ->
                binding.nationality.text = data.name
                nationalityName = data.name.toString()
            })
        showBottomSheet(chooseTextBottomSheet!!, "NationalityBottomSheet")
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
                // cityName = null
                // binding.city.text = getString(R.string.choose_city)
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
                isSelected = nationalityName == it,
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
                isSelected = countryName == it.country,
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
                name = it.state,
                id = it.stateId,
                isSelected = cityName == it.state,
            )
            chooserItemList.add(item)
        }
        return chooserItemList
    }

    private fun subscribeData() {
        vm.personalInformation.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    // save user
                    SharedPreference.saveUser(it.data?.user!!)
                    // SharedPreference.setCityId(it.data.user.destCityId)
                    if (Constant.ROLE_TOURIST == it.data.user.type) {
                        SharedPreference.setCityId(it.data.user.desCityId ?: it.data.user.destCityId)
                        SharedPreference.setCountryId(it.data.user.destCountryId?.toInt())
                    } else {
                        SharedPreference.setCityId(it.data.user.stateId)
                        SharedPreference.setCountryId(it.data.user.countryId)
                    }
                    toast(it.data.message.toString())
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

    private fun subscribeActivityResult() {
        activityResultsCallBack.observe(viewLifecycleOwner) {
            if (it != null) { // from gallery
                if (it.data != null) {
                    it.data?.let { uri ->
                        fileUtils.getFilePath(uri, { error ->
                            Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
                        }, { path ->
                            imageCompressor
                                .setImagePath(path).compressImage(
                                    { error ->
                                        Toast.makeText(
                                            requireContext(),
                                            error,
                                            Toast.LENGTH_SHORT,
                                        ).show()
                                    },
                                    { path ->
                                        imagePath = path
                                        Glide.with(requireContext())
                                            .load(imagePath)
                                            .centerCrop()
                                            .circleCrop()
                                            .into(binding.imgUser)
                                        currentUser.personalPhoto = imagePath
                                    },
                                )
                        })
                    }
                } else { // from camera
                    if (fileUtils.checkTmpFileLength()) {
                        imageCompressor.setImagePath(fileUtils.getRealPathFromURI()).compressImage(
                            { error ->
                                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                            },
                            { path ->
                                imagePath = path
                                Glide.with(requireContext())
                                    .load(imagePath)
                                    .centerCrop()
                                    .circleCrop()
                                    .into(binding.imgUser)
                                currentUser.personalPhoto = imagePath
                            },
                        )
                    }
                }
            }
        }
    }

    private fun handleProfilePictureChange() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionHelper
                .addPermissionsToAsk(Permission.Camera, Permission.Storage13)
                .isRequired(true)
                .requestPermissions { grantedList ->
                    for (permission in grantedList) {
                        when (permission) {
                            Permission.Camera, Permission.Storage13 -> {
                                performCameraAndGalleyAction()
                                break
                            }

                            else -> {}
                        }
                    }
                }
        } else {
            permissionHelper
                .addPermissionsToAsk(Permission.Camera, Permission.Storage)
                .isRequired(true)
                .requestPermissions { grantedList ->
                    for (permission in grantedList) {
                        when (permission) {
                            Permission.Camera, Permission.Storage -> {
                                performCameraAndGalleyAction()
                                break
                            }

                            else -> {}
                        }
                    }
                }
        }
    }

    private fun performCameraAndGalleyAction() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val uri = fileUtils.createTmpFileUri()
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        val chooserIntent = Intent(Intent.ACTION_CHOOSER)
        chooserIntent.putExtra(Intent.EXTRA_INTENT, galleryIntent)
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(cameraIntent))
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "Select from:")
        launchActivityForResult(chooserIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        fileUtils.clearTempFile()
    }
}

/*
private fun initNationalitySpinner() {
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
                    selectedNationalName = nationalityName!!
                    filterCountriesByNationality()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Handle when nothing is selected
                }
            }
    }

 private fun performGalleyMultipleSelection() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryIntent.type = "image/*"
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        galleryIntent.action = Intent.ACTION_GET_CONTENT
        val chooserIntent = Intent(Intent.ACTION_CHOOSER)
        chooserIntent.putExtra(Intent.EXTRA_INTENT, galleryIntent)
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "Select from:")
        launchActivityForResult(chooserIntent)
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
                    Utils.selectedCountryId = selectedCountry.country_id
                    countryId = selectedCountry.country_id.toInt()
                    initCitySpinner()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Handle when nothing is selected
                }
            }
//        binding.spinnerCountry.spinner.setOnItemClickListener { _, _, position, _ -> // parent, view, position, long
//            // Retrieve the selected country name
//            val selectedCountryName = countryList[position]
//            // Retrieve the corresponding country ID from the map
//            countryId = countries[selectedCountryName]!!
//        }
    }

    private fun initCitySpinner() {
        Utils.filterCitiesByCountryId()
        val cityList = filteredCitiesString

        val arrayAdapter = // android.R.layout.simple_spinner_item
            ArrayAdapter(
                requireContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                cityList,
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
                    // Retrieve the selected country name
                    val selectedCity = Utils.filteredCities[position]
                    val selectedCityName = selectedCity.stateId
                    cityId = selectedCity.stateId.toInt()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Handle when nothing is selected
                }
            }
    }
 */