package com.visualinnovate.almursheed.home.view.profile

import android.content.Intent
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
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.auth.view.UploadImageSheetFragment
import com.visualinnovate.almursheed.common.ImageCompressorHelper
import com.visualinnovate.almursheed.common.base.BaseFragment
import com.visualinnovate.almursheed.common.isEmptySting
import com.visualinnovate.almursheed.common.permission.FileUtils
import com.visualinnovate.almursheed.common.permission.Permission
import com.visualinnovate.almursheed.common.permission.PermissionHelper
import com.visualinnovate.almursheed.common.toast
import com.visualinnovate.almursheed.common.value
import com.visualinnovate.almursheed.databinding.FragmentEditProfileDriverBinding
import com.visualinnovate.almursheed.utils.Constant
import com.visualinnovate.almursheed.utils.Utils.carBrand
import com.visualinnovate.almursheed.utils.Utils.carType
import com.visualinnovate.almursheed.utils.Utils.carYears
import com.visualinnovate.almursheed.utils.Utils.languages

class EditProfileDriverFragment : BaseFragment() {

    private var _binding: FragmentEditProfileDriverBinding? = null
    private val binding get() = _binding!!

    private val vm: ProfileViewModel by activityViewModels()

    private lateinit var phoneNumber: String
    private lateinit var governmentID: String
    private lateinit var licenseNumber: String
    private var carNumber: String? = ""
    private var carTypeName: String? = ""
    private var year: String? = ""
    private var brandId: String? = ""
    private var languagesIdsList: ArrayList<Int> = ArrayList()

    //
    private var carImagePath: String = ""
    private var carImagesList: ArrayList<String> = ArrayList()
    private var documentImagePath: String = ""
    private val fileUtils by lazy { FileUtils(requireContext()) }
    private val imageCompressor by lazy { ImageCompressorHelper.with(requireContext()) }
    private lateinit var permissionHelper: PermissionHelper

    private val btnBackCallBackFunc: () -> Unit = {
        findNavController().navigateUp()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileDriverBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        permissionHelper = PermissionHelper.init(this)
        //
        initViews()
        initToolbar()
        subscribeActivityResult()
        setBtnListener()
        subscribeData()
    }

    private fun initViews() {
        // binding.spinnerCarType.spinnerText.setText(getString(R.string.select_car_type))
        // binding.spinnerCarBrandName.spinnerText.setText(getString(R.string.select_car_brand_name))
        // binding.spinnerCarManufacturingDate.spinnerText.setText(getString(R.string.select_car_manufacturing_date))
        // binding.spinnerLanguage.spinnerText.setText(getString(R.string.select_language))
        initCarTypeSpinner()
        initYearSpinner()
        initBrandSpinner()
        initLanguageSpinner()
    }

    private fun initToolbar() {
        binding.appBarDriverRegisterSecond.setTitleString(getString(R.string.driver_register_second))
        binding.appBarDriverRegisterSecond.setTitleCenter(true)
        binding.appBarDriverRegisterSecond.useBackButton(
            true,
            btnBackCallBackFunc,
            R.drawable.ic_back
        )
    }

    private fun setBtnListener() {
        binding.btnUploadCarPhoto.setOnClickListener {
            handleProfilePictureChange()
        }
        binding.btnUploadDocument.setOnClickListener {
            // handleProfilePictureChange()
        }

        binding.btnRegister.setOnClickListener {
            if (validate()) {
//                vm.registerDriverRequest.gov_id = governmentID
//                vm.registerDriverRequest.phone = phoneNumber
//                vm.registerDriverRequest.driver_licence_number = licenseNumber
//                vm.registerDriverRequest.car_number = carNumber
//                vm.registerDriverRequest.bio = "bio bio bio bio"
//                vm.registerDriverRequest.car_photos = carImagePath
//                vm.registerDriverRequest.carImagesList = carImagesList
//                vm.registerDriverRequest.languages = languagesIdsList
                // call api driver create
                // vm.createDriverMultiPart()
            }
        }
    }

    private fun subscribeData() {
//        vm.registerDriverLiveData.observe(viewLifecycleOwner) {
//            when (it) {
//                is ResponseHandler.Success -> {
//                    // bind data to the view
//                    Log.d("registerDriverLiveData", "it.data ${it.data}")
//                    findNavController().customNavigate(R.id.loginFragment)
//                }
//                is ResponseHandler.Error -> {
//                    // show error message
//                    toast(it.message)
//                    Log.d("ResponseHandler.Error -> Register Driver", it.message)
//                }
//                is ResponseHandler.Loading -> {
//                    // show a progress bar
//                }
//                else -> {
//                    toast("Else")
//                }
//            }
//        }
    }

    private fun initYearSpinner() {
        val yearsList = carYears.keys.toList().sorted()

        val arrayAdapter = // android.R.layout.simple_spinner_item
            ArrayAdapter(
                requireContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                yearsList
            )

        binding.spinnerCarManufacturingDate.spinner.adapter = arrayAdapter
        arrayAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
        binding.spinnerCarManufacturingDate.spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    // Retrieve the selected Car Manufacturing Date
                    val selectedYear = yearsList[position]
                    Log.d("readJsonFile", "selectedCountryName $selectedYear")

                    // Retrieve the corresponding country ID from the map
                    year = carYears[selectedYear]!!
                    Log.d("readJsonFile", "selectedCountryId $year")
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Handle when nothing is selected
                }
            }

//        binding.spinnerCarManufacturingDate.spinner.setOnItemClickListener { _, _, position, _ -> // parent, view, position, long
//            // Retrieve the selected Car Manufacturing Date
//            val selectedYear = yearsList[position]
//            Log.d("readJsonFile", "selectedCountryName $selectedYear")
//
//            // Retrieve the corresponding country ID from the map
//            year = carYears[selectedYear]!!
//            Log.d("readJsonFile", "selectedCountryId $year")
//        }
    }

    private fun initCarTypeSpinner() {
        val carTypeList = carType.keys.toList().sorted()

        val arrayAdapter = // android.R.layout.simple_spinner_item
            ArrayAdapter(
                requireContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                carTypeList
            )

        binding.spinnerCarType.spinner.adapter = arrayAdapter
        arrayAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
        binding.spinnerCarType.spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    // Retrieve the selected Car type name
                    val selectedCarType = carTypeList[position]
                    Log.d("readJsonFile", "selectedCarType $selectedCarType")

                    // Retrieve the corresponding country ID from the map
                    carTypeName = carType[selectedCarType]!!
                    Log.d("readJsonFile", "selectedCountryId $carTypeName")
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Handle when nothing is selected
                }
            }
    }

    private fun initBrandSpinner() {
        val brandNameList = carBrand.keys.toList()

        val arrayAdapter = // android.R.layout.simple_spinner_item
            ArrayAdapter(
                requireContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                brandNameList
            )

        binding.spinnerCarBrandName.spinner.adapter = arrayAdapter
        arrayAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
        binding.spinnerCarBrandName.spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    // Retrieve the selected Car brand name
                    val selectedBrandName = brandNameList[position]
                    Log.d("readJsonFile", "selectedBrandName $selectedBrandName")

                    // Retrieve the corresponding country ID from the map
                    brandId = carBrand[selectedBrandName]!!
                    Log.d("readJsonFile", "brandId $brandId")
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Handle when nothing is selected
                }
            }
//        binding.spinnerCarBrandName.spinner.setOnItemClickListener { _, _, position, _ -> // parent, view, position, long
//            // Retrieve the selected Car brand name
//            val selectedBrandName = brandNameList[position]
//            Log.d("readJsonFile", "selectedBrandName $selectedBrandName")
//
//            // Retrieve the corresponding country ID from the map
//            brandId = carBrand[selectedBrandName]!!
//            Log.d("readJsonFile", "brandId $brandId")
//        }
    }

    private fun initLanguageSpinner() {
        val languagesList = languages.keys.toList().sorted()

        val arrayAdapter = // android.R.layout.simple_spinner_item
            ArrayAdapter(
                requireContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                languagesList
            )

        binding.spinnerLanguage.spinner.adapter = arrayAdapter
        arrayAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
        binding.spinnerLanguage.spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    // Retrieve the selected language name
                    val selectedLanguage = languagesList[position]
                    Log.d("readJsonFile", "selectedBrandName $selectedLanguage")
                    val languageId = languages[selectedLanguage]!!
                    languagesIdsList.add(languageId)
                    Log.d("readJsonFile", "languageId $languageId")
                    Log.d("readJsonFile", "languagesIdsList $languagesIdsList")
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Handle when nothing is selected
                }
            }
//        binding.spinnerLanguage.spinner.setOnItemClickListener { _, _, position, _ -> // parent, view, position, long
//            // Retrieve the selected language name
//            val selectedLanguage = languagesList[position]
//            Log.d("readJsonFile", "selectedBrandName $selectedLanguage")
//            val languageId = languages[selectedLanguage]!!
//            languagesIdsList.add(languageId)
//            Log.d("readJsonFile", "languageId $languageId")
//            Log.d("readJsonFile", "languagesIdsList $languagesIdsList")
//        }
    }

    private fun validate(): Boolean {
        var isValid = true
        phoneNumber = binding.edtPhone.value
        governmentID = binding.edtGovernmentID.value
        licenseNumber = binding.edtLicenseNumber.value
        carNumber = binding.edtCarNumber.value

        if (phoneNumber.isEmptySting()) {
            binding.edtPhone.error = getString(R.string.required)
            isValid = false
        } else {
            binding.edtPhone.error = null
        }
        if (governmentID.isEmptySting()) {
            binding.edtGovernmentID.error = getString(R.string.required)
            isValid = false
        } else {
            binding.edtGovernmentID.error = null
        }
        if (licenseNumber.isEmptySting()) {
            binding.edtLicenseNumber.error = getString(R.string.required)
            isValid = false
        } else {
            binding.edtLicenseNumber.error = null
        }
        if (carNumber?.isEmptySting() == true) {
            binding.edtCarNumber.error = getString(R.string.required)
            isValid = false
        } else {
            binding.edtCarNumber.error = null
        }
        if (year?.isEmptySting() == true) {
            toast("Please choose car manufacturing date")
            isValid = false
        }
        if (brandId?.isEmptySting() == true) {
            toast("Please choose car brand name")
            isValid = false
        }
        if (carImagePath.isEmptySting()) {
            toast("Please choose car photo")
            isValid = false
        }
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
                                        Toast.makeText(
                                            requireContext(),
                                            error,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                    { path ->
                                        carImagePath = path
                                        carImagesList.add(carImagePath)
                                        showProfileImageBottomSheet()
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
                                carImagePath = path
                                carImagesList.add(carImagePath)
                                showProfileImageBottomSheet()
                            }
                        )
                    }
                }
            }
        }
    }

    private fun showProfileImageBottomSheet() {
        val bundle = Bundle()
        bundle.putString(Constant.UPLOAD_IMAGE_FRAGMENT, carImagePath)

        // Create an instance of the bottom sheet dialog fragment with the data
        val uploadImageSheetFragment = UploadImageSheetFragment.newInstance(bundle)

        // Show the bottom sheet dialog fragment
        uploadImageSheetFragment.show(
            childFragmentManager,
            "UploadImageFragment"
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
    }
}
