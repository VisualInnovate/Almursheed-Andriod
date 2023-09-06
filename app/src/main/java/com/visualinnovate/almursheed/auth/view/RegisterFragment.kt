package com.visualinnovate.almursheed.auth.view

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
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.auth.viewmodel.DriverViewModel
import com.visualinnovate.almursheed.common.*
import com.visualinnovate.almursheed.utils.Constant.ROLE_TOURIST
import com.visualinnovate.almursheed.common.base.BaseFragment
import com.visualinnovate.almursheed.common.permission.FileUtils
import com.visualinnovate.almursheed.common.permission.Permission
import com.visualinnovate.almursheed.common.permission.PermissionHelper
import com.visualinnovate.almursheed.databinding.FragmentRegisterBinding
import com.visualinnovate.almursheed.utils.Constant
import com.visualinnovate.almursheed.utils.ResponseHandler
import com.visualinnovate.almursheed.utils.Utils.cities
import com.visualinnovate.almursheed.utils.Utils.countries
import com.visualinnovate.almursheed.utils.Utils.nationalities
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : BaseFragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val vm: DriverViewModel by activityViewModels()

    private lateinit var role: String
    private lateinit var username: String
    private lateinit var email: String
    private var nationalityName: String = ""
    private var countryId: Int? = null
    private var cityId: Int? = null
    private var gender = "1" // private var gender = "" -> Int 1-> Male 2-Female
    private lateinit var password: String

    //
    private var imagePath: String = ""
    private val fileUtils by lazy { FileUtils(requireContext()) }
    private val imageCompressor by lazy { ImageCompressorHelper.with(requireContext()) }
    private lateinit var permissionHelper: PermissionHelper

    private val btnLoginCallBackListener: () -> Unit = {
        // navigate to login
        findNavController().customNavigate(R.id.loginFragment)
    }

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
        permissionHelper = PermissionHelper.init(this)
        initView()
        initToolbar()
        subscribeActivityResult()
        setBtnListener()
        subscribeData()
    }

    private fun subscribeData() {
        vm.registerDriverLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    hideAuthLoading()
                    toast(it.data!!.message!!)
                    val bundle = Bundle()
                    bundle.putString("email",email)
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
        // binding.spinnerNationality.spinnerText.text = getString(R.string.select_your_nationality)
        // binding.spinnerCountry.spinnerText.text = getString(R.string.select_your_country)
        // binding.spinnerCity.spinnerText.text = getString(R.string.select_your_city)
        if (role == ROLE_TOURIST) {
            binding.txtCountry.text = getString(R.string.destination_country)
            binding.txtCity.text = getString(R.string.destination_city)
        }
        binding.btnUploadPicture.gone()
        binding.txtPersonalPicture.gone()
        initCountrySpinner()
        initNationalitySpinner()
        initCitySpinner()
    }

    private fun initToolbar() {
        binding.appBar.setTitleString(getString(R.string.driver_register))
        binding.appBar.setTitleCenter(true)
        binding.appBar.useBackButton(
            true,
            { findNavController().navigateUp() },
            R.drawable.ic_back,
        )
        binding.appBar.showButtonOneWithoutImage(
            getString(R.string.login),
            btnLoginCallBackListener,
        )
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
                    cityId = cities[selectedCityName]!!
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Handle when nothing is selected
                }
            }

//        binding.spinnerCity.spinner.setOnItemClickListener { _, _, position, _ -> // parent, view, position, long
//            // Retrieve the selected country name
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

        binding.btnUploadPicture.setOnClickListener {
            handleProfilePictureChange()
        }
        binding.btnNext.setOnClickListener {
            if (validate()) {
                // vm.registerDriverRequest.name = username
                // vm.registerDriverRequest.email = email
                // vm.registerDriverRequest.gender = gender.toInt()
                // vm.registerDriverRequest.password = password
                // vm.registerDriverRequest.country_id = countryId
                // vm.registerDriverRequest.state_id = cityId
                // vm.registerDriverRequest.personal_pictures = imagePath
                vm.registerDriver(username,email,password,gender,nationalityName,countryId!!,cityId!!,role)
               // findNavController().customNavigate(R.id.verifyAccountFragment)
            }
        }
    }

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
        if (password.isEmpty()) {
            binding.edtPassword.error = getString(R.string.required)
            isValid = false
        } else if (password.length < 8) {
            binding.edtPassword.error = "password should be more than 8 characters"
            isValid = false
        } else {
            binding.edtPassword.error = null
        }
        if (nationalityName.isEmptySting()) {
            toast("Please choose nationality")
            isValid = false
        }
        if (countryId == null) {
            toast("Please choose country")
            isValid = false
        }
        if (cityId == null) {
            toast("Please choose city")
            isValid = false
        }
//        if (imagePath.isEmptySting()) {
//            toast("Please choose personal picture")
//            isValid = false
//        }
        return isValid
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
                                        showProfileImageBottomSheet()
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
                                showProfileImageBottomSheet()
                            },
                        )
                    }
                }
            }
        }
    }

    private fun showProfileImageBottomSheet() {
        val bundle = Bundle()
        bundle.putString(Constant.UPLOAD_IMAGE_FRAGMENT, imagePath)

        // Create an instance of the bottom sheet dialog fragment with the data
        val uploadImageSheetFragment = UploadImageSheetFragment.newInstance(bundle)

        // Show the bottom sheet dialog fragment
        uploadImageSheetFragment.show(
            childFragmentManager,
            "UploadImageFragment",
        ) // uploadImageSheetFragment.tag
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
