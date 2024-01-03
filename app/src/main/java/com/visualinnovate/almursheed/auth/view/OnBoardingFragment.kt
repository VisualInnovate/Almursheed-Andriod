package com.visualinnovate.almursheed.auth.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.common.base.BaseFragment
import com.visualinnovate.almursheed.common.customNavigate
import com.visualinnovate.almursheed.common.showBottomSheet
import com.visualinnovate.almursheed.commonView.bottomSheets.ChooseTextBottomSheet
import com.visualinnovate.almursheed.commonView.bottomSheets.model.ChooserItemModel
import com.visualinnovate.almursheed.databinding.FragmentOnBoardingBinding

class OnBoardingFragment : BaseFragment() {

    private lateinit var binding: FragmentOnBoardingBinding

    private var chooseTextBottomSheet: ChooseTextBottomSheet? = null

    private var language: String = "en"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentOnBoardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // load icon next of get started
        loadGifImage(requireContext(), R.drawable.ic_next_animation, binding.icNextOnBoarding)
        setBtnListener()
    }

    private fun setBtnListener() {
        binding.language.setOnClickListener {
            showLanguageChooser()
        }

        binding.btnGetStarted.setOnClickListener {
            findNavController().customNavigate(R.id.action_onBoarding_to_registerType)
        }

        binding.btnLogin.setOnClickListener {
            findNavController().customNavigate(R.id.loginFragment)
        }
    }

    private fun showLanguageChooser() {
        chooseTextBottomSheet?.dismiss()
        chooseTextBottomSheet = ChooseTextBottomSheet(
            getString(R.string.priority),
            setupLanguageList(),
            { data, _ ->
                language = data.name.toString()
                binding.language.text = data.name.toString()
                /*language = when (data.name) {
                    getString(R.string.english) -> "en"
                    getString(R.string.arabic) -> "ar"
                    getString(R.string.azerbaijan) -> "az"
                    getString(R.string.georgia) -> "ge"
                    getString(R.string.russia) -> "ru"
                    getString(R.string.turkey) -> "tu"
                    else -> "en"
                }*/
            })
        showBottomSheet(chooseTextBottomSheet!!, "languagesBottomSheet")
    }

    private fun setupLanguageList(): ArrayList<ChooserItemModel> {
        val allTypes = arrayOf(
            getString(R.string.english),
            getString(R.string.arabic),
            getString(R.string.azerbaijan),
            getString(R.string.georgia),
            getString(R.string.russia),
            getString(R.string.turkey)
        )
        val chooserItemList = ArrayList<ChooserItemModel>()
        allTypes.forEach {
            val item = ChooserItemModel(name = it, isSelected = it == language)
            chooserItemList.add(item)
        }
        return chooserItemList
    }
}
