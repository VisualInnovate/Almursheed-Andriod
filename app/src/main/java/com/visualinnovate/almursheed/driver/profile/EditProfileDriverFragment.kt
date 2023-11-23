package com.visualinnovate.almursheed.driver.profile

import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.auth.model.User
import com.visualinnovate.almursheed.auth.view.UploadImageSheetFragment
import com.visualinnovate.almursheed.common.SharedPreference
import com.visualinnovate.almursheed.common.base.BaseFragment
import com.visualinnovate.almursheed.common.isEmptySting
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.common.permission.Permission
import com.visualinnovate.almursheed.common.permission.PermissionHelper
import com.visualinnovate.almursheed.common.showBottomSheet
import com.visualinnovate.almursheed.common.toast
import com.visualinnovate.almursheed.commonView.bottomSheets.ChooseTextBottomSheet
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
    private var carManufacture: String? = null
    private var language: String? = null
    private var carImages: ArrayList<String> = ArrayList()

    private var chooseTextBottomSheet: ChooseTextBottomSheet? = null

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
        binding.edtGovernmentID.setText(currentUser.govId)
        binding.edtCarNumber.setText(currentUser.carNumber)
        binding.carType.text = currentUser.carType ?: getString(R.string.choose_car_type)
        binding.carBrand.text = currentUser.carBrandName ?: getString(R.string.car_brand)
        binding.carManufacture.text = currentUser.carManufacturingDate ?: getString(R.string.car_model)
        //  binding.language.text = currentUser.language
        //  carImages = currentUser.carImages
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
            handleProfilePictureChange()
        }
        binding.btnUploadDocument.onDebouncedListener {
            // handleProfilePictureChange()
        }

        binding.btnRegister.onDebouncedListener {
            if (validate()) {
                toast("call api")
            }
        }
//        binding.btnRegister.setOnClickListener {
//            currentUser.govId = binding.edtGovernmentID.value
//            currentUser.driverLicenceNumber = binding.edtLicenseNumber.value
//            currentUser.carNumber = binding.edtCarNumber.value
//            currentUser.carBrandName = brandId
//            currentUser.carType = carTypeName
//            currentUser.carManufacturingDate = year
//
//            // call api driver create
//            vm.updateDriver(currentUser)
//        }
    }

    private fun validate(): Boolean {
        var isValid = true
        governmentId = binding.edtGovernmentID.text.toString().trim()
        carNumber = binding.edtCarNumber.text.toString().trim()

        if (governmentId?.isEmptySting() == true) {
            isValid = false
            toast(getString(R.string.enter_your_id))
        }
        if (carNumber?.isEmptySting() == true) {
            isValid = false
            toast(getString(R.string.enter_car_number))
        }

        if (carImages.isEmpty()) {
            isValid = false
            toast(getString(R.string.must_choose_more_than_1_image))
        }

        return isValid
    }

    private fun subscribeData() {
        vm.updateDriverLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    // bind data to the view
                    SharedPreference.saveUser(it.data?.user!!)
                    toast(it.data.message ?: "")
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
                    for (i in 0..count - 1) {
                        carImages.add(data.clipData!!.getItemAt(i).uri.toString())
                    }
                    showProfileImageBottomSheet(carImages[0])
                } else {
                    toast(getString(R.string.must_choose_more_than_1_image))
                }
            }
        }
    }

    private fun showProfileImageBottomSheet(image: String) {
        val bundle = Bundle()
        bundle.putString(Constant.UPLOAD_IMAGE_FRAGMENT, image)

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
        chooseTextBottomSheet = ChooseTextBottomSheet(getString(R.string.car_model), setupCarYearsList(), { data, _ ->
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
        chooseTextBottomSheet = ChooseTextBottomSheet(getString(R.string.language_text), setupLanguagesList(), { data, _ ->
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
        chooseTextBottomSheet = ChooseTextBottomSheet(getString(R.string.car_type), setupCarTypesList(), { data, _ ->
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
        chooseTextBottomSheet = ChooseTextBottomSheet(getString(R.string.car_brand), setupCarBrandList(), { data, _ ->
            carBrand = data.name
            binding.carBrand.text = carBrand
        })
        showBottomSheet(chooseTextBottomSheet!!, "CarBrandBottomSheet")
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
