package com.visualinnovate.almursheed.guide.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.auth.model.User
import com.visualinnovate.almursheed.common.SharedPreference
import com.visualinnovate.almursheed.common.base.BaseFragment
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.common.showBottomSheet
import com.visualinnovate.almursheed.common.toast
import com.visualinnovate.almursheed.common.value
import com.visualinnovate.almursheed.commonView.bottomSheets.ChooseTextBottomSheet
import com.visualinnovate.almursheed.commonView.bottomSheets.model.ChooserItemModel
import com.visualinnovate.almursheed.commonView.profile.ProfileViewModel
import com.visualinnovate.almursheed.databinding.FragmentEditProfileGuideBinding
import com.visualinnovate.almursheed.utils.ResponseHandler
import com.visualinnovate.almursheed.utils.Utils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileGuideFragment : BaseFragment() {

    private var _binding: FragmentEditProfileGuideBinding? = null
    private val binding get() = _binding!!

    private val vm: ProfileViewModel by viewModels()

    private lateinit var currentUser: User

    private var governmentId: String? = null
    private var language: String? = null
    private var bio: String? = null
    private var chooseTextBottomSheet: ChooseTextBottomSheet? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileGuideBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentUser = requireArguments().getParcelable("userData")!!
        initViews()
        initToolbar()
        setBtnListener()
        subscribeData()
    }

    private fun initViews() {
        binding.edtBiography.setText(currentUser.bio)
        binding.edtGovernmentID.setText(currentUser.govId)
    }


    private fun setBtnListener() {
        binding.language.onDebouncedListener {
            showLanguagesChooser()
        }

        binding.btnSubmit.onDebouncedListener {
            saveData()
            // call api update guide
            vm.updatePersonalInformation(currentUser, currentUser.personalPhoto)
        }
    }

    private fun saveData() {
        currentUser.bio = binding.edtBiography.value
        currentUser.govId = binding.edtGovernmentID.value
        // currentUser.languages?.add(language.toString())
    }

    private fun initToolbar() {
        binding.appBarEditProfileGuide.setTitleString(getString(R.string.edit_profile))
        binding.appBarEditProfileGuide.setTitleCenter(true)
        binding.appBarEditProfileGuide.useBackButton(
            true,
            { findNavController().navigateUp() },
            R.drawable.ic_back
        )
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

    private fun setupLanguagesList(): ArrayList<ChooserItemModel> {
        val chooserItemList = ArrayList<ChooserItemModel>()
        Utils.allLanguages.forEach {
            val item = ChooserItemModel(name = it.lang, isSelected = it.lang == language)
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
                    toast(it.data.message.toString())
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}