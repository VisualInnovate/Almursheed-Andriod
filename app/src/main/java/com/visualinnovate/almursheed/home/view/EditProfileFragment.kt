package com.visualinnovate.almursheed.home.view

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.ImageCompressorHelper
import com.visualinnovate.almursheed.common.SharedPreference
import com.visualinnovate.almursheed.common.base.BaseFragment
import com.visualinnovate.almursheed.common.customNavigate
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.common.permission.FileUtils
import com.visualinnovate.almursheed.common.permission.Permission
import com.visualinnovate.almursheed.common.permission.PermissionHelper
import com.visualinnovate.almursheed.common.value
import com.visualinnovate.almursheed.databinding.FragmentEditProfileBinding
import com.visualinnovate.almursheed.utils.Utils.cities
import com.visualinnovate.almursheed.utils.Utils.countries
import com.visualinnovate.almursheed.utils.Utils.nationalities
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.parcelize.Parcelize

@AndroidEntryPoint
class EditProfileFragment : BaseFragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    private val user = SharedPreference.getUser()

    private var userName: String? = null
    private var email: String? = null
    private var nationalityName: String? = null
    private var countryId: Int? = null
    private var cityId: Int? = null
    private var gender: Int? = null
    private var phoneNumber: String? = null

    //
    private var imagePath: String = ""
    private val fileUtils by lazy { FileUtils(requireContext()) }
    private val imageCompressor by lazy { ImageCompressorHelper.with(requireContext()) }
    private lateinit var permissionHelper: PermissionHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        permissionHelper = PermissionHelper.init(this)
        Log.d("getUser", "$user")
        initToolbar()
        initView()
        setBtnListener()
        subscribeActivityResult()
    }

    private fun initToolbar() {
        binding.appBarEditProfile1.setTitleString(getString(R.string.edit_profile))
        binding.appBarEditProfile1.setTitleCenter(true)
        binding.appBarEditProfile1.useBackButton(
            true,
            { findNavController().navigateUp() },
            R.drawable.ic_back
        )
    }

    private fun initView() {
        // binding.spinnerNationality.spinnerText.text = getString(R.string.select_your_nationality)
        // binding.spinnerCountry.spinnerText.text = getString(R.string.select_your_country)
        // binding.spinnerCity.spinnerText.text = getString(R.string.select_your_city)
        initCountrySpinner()
        initNationalitySpinner()
        initCitySpinner()
        if (user?.type == "Driver" || user?.type == "Guide") {
            binding.btnNext.text = getString(R.string.next)
        } else {
            binding.btnNext.text = getString(R.string.submit)
        }
        Glide.with(requireContext())
            .load(user?.personalPhoto)
            .error(R.drawable.ic_launcher_foreground)
            .centerCrop()
            .circleCrop()
            .into(binding.imgUser)
        binding.edtUserName.setText(user?.name)
        binding.edtEmailAddress.setText(user?.email)
        // binding.spinnerNationality.spinner.selectedView
    }

    private fun initNationalitySpinner() {
        val nationalityList = nationalities.keys.toList()

        val arrayAdapter = // android.R.layout.simple_spinner_item
            ArrayAdapter(
                requireContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                nationalityList
            )

        binding.spinnerNationality.spinner.adapter = arrayAdapter
        arrayAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
        binding.spinnerNationality.spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
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
                countryList
            )

        binding.spinnerCountry.spinner.adapter = arrayAdapter
        arrayAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
        binding.spinnerCountry.spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    // Retrieve the selected country name
                    val selectedCountryName = countryList[position]
                    // Retrieve the corresponding country ID from the map
                    countryId = countries[selectedCountryName]!!.toInt()
                    Log.d(
                        "MyDebugData",
                        "selectedCountryName $selectedCountryName countryId $countryId"
                    )
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
                cityList
            )

        binding.spinnerCity.spinner.adapter = arrayAdapter
        arrayAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
        binding.spinnerCity.spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    // Retrieve the selected country name
                    val selectedCityName = cityList[position]
                    Log.d("readJsonFile", "selectedCityName $selectedCityName")

                    // Retrieve the corresponding country ID from the map
                    cityId = cities[selectedCityName]!!.toInt()
                    val cityId22 = cities[selectedCityName]!!
                    Log.d("readJsonFile", "cityId $cityId")
                    Log.d("readJsonFile", "cityId22 $cityId22")
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
            gender = 1 // -> Constant
        }
        binding.txtFemale.setOnClickListener {
            binding.txtFemale.setBackgroundResource(R.drawable.bg_rectangle_corner_primary)
            binding.txtFemale.setTextColor(resources.getColor(R.color.white, resources.newTheme()))
            binding.txtMale.setBackgroundColor(Color.TRANSPARENT)
            binding.txtMale.setTextColor(resources.getColor(R.color.grey, resources.newTheme()))
            gender = 2 // -> Constant
        }

        binding.icEditUserImage.setOnClickListener {
            handleProfilePictureChange()
        }

        binding.btnNext.onDebouncedListener {
            userName = binding.edtUserName.value
            email = binding.edtEmailAddress.value
            phoneNumber = binding.edtPhone.value

            val profileData = ProfileData(
                imagePath,
                userName,
                email,
                nationalityName,
                countryId,
                cityId,
                gender,
                phoneNumber
            )
            val bundle = Bundle()
            bundle.putParcelable("ProfileData", profileData)
            when (user?.type) {
                "Driver" -> {
                    findNavController().customNavigate(
                        R.id.editProfileDriverFragment,
                        data = bundle
                    )
                }

                "Guide" -> {
                    findNavController().customNavigate(R.id.editProfileGuideFragment, data = bundle)
                }

                else -> {
                    // call api update tourist
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
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                    { path ->
                                        imagePath = path
                                        Glide.with(requireContext())
                                            .load(imagePath ?: user?.personalPhoto)
                                            .into(binding.imgUser)
                                    }
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
                                    .load(imagePath ?: user?.personalPhoto)
                                    .into(binding.imgUser)
                            }
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

@Parcelize
data class ProfileData(
    var personal_pictures: String? = null,
    var userName: String? = null,
    var email: String? = null,
    var nationality: String? = null,
    var countryId: Int? = null,
    var cityId: Int? = null,
    var gender: Int? = null,
    var phoneNumber: String? = null,

    ) : Parcelable