package com.visualinnovate.almursheed.commonView.profile

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.auth.model.User
import com.visualinnovate.almursheed.common.ImageCompressorHelper
import com.visualinnovate.almursheed.common.SharedPreference
import com.visualinnovate.almursheed.common.base.BaseFragment
import com.visualinnovate.almursheed.common.customNavigate
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.common.permission.FileUtils
import com.visualinnovate.almursheed.common.permission.Permission
import com.visualinnovate.almursheed.common.permission.PermissionHelper
import com.visualinnovate.almursheed.common.toast
import com.visualinnovate.almursheed.common.value
import com.visualinnovate.almursheed.databinding.FragmentEditProfileBinding
import com.visualinnovate.almursheed.utils.ResponseHandler
import com.visualinnovate.almursheed.utils.Utils.cities
import com.visualinnovate.almursheed.utils.Utils.countries
import com.visualinnovate.almursheed.utils.Utils.nationalities
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileFragment : BaseFragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    private val vm: ProfileViewModel by viewModels()

    private val currentUser: User = SharedPreference.getUser()!!
    private var nationalityName: String? = null
    private var countryId: Int? = null
    private var cityId: Int? = null
    private var gender: String? = "1"

    // for image
    private var imagePath: String = ""
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
        initToolbar()
        initView()
        setBtnListener()
        subscribeActivityResult()
        subscribeData()
    }

    private fun subscribeData() {
        vm.updateTouristLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    // save user
                    SharedPreference.saveUser(it.data?.user!![0])
                    toast(it.data.message.toString())
                }

                is ResponseHandler.Error -> {
                    // show error message
                    toast(it.message)
                    Log.d("ResponseHandler.Error", it.message)
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

    private fun initToolbar() {
        binding.appBarEditProfile1.setTitleString(getString(R.string.edit_profile))
        binding.appBarEditProfile1.setTitleCenter(true)
        binding.appBarEditProfile1.useBackButton(
            true,
            { findNavController().navigateUp() },
            R.drawable.ic_back,
        )
    }

    private fun initView() {
        // binding.spinnerNationality.spinnerText.text = getString(R.string.select_your_nationality)
        // binding.spinnerCountry.spinnerText.text = getString(R.string.select_your_country)
        // binding.spinnerCity.spinnerText.text = getString(R.string.select_your_city)
        initCountrySpinner()
        initNationalitySpinner()
        initCitySpinner()
        imagePath = currentUser.personalPhoto ?: ""
        if (currentUser.type == "Driver" || currentUser.type == "Guides") {
            binding.btnNext.text = getString(R.string.next)
        } else {
            binding.btnNext.text = getString(R.string.submit)
        }
        Glide.with(requireContext())
            .load(currentUser.personalPhoto ?: imagePath)
            .error(R.drawable.ic_launcher_foreground)
            .centerCrop()
            .circleCrop()
            .into(binding.imgUser)
        binding.edtUserName.setText(currentUser.name)
        binding.edtEmailAddress.setText(currentUser.email)
        binding.edtEmailAddress.isEnabled = false

        // binding.spinnerNationality.spinner.selectedView
    }

    private fun initNationalitySpinner() {
        val nationalityList = nationalities.keys.toList()

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
                    // Retrieve the corresponding country ID from the map
                    Log.d("MyDebugData", "nationalityName $nationalityName")
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
        val countryList = countries.keys.toList().sorted()

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
                    // Retrieve the selected country name
                    val selectedCountryName = countryList[position]
                    // Retrieve the corresponding country ID from the map
                    countryId = countries[selectedCountryName]!!.toInt()
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
        val cityList = cities.keys.toList().sorted()

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
                    val selectedCityName = cityList[position]

                    // Retrieve the corresponding country ID from the map
                    cityId = cities[selectedCityName]!!.toInt()
                    val cityId22 = cities[selectedCityName]!!
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Handle when nothing is selected
                }
            }

//        binding.spinnerCity.spinner.setOnItemClickListener { _, _, position, _ -> // parent, view, position, long
//            // Ret rieve the selected country name
//            val selectedCityName = cityList[position]
//            Log.d("readJsonFile", "selectedCityName $selectedCityName")
//
//            // Retrieve the corresponding country ID from the map
//            cityId = cities[selectedCityName]!!
//            val cityId22 = cities[selectedCityName]!!
//            Log.d("readJsonFile", "cityId $cityId")
//            Log.d("readJsonFile", "cityId22 $cityId22")
//        }
    }

    private fun setBtnListener() {
        binding.txtMale.setOnClickListener {
            binding.txtMale.setBackgroundResource(R.drawable.bg_rectangle_corner_primary)
            binding.txtMale.setTextColor(resources.getColor(R.color.white, resources.newTheme()))
            binding.txtFemale.setBackgroundColor(Color.TRANSPARENT)
            binding.txtFemale.setTextColor(resources.getColor(R.color.grey, resources.newTheme()))
            gender = "1" // -> Constant
        }
        binding.txtFemale.setOnClickListener {
            binding.txtFemale.setBackgroundResource(R.drawable.bg_rectangle_corner_primary)
            binding.txtFemale.setTextColor(resources.getColor(R.color.white, resources.newTheme()))
            binding.txtMale.setBackgroundColor(Color.TRANSPARENT)
            binding.txtMale.setTextColor(resources.getColor(R.color.grey, resources.newTheme()))
            gender = "2" // -> Constant
        }

        binding.icEditUserImage.setOnClickListener {
            handleProfilePictureChange()
        }

        binding.btnNext.onDebouncedListener {
            if (binding.edtUserName.value.isNotEmpty()) {
                currentUser.name = binding.edtUserName.value
            }
            currentUser.phone = binding.edtPhone.value
            currentUser.personalPhoto = imagePath
            currentUser.gender = gender.toString()
            currentUser.countryId = countryId
            currentUser.stateId = cityId
            currentUser.desCityId = cityId

            val bundle = Bundle()
            bundle.putParcelable("userData", currentUser)
            when (currentUser.type) {
                "Driver" -> {
                    findNavController().customNavigate(
                        R.id.editProfileDriverFragment,
                        data = bundle,
                    )
                }

                "Guides" -> {
                    findNavController().customNavigate(R.id.editProfileGuideFragment, data = bundle)
                }

                else -> {
                    // call api update tourist
                    vm.updateTourist(currentUser)
                }
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
                                        Log.d("activityResultsCallBack", "error $error")
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
 */