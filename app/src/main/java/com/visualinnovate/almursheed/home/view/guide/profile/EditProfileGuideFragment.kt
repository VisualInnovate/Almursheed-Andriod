package com.visualinnovate.almursheed.home.view.guide.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.auth.model.User
import com.visualinnovate.almursheed.common.SharedPreference
import com.visualinnovate.almursheed.common.base.BaseFragment
import com.visualinnovate.almursheed.common.onDebouncedListener
import com.visualinnovate.almursheed.common.toast
import com.visualinnovate.almursheed.common.value
import com.visualinnovate.almursheed.databinding.FragmentEditProfileGuideBinding
import com.visualinnovate.almursheed.home.view.commonView.profile.ProfileViewModel
import com.visualinnovate.almursheed.utils.ResponseHandler
import com.visualinnovate.almursheed.utils.Utils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileGuideFragment : BaseFragment() {

    private var _binding: FragmentEditProfileGuideBinding? = null
    private val binding get() = _binding!!

    private val vm: ProfileViewModel by viewModels()

    private lateinit var currentUser: User
    private var govId: Int? = null
    private var languagesIdsList: ArrayList<Int> = ArrayList()
    private var bio: String? = null

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
        initLanguageSpinner()
    }

    private fun initLanguageSpinner() {
        val languagesList = Utils.languages.keys.toList().sorted()

        val arrayAdapter = // android.R.layout.simple_spinner_item
            ArrayAdapter(
                requireContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                languagesList,
            )

        binding.spinnerLanguage.spinner.adapter = arrayAdapter
        arrayAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
        binding.spinnerLanguage.spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    // Retrieve the selected language name
                    val selectedLanguage = languagesList[position]
                    Log.d("readJsonFile", "selectedBrandName $selectedLanguage")
                    val languageId = Utils.languages[selectedLanguage]!!
                    languagesIdsList.add(languageId)
                    Log.d("readJsonFile", "languageId $languageId")
                    Log.d("readJsonFile", "languagesIdsList $languagesIdsList")
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Handle when nothing is selected
                }
            }
        /*binding.spinnerLanguage.spinner.setOnItemClickListener { _, _, position, _ -> // parent, view, position, long
            // Retrieve the selected language name
            val selectedLanguage = languagesList[position]
            Log.d("readJsonFile", "selectedBrandName $selectedLanguage")
            val languageId = languages[selectedLanguage]!!
            languagesIdsList.add(languageId)
            Log.d("readJsonFile", "languageId $languageId")
            Log.d("readJsonFile", "languagesIdsList $languagesIdsList")
        }*/
    }

    private fun setBtnListener() {
        binding.btnSubmit.onDebouncedListener {
            currentUser.bio = binding.edtBiography.value
            currentUser.govId = binding.edtGovernmentID.value
            // call api update guide
            vm.updateGuide(currentUser)
        }
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

    private fun subscribeData() {
        vm.updateGuideLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    // save user
                    SharedPreference.saveUser(it.data?.user!![0])
                    toast(it.data.message.toString())
                    findNavController().navigateUp()
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}