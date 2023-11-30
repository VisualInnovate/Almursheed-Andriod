package com.visualinnovate.almursheed.driver.profile

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.auth.model.User
import com.visualinnovate.almursheed.common.SharedPreference
import com.visualinnovate.almursheed.common.base.BaseFragment
import com.visualinnovate.almursheed.common.isEmptySting
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.common.permission.Permission
import com.visualinnovate.almursheed.common.permission.PermissionHelper
import com.visualinnovate.almursheed.common.showBottomSheet
import com.visualinnovate.almursheed.common.toast
import com.visualinnovate.almursheed.commonView.bottomSheets.ChooseTextBottomSheet
import com.visualinnovate.almursheed.commonView.bottomSheets.UploadImageSheetFragment
import com.visualinnovate.almursheed.commonView.bottomSheets.model.ChooserItemModel
import com.visualinnovate.almursheed.commonView.profile.ProfileViewModel
import com.visualinnovate.almursheed.databinding.FragmentEditProfileDriverBinding
import com.visualinnovate.almursheed.utils.Constant
import com.visualinnovate.almursheed.utils.ResponseHandler
import com.visualinnovate.almursheed.utils.Utils.allCarBrand
import com.visualinnovate.almursheed.utils.Utils.allLanguages
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileDriverFragment : BaseFragment() {

    private var _binding: FragmentEditProfileDriverBinding? = null
    private val binding get() = _binding!!

    private val vm: ProfileViewModel by activityViewModels()

    private var governmentId: String? = null
    private var carType: String? = null
    private var carBrand: String? = null
    private var carNumber: String? = null
    private var licenceNumber: String? = null
    private var carManufacture: String? = null
    private var language: String? = null
    private var carImages: ArrayList<String> = ArrayList()

    private var chooseTextBottomSheet: ChooseTextBottomSheet? = null
    private var showImageSheetFragment: UploadImageSheetFragment? = null

    private lateinit var permissionHelper: PermissionHelper
    private lateinit var currentUser: User

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentEditProfileDriverBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        permissionHelper = PermissionHelper.init(this)
        // currentUser = vm.currentUser
        currentUser = requireArguments().getParcelable("userData")!!
        initToolbar()
        initData()
        subscribeActivityResult()
        setBtnListener()
        subscribeData()
    }

    private fun initData() {
        governmentId = currentUser.govId
        carNumber = currentUser.carNumber
        carType = currentUser.carType
        carBrand = currentUser.carBrandName
        carManufacture = currentUser.carManufacturingDate
        licenceNumber = currentUser.licenceNumber
        // language = currentUser.language

        binding.edtGovernmentID.setText(currentUser.govId)
        binding.edtCarNumber.setText(currentUser.carNumber)
        binding.edtLicenseNumber.setText(currentUser.licenceNumber)
        binding.carType.text = currentUser.carType ?: getString(R.string.choose_car_type)
        binding.carBrand.text = currentUser.carBrandName ?: getString(R.string.car_brand)
        binding.carManufacture.text =
            currentUser.carManufacturingDate ?: getString(R.string.car_model)

        //  binding.language.text = currentUser.language
        carImages = currentUser.carPhotos ?: ArrayList()
    }

    private fun initToolbar() {
        binding.appBarDriverRegisterSecond.setTitleString(getString(R.string.edit_profile))
        binding.appBarDriverRegisterSecond.setTitleCenter(true)
        binding.appBarDriverRegisterSecond.useBackButton(
            true,
            { findNavController().navigateUp() },
            R.drawable.ic_back,
        )
    }

    private fun setBtnListener() {
        binding.carManufacture.onDebouncedListener {
            showCarYearsChooser()
        }

        binding.language.onDebouncedListener {
            showLanguagesChooser()
        }

        binding.carType.onDebouncedListener {
            showCarTypesChooser()
        }

        binding.carBrand.onDebouncedListener {
            showCarBrandChooser()
        }

        binding.btnUploadCarPhoto.onDebouncedListener {
            handleCarImagesChange()
        }

        binding.btnUploadDocument.onDebouncedListener {
            // handleProfilePictureChange()
        }

        binding.btnRegister.onDebouncedListener {
            if (validate()) {
                saveData()
                // call api driver create
                vm.updateDriverCarInformation(currentUser, carImages)
            }
        }
    }

    private fun saveData() {
        currentUser.govId = governmentId
        currentUser.licenceNumber = licenceNumber
        currentUser.carNumber = licenceNumber
        currentUser.carBrandName = carBrand
        currentUser.carType = carType
        currentUser.carManufacturingDate = carManufacture
        //  currentUser.language = language
    }

    private fun validate(): Boolean {
        var isValid = true
        governmentId = binding.edtGovernmentID.text.toString().trim()
        carNumber = binding.edtCarNumber.text.toString().trim()
        licenceNumber = binding.edtCarNumber.text.toString().trim()

        if (governmentId?.isEmptySting() == true) {
            isValid = false
            toast(getString(R.string.enter_your_id))
        }
        if (carNumber?.isEmptySting() == true) {
            isValid = false
            toast(getString(R.string.enter_car_number))
        }
        if (licenceNumber?.isEmptySting() == true) {
            isValid = false
            toast(getString(R.string.enter_licence_number))
        }

        return isValid
    }

    private fun subscribeData() {
        vm.updateDriverLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    // bind data to the view
                    SharedPreference.saveUser(it.data?.user)
                    toast(it.data?.message ?: "")
                    findNavController().navigateUp()
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
            if (it != null) {
                val data = it
                if (data.clipData != null) {
                    val count = data.clipData!!.itemCount
                    carImages.clear()
                    for (i in 0 until count) {
                        carImages.add(
                            getRealPathFromURI(
                                requireContext(),
                                data.clipData!!.getItemAt(i).uri
                            ).toString()
                        )
                    }
                    showCarImageBottomSheet()
                } else {
                    toast(getString(R.string.must_choose_more_than_1_image))
                }
            }
        }
    }

    private fun showCarImageBottomSheet() {
        // currentUser.carImages
        if (carImages.isNotEmpty()) {
            showImageSheetFragment?.dismiss()
            val bundle = Bundle()
            bundle.putStringArrayList(Constant.UPLOAD_IMAGE_FRAGMENT, carImages)
            showImageSheetFragment = UploadImageSheetFragment(onSelectImageBtnClick = {
                carImages.clear()
                handleCarImagesChange()
            })
            showImageSheetFragment?.arguments = bundle

            showBottomSheet(showImageSheetFragment!!, "UploadImageBottomSheet")
        } else {
            handleCarImagesChange()
        }
    }

    private fun handleCarImagesChange() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionHelper
                .addPermissionsToAsk(Permission.Camera, Permission.Storage13)
                .isRequired(true)
                .requestPermissions { grantedList ->
                    for (permission in grantedList) {
                        when (permission) {
                            Permission.Camera, Permission.Storage13 -> {
                                performGalleryAction()
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
                                performGalleryAction()
                                break
                            }

                            else -> {}
                        }
                    }
                }
        }
    }

    private fun performGalleryAction() {
        val galleryIntent = Intent()
        galleryIntent.type = "image/*"
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        galleryIntent.action = ACTION_GET_CONTENT
        val chooserIntent = Intent(Intent.ACTION_CHOOSER)
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "Select from:")
        launchActivityForResult(galleryIntent)
    }

    private fun setupCarYearsList(): ArrayList<ChooserItemModel> {
        val chooserItemList = ArrayList<ChooserItemModel>()
        resources.getStringArray(R.array.car_years).forEach {
            val item = ChooserItemModel(name = it, isSelected = it == carManufacture)
            chooserItemList.add(item)
        }
        return chooserItemList
    }

    private fun showCarYearsChooser() {
        chooseTextBottomSheet?.dismiss()
        chooseTextBottomSheet =
            ChooseTextBottomSheet(getString(R.string.car_model), setupCarYearsList(), { data, _ ->
                carManufacture = data.name
                binding.carManufacture.text = carManufacture
            })
        showBottomSheet(chooseTextBottomSheet!!, "CarYearsBottomSheet")
    }

    private fun setupLanguagesList(): ArrayList<ChooserItemModel> {
        val chooserItemList = ArrayList<ChooserItemModel>()
        allLanguages.forEach {
            val item = ChooserItemModel(name = it.lang, isSelected = it.lang == language)
            chooserItemList.add(item)
        }
        return chooserItemList
    }

    private fun showLanguagesChooser() {
        chooseTextBottomSheet?.dismiss()
        chooseTextBottomSheet = ChooseTextBottomSheet(
            getString(R.string.language_text),
            setupLanguagesList(),
            { data, _ ->
                language = data.name
                binding.language.text = language
            })
        showBottomSheet(chooseTextBottomSheet!!, "LanguagesBottomSheet")
    }

    private fun setupCarTypesList(): ArrayList<ChooserItemModel> {
        val chooserItemList = ArrayList<ChooserItemModel>()
        resources.getStringArray(R.array.car_categories).forEach {
            val item = ChooserItemModel(name = it, isSelected = carType == it)
            chooserItemList.add(item)
        }
        return chooserItemList
    }

    private fun showCarTypesChooser() {
        chooseTextBottomSheet?.dismiss()
        chooseTextBottomSheet =
            ChooseTextBottomSheet(getString(R.string.car_type), setupCarTypesList(), { data, _ ->
                carType = data.name
                binding.carType.text = carType
            })
        showBottomSheet(chooseTextBottomSheet!!, "CarTypeBottomSheet")
    }

    private fun setupCarBrandList(): ArrayList<ChooserItemModel> {
        val chooserItemList = ArrayList<ChooserItemModel>()
        allCarBrand.forEach {
            val item = ChooserItemModel(name = it.make, isSelected = carBrand == it.make)
            chooserItemList.add(item)
        }
        return chooserItemList
    }

    private fun showCarBrandChooser() {
        chooseTextBottomSheet?.dismiss()
        chooseTextBottomSheet =
            ChooseTextBottomSheet(getString(R.string.car_brand), setupCarBrandList(), { data, _ ->
                carBrand = data.name
                binding.carBrand.text = carBrand
            })
        showBottomSheet(chooseTextBottomSheet!!, "CarBrandBottomSheet")
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun getRealPathFromURI(context: Context, uri: Uri): String? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getRealPathFromUriAboveKitkat(context, uri)
        } else {
            getRealPathFromUriBelowKitkat(context, uri)
        }
    }

    private fun getRealPathFromUriAboveKitkat(context: Context, uri: Uri): String? {
        if (DocumentsContract.isDocumentUri(context, uri)) {
            if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                val contentUri: Uri = when (type) {
                    "image" -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    else -> return null
                }
                val selection = MediaStore.Images.Media._ID + "=?"
                val selectionArgs = arrayOf(split[1])
                return getDataColumn(context, contentUri, selection, selectionArgs)
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {
            return getDataColumn(context, uri, null, null)
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    private fun getRealPathFromUriBelowKitkat(context: Context, uri: Uri): String? {
        return getDataColumn(context, uri, null, null)
    }

    private fun getDataColumn(
        context: Context,
        uri: Uri,
        selection: String?,
        selectionArgs: Array<String>?,
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)
        try {
            cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val columnIndex: Int = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(columnIndex)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }
}
